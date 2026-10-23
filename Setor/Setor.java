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

    public boolean tem_inimigo() {
        return !inimigos.isEmpty();
    }
    
    //métodos que "sofrerão" polimorfismo nas subclasses
    public void processarAtaque(Jogador j, Inimigo i) {
        // Implementação padrão para o ataque
    }


    public void processarProcura(Jogador j) {
        // Implementação padrão para a procura de itens
    }
}