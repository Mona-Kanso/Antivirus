public class Inimigo {
    int ATK;
    int DEF;
    Bool vida;

    public void Inimigo () {}
    public void Inimigo (int ATK, int DEF){
        if (ATK > 0 && DEF > 0){
            this.setATK (ATK);
            this.setDEF (DEF);
        }
    }

    public setATK (int ATK){this.ATK = ATK;}
    public setDEF (int DEF){this.DEF = DEF;}

    public Bool getVida (){return this.vida;}

    public void atacar (Jogador p){
        p.DEF -= this.ATK;
    }

    public void imprime_inimigo (){

    }

}