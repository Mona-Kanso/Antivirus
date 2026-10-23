public class SetorPrivado extends Setor {
    
    public SetorPrivado(int x, int y) {
        super(x, y);
    }

    @Override
    public void processarProcura(Jogador j) {
        //ação bloqueada
        System.out.println("Ação de procura não permitida em Setores Privados.");
    }
}