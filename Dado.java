import java.util.Random;

public class Dado {
    private int valor;
    private static final Random random = new Random();

    public Dado(int i) {
        this.valor = 0; // Por defecto, el dado inicia en 1
    }

    public void lanzar() {
        this.valor = random.nextInt(6) + 1; // Genera un número entre 1 y 6
    }

    public int getValor() {
        return valor;
    }
   public void setValor(int valor) {}
    @Override
    public String toString() {
        return "Dado: " + valor;
    }
}
