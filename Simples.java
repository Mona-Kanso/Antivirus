import java.util.Scanner;

public class Simples extends Jogador {

	public Simples(Setor setor, Scanner scanner){
		super(2, 6, setor, scanner);
	}
	
	public void imprimeJogador(){
		System.out.print(Cores.ANSI_BLUE + "P1" + Cores.ANSI_RESET);
	}
}
