public class Simples extends Jogador {

	public Simples(Setor setor){
		int ATK = 2;
		int DEF = 6;
		super(ATK, DEF, setor);
	}
	
	public void imprimeJogador(){
		System.out.println ("P1");
		this.imprime_status();
	}
}
