import javax.swing.*;
import java.awt.*;
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
        dadosPanel.removeAll();
        dadoBotones = new JButton[6];

        for (int i = 0; i < dados.size(); i++) {
            final int index = i;
            dadoBotones[i] = new JButton(String.valueOf(dados.get(i).getValor()));
            dadoBotones[i].setFont(new Font("Arial", Font.BOLD, 15));

            if (farkle.getDadosGuardados().get(i)) {
                dadoBotones[i].setBackground(Color.GREEN);
            } else {
                dadoBotones[i].setBackground(Color.WHITE);
            }

            dadoBotones[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            dadoBotones[i].addActionListener(e -> {
                farkle.guardarDado(index);
                actualizarDados();
            });

            dadosPanel.add(dadoBotones[i]);
        }

        dadosPanel.revalidate();
        dadosPanel.repaint();
    }


    private void cambiarTurno() {
        farkle.cambiarTurno(); // Llama al método que ya cambia el turno en la lógica del juego

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
        if (farkle.getPuntosTurno() == 0) {
            JOptionPane.showMessageDialog(this, "¡Farkle! No obtuviste puntos en este turno.");
            guardarButton.setEnabled(false);
            cambiarTurno();
        }
    }








    private void guardarPuntos() {
        farkle.guardarPuntos();
        actualizarPuntuaciones();
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
