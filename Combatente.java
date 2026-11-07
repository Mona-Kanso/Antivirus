public abstract class Combatente{
    protected int atk;
    protected int def;
    protected boolean vida;

    public Combatente(int atk, int def){
        this.setAtk(atk);
        this.setDef(def);
        this.vida = true;
    }

    public int getAtk(){
        return this.atk;
    }

    public int getDef(){
        return this.def;
    }
    
	public void setAtk(int atk){
		if(atk > 0){
			this.atk= atk;
		}
	}

    public void setDef(int def){
        this.def = def;
    }

    public boolean getVida(){
        return this.vida;
    }

    public void setVida(boolean vida){
        this.vida = vida;
    }
    
	public void receberDano(int atk){
		this.setDef(this.getDef() - atk);
		if(this.getDef() < 0){
			this.setVida(false);
            System.out.println("Morte de combatente");
		}
	}

    public void imprimirStatus(){
        System.out.printf("%d/%d", this.getAtk(), this.getDef());
    }
}