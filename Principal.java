import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Principal {

    private static final int MAX_CICLOS = 25;
    private static final int ACOES_POR_TURNO = 2;

    private final Scanner teclado = new Scanner(System.in);
    private final Random aleatorio = new Random();
    private final ArrayList<Jogador> jogadores = new ArrayList<>();   // [P1, P2]
    private Tabuleiro tabuleiro;
    private Jogador vencedor;

    public static void main(String[] args) {
        try {
            new Principal().jogar();
        } catch (IllegalStateException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    // ------------------------------------------------------------------ fluxo geral
    public void jogar() {
        iniciar();

        turnoZero();

        int ciclo = 1;
        while (!jogoAcabou() && ciclo <= MAX_CICLOS) {
            System.out.println("\n================ CICLO " + ciclo + " de " + MAX_CICLOS + " ================");

            for (Jogador j : jogadores) {
                if (j.getVida() && vencedor == null) {
                    turnoJogador(j);
                }
            }
            if (vencedor == null) {
                turnoInimigos();
            }
            ++ciclo;
        }

        fimDeJogo();
    }

    private void iniciar() {
        tabuleiro = new Tabuleiro();

        Simples p1 = new Simples(tabuleiro.getSetorCentral());
        Suporte p2 = new Suporte(tabuleiro.getSetorCentral(), p1);
        jogadores.add(p1);
        jogadores.add(p2);

        // Jogador.vida comeca em false; garantimos que os dois iniciam vivos.
        for (Jogador j : jogadores) {
            j.setVida(true);
        }

        System.out.println("==============================");
        System.out.println("|     ANTIVIRUS POR UM DIA   |");
        System.out.println("==============================");
        System.out.println("Encontrem a fonte de infeccao (X) em ate " + MAX_CICLOS + " ciclos.");
        System.out.println("P1 (simples) = ATK/DEF 2/6   P2 (suporte) = ATK/DEF 1/7");
    }

    // Turno 0: cada jogador, comecando por P1, escolhe uma porta do setor inicial.
    private void turnoZero() {
        System.out.println("\n========== TURNO 0: escolham uma porta do setor inicial ==========");
        for (Jogador j : jogadores) {
            if (vencedor != null) {
                return;
            }
            System.out.println("\n--- Vez de " + nomeDe(j) + " ---");
            imprimeTabuleiro();
            while (!movimentar(j)) {
                // repete ate o jogador escolher uma porta valida
            }
        }
    }

    // ------------------------------------------------------------------ turno dos jogadores
    private void turnoJogador(Jogador j) {
        System.out.println("\n--------------- Turno de " + nomeDe(j) + " ---------------");

        int acoesFeitas = 0;
        while (acoesFeitas < ACOES_POR_TURNO && vencedor == null) {
            mostrarSituacao(j);
            System.out.println("\nAcao " + (acoesFeitas + 1) + " de " + ACOES_POR_TURNO);
            j.listarAcoes();                       // polimorfismo: o suporte lista uma opcao a mais
            System.out.println("[4] Movimentar");
            int escolha = lerInteiro("> ", 1, 4);

            if (executarAcao(j, escolha)) {
                ++acoesFeitas;
            }
        }
    }

    // Devolve true se a acao foi realizada (e deve contar no turno).
    private boolean executarAcao(Jogador j, int escolha) {
        switch (escolha) {
            case 1:  return atacar(j);
            case 2:  return procurar(j);
            case 3:  return recuperarDefesa(j);
            case 4:  return movimentar(j);
            default: return false;
        }
    }

    private boolean atacar(Jogador j) {
        Setor setor = j.getSetor();
        if (!setor.temInimigo()) {
            System.out.println("Nao ha inimigos neste setor para atacar.");
            return false;
        }
        listarInimigos(setor);
        int total = setor.inimigos.size();
        int indice = lerInteiro("Qual inimigo atacar? [1-" + total + "] > ", 1, total) - 1;

        j.atacar(setor.retornarInimigo(indice));   // polimorfismo: depende do tipo do setor
        return true;
    }

    private boolean procurar(Jogador j) {
        // Em setor privado a acao e proibida; nao gastamos a acao do jogador.
        if (j.getSetor() instanceof SetorPrivado) {
            System.out.println("Procurar nao e permitido em setores privados.");
            return false;
        }
        j.procurar();
        return true;
    }

    private boolean recuperarDefesa(Jogador j) {
        Jogador p1 = jogadores.get(0);
        if (j == p1) {
            System.out.println("Acao invalida: so o jogador de suporte recupera defesa.");
            return false;
        }

        int alvo = 2;   // por padrao, o proprio suporte
        if (p1.getVida() && p1.getSetor() == j.getSetor()) {
            System.out.println("Recuperar defesa de quem? [1] P1  [2] P2 (voce)");
            alvo = lerInteiro("> ", 1, 2);
        }
        return j.recuperarDefesa(alvo);
    }

    // Move o jogador por uma porta do setor atual. Devolve true se conseguiu.
    private boolean movimentar(Jogador j) {
        Setor atual = j.getSetor();
        if (atual.temInimigo()) {
            System.out.println("Voce nao pode se mover: ainda ha inimigos no setor!");
            return false;
        }

        char direcao = lerDirecao(atual);
        if (!j.movimentar(direcao)) {
            System.out.println("Nao ha porta nessa direcao.");
            return false;
        }

        Setor novo = j.getSetor();
        boolean primeiraVisita = tabuleiro.entrarNoSetor(novo);   // gera os inimigos
        System.out.println(nomeDe(j) + " entrou no setor [" + novo.x + "," + novo.y + "] ("
                + tipoDoSetor(novo) + ").");

        if (tabuleiro.ehFonte(novo)) {
            vencedor = j;
        } else if (primeiraVisita) {
            System.out.println("Foram gerados " + novo.inimigos.size() + " inimigo(s) neste setor!");
        }
        return true;
    }

    // ------------------------------------------------------------------ turno dos inimigos
    private void turnoInimigos() {
        System.out.println("\n--------------- Turno dos inimigos ---------------");

        ArrayList<Setor> setoresAtacados = new ArrayList<>();
        for (Jogador j : jogadores) {
            Setor s = j.getSetor();
            if (j.getVida() && !setoresAtacados.contains(s)) {
                setoresAtacados.add(s);
                ataquesDoSetor(s);
            }
        }
    }

    // Cada inimigo vivo do setor ataca cada jogador vivo que esta nele, uma vez.
    private void ataquesDoSetor(Setor s) {
        int numero = 0;
        for (Inimigo inimigo : s.inimigos) {
            ++numero;
            for (Jogador alvo : jogadores) {
                if (!alvo.getVida() || alvo.getSetor() != s) {
                    continue;
                }
                int dado = aleatorio.nextInt(6) + 1;
                System.out.print("Inimigo " + numero + " (" + inimigo.ATK + "/" + inimigo.DEF
                        + ") ataca " + nomeDe(alvo) + " - dado " + dado + ": ");

                if (dado % 2 == 0) {
                    int antes = alvo.getDEF();
                    inimigo.atacar(alvo);
                    System.out.println("acertou! DEF " + antes + " -> " + alvo.getDEF());
                    if (alvo.getDEF() <= 0) {
                        alvo.setVida(false);
                        System.out.println(">>> " + nomeDe(alvo) + " foi eliminado!");
                    }
                } else {
                    System.out.println("errou.");
                }
            }
        }
    }

    // ------------------------------------------------------------------ fim de jogo
    private boolean todosEliminados() {
        for (Jogador j : jogadores) {
            if (j.getVida()) {
                return false;
            }
        }
        return true;
    }

    private boolean jogoAcabou() {
        return vencedor != null || todosEliminados();
    }

    private void fimDeJogo() {
        System.out.println("\n==============================================");
        imprimeTabuleiro();
        if (vencedor != null) {
            System.out.println("VITORIA! " + nomeDe(vencedor) + " encontrou a fonte de infeccao!");
        } else if (todosEliminados()) {
            System.out.println("DERROTA! Todos os jogadores foram eliminados pelos virus.");
        } else {
            System.out.println("FIM DOS CICLOS! A fonte de infeccao nao foi encontrada a tempo.");
        }
        System.out.println("==============================================");
    }

    // ------------------------------------------------------------------ apresentacao
    private void imprimeTabuleiro() {
        Jogador p1 = jogadores.get(0);
        Jogador p2 = jogadores.get(1);
        // Jogadores eliminados nao sao desenhados (o Tabuleiro ignora null).
        tabuleiro.imprimeTabuleiro(p1.getVida() ? p1 : null, p2.getVida() ? p2 : null);
    }

    // Setor ampliado dos jogadores vivos: um só se estiverem juntos, dois se separados.
    private void imprimeSetores() {
        Jogador p1 = jogadores.get(0);
        Jogador p2 = jogadores.get(1);
        System.out.println();
        tabuleiro.imprimeSetores(p1.getVida() ? p1 : null, p2.getVida() ? p2 : null);
    }

    private void mostrarSituacao(Jogador j) {
        System.out.println();
        imprimeTabuleiro();

        Setor s = j.getSetor();
        imprimeSetores ();
        listarInimigos(s);
        for (Jogador p : jogadores) {
            System.out.println(nomeDe(p) + ": ATK/DEF " + p.getATK() + "/" + p.getDEF()
                    + " - setor [" + p.getSetor().x + "," + p.getSetor().y + "]"
                    + (p.getVida() ? "" : " (ELIMINADO)"));
        }
    }

    private void listarInimigos(Setor s) {
        if (!s.temInimigo()) {
            System.out.println("Inimigos no setor: nenhum");
            return;
        }
        System.out.println("Inimigos no setor:");
        for (int i = 0; i < s.inimigos.size(); ++i) {
            Inimigo inimigo = s.retornarInimigo(i);
            System.out.println("  [" + (i + 1) + "] ATK/DEF " + inimigo.getATK() + "/" + inimigo.getDEF());
        }
    }

    private String nomeDe(Jogador j) {
        return "P" + (jogadores.indexOf(j) + 1);
    }

    // "SetorOculto" -> "Oculto"
    private String tipoDoSetor(Setor s) {
        return s.getClass().getSimpleName().replace("Setor", "");
    }

    // ------------------------------------------------------------------ entrada de dados
    private String lerLinha(String prompt) {
        System.out.print(prompt);
        if (!teclado.hasNextLine()) {
            throw new IllegalStateException("Entrada encerrada. Jogo interrompido.");
        }
        return teclado.nextLine().trim();
    }

    private int lerInteiro(String prompt, int min, int max) {
        while (true) {
            String texto = lerLinha(prompt);
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("Digite um numero entre " + min + " e " + max + ".");
        }
    }

    private char lerDirecao(Setor s) {
        StringBuilder opcoes = new StringBuilder("Portas disponiveis:");
        if (s.getPortaNorte()) opcoes.append(" [w] Norte");
        if (s.getPortaSul())   opcoes.append(" [s] Sul");
        if (s.getPortaOeste()) opcoes.append(" [a] Oeste");
        if (s.getPortaLeste()) opcoes.append(" [d] Leste");
        System.out.println(opcoes);

        while (true) {
            String texto = lerLinha("Para onde deseja ir? > ").toLowerCase();
            if (texto.length() == 1 && "wasd".indexOf(texto.charAt(0)) >= 0) {
                return texto.charAt(0);
            }
            System.out.println("Digite w, a, s ou d.");
        }
    }
}