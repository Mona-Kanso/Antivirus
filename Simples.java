public class Simples extends Jogador {

	public Simples(Setor setor){
		super(2, 6, setor);
	}
	
	public void imprimeJogador(){
		System.out.println ("P1");
		this.imprime_status();
	}
}
