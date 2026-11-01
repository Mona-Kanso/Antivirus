import java.util.Random;

public class SetorOculto extends Setor {
    
    public SetorOculto(int x, int y) {
        super(x, y);
    }


    @Override
    public void processarAtaqueJogador(Jogador j, Inimigo i) {
        java.util.Random aleatorio = new java.util.Random();
        if (aleatorio.nextBoolean()) {
            int novaDefesa = i.getDEF() - j.getATK();
            i.setDEF(novaDefesa);
            System.out.println("Acerto no setor oculto! Dano causado: " + j.getATK());
            this.inimigos.removeIf(inimigo -> inimigo.getDEF() <= 0);

        } else {
            System.out.println("O ataque falhou. O vírus não foi atingido.");
        }
    }
}