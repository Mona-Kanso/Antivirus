import java.util.Scanner;

public abstract class Jogador implements Combatentes{
	protected int ATK;
	protected int DEF;
	protected Setor setor;
	protected boolean vida;

	//Sem construtor padrão, pois o construtor somente será chamado pelas classes filhas
	public Jogador (int atk, int def, Setor setor){
		if (atk > 0 && def > 0){
			this.setATK (atk);
			this.setDEF (def);
			this.setSetor(setor);
		}
	}

	public void setATK (int atk){
		if(atk > 0){
			this.ATK = atk;
		}
	}

	public void setDEF (int def){
		this.DEF = def;
	}

	public int getATK(){
		return this.ATK;	
	}

	public int getDEF(){
		return this.DEF;
	}

	public Setor getSetor(){
		return this.setor;
	}

	public void setSetor(Setor setor){
		if(setor != null){
			this.setor = setor;
		}
	}
	
	public void setVida(boolean vida){
		this.vida = vida;
	}

	public boolean getVida(){
		return this.vida;
	}

	//Aumenta defesa do jogador após a recuperação de defesa
	public void aumentarDefesa(int defesa){
		this.setDEF(this.getDEF() + defesa);
		System.out.print("Jogador ");
		this.imprimeJogador();
		System.out.printf(" recebeu +%d de defesa%n", defesa);
	}

	public void receberDano(int ataque){
		this.setDEF(this.getDEF() + ataque);
		if(this.getDEF() < 0){
			this.setVida(false);
		}
	}

	public boolean movimentar (char direcao){

		switch(direcao){
			case 'w':
				if(this.setor.getPortaNorte()){
					this.setSetor(this.setor.getVizinhoNorte());
					return true;
				}
				return false;

			case 'a':
				if(this.setor.getPortaOeste()){
					this.setSetor(this.setor.getVizinhoOeste());
					return true;
				}
				return false;

			case 's':
				if(this.setor.getPortaSul()){
					this.setSetor(this.setor.getVizinhoSul());
					return true;
				}
				return false;

			case 'd':
				if(this.setor.getPortaLeste()){
					this.setSetor(this.setor.getVizinhoLeste());
					return true;
				}
				return false;

			default:
				return false;
		}

	}

	public void atacar(Inimigo inimigo){
		this.setor.processarAtaqueJogador(this, inimigo);
	}
	
	public void procurar(){
		this.setor.processarProcura(this);
	}

	public boolean recuperarDefesa(int indiceJogador){
		System.out.print("Ação inválida");
		return false;
	}

	public void imprime_status(){
		System.out.printf ("%d/%d%n", this.getATK(), this.getDEF());
	}

	public abstract void imprimeJogador();

	public void listarAcoes(){
		System.out.println("Qual ação você deseja realizar?");
		System.out.println("[1] Atacar");
		System.out.println("[2] Procurar");
	}

}
