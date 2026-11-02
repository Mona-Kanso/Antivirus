import java.util.ArrayList;

public abstract class Setor {
    protected int x;
    protected int y;
    
    //status das portas (true = porta, false = parede)
    //protected pois as subclasses precisam acessar, mas não queremos que outras classes acessem diretamente
    protected boolean portaNorte, portaSul, portaLeste, portaOeste;
    
    //referências para os setores vizinhos
    //protected pois as subclasses precisam acessar, mas não queremos que outras classes acessem diretamente
    protected Setor vizinhoNorte, vizinhoSul, vizinhoLeste, vizinhoOeste;

    //coleções?
    protected ArrayList<Jogador> jogadores;
    protected ArrayList<Inimigo> inimigos;

    public Setor(int x, int y) {
        this.x = x;
        this.y = y;
        this.jogadores = new ArrayList<>();
        this.inimigos = new ArrayList<>();
    }

    public void setPortaNorte(boolean porta){
        this.portaNorte = porta;
    }

    public void setPortaSul(boolean porta){
        this.portaSul = porta;
    }

    public void setPortaLeste(boolean porta){
        this.portaLeste = porta;
    }

    public void setPortaOeste(boolean porta){
        this.portaOeste = porta;
    }


    public boolean getPortaNorte(){
        return this.portaNorte;
    }

    public boolean getPortaSul(){
        return this.portaSul;
    }

    public boolean getPortaLeste(){
        return this.portaLeste;
    }

    public boolean getPortaOeste(){
        return this.portaOeste;
    }

    public Setor getVizinhoNorte(){
        return this.vizinhoNorte;
    }

    public Setor getVizinhoSul(){
        return this.vizinhoSul;
    }

    public Setor getVizinhoLeste(){
        return this.vizinhoLeste;
    }

    public Setor getVizinhoOeste(){
        return this.vizinhoOeste;
    }

    public void setVizinhoNorte(Setor setor){
        if(setor != null){
            this.vizinhoNorte = setor;
        }
    }

    public void setVizinhoSul(Setor setor){
        if(setor != null){
            this.vizinhoSul = setor;
        }
    }

    public void setVizinhoLeste(Setor setor){
        if(setor != null){
            this.vizinhoLeste = setor;
        }
    }

    public void setVizinhoOeste(Setor setor){
        if(setor != null){
            this.vizinhoOeste = setor;
        }
    }

    public Inimigo retornarInimigo(int indice){
        return this.inimigos.get(indice);
    }

    public boolean temInimigo() {
        return !inimigos.isEmpty();
    }

    public void listarInimigos() {
        for (Inimigo inimigo : inimigos) {
            System.out.print("Inimigo: ");
            inimigo.getNome();
        }
    }

    //depende da implementação do tabuleiro
    public void ImprimeSetor(){

    }

    public boolean jogadoresNoMesmoSetor() {
        return jogadores.size() > 1;
    }

    //métodos que "sofrerão" polimorfismo nas subclasses
    public void processarAtaqueJogador(Jogador j, Inimigo i) {
        i.receberDano(j.getATK());
        System.out.println("Ataque realizado! Dano causado: " + j.getATK());

        this.inimigos.removeIf(inimigo -> inimigo.getDEF() <= 0);
    }

    public void processarAtaqueInimigo(){
        
    }

    public void processarProcura(Jogador j) {
        java.util.Random aleatorio = new java.util.Random();
        int sorteio = aleatorio.nextInt(6) + 1;
        
        if (sorteio >= 1 && sorteio <= 3) {
            System.out.println("Nada foi encontrado no setor.");
        } else if (sorteio == 4) {
            System.out.print("Encontrou um item!");
            j.aumentarDefesa(1);
        } else if (sorteio == 5) {
            System.out.print("Encontrou um item raro!");
            j.aumentarDefesa(2);
        } else if (sorteio == 6) {
            System.out.println("Vulnerabilidade encontrada! Todos os inimigos do setor perdem 1 de DEF."); 
            
            for (int i = 0; i < this.inimigos.size(); i++) {
                Inimigo inimigo = this.inimigos.get(i);
                inimigo.setDEF(inimigo.getDEF() - 1);
            }
            
            // Limpa da coleção os inimigos que foram eliminados por esta ação
            this.inimigos.removeIf(inimigo -> inimigo.getDEF() <= 0);
        }
    }
}