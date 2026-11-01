public class Suporte extends Jogador {

	public Jogador p1;

	public Suporte (Setor setor, Jogador p1){
		super(1, 7, setor);
		this.setP1(p1);
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
		System.out.println ("P2");
		this.imprime_status();
	}

	@Override
	public void listarAcoes(){
		super.listarAcoes();
		System.out.println("[3] Recuperar defesa");
	}

	@Override
	public boolean recuperarDefesa(int indiceJogador){
		if(indiceJogador == 1){
			p1.aumentarDefesa(2);
		}
		else{
			this.aumentarDefesa(2);
		}
		return true;
	}

}
