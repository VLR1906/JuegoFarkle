import java.util.List;

public class ReglasFarkle {
    public static int calcularPuntuacion(List<Dado> dados) {
        int puntos = 0;
        int[] contador = new int[7];

        for (Dado dado : dados) {
            contador[dado.getValor()]++;
        }

        if (contador[1] >= 3) puntos += 1000;
        for (int i = 2; i <= 6; i++) {
            if (contador[i] >= 3) puntos += i * 100;
        }

        puntos += (contador[1] % 3) * 100;
        puntos += (contador[5] % 3) * 50;

        return puntos;
    }
}