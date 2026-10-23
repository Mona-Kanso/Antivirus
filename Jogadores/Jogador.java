import java.util.Scanner;

public absctract class Jogador {
	int ATK;
	int DEF;
	Setor set; //verificar

	public void Jogador () {}

	public void Jogador (int atk, int def){
		if (atk > 0 && def > 0){
			this.setATK (atk);
			this.setDEF (def);
			this.set = new Setor (3, 3);
		}
	}

	public setATK (int atk) {this.ATK = atk;}
	public setDEF (int def) {this.DEF = def;}

	public int getATK () {return this.ATK;}
	public int getDEF () {return this.DEF;}
	
	public void check_porta (char dir){

	}

	public void aumentaDefesa(){
		this.DEF = this.DEF + 2;
	}

	public void movimentar (){

		char direcao;
		direcao = scanner.next().charAt(0);
		direcao = Character.toLowerCase(direcao);

		while (direcao != 'a' || direcao != 'd' || direcao != 'w' || direcao != 's'){
			System.out.print ("Direcao incorreta");
			direcao = scanner.next().charAt(0);
			direcao = Character.toLowerCase(direcao);
		}
	}

	public void atacar (Inimigo i){
		if (set.temInimigo()){
			System.out.println ("Qual inimigo você quer atacar? ");
			set.listarInimigo();
			Scanner scanner = new Scanner(System.in);

			int inimigo = scanner.nextInt();
			
		}
	}

	piblic void procurar (){}
	
	public void imprime_status (){
		System.out.printf ("%d/%d\n", this.getATK(), this.getDEF());
	}

	public abstract void imprimeJogador (){};

	public void movimentar(){
		if(!temInimigo(set)){
			this.movimentar();
		}
	}

	public void listarAcoes(){
		System.out.println("Qual ação você deseja realizar?");
		System.out.println("[1] Atacar");
		System.out.println("[2] Procurar");
	}

	public int realizarAcao(int acao){
		switch (acao){
			case == 1:
				this.atacar();
				return 0;
			case == 2:
				this.procurar();
				return 0;
			default:
				System.out.println("Ação inválida");
				return 1;
		}
	}

	public void realizarTurno(){
		int acao;
		int acaoInvalida;
		this.movimentar();

		for(int i = 0; i < 2; i++){
			while(acaoInvalida){
				this.listarAcoes();
				acao = scanner.nextInt();
				acaoInvalida = this.realizarAcao();
			}
		}
	}

}
