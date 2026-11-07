import java.util.Random;
public class Inimigo extends Combatente{

    protected Random aleatorio = new Random();

    public Inimigo(int forca){
        super(forca, forca);
    }

    public void imprimeStatus(){
        System.out.print (this.getDef() + "/" + this.getAtk() + " ");
    }

    public void atacar (Jogador jogador){
        aleatorio.nextInt(6);
        if(6%2 == 0){
            System.out.print("Jogador ");
            jogador.imprimeJogador();
            System.out.printf(" recebeu %d de dano%n", this.getAtk());
            jogador.receberDano(this.getAtk());
        }
    }

    public void receberDano(int dano){
		this.setDef(this.getDef() - dano);
		if(this.getDef() < 0){
			this.setVida(false);
		}
	}
}