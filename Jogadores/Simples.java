public class Simples extends Jogador {
	public Simples (int ATK, int DEF){
		super(ATK, DEF);
	}
	
	public void imprime_jogador(){
		System.out.println ("P1");
		this.imprime_status();
	}
}
