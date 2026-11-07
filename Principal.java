import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Principal {

    private static final int MAX_CICLOS = 25;

    private static Scanner scanner = new Scanner(System.in);
    private static Random aleatorio = new Random();
    private static ArrayList<Jogador> jogadores = new ArrayList<>();
    private static int ciclos;
    private static Tabuleiro tabuleiro;
    private static Jogador vencedor;

    private static void iniciar() {
        tabuleiro = new Tabuleiro();
        ciclos = 0;
        Setor central = tabuleiro.retornaSetorCentral();

        Jogador p1 = new Simples(central);
        Jogador p2 = new Suporte(central, p1);
        jogadores.add(p1);
        jogadores.add(p2);

        for(Jogador jogador : jogadores){
            central.adicionaJogador(jogador);
        }

        System.out.println("==============================");
        System.out.println("|     ANTIVIRUS POR UM DIA   |");
        System.out.println("==============================");
        System.out.println("Encontrem a fonte de infeccao (X) em ate " + MAX_CICLOS + " ciclos.");
        System.out.println("P1 (simples) = ATK/DEF 2/6   P2 (suporte) = ATK/DEF 1/7");
    }


//tirar jogadores.get(1) em todas as linhas que é mencionado
    private static boolean jogoAcabou() {
        return vencedor != null || ciclos > 25 || !jogadores.get(1).getVida();
    }

    private static void turnoJogador(Jogador jogador){

        if(jogador.getVida()){
            //Movimentar o jogador
            char direcao = ' ';
            if(jogador.setor.temInimigo()){
                System.out.print("Jogador ");
                jogador.imprimeJogador();
                System.out.println(" não pode se movimentar. Ainda há inimigos no setor!");
            } 
            
            else{

                boolean movimentoRealizado = false;
                while(!movimentoRealizado){

                    while (direcao != 'a' && direcao != 'd' && direcao != 'w' && direcao != 's'){
                        System.out.print("Digite a direção do movimento: ");
                        direcao = scanner.next().charAt(0);
                        direcao = Character.toLowerCase(direcao);
                    }

                    movimentoRealizado = tabuleiro.movimentarJogador(jogador, direcao);
                }

                Setor novo = jogador.getSetor();
                boolean primeiraVisita = tabuleiro.entrarNoSetor(novo);   // gera os inimigos
                jogador.imprimeJogador();
                System.out.println("entrou no setor [" + novo.getCoordenadaX() + "," + novo.getCoordenadaY() + "] (");

                if (tabuleiro.ehFonte(novo)) {
                    vencedor = jogador;
                } 
                
                else if (primeiraVisita) {
                    System.out.println("Foram gerados " + novo.inimigos.size() + " inimigo(s) neste setor!");
                }

            }

            jogador.executarAcoes();

        }
    }

    public static void main(String[] args) {
        iniciar();

        while (!jogoAcabou()) {
            System.out.println("\n================ CICLO " + ciclos + " de " + MAX_CICLOS + " ================");

            imprimeTabuleiro();
            for (Jogador jogador : jogadores) {
                turnoJogador(jogador);
            }

            if (vencedor == null) {
                turnoInimigos();
            }
            ciclos++;
        }

        fimDeJogo();
    }

    private static void turnoInimigos() {
        System.out.println("\n--------------- Turno dos inimigos ---------------");

        Setor setor;
        ArrayList<Inimigo> inimigos;
        for (Jogador jogador : jogadores) {
            setor = jogador.getSetor();
            inimigos = setor.getInimigos();
            for(Inimigo inimigo : inimigos){
                if (jogador.getVida()) {
                    inimigo.atacar(jogador);
                }
            }
        }
    }

    private static void fimDeJogo() {
        System.out.println("\n==============================================");
        imprimeTabuleiro();
        if (vencedor != null) {
            System.out.print("VITORIA! ");
            vencedor.imprimeJogador();
            System.out.println("encontrou a fonte de infeccao!");
        } else if (!jogadores.get(1).getVida()) {
            System.out.println("DERROTA! O jogador simples morreu.");
        } else {
            System.out.println("FIM DOS CICLOS! A fonte de infeccao nao foi encontrada a tempo.");
        }
        System.out.println("==============================================");
    }

    // ------------------------------------------------------------------ apresentacao
    private static void imprimeTabuleiro() {
        Jogador p1 = jogadores.get(0);
        Jogador p2 = jogadores.get(1);
        // Jogadores eliminados nao sao desenhados (o Tabuleiro ignora null).
        tabuleiro.imprimeTabuleiro(p1.getVida() ? p1 : null, p2.getVida() ? p2 : null);
    }

    // Setor ampliado dos jogadores vivos: um só se estiverem juntos, dois se separados.
    private static void imprimeSetores() {
        Jogador p1 = jogadores.get(0);
        Jogador p2 = jogadores.get(1);
        System.out.println();
        tabuleiro.imprimeSetores(p1.getVida() ? p1 : null, p2.getVida() ? p2 : null);
    }

    private static void mostrarSituacao() {
        System.out.println();
        imprimeTabuleiro();
        imprimeSetores();
        Setor setor;
        for (Jogador jogador : jogadores) {
            setor = jogador.getSetor();
            setor.listarInimigos();
            jogador.imprimeJogador();
            System.out.printf(": ATK/DEF %d/%d", jogador.getAtk(), jogador.getDef());  
            setor.imprimeTitulo();
            if(!jogador.getVida()){
                System.out.println(" (ELIMINADO)");
            }
        }
    }
}