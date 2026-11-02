public class Inimigo {
    int ATK;
    int DEF;

    public void Inimigo () {}
    public void Inimigo (int ATK, int DEF){
        if (ATK > 0 && DEF > 0){
            this.setATK (ATK);
            this.setDEF (DEF);
        }
    }

    public void setATK (int ATK){this.ATK = ATK;}
    public void setDEF (int DEF){this.DEF = DEF;}

    public void getNome(){

    }

    public void atacar (Jogador p){
        p.DEF -= this.ATK;
    }

    public void imprime_inimigo (){

    }

    public void receberDano(int sla){

    }

    public int getDEF(){
        return this.DEF;
    }

    public void setDEF(){

    }

}