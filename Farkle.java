import java.util.ArrayList;
import java.util.List;

public class Farkle {
    private List<Jugador> jugadores;
    private List<Dado> dados;
    private List<Boolean> dadosGuardados; // Para saber qué dados están guardados
    private int turnoActual;
    private int puntosTurno;
    private boolean farkle;

    public Farkle(List<String> nombres) {
        jugadores = new ArrayList<>();
        for (String nombre : nombres) {
            jugadores.add(new Jugador(nombre));
        }

        dados = new ArrayList<>();
        dadosGuardados = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dados.add(new Dado(1));
            dadosGuardados.add(false); // Ningún dado está guardado al inicio
        }

        turnoActual = 0;
        puntosTurno = 0;
        farkle = false;
    }

    public void lanzarDados() {
        for (int i = 0; i < dados.size(); i++) {
            if (!dadosGuardados.get(i)) { // Solo lanza los dados que NO están guardados
                dados.get(i).lanzar();
            }
        }
        calcularPuntos();
    }

    public void guardarDado(int indice) {
        if (indice >= 0 && indice < dados.size()) {
            dadosGuardados.set(indice, true);
        }
    }

    public void limpiarDadosGuardados() {
        for (int i = 0; i < dadosGuardados.size(); i++) {
            dadosGuardados.set(i, false);
        }
    }

    private void calcularPuntos() {
        int[] conteo = new int[7]; // Indices 1-6 para contar cuántos hay de cada número
        for (Dado dado : dados) {
            conteo[dado.getValor()]++;
        }

        puntosTurno = 0;
        farkle = true;

        // Puntos por tríos
        if (conteo[1] >= 3) puntosTurno += 1000;
        if (conteo[2] >= 3) puntosTurno += 200;
        if (conteo[3] >= 3) puntosTurno += 300;
        if (conteo[4] >= 3) puntosTurno += 400;
        if (conteo[5] >= 3) puntosTurno += 500;
        if (conteo[6] >= 3) puntosTurno += 600;

        // Puntos por dados individuales
        if (conteo[1] < 3) puntosTurno += conteo[1] * 100;
        if (conteo[5] < 3) puntosTurno += conteo[5] * 50;

        // Escalera de 1, 2, 3, 4, 5, 6
        if (conteo[1] == 1 && conteo[2] == 1 && conteo[3] == 1 && conteo[4] == 1 && conteo[5] == 1 && conteo[6] == 1) {
            puntosTurno += 1500;
        }

        // Si sacamos puntos, no es un Farkle
        if (puntosTurno > 0) {
            farkle = false;
        }
    }

    public void guardarPuntos() {
        if (!farkle) {
            jugadores.get(turnoActual).sumarPuntos(puntosTurno);
        }
        cambiarTurno();
    }

    private void cambiarTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
        puntosTurno = 0;
        limpiarDadosGuardados(); // Limpiar los dados guardados al cambiar de turno
    }

    public List<Dado> getDados() {
        return dados;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }

    public int getPuntosTurno() {
        return puntosTurno;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Boolean> getDadosGuardados() {
        return dadosGuardados;
    }
}
