import java.util.Random;

// Clase que representa un dado de seis caras
public class Dado {
    private int valor;                      // Valor actual del dado (1 a 6)
    private static final Random random = new Random(); // Generador de números aleatorios

    // Constructor: al crear un dado, se le asigna un valor aleatorio entre 1 y 6
    public Dado() {
        this.valor = random.nextInt(6) + 1;
    }

    // Método para lanzar el dado: genera un nuevo valor aleatorio entre 1 y 6
    public void lanzar() {
        this.valor = random.nextInt(6) + 1;
    }

    // Devuelve el valor actual del dado
    public int getValor() {
        return valor;
    }

    // Setter vacío: no hace nada. Podría eliminarse si no se usa.
    public void setValor(int valor) {}

    // Representación en texto del dado
    @Override
    public String toString() {
        return "Dado: " + valor;
    }
}
