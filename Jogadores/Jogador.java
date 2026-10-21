import java.util.Scanner;

public class Jogador {
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

	public void movimentar (char direcao){
		char dir = Character.toLowerCase (direcao);

		while (dir != 'a' || dir != 'd' || dir != 'w' || dir != 's'){
			System.out.print ("Direcao incorreta")
		}
	}

	public void atacar (Inimigo i){
		if (set.tem_inimigo()){
			System.out.println ("Qual inimigo você quer atacar? ");
			set.listar_inimigo();
			Scanner scanner = new Scanner(System.in);

			int inimigo = scanner.nextInt();
			
		}
	}

	piblic void procurar (){}
	
	public void imprime_status (){
		System.out.printf ("%d/%d\n", this.getATK(), this.getDEF());
	}

	public abstract void imprime_jogador (){};

}
