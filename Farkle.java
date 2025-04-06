import java.util.*;

public class Farkle {
    public static final int PUNTOS_GANADOR = 10000;
    private List<Jugador> jugadores;
    private int turnoActual;
    private List<Dado> dados;
    private List<Boolean> dadosGuardados;
    private int puntosTurno;
    private int lanzamientosTurno;

    public Farkle(List<String> nombres) {

        jugadores = new ArrayList<>();
        for (String nombre : nombres) {
            jugadores.add(new Jugador(nombre));
        }

        turnoActual = 0;
        dados = new ArrayList<>();
        dadosGuardados = new ArrayList<>(Collections.nCopies(6, false));
        for (int i = 0; i < 6; i++) {
            dados.add(new Dado());
        }
        lanzamientosTurno = 0;
        puntosTurno = 0;
    }

    public void lanzarDados() {
        if (lanzamientosTurno >= 6) return; // No permitir más lanzamientos

        for (int i = 0; i < dados.size(); i++) {
            if (!dadosGuardados.get(i)) {
                dados.get(i).lanzar();
            }
        }
        lanzamientosTurno++;
        puntosTurno = calcularPuntos();
    }

    public int getLanzamientosTurno() {
        return lanzamientosTurno;
    }
    public void resetearTurno() {
        puntosTurno = 0;
        dadosGuardados = new ArrayList<>(Collections.nCopies(6, false));
    }




    public void guardarDado(int index) {
        dadosGuardados.set(index, !dadosGuardados.get(index));
    }

    public int calcularPuntos() {
        int[] conteo = new int[7]; // Índices 1-6
        for (Dado d : dados) {
            conteo[d.getValor()]++;
        }

        int puntos = 0;

        boolean escalera = (conteo[1] == 1 && conteo[2] == 1 && conteo[3] == 1 &&
                conteo[4] == 1 && conteo[5] == 1 && conteo[6] == 1);
        boolean tresParejas = (conteo[1]/2 + conteo[2]/2 + conteo[3]/2 +
                conteo[4]/2 + conteo[5]/2 + conteo[6]/2) == 3;
        boolean dosTrios = (conteo[1]/3 + conteo[2]/3 + conteo[3]/3 +
                conteo[4]/3 + conteo[5]/3 + conteo[6]/3) == 2;

        if (escalera) return 1500;
        if (tresParejas) return 1500;
        if (dosTrios) return 2500;

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

        if (conteo[4] == 4 && (conteo[1] == 2 || conteo[2] == 2 || conteo[3] == 2 || conteo[5] == 2 || conteo[6] == 2)) {
            puntos = Math.max(puntos, 1500); // No sobreescribe lo anterior
        }

        // Ahora sumar los unos y cincos restantes
        puntos += conteo[1] * 100;
        puntos += conteo[5] * 50;

        return puntos;
    }

    public boolean generaPuntos() {
        int[] conteo = new int[7]; // índices 1 al 6
        for (int i = 0; i < dados.size(); i++) {
            if (!dadosGuardados.get(i)) {
                conteo[dados.get(i).getValor()]++;
            }
        }

        boolean escalera = (conteo[1] == 1 && conteo[2] == 1 && conteo[3] == 1 &&
                conteo[4] == 1 && conteo[5] == 1 && conteo[6] == 1);
        boolean tresParejas = (conteo[1]/2 + conteo[2]/2 + conteo[3]/2 +
                conteo[4]/2 + conteo[5]/2 + conteo[6]/2) == 3;
        boolean dosTrios = (conteo[1]/3 + conteo[2]/3 + conteo[3]/3 +
                conteo[4]/3 + conteo[5]/3 + conteo[6]/3) == 2;

        if (escalera || tresParejas || dosTrios) return true;

        for (int i = 1; i <= 6; i++) {
            if (conteo[i] >= 3) return true;
        }

        if (conteo[1] > 0 || conteo[5] > 0) return true;

        return false; // no generó puntos
    }




    public void guardarPuntos() {
        jugadores.get(turnoActual).sumarPuntos(puntosTurno);
        puntosTurno = 0;
        lanzamientosTurno = 0;
        dadosGuardados = new ArrayList<>(Collections.nCopies(6, false));
        cambiarTurno();

    }

    public void cambiarTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
        puntosTurno = 0;
        lanzamientosTurno = 0;
        dadosGuardados = new ArrayList<>(Collections.nCopies(6, false));
    }


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
    }}



