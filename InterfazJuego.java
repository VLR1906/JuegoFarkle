import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal de la interfaz gráfica del juego Farkle.
 * Hereda de JFrame y gestiona la visualización, interacción y flujo del juego.
 */
public class InterfazJuego extends JFrame {
    private Farkle farkle; // Lógica del juego
    private JButton lanzarButton, guardarButton; // Botones para lanzar dados y guardar puntos
    private JLabel puntosTurnoLabel, mensajeTurnoLabel; // Etiquetas para mostrar información del turno
    private JPanel dadosPanel, puntosPanel; // Paneles de interfaz
    private List<Dado> dados; // Lista de dados actuales
    private JButton[] dadoBotones; // Botones de los dados (no usados directamente en esta versión)
    private List<JLabel> etiquetasPuntosJugadores; // Etiquetas que muestran puntos de los jugadores

    /**
     * Constructor: inicializa la interfaz, lógica del juego y componentes.
     */
    public InterfazJuego(List<String> nombres) {
        setTitle("Juego  Farkle");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        farkle = new Farkle(nombres);
        dados = farkle.getDados();

        // Botones de control
        lanzarButton = new JButton("Lanzar Dados");
        guardarButton = new JButton("Guardar Puntos");
        guardarButton.setEnabled(false); // Desactivado hasta lanzar

        // Panel de botones
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(lanzarButton);
        controlPanel.add(guardarButton);

        // Panel para mostrar los dados
        dadosPanel = new JPanel();
        dadosPanel.setLayout(new GridLayout(1, 6));
        dadosPanel.setBackground(Color.WHITE);

        // Etiquetas de puntuación
        puntosTurnoLabel = new JLabel("Puntos Turno: 0");
        mensajeTurnoLabel = new JLabel("Es el turno de: " + farkle.getJugadorActual().getNombre());

        // Panel lateral con puntuaciones
        puntosPanel = new JPanel();
        puntosPanel.setLayout(new GridLayout(nombres.size() + 2, 1));
        puntosPanel.add(puntosTurnoLabel);
        puntosPanel.add(mensajeTurnoLabel);

        etiquetasPuntosJugadores = new ArrayList<>();
        for (String nombre : nombres) {
            JLabel label = new JLabel(nombre + ": 0 puntos");
            etiquetasPuntosJugadores.add(label);
            puntosPanel.add(label);
        }

        // Añadir paneles al frame principal
        add(puntosPanel, BorderLayout.WEST);
        add(dadosPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Acción al presionar los botones
        lanzarButton.addActionListener(e -> lanzarDados());
        guardarButton.addActionListener(e -> guardarPuntos());

        actualizarDados(); // Mostrar los dados inicialmente
        setVisible(true);
    }

    /**
     * Actualiza la visualización de los dados en la interfaz.
     */
    private void actualizarDados() {
        dadosPanel.removeAll(); // Limpiar el panel actual
        dadoBotones = new JButton[6]; // (Aunque no se usan aquí)

        for (int i = 0; i < dados.size(); i++) {
            final int index = i;

            // Cargar imagen del dado según su valor
            String nombreImagen = "/Dados/cara" + dados.get(i).getValor() + ".png";
            ImageIcon dadoIcon = new ImageIcon(getClass().getResource(nombreImagen));
            Image imagenEscalada = dadoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            dadoIcon = new ImageIcon(imagenEscalada);

            // Crear etiqueta con la imagen
            JLabel dadoLabel = new JLabel(dadoIcon);
            dadoLabel.setHorizontalAlignment(JLabel.CENTER);
            dadoLabel.setVerticalAlignment(JLabel.CENTER);

            // Borde visual para dados guardados
            if (farkle.getDadosGuardados().get(i)) {
                dadoLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
            } else {
                dadoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            }

            // Permitir hacer clic para guardar/soltar el dado
            dadoLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    farkle.guardarDado(index);
                    actualizarDados();
                }
            });

            dadosPanel.add(dadoLabel);
        }

        dadosPanel.revalidate();
        dadosPanel.repaint();
    }

    /**
     * Lógica para cambiar el turno actual.
     * Muestra un mensaje con la puntuación de todos los jugadores.
     */
    private void cambiarTurno() {
        farkle.cambiarTurno();

        // Mostrar puntuaciones
        StringBuilder puntuaciones = new StringBuilder("Puntuaciones actuales:\n");
        for (Jugador jugador : farkle.getJugadores()) {
            puntuaciones.append(jugador.getNombre()).append(": ").append(jugador.getPuntosTotales()).append(" puntos\n");
        }

        JOptionPane.showMessageDialog(this, puntuaciones.toString(), "Cambio de turno", JOptionPane.INFORMATION_MESSAGE);

        // Actualizar interfaz
        mensajeTurnoLabel.setText("Es el turno de: " + farkle.getJugadorActual().getNombre());
        puntosTurnoLabel.setText("Puntos Turno: 0");
        actualizarDados();
    }

    /**
     * Ejecuta un lanzamiento de dados y gestiona la lógica de Farkle.
     */
    private void lanzarDados() {
        farkle.lanzarDados();
        actualizarDados();
        puntosTurnoLabel.setText("Puntos Turno: " + farkle.getPuntosTurno());
        guardarButton.setEnabled(true);

        // Si no se generan puntos → Farkle
        if (!farkle.generaPuntos()) {
            JOptionPane.showMessageDialog(this, "¡Farkle! No obtuviste puntos. Pierdes los acumulados del turno.");
            farkle.resetearTurno();
            guardarButton.setEnabled(false);
            cambiarTurno();
            return;
        }

        // Si se han lanzado los dados 6 veces → cambio de turno automático
        if (farkle.getLanzamientosTurno() >= 6) {
            JOptionPane.showMessageDialog(this, "¡Has alcanzado el máximo de 6 lanzamientos! Se cambia el turno.");
            guardarPuntos(); // Guarda automáticamente
        }
    }

    /**
     * Guarda los puntos acumulados del turno y verifica si hay un ganador.
     */
    private void guardarPuntos() {
        Jugador jugadorQueJugo = farkle.getJugadorActual();

        farkle.guardarPuntos(); // Cambia el turno internamente
        actualizarPuntuaciones();

        // Verificar si ganó el jugador
        if (jugadorQueJugo.getPuntosTotales() >= Farkle.PUNTOS_GANADOR) {
            JOptionPane.showMessageDialog(this, jugadorQueJugo.getNombre() + " ha ganado el juego con " +
                    jugadorQueJugo.getPuntosTotales() + " puntos!", "¡Ganador!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Finaliza el juego
            return;
        }

        mensajeTurnoLabel.setText("Es el turno de: " + farkle.getJugadorActual().getNombre());
        guardarButton.setEnabled(false);
        actualizarDados();
    }

    /**
     * Actualiza las etiquetas con las puntuaciones de todos los jugadores.
     */
    private void actualizarPuntuaciones() {
        List<Jugador> jugadores = farkle.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            etiquetasPuntosJugadores.get(i).setText(jugadores.get(i).getNombre() + ": " + jugadores.get(i).getPuntosTotales() + " puntos");
        }
    }

    /**
     * Método principal para lanzar el juego. Solicita número y nombres de jugadores.
     */
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

            // Pedir nombres de jugadores
            List<String> nombres = new ArrayList<>();
            for (int i = 1; i <= numJugadores; i++) {
                String nombre = JOptionPane.showInputDialog(null, "Nombre del jugador " + i + ":", "Jugadores", JOptionPane.QUESTION_MESSAGE);
                if (nombre == null || nombre.trim().isEmpty()) {
                    nombre = "Jugador " + i;
                }
                nombres.add(nombre);
            }

            new InterfazJuego(nombres); // Iniciar el juego
        });
    }
}
