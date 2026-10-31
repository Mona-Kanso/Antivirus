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

    //depende da implementação do tabuleiro
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
        java.util.Random aleatorio = new java.util.Random();
        int sorteio = aleatorio.nextInt(6) + 1;
        
        if (sorteio >= 1 && sorteio <= 3) {
            System.out.println("Nada foi encontrado no setor.");
        } else if (sorteio == 4) {
            j.aumentarDefesa(1);//implementada em Jogador.java
            System.out.println("Encontrou um item! Ganhou 1 de DEF.");
        } else if (sorteio == 5) {
            j.aumentarDefesa(2);//implementada em Jogador.java
            System.out.println("Encontrou um item raro! Ganhou 2 de DEF.");
        } else if (sorteio == 6) {
            System.out.println("Vulnerabilidade encontrada! Todos os inimigos do setor perdem 1 de DEF."); 
            
            for (int i = 0; i < this.inimigos.size(); i++) {
                Inimigo inimigo = this.inimigos.get(i);
                inimigo.setDef(inimigo.getDef() - 1);
            }
            
            // Limpa da coleção os inimigos que foram eliminados por esta ação
            this.inimigos.removeIf(inimigo -> inimigo.getDef() <= 0);
        }
    }
}