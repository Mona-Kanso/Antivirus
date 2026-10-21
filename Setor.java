public class Setor {
    Int x;
    Int y;
    Jogador[] jogadores;
    Inimigo[] inimigos;

    public void Setor (){}
    public void Setor (Int x, Int y){
        this.setX (x);
        this.setY (y);
    }

    public void setX (Int x){this.x = x;}
    public void setY (Int y){this.y = y;}

    public Bool tem_inimigo(){
        for (Int i = 0; i < inimigos.length(); i++){
            if (inimigos[i].getVida()){
                return 1;
            }
        }
        return 0;
    }
    
    
}