public class Suporte extends Jogador {
	public Suporte (int ATK, int DEF){
		super(ATK, DEF);
	}
	
	public void recuperar (Jogador p){
		
		if (p.set == this.set)
			p.DEF += 2;
		else 
			System.out.println ("Jogador nao esta no setor!");
	}

	public void imprime_jogador(){
		System.out.println ("P2");
		this.imprime_status();
	}
}
