import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//Mona Aya Kanso | GRR:20250270
//Marcello Eduardo Pereira Jorge | GRR:20244350
//Raphael Gaias Wrublewski | GRR:20244362

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

    private static boolean todosMortos() {
        for (Jogador jogador : jogadores) {
            if (jogador.getVida()) {
                return false;
            }
        }
        return true;
    }

    private static boolean jogoAcabou() {
        return vencedor != null || ciclos >= MAX_CICLOS || todosMortos();
    }

    private static void turnoJogador(Jogador jogador){

        if(jogador.getVida()){
            System.out.println("==============================");
            System.out.print("|    TURNO DO JOGADOR ");
            jogador.imprimeJogador();
            System.out.println("    |");
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
                    if(tabuleiro.ehFonte(jogador.getSetor())){
                        vencedor = jogador;
                        return;
                    }
                    direcao = ' ';
                }

                Setor novo = jogador.getSetor();
                boolean primeiraVisita = tabuleiro.entrarNoSetor(novo);   // gera os inimigos
                jogador.imprimeJogador();
                System.out.println(" entrou no setor [" + novo.getCoordenadaX() + "," + novo.getCoordenadaY() + "] ");

                if (tabuleiro.ehFonte(novo)) {
                    vencedor = jogador;
                } 
                
                else if (primeiraVisita) {
                    System.out.println("Foram gerados " + novo.inimigos.size() + " inimigo(s) neste setor!");
                }

            }

            imprimeTabuleiroComSetores();

            jogador.executarAcoes();

        }
    }

    public static void main(String[] args) {
        iniciar();

        while (!jogoAcabou()) {
            System.out.println("\n================ CICLO " + ciclos + " de " + MAX_CICLOS + " ================");

            imprimeTabuleiro();
            for (Jogador jogador : jogadores){
                if(vencedor == null){
                    turnoJogador(jogador);
                }
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
            System.out.println(" encontrou a fonte de infeccao!");
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

    private static void imprimeTabuleiroComSetores() {
        Jogador p1 = jogadores.get(0);
        Jogador p2 = jogadores.get(1);
        tabuleiro.imprimeTabuleiroComSetores(p1.getVida() ? p1 : null, p2.getVida() ? p2 : null);
    }
}