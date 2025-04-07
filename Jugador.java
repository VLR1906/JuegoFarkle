// Clase que representa a un jugador del juego Farkle
public class Jugador {
    private String nombre;           // Nombre del jugador
    private int puntosTotales;       // Puntos acumulados durante la partida
    private int farklesSeguidos;     // Cantidad de turnos seguidos sin anotar (farkles)

    // Constructor: inicializa el jugador con su nombre y cero puntos
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntosTotales = 0;
        this.farklesSeguidos = 0;
    }

    // Suma puntos al total del jugador
    public void sumarPuntos(int puntos) {
        puntosTotales += puntos;
    }

    // Registra un "Farkle" (turno sin puntos)
    public void registrarFarkle() {
        farklesSeguidos++;
        // Si acumula 3 farkles seguidos, pierde 1000 puntos (sin bajar de 0)
        if (farklesSeguidos == 3) {
            puntosTotales = Math.max(0, puntosTotales - 1000);
            System.out.println(nombre + " ha sacado 3 Farkles seguidos y pierde 1000 puntos.");
            farklesSeguidos = 0; // Se reinicia el conteo
        }
    }

    // Reinicia el contador de farkles (cuando anota puntos)
    public void reiniciarFarkles() {
        farklesSeguidos = 0;
    }

    // Getter del nombre del jugador
    public String getNombre() {
        return nombre;
    }

    // Getter de los puntos totales acumulados
    public int getPuntosTotales() {
        return puntosTotales;
    }
}

