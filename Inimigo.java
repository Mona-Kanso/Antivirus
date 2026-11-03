public class Inimigo {
    int ATK;
    int DEF;

    public Inimigo () {}
    public Inimigo (int forca){
        System.out.println (forca);
        if (forca > 0){
            this.setATK (forca);
            this.setDEF (forca);
        }
    }

    public void setATK (int ATK){this.ATK = ATK;}
    public void setDEF (int DEF){this.DEF = DEF;}

    public int getATK () {return this.ATK;}
    public int getDEF () {return this.DEF;}

    public void getInimigo(){
        System.out.print (this.getDEF() + "/" + this.getATK() + " ");
    }

    public void atacar (Jogador p){
        int newDEF = p.getDEF() - this.getATK();
        p.setDEF(newDEF);
    }

    public void receberDano(int atk){
        int newDEF = this.getDEF() - atk;
        this.setDEF(newDEF);
    }

}