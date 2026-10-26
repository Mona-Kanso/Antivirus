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

    public void ListaInimigos() {
        for (Inimigo inimigo : inimigos) {
            System.out.println("Inimigo: " + inimigo.getNome());
        }
    }

    public void ImprimeSetor(){

    }

    public boolean MaisDeUmJogadorNoSetor() {
        return jogadores.size() > 1;
    }

    //métodos que "sofrerão" polimorfismo nas subclasses
    public void processarAtaque(Jogador j, Inimigo i) {
        int novaDefesa = i.getDef() - j.getAtk();
        i.setDef(novaDefesa);
        System.out.println("Ataque realizado! Dano causado: " + j.getAtk());
    }

    public void processarProcura(Jogador j) {
        // implementação para a procura de itens
    }
}