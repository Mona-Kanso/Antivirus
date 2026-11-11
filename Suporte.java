import java.util.Scanner;

public class Suporte extends Jogador implements Curandeiro {

	public Jogador p1;

	public Suporte (Setor setor, Jogador p1, Scanner scanner){
		super(1, 7, setor, scanner);

		//suporte exige que exista um simples
		if(p1 == null){
			throw new IllegalArgumentException("O jogador de Suporte não pode existir sem um Jogador Simples (P1)");
		}
		this.p1 = p1;
	}

	public Jogador getP1(){
		return p1;
	}
	
	public void setP1(Jogador p1){
		if(p1 != null){
			this.p1 = p1;
		}
	}

	public void imprimeJogador(){
		System.out.print(Cores.ANSI_GREEN + "P2" + Cores.ANSI_RESET);
	}

	public boolean recuperarDefesa(int indiceJogador){
		if(indiceJogador == 1){
			p1.aumentarDefesa(2);
		}
		else{
			this.aumentarDefesa(2);
		}
		return true;
	}

	@Override
	public void executarAcoes(){
		for(int i = 0; i < 2; i++){
			boolean acaoValida = false;
			while(!acaoValida){
				super.listarAcoes();
				if(i == 0){
					System.out.println("[3] Recuperar defesa");
				}

				System.out.print("Qual ação você gostaria de realizar? ");

				if(!scanner.hasNextInt()){
					System.out.println("Entrada inválida! Digite apenas números.");
					scanner.nextLine();
					continue;
				}

				int acao = scanner.nextInt();
				scanner.nextLine();

				switch(acao){
					//Ação de atacar
					case 1:
						acaoValida = this.atacar();
						break;
						
					case 2:
						acaoValida = this.procurar();
						break;

					case 3:
						if(i == 0){
							if(this.setor.jogadoresNoMesmoSetor()){
								System.out.println("[1] P1");
								System.out.println("[2] P2");
								int indiceJogador = 0;
								while(indiceJogador != 1 && indiceJogador != 2){
									System.out.print("De qual jogador você gostaria de recuperar a defesa?");

									if(!scanner.hasNextInt()){
										System.out.println("Entrada inválida! Digite apenas números.");
										scanner.nextLine();
										continue;
									}									

									indiceJogador = scanner.nextInt();
									scanner.nextLine();

								} 
								this.recuperarDefesa(indiceJogador);
							}

							else{
								this.aumentarDefesa(2);
							}

							return;
						} 
						
						break;
					
					default:
						System.out.println("Opção inválida");
						acaoValida = false;;
				}
			}
		}
	}
}
