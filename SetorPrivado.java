public class SetorPrivado extends Setor {
    
    public SetorPrivado(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean processarProcura(Jogador j) {
        return false;
    }
}