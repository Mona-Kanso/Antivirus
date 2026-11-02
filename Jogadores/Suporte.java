public class Suporte extends Jogador {

	public Jogador p1;

	public Suporte (int ATK, int DEF, Jogador p1){
		super(ATK, DEF);
		this.p1 = p1;
	}
	

	public void imprimeJogador(){
		System.out.println ("P2");
		this.imprime_status();
	}

	@Override
	public void listarAcoes(){
		super.listarAcoes();
		System.out.println("[3] Recuperar defesa");
	}

	@Override
	public int realizarAcao(int acao){
		if(acao == 3){
			this.recuperarDefesa();
			return 0;
		} else{
			super.realizarAcao();
		}
	}

//n sei se isso pode
	@Override
	public void realizarTurno(){
		int acaoInvalida = 1;
		int acao;

		super.movimentar();
		for(int i = 0; i < 2; i++){
			while(acaoInvalida){
				this.listarAcoes();
				acao = scanner.nextInt();
				acaoInvalida = realizarAcao(acao);
			}
		}


	}

	public void recuperarDefesa(){
		if(jogadresNoMesmoSetor()){
			System.out.println("De qual jogador você gostaria de recuperar a defesa?")
			System.out.println("[1] P1");
			System.out.println("[2] P2");
			int jogador = 0;
			while(jogador != 1 || jogador != 2){
				jogador = scanner.nextInt();
			} 
			if(jogador == 1){
				super.aumentaDefesa(p1)
			} else{
				super.aumentaDefesa(this);
			}
		}
	}
}
