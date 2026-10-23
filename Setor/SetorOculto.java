import java.util.Random;

public class SetorOculto extends Setor {
    
    public SetorOculto(int x, int y) {
        super(x, y);
    }

    @Override
    public void processarAtaque(Jogador j, Inimigo i) {
        Random aleatorio = new Random();
        //lógica aleatória (o vírus pode ou não ser eliminado)
    }
}