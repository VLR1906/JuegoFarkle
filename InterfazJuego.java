import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InterfazJuego extends JFrame {
    private Farkle farkle;
    private JButton lanzarButton, guardarButton;
    private JLabel puntosTurnoLabel, mensajeTurnoLabel;
    private JPanel dadosPanel, puntosPanel;
    private List<Dado> dados;
    private JButton[] dadoBotones;
    private List<JLabel> etiquetasPuntosJugadores;

    public InterfazJuego(List<String> nombres) {
        setTitle("Juego  Farkle");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        farkle = new Farkle(nombres);
        dados = farkle.getDados();

        lanzarButton = new JButton("Lanzar Dados");
        guardarButton = new JButton("Guardar Puntos");
        guardarButton.setEnabled(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(lanzarButton);
        controlPanel.add(guardarButton);

        dadosPanel = new JPanel();
        dadosPanel.setLayout(new GridLayout(1, 6));
        dadosPanel.setBackground(Color.WHITE);

        puntosTurnoLabel = new JLabel("Puntos Turno: 0");
        mensajeTurnoLabel = new JLabel("Es el turno de: " + farkle.getJugadorActual().getNombre());

        puntosPanel = new JPanel();
        puntosPanel.setLayout(new GridLayout(nombres.size() + 2, 1)); // +2 para etiquetas extra
        puntosPanel.add(puntosTurnoLabel);
        puntosPanel.add(mensajeTurnoLabel);

        etiquetasPuntosJugadores = new ArrayList<>();
        for (String nombre : nombres) {
            JLabel label = new JLabel(nombre + ": 0 puntos");
            etiquetasPuntosJugadores.add(label);
            puntosPanel.add(label);
        }

        add(puntosPanel, BorderLayout.WEST);
        add(dadosPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        lanzarButton.addActionListener(e -> lanzarDados());
        guardarButton.addActionListener(e -> guardarPuntos());

        actualizarDados();
        setVisible(true);
    }

    private void actualizarDados() {
        dadosPanel.removeAll();  // Limpiar el panel de dados

        dadoBotones = new JButton[6];  // Limpiar los botones si es necesario

        for (int i = 0; i < dados.size(); i++) {
            final int index = i;

            // Cargar la imagen correspondiente al valor del dado
            String nombreImagen = "/Dados/cara" + dados.get(i).getValor() + ".png"; // Ajustar el nombre
            ImageIcon dadoIcon = new ImageIcon(getClass().getResource(nombreImagen));

            // Escalar la imagen para que tenga un tamaño adecuado
            Image imagenEscalada = dadoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            dadoIcon = new ImageIcon(imagenEscalada);

            // Crear una etiqueta JLabel con la imagen del dado
            JLabel dadoLabel = new JLabel(dadoIcon);
            dadoLabel.setHorizontalAlignment(JLabel.CENTER);
            dadoLabel.setVerticalAlignment(JLabel.CENTER);

            // Si el dado está guardado, agregar un borde verde
            if (farkle.getDadosGuardados().get(i)) {
                dadoLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
            } else {
                dadoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            }

            // Agregar un evento de clic para guardar los dados
            dadoLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    farkle.guardarDado(index);
                    actualizarDados();  // Actualiza la interfaz después de guardar el dado
                }
            });

            dadosPanel.add(dadoLabel);  // Añadir la etiqueta con la imagen al panel
        }

        dadosPanel.revalidate();  // Revalidar el panel después de actualizarlo
        dadosPanel.repaint();  // Redibujar el panel
    }



    private void cambiarTurno() {
        farkle.cambiarTurno(); // Llama al método que ya cambia el turno en la lógica del juego

//        // Verificar si alguien ha ganado después del turno
//        if (farkle.getJugadorActual().getPuntosTotales() >= Farkle.PUNTOS_GANADOR) {
//            JOptionPane.showMessageDialog(this, farkle.getJugadorActual().getNombre() + " ha ganado el juego!", "¡Ganador!", JOptionPane.INFORMATION_MESSAGE);
//            System.exit(0); // Termina el juego
//        }

        // Mostrar las puntuaciones de todos los jugadores
        StringBuilder puntuaciones = new StringBuilder("Puntuaciones actuales:\n");
        for (Jugador jugador : farkle.getJugadores()) {
            puntuaciones.append(jugador.getNombre()).append(": ").append(jugador.getPuntosTotales()).append(" puntos\n");
        }

        JOptionPane.showMessageDialog(this, puntuaciones.toString(), "Cambio de turno", JOptionPane.INFORMATION_MESSAGE);

        // Actualizar interfaz con el nuevo jugador
        mensajeTurnoLabel.setText("Es el turno de: " + farkle.getJugadorActual().getNombre());
        puntosTurnoLabel.setText("Puntos Turno: 0");
        actualizarDados();
    }




    private void lanzarDados() {
        farkle.lanzarDados();
        actualizarDados();
        puntosTurnoLabel.setText("Puntos Turno: " + farkle.getPuntosTurno());
        guardarButton.setEnabled(true);

        // Si no hay puntos, es un Farkle
        if (!farkle.generaPuntos()) {
            JOptionPane.showMessageDialog(this, "¡Farkle! No obtuviste puntos. Pierdes los acumulados del turno.");
            farkle.resetearTurno(); // o lo que uses para reiniciar puntaje
            guardarButton.setEnabled(false);
            cambiarTurno();
            return;
        }



        // Si alcanzó los 6 lanzamientos, forzar el cambio de turno
        if (farkle.getLanzamientosTurno() >= 6) {
            JOptionPane.showMessageDialog(this, "¡Has alcanzado el máximo de 6 lanzamientos! Se cambia el turno.");
            guardarPuntos(); // Guarda automáticamente los puntos antes de cambiar
        }
    }


    private void guardarPuntos() {
        Jugador jugadorQueJugo = farkle.getJugadorActual(); // <-- Guardamos quién estaba jugando

        farkle.guardarPuntos(); // Esto cambia el turno internamente
        actualizarPuntuaciones();

        // Verificamos los puntos del jugador que acaba de jugar
        if (jugadorQueJugo.getPuntosTotales() >= Farkle.PUNTOS_GANADOR) {
            JOptionPane.showMessageDialog(this, jugadorQueJugo.getNombre() + " ha ganado el juego con " +
                    jugadorQueJugo.getPuntosTotales() + " puntos!", "¡Ganador!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Termina el juego
            return;
        }

        mensajeTurnoLabel.setText("Es el turno de: " + farkle.getJugadorActual().getNombre());
        guardarButton.setEnabled(false);
        actualizarDados();
    }



    private void actualizarPuntuaciones() {
        List<Jugador> jugadores = farkle.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            etiquetasPuntosJugadores.get(i).setText(jugadores.get(i).getNombre() + ": " + jugadores.get(i).getPuntosTotales() + " puntos");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int numJugadores;
            do {
                String input = JOptionPane.showInputDialog(null, "¿Cuántos jugadores van a jugar? (2-6)", "Jugadores", JOptionPane.QUESTION_MESSAGE);
                if (input == null) return;
                try {
                    numJugadores = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    numJugadores = 0;
                }
            } while (numJugadores < 2 || numJugadores > 6);

            List<String> nombres = new ArrayList<>();
            for (int i = 1; i <= numJugadores; i++) {
                String nombre = JOptionPane.showInputDialog(null, "Nombre del jugador " + i + ":", "Jugadores", JOptionPane.QUESTION_MESSAGE);
                if (nombre == null || nombre.trim().isEmpty()) {
                    nombre = "Jugador " + i;
                }
                nombres.add(nombre);
            }

            new InterfazJuego(nombres);
        });
    }
}