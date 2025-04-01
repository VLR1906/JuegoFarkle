import java.util.ArrayList;
import java.util.List;

public class Lanzamiento {
    private List<Dado> dados;

    public Lanzamiento() {
        dados = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dados.add(new Dado());
        }
    }

    public void lanzarDados() {
        for (Dado dado : dados) {
            dado.lanzar();
        }
    }

    public List<Dado> getDados() {
        return dados;
    }
}
