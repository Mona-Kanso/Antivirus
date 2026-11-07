import java.util.Scanner;

public abstract class Jogador extends Combatente {

	protected Setor setor;
	protected Scanner scanner;

	public Jogador (int atk, int def, Setor setor){
		super(atk, def);
		this.setSetor(setor);
		scanner = new Scanner(System.in);
	}

	public Setor getSetor(){
		return this.setor;
	}

	public void setSetor(Setor setor){
		if(setor != null){
			this.setor = setor;
		}
	}

	public void aumentarDefesa(int defesa){
		this.setDef(this.getDef() + defesa);
		System.out.print("Jogador ");
		this.imprimeJogador();
		System.out.printf(" recebeu +%d de defesa%n", defesa);
	}

	public void receberDano(int dano){
		this.setDef(this.getDef() - dano);
		if(this.getDef() < 0){
			this.setVida(false);
		}
	}

	public boolean atacar(){
		if(this.setor.temInimigo()){
			Inimigo inimigoAtacado = null;
			int indiceInimigoAtacado;

			while(inimigoAtacado == null){
				this.setor.listarInimigos();
				System.out.print("Qual inimigo você gostaria de atacar? ");
				indiceInimigoAtacado = scanner.nextInt();
				inimigoAtacado = this.setor.retornarInimigo(indiceInimigoAtacado); 
				if(inimigoAtacado == null){
					System.out.println("Inimigo inválido");
				}
			}

			this.setor.processarAtaqueJogador(this, inimigoAtacado);
			return true;
		
		} else{
			return false;
		}
		
	}
	
	public boolean procurar(){
		return this.setor.processarProcura(this);
	}

	public void imprime_status(){
		System.out.printf ("%d/%d%n", this.getAtk(), this.getDef());
	}

	public abstract void imprimeJogador();

	public void listarAcoes(){
		System.out.println("Qual ação você deseja realizar?");
		System.out.println("[1] Atacar");
		System.out.println("[2] Procurar");
	}

	public void executarAcoes(){
		int jogadorPreso = 0;
		for(int i = 0; i < 2; i++){
			boolean acaoValida = false;
			while(!acaoValida){
				this.listarAcoes();
				System.out.print("Qual ação você gostaria de realizar? ");
				int acao = scanner.nextInt();

				switch(acao){
					case 1:
						acaoValida = this.atacar();
						jogadorPreso++;
						break;
						
					case 2:
						acaoValida = this.procurar();
						jogadorPreso++;
						break;
					
					default:
						jogadorPreso++;
						break;
				}

				if(!acaoValida){
					System.out.println("AÇÃO INVÁLIDA");
				}

				if(jogadorPreso > 10){
					System.out.println("=========== AÇÃO PULADA =========== ");
					return;
				}
			}
		}
	}
}

