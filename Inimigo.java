public class Inimigo extends Combatente{

    public Inimigo(int forca){
        super(forca, forca);
    }

    public void imprimeStatus(){
        System.out.print (this.getDef() + "/" + this.getAtk() + " ");
    }

    public void atacar (Jogador jogador){
        System.out.print("Jogador");
        jogador.imprimeJogador();
        System.out.printf("recebeu %d de dano%n", this.getAtk());
        jogador.receberDano(this.getAtk());
    }

    public void receberDano(int dano){
		this.setDef(this.getDef() - dano);
		if(this.getDef() < 0){
			this.setVida(false);
		}
	}
}