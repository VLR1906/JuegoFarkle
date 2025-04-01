import java.util.*;

class JuegoFarkle {
    private List<Jugador> jugadores;
    private int turnoActual;
    private final Scanner scanner;

    public JuegoFarkle(String[] nombresJugadores) {
        jugadores = new ArrayList<>();
        for (String nombre : nombresJugadores) {
            jugadores.add(new Jugador(nombre));
        }
        turnoActual = 0;
        scanner = new Scanner(System.in);
    }

    public void jugarTurno() {
        Jugador jugador = jugadores.get(turnoActual);
        System.out.println("Turno de " + jugador.getNombre());
        List<Dado> dadosDisponibles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dadosDisponibles.add(new Dado());
        }

        boolean continuar = true;
        int puntosRonda = 0;

        while (continuar && !dadosDisponibles.isEmpty()) {
            System.out.println("Lanzando dados...");
            for (Dado dado : dadosDisponibles) {
                dado.lanzar();
            }

            System.out.println("Resultados:");
            for (int i = 0; i < dadosDisponibles.size(); i++) {
                System.out.println((i + 1) + ". " + dadosDisponibles.get(i));
            }

            int puntos = ReglasFarkle.calcularPuntuacion(dadosDisponibles);

            if (puntos == 0) {
                System.out.println("¡Farkle! Pierdes los puntos acumulados en este turno.");
                puntosRonda = 0;
                break;
            }

            System.out.println("Has obtenido " + puntos + " puntos en esta tirada.");
            puntosRonda += puntos;

            System.out.println("¿Quieres conservar algunos dados? Ingresa los números separados por espacio o presiona Enter para volver a tirar todos.");
            scanner.nextLine();
            String input = scanner.nextLine();

            if (!input.isEmpty()) {
                Set<Integer> indices = new HashSet<>();
                for (String num : input.split(" ")) {
                    try {
                        int index = Integer.parseInt(num) - 1;
                        if (index >= 0 && index < dadosDisponibles.size()) {
                            indices.add(index);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Intenta de nuevo.");
                        continue;
                    }
                }

                List<Dado> dadosConservados = new ArrayList<>();
                for (int index : indices) {
                    dadosConservados.add(dadosDisponibles.get(index));
                }
                dadosDisponibles = dadosConservados;
            }

            System.out.println("¿Quieres seguir lanzando? (s/n)");
            if (!scanner.next().equalsIgnoreCase("s")) {
                continuar = false;
            }
        }

        jugador.sumarPuntos(puntosRonda);
        System.out.println(jugador.getNombre() + " ahora tiene " + jugador.getPuntuacion() + " puntos.");
        turnoActual = (turnoActual + 1) % jugadores.size();
    }

    public void jugar() {
        for (int i = 0; i < 5; i++) { // Simula 5 rondas
            System.out.println("----------------------");
            jugarTurno();
        }
    }


}
