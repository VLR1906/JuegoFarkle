// Importación de utilidades de Java necesarias para listas y funciones de streams
import java.util.*;
import java.util.stream.Collectors;

// Clase principal del juego Farkle
public class Farkle {
    public static final int PUNTOS_GANADOR = 10000; // Puntos necesarios para ganar el juego

    private List<Jugador> jugadores;         // Lista de jugadores
    private int turnoActual;                 // Índice del jugador en turno
    private List<Dado> dados;                // Lista de los 6 dados
    private List<Boolean> dadosGuardados;    // Marca qué dados están guardados
    private int puntosTurno;                 // Puntos acumulados en el turno actual
    private int lanzamientosTurno;           // Número de lanzamientos en el turno actual

    // Constructor: inicializa el juego con los nombres de los jugadores
    public Farkle(List<String> nombres) {
        jugadores = new ArrayList<>();
        nombres.stream().map(Jugador::new).forEach(jugadores::add);

        turnoActual = 0;
        dados = new ArrayList<>();
        dadosGuardados = new ArrayList<>(Collections.nCopies(6, false)); // Inicialmente ningún dado está guardado
        for (int i = 0; i < 6; i++) {
            dados.add(new Dado()); // Crea 6 dados
        }
        lanzamientosTurno = 0;
        puntosTurno = 0;
    }

    // Lanza todos los dados que no estén guardados
    public void lanzarDados() {
        if (lanzamientosTurno >= 6) return; // Máximo 6 lanzamientos por turno

        for (int i = 0; i < dados.size(); i++) {
            if (!dadosGuardados.get(i)) {
                dados.get(i).lanzar(); // Lanza dado si no está guardado
            }
        }
        lanzamientosTurno++;
        puntosTurno = calcularPuntos(); // Actualiza los puntos del turno
    }

    // Getters simples
    public int getLanzamientosTurno() {
        return lanzamientosTurno;
    }

    // Reinicia el estado del turno actual
    public void resetearTurno() {
        reiniciarTurno();
    }

    // Marca o desmarca un dado como guardado
    public void guardarDado(int index) {
        dadosGuardados.set(index, !dadosGuardados.get(index));
    }

    // Calcula los puntos obtenidos con los valores actuales de los dados
    public int calcularPuntos() {
        int[] conteo = new int[7]; // Conteo de cada valor de dado (1 a 6)
        for (Dado d : dados) {
            conteo[d.getValor()]++;
        }

        int puntos = 0;

        // Comprobación de combinaciones especiales
        boolean escalera = (conteo[1] == 1 && conteo[2] == 1 && conteo[3] == 1 &&
                conteo[4] == 1 && conteo[5] == 1 && conteo[6] == 1);
        boolean tresParejas = (conteo[1]/2 + conteo[2]/2 + conteo[3]/2 +
                conteo[4]/2 + conteo[5]/2 + conteo[6]/2) == 3;
        boolean dosTrios = (conteo[1]/3 + conteo[2]/3 + conteo[3]/3 +
                conteo[4]/3 + conteo[5]/3 + conteo[6]/3) == 2;

        if (escalera) return 1500;
        if (tresParejas) return 1500;
        if (dosTrios) return 2500;

        // Comprobación de otras combinaciones por cantidad
        for (int i = 1; i <= 6; i++) {
            if (conteo[i] >= 6) {
                puntos += 3000;
                conteo[i] -= 6;
            } else if (conteo[i] == 5) {
                puntos += 2000;
                conteo[i] -= 5;
            } else if (conteo[i] == 4) {
                puntos += 1000;
                conteo[i] -= 4;
            } else if (conteo[i] == 3) {
                if (i == 1) puntos += 1000;
                else puntos += i * 100;
                conteo[i] -= 3;
            }
        }

        // Combinación especial: 4 iguales + una pareja
        if (conteo[4] == 4 && (conteo[1] == 2 || conteo[2] == 2 || conteo[3] == 2 || conteo[5] == 2 || conteo[6] == 2)) {
            puntos = Math.max(puntos, 1500);
        }

        // Puntos adicionales por unos y cincos restantes
        puntos += conteo[1] * 100;
        puntos += conteo[5] * 50;

        return puntos;
    }

    // Verifica si los dados actuales pueden generar puntos
    public boolean generaPuntos() {
        Map<Integer, Long> conteo = dados.stream()
                .filter(d -> !dadosGuardados.get(dados.indexOf(d)))
                .collect(Collectors.groupingBy(Dado::getValor, Collectors.counting()));

        boolean escalera = conteo.size() == 6 && conteo.values().stream().allMatch(c -> c == 1);
        boolean tresParejas = conteo.values().stream().filter(c -> c >= 2).count() == 3;
        boolean dosTrios = conteo.values().stream().filter(c -> c >= 3).count() == 2;

        if (escalera || tresParejas || dosTrios) return true;

        // También se puede obtener puntos si hay 1, 5 o al menos tres iguales
        return conteo.entrySet().stream().anyMatch(e ->
                e.getKey() == 1 || e.getKey() == 5 || e.getValue() >= 3
        );
    }

    // Guarda los puntos acumulados por el jugador en turno
    public void guardarPuntos() {
        jugadores.get(turnoActual).sumarPuntos(puntosTurno);
        reiniciarTurno();
        cambiarTurno();
    }

    // Cambia el turno al siguiente jugador
    public void cambiarTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
        reiniciarTurno();
    }

    // Reinicia los valores del turno actual
    private void reiniciarTurno() {
        puntosTurno = 0;
        lanzamientosTurno = 0;
        dadosGuardados.replaceAll(d -> false); // Desmarca todos los dados guardados
    }

    // Métodos auxiliares para obtener información del estado del juego
    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Dado> getDados() {
        return dados;
    }

    public List<Boolean> getDadosGuardados() {
        return dadosGuardados;
    }

    public int getPuntosTurno() {
        return puntosTurno;
    }
}
