import java.util.Random;

public class SetorOculto extends Setor {
    
    public SetorOculto(int x, int y) {
        super(x, y);
    }

    @Override
    public void processarAtaque(Jogador j, Inimigo i) {
        java.util.Random aleatorio = new java.util.Random();
        if (aleatorio.nextBoolean()) {
            int novaDefesa = i.getDef() - j.getAtk();
            i.setDef(novaDefesa);
            System.out.println("Acerto no setor oculto! Dano causado: " + j.getAtk());
        } else {
            System.out.println("O ataque falhou. O vírus não foi atingido.");
        }
}