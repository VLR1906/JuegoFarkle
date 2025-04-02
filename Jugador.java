public class Jugador {
    private String nombre;
    private int puntosTotales;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntosTotales = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public void sumarPuntos(int puntos) {
        this.puntosTotales += puntos;
    }
}
