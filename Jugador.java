public class Jugador {
    private String nombre;
    private int puntosTotales;
    private int farklesSeguidos;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntosTotales = 0;
        this.farklesSeguidos = 0;
    }

    public void sumarPuntos(int puntos) {
        puntosTotales += puntos;
    }

    public void registrarFarkle() {
        farklesSeguidos++;
        if (farklesSeguidos == 3) {
            puntosTotales = Math.max(0, puntosTotales - 1000);
            System.out.println(nombre + " ha sacado 3 Farkles seguidos y pierde 1000 puntos.");
            farklesSeguidos = 0;
        }
    }

    public void reiniciarFarkles() {
        farklesSeguidos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }
}
