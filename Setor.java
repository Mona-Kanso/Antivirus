import java.util.ArrayList;
import java.util.Iterator;

public abstract class Setor {
    protected int coordenadaX;
    protected int coordenadaY;
    protected boolean visitado;
    
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
        this.jogadores = new ArrayList<>();
        this.inimigos = new ArrayList<>();
        this.setCoordenadaX(x);
        this.setCoordenadaY(y);
        this.setVisitado(false);
    }


    public ArrayList<Jogador> getJogadores(){
        return this.jogadores;
    }

    public ArrayList<Inimigo> getInimigos(){
        return this.inimigos;
    }

    public int setCoordenadaX(int x){
        return this.coordenadaX = x;
    }

    public int setCoordenadaY(int y){
        return this.coordenadaY = y;
    }

    public int getCoordenadaX(){
        return this.coordenadaX;
    }

    public int getCoordenadaY(){
        return this.coordenadaY;
    }

    public boolean getVisitado(){
        return this.visitado;
    }

    public void setVisitado(boolean visitado){
        this.visitado = visitado;
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
        if (indice < 1 || indice > this.inimigos.size()) {
            return null;
        }
        return this.inimigos.get(indice-1);
    }

    public boolean temInimigo() {
        return !inimigos.isEmpty();
    }

    public boolean temJogador(Jogador jogador){
        return jogadores.contains(jogador);
    }

    public void listarInimigos() {
        System.out.println("Inimigos no setor:");
        Iterator<Inimigo> i = inimigos.iterator();
        Inimigo inimigo;
        int contador = 1;
        while(i.hasNext()){
            inimigo = i.next();
            System.out.println("  [" + (contador) + "] ATK/Def " + inimigo.getAtk() + "/" + inimigo.getDef());
            contador++;
        }
    }

    public String getTitulo(){
        return String.format("Setor [%d, %d]", this.getCoordenadaX(), this.getCoordenadaY());
    }

    public void imprimeTitulo(){
        System.out.print(this.getTitulo());
    }

    public boolean jogadoresNoMesmoSetor() {
        return jogadores.size() > 1;
    }

    //métodos que "sofrerão" polimorfismo nas subclasses
    public void processarAtaqueJogador(Jogador jogador, Inimigo inimigo) {
        inimigo.receberDano(jogador.getAtk());
        System.out.println("Ataque realizado! Dano causado: " + jogador.getAtk());

        if(inimigo.getDef() <= 0){
            this.removerInimigo(inimigo);
        }
    }

    public boolean processarProcura(Jogador jogador) {
        java.util.Random aleatorio = new java.util.Random();
        int sorteio = aleatorio.nextInt(6) + 1;
        
        if (sorteio >= 1 && sorteio <= 3) {
            System.out.println("Nada foi encontrado no setor.");
        } else if (sorteio == 4) {
            System.out.print("Encontrou um item!");
            jogador.aumentarDefesa(1);
        } else if (sorteio == 5) {
            System.out.print("Encontrou um item raro!");
            jogador.aumentarDefesa(2);
        } else if (sorteio == 6) {
            System.out.println("Vulnerabilidade encontrada! Todos os inimigos do setor perdem 1 de Def."); 
            
            for (int i = 0; i < this.inimigos.size(); i++) {
                Inimigo inimigo = this.inimigos.get(i);
                inimigo.receberDano(1);
                if(inimigo.getDef() < 0){
                    inimigos.remove(inimigos.indexOf(inimigo));
                }
            }            
        }

        return true;
    }

    public void adicionaInimigo(Inimigo inimigo){
        inimigos.add(inimigo);
    }

    public void adicionaJogador(Jogador jogador){
        jogadores.add(jogador);
    }

    public void removerJogador(Jogador jogador){
        jogadores.remove(jogadores.indexOf(jogador));
    }

    public void removerInimigo(Inimigo inimigo){
        inimigos.remove(inimigo);
    }

}