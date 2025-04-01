import java.util.Random;

public class Dado {
    private static final Random random = new Random();
    private int valor;

    public Dado() {
        lanzar();
    }

    public void lanzar() {
        valor = random.nextInt(6) + 1;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "[" + valor + "]";
    }
}
