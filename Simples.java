public class Simples extends Jogador {

	public Simples(Setor setor){
		super(2, 6, setor);
	}
	
	public void imprimeJogador(){
		System.out.print(Cores.ANSI_BLUE + "P1" + Cores.ANSI_RESET);
	}
}
