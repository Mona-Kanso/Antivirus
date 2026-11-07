import java.util.Random;

public class SetorOculto extends Setor {
    
    public SetorOculto(int x, int y) {
        super(x, y);
    }

//marcello ver!!
    @Override
    public void processarAtaqueJogador(Jogador jogador, Inimigo inimigo) {
        java.util.Random aleatorio = new java.util.Random();
        if (aleatorio.nextBoolean()) {
            inimigo.receberDano(jogador.getAtk());
            System.out.printf("Acerto no setor oculto! Dano causado: %d%n" + jogador.getAtk());
            if(inimigo.getDef() < 0){
                this.removerInimigo(inimigo);
            }
        }
    }
}