import java.util.ArrayList;
import java.util.Random;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class Tabuleiro {

        // Executa um desenho e devolve o texto que ele imprimiria, sem mostrar nada na tela.
    private String capturar(Runnable desenho) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            desenho.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString();
    }
    
    // tira a cor do texto para fazer o calculo do espaco necessario
    private String semCor(String s) { return s.replaceAll("\u001B\\[[;\\d]*m", ""); }

    // Tabuleiro à esquerda, setor(es) ampliado(s) à direita.
    public void imprimeTabuleiroComSetores(Jogador p1, Jogador p2) {
        String[] tab = capturar(() -> imprimeTabuleiro(p1, p2)).split("\\R");
        String[] set = capturar(() -> imprimeSetores(p1, p2)).split("\\R");

        int larguraTab = 0;
        for (String linha : tab) {
            String linhaSemCor = semCor (linha);
            larguraTab = Math.max(larguraTab, linhaSemCor.length());
        }

        final int DESLOCAMENTO = 2;   // o setor começa na altura da linha 1 do tabuleiro
        int total = Math.max(tab.length, set.length + DESLOCAMENTO);

        for (int i = 0; i < total; ++i) {
            String esquerda = i < tab.length ? tab[i] : "";
            int j = i - DESLOCAMENTO;
            String direita = (j >= 0 && j < set.length) ? set[j] : "";
            System.out.println(String.format("%-" + larguraTab + "s", esquerda) + "    " + direita);
        }
    }

    private static final int TAMANHO = 5;
    private static final int CENTRO = 3;
    int fonteX;
    int fonteY;

    // Layout do setor ampliado: largura interna da caixa e espaco de cada coluna.
    private static final int LARGURA_SETOR = 13;
    private static final int LARGURA_COLUNA = 22;
    private static final int LINHAS_SETOR = 9;

    // N = normal, O = oculto, P = privado (o centro deve ser normal).
    private static final String[] MAPA_TIPOS = {
        "NOPNN",
        "ONNOP",
        "PNNNO",
        "NONPN",
        "NPONP"
    };

    private final Setor[][] setores = new Setor[TAMANHO][TAMANHO];          // [y][x]
    private final Random aleatorio = new Random();

    private boolean portasLR[][] = {
        {true, false, true, true},
        {true, true, false, true}, 
        {true, true, true, true},
        {false, true, true, false},
        {true, false, true, true}
    };

    private boolean portasUD[][] = {
        {true, false, true, true, false},
        {false, true, true, false, true},
        {true, false, true, false, true},
        {false, true, false, true, true}
    };

    public Setor getSetor(int x, int y) {
        return setores[y - 1][x - 1];
    }

    public Tabuleiro() {
        criaSetores();
        ligaSetores();

        // Os jogadores comecam no centro, que ja conta como visitado (sem inimigos).
        Setor central = retornaSetorCentral();
        central.setVisitado(true);

        // Fonte de infeccao: qualquer posicao, menos o centro.
        do {
            fonteX = aleatorio.nextInt(TAMANHO) + 1;
            fonteY = aleatorio.nextInt(TAMANHO) + 1;
            
        } while (fonteX == CENTRO && fonteY == CENTRO);
    }

    public boolean movimentarJogador(Jogador jogador, char direcao){

        Setor setorAntigo = jogador.getSetor();

		switch(direcao){
			case 'w':
				if(setorAntigo.getPortaNorte()){
					jogador.setSetor(setorAntigo.getVizinhoNorte());
                    setorAntigo.getVizinhoNorte().adicionaJogador(jogador);
					setorAntigo.removerJogador(jogador);
					return true;
				}
				return false;

			case 'a':
				if(setorAntigo.getPortaOeste()){
					jogador.setSetor(setorAntigo.getVizinhoOeste());
                    setorAntigo.getVizinhoOeste().adicionaJogador(jogador);
                    setorAntigo.removerJogador(jogador);
					return true;
				}
				return false;

			case 's':
				if(setorAntigo.getPortaSul()){
					jogador.setSetor(setorAntigo.getVizinhoSul());
                    setorAntigo.getVizinhoSul().adicionaJogador(jogador);
                    setorAntigo.removerJogador(jogador);
					return true;
				}
				return false;

			case 'd':
				if(setorAntigo.getPortaLeste()){
					jogador.setSetor(setorAntigo.getVizinhoLeste());
                    setorAntigo.getVizinhoLeste().adicionaJogador(jogador);
                    setorAntigo.removerJogador(jogador);
					return true;
				}
				return false;

			default:
				return false;
		}

	}

    // ---------------------------------------------------------------- montagem

    private void criaSetores() {
        for (int y = 1; y <= TAMANHO; ++y) {
            for (int x = 1; x <= TAMANHO; ++x) {
                char tipo = MAPA_TIPOS[y - 1].charAt(x - 1);
                Setor setor;
                switch (tipo) {
                    case 'O': setor = new SetorOculto(x, y);  break;
                    case 'P': setor = new SetorPrivado(x, y); break;
                    default:  setor = new SetorNormal(x, y);  break;
                }
                setores[y - 1][x - 1] = setor;
            }
        }
    }

    // Preenche as portas e os vizinhos de cada setor, que e o que
    // Jogador.movimentar() consulta para andar pelo tabuleiro.
    private void ligaSetores() {
        for (int y = 1; y <= TAMANHO; ++y) {
            for (int x = 1; x <= TAMANHO; ++x) {
                Setor setor = getSetor(x, y);

                setor.setPortaNorte(temPorta(x, y, 'w'));
                setor.setPortaSul(temPorta(x, y, 's'));
                setor.setPortaOeste(temPorta(x, y, 'a'));
                setor.setPortaLeste(temPorta(x, y, 'd'));

                // setVizinho ignora null, entao so ligamos onde ha porta.
                if (temPorta(x, y, 'w')) setor.setVizinhoNorte(getSetor(x, y - 1));
                if (temPorta(x, y, 's')) setor.setVizinhoSul(getSetor(x, y + 1));
                if (temPorta(x, y, 'a')) setor.setVizinhoOeste(getSetor(x - 1, y));
                if (temPorta(x, y, 'd')) setor.setVizinhoLeste(getSetor(x + 1, y));
            }
        }
    }

    // ---------------------------------------------------------------- consultas
    public boolean coordenadaValida(int x, int y) {
        return x >= 1 && x <= TAMANHO && y >= 1 && y <= TAMANHO;
    }

    // Verifica se tem porta saindo do setor (x,y) na direcao d ('w','a','s','d').
    // Os limites externos do tabuleiro nunca tem porta.
    public boolean temPorta(int x, int y, char direcao) {
        if (!coordenadaValida(x, y)) {
            return false;
        }
        int ix = x - 1;
        int iy = y - 1;

        switch (direcao) {
            case 'w': return iy > 0 && portasUD[iy - 1][ix];
            case 's': return iy < TAMANHO - 1 && portasUD[iy][ix];
            case 'a': return ix > 0 && portasLR[iy][ix - 1];
            case 'd': return ix < TAMANHO - 1 && portasLR[iy][ix];
            default:  return false;
        }
    }

    public boolean ehFonte(Setor setor) {
        return setor == getSetor(fonteX, fonteY);
    }

    // ---------------------------------------------------------------- jogo
    // Chamar depois de Jogador.movimentar() bem-sucedido: na primeira visita marca o
    // setor como visitado e gera de 1 a 3 inimigos. Devolve true se era a primeira visita.
    public boolean entrarNoSetor(Setor setor) {
        if(setor.getVisitado()){
            return false;
        }
        setor.setVisitado(true);
        gerarInimigos(setor);
        return true;
    }

    private void gerarInimigos(Setor setor) {
        int quantidade = aleatorio.nextInt(3) + 1;          // 1 a 3 inimigos
        for (int i = 0; i < quantidade; ++i) {
            int forca = aleatorio.nextInt(3) + 1;           // ATK = DEF, entre 1 e 3
            Inimigo inimigo = new Inimigo(forca);
            setor.adicionaInimigo(inimigo);                   // protected: acessivel no mesmo pacote
        }
    }

    // ---------------------------------------------------------------- desenho
    // Imprime o tabuleiro inteiro. p1 e p2 podem ser null (jogador eliminado):
    // nesse caso ele simplesmente nao aparece no desenho.
    public void imprimeTabuleiro(Jogador p1, Jogador p2) {
        System.out.print("    ");
        for (int x = 1; x <= TAMANHO; ++x) {
            System.out.print("  " + x + " "); // linha de numeros x
        }
        System.out.println();

        imprimeLinhaHorizontal(0);
        for (int y = 1; y <= TAMANHO; ++y) {
            System.out.print(y + "   |");
            for (int x = 1; x <= TAMANHO; ++x) {
                conteudoCelula(x, y, p1, p2);
                boolean porta = x < TAMANHO && portaVisivel(x, y, 'd');
                System.out.print(porta ? '*' : '|');
            }
            System.out.println();
            imprimeLinhaHorizontal(y);
        }
    }

    // Imprime o setor ampliado de onde os jogadores estao:
    //  - mesmo setor      -> uma caixa so, com P1 e P2 dentro;
    //  - setores distintos -> duas caixas lado a lado.
    // p1 e p2 podem ser null (jogador eliminado).
    public void imprimeSetores(Jogador p1, Jogador p2) {
        ArrayList<Setor> distintos = new ArrayList<>();
        if (p1 != null) {
            distintos.add(p1.getSetor());
        }
        if (p2 != null && !distintos.contains(p2.getSetor())) {
            distintos.add(p2.getSetor());
        }

        for (int linha = 0; linha < LINHAS_SETOR; ++linha) {
            for (Setor s : distintos) {
                imprimeLinhaSetor(s, linha, p1, p2);
            }
            System.out.println();
        }
    }

// Imprime uma linha (0 a LINHAS_SETOR - 1) da caixa de um setor, ja preenchida
    // ate LARGURA_COLUNA para que as caixas fiquem lado a lado.
    private void imprimeLinhaSetor(Setor setor, int linha, Jogador p1, Jogador p2) {
        int x = setor.getCoordenadaX();
        int y = setor.getCoordenadaY();
        boolean temP1 = setor.temJogador(p1);
        boolean temP2 = setor.temJogador(p2);
        int largura = LARGURA_SETOR + 2;   // caixa com as duas bordas laterais

        switch (linha) {
            case 0:
                System.out.printf("%-" + LARGURA_COLUNA + "s", setor.getTitulo());
                return;
            case 1:
                imprimeBordaHorizontal(portaVisivel(x, y, 'w'));
                break;
            case 2:
                System.out.print("| ");
                int espacamento = 0;
                ArrayList<Inimigo> inimigos = setor.getInimigos(); 
                for (Inimigo inimigo : inimigos) {
                    if (espacamento > 0) {
                        System.out.print(' ');
                        ++espacamento;
                    }
                    System.out.print(Cores.ANSI_RED);
                    inimigo.imprimirStatus();
                    System.out.print(Cores.ANSI_RESET);   // volta ao texto preto, mantendo o fundo
                    espacamento += 3; // tamanho da string impressa
                }
                System.out.printf("%" + (LARGURA_SETOR - 1 - espacamento) + "s|", "");
                break;
            case 3:
            case 5:
                System.out.printf("|%" + LARGURA_SETOR + "s|", "");
                break;
            case 4:
                System.out.print(portaVisivel(x, y, 'a') ? '*' : '|');
                System.out.printf("%" + LARGURA_SETOR + "s", "");
                System.out.print(portaVisivel(x, y, 'd') ? '*' : '|');
                break;
            case 6: // P1 e P2
                System.out.print("| ");
                System.out.print(temP1 ? Cores.ANSI_BLUE + "P1  " + Cores.ANSI_RESET : "    ");
                System.out.print("   ");
                System.out.print(temP2 ? Cores.ANSI_GREEN + "P2  " + Cores.ANSI_RESET : "    ");
                System.out.print(" |");
                break;
            case 7: // ATK/DEF
                System.out.print("| ");
                System.out.printf("%-4s", temP1 ? p1.getAtk() + "/" + p1.getDef() : "");
                System.out.print("   ");
                System.out.printf("%-4s", temP2 ? p2.getAtk() + "/" + p2.getDef() : "");
                System.out.print(" |");
                break;
            default:
                imprimeBordaHorizontal(portaVisivel(x, y, 's'));
                break;
        }
        System.out.printf("%" + (LARGURA_COLUNA - largura) + "s", "");
    }

    // "|------*------|" (com porta) ou "|-------------|" (parede)
    private void imprimeBordaHorizontal(boolean porta) {
        int metade = LARGURA_SETOR / 2;
        System.out.print("|");
        for (int i = 0; i < metade; ++i) {
            System.out.print('-');
        }
        System.out.print(porta ? '*' : '-');
        for (int i = 0; i < metade; ++i) {
            System.out.print('-');
        }
        System.out.print("|");
    }

    // Linha entre a linha y e y+1 (y = 0 e a borda de cima, y = 5 a de baixo).
    private void imprimeLinhaHorizontal(int y) {
        System.out.print("    |");
        for (int x = 1; x <= TAMANHO; ++x) {
            boolean porta = y >= 1 && y < TAMANHO && portaVisivel(x, y, 's');
            System.out.print(porta ? "-*-" : "---");
            System.out.print('|');
        }
        System.out.println();
    }

    private boolean portaVisivel(int x, int y, char c) {
        if (!temPorta(x, y, c)) {
            return false;
        }
        int vx = x + (c == 'd' ? 1 : c == 'a' ? -1 : 0);
        int vy = y + (c == 's' ? 1 : c == 'w' ? -1 : 0);
        Setor setorAnalisado = getSetor(x, y);
        Setor setorVizinho = getSetor(vx, vy);
        return setorAnalisado.getVisitado() || setorVizinho.getVisitado();
    }

    // Conteudo de 3 caracteres: ambos = " P ", so P1 = "P1 ", so P2 = "P2 ",
    // senao centro (C), fonte (X) ou vazio.
    private void conteudoCelula(int x, int y, Jogador p1, Jogador p2) {
        Setor setor = getSetor(x, y);

        if (setor.jogadoresNoMesmoSetor()){
            System.out.print(" P ");
        } 

        else if (setor.temJogador(p1)){
            p1.imprimeJogador();
            System.out.print(" ");
        }

        else if (setor.temJogador(p2)){
            p2.imprimeJogador();
            System.out.print(" ");
        }

        else if (setor == this.retornaSetorCentral()){
            System.out.print(" C ");
        }

        else if (ehFonte(setor)){
            System.out.print(Cores.ANSI_YELLOW + " X " + Cores.ANSI_RESET);
        }

        else{
            System.out.print("   ");
        }
    }

    public Setor retornaSetorCentral(){
        return getSetor(CENTRO, CENTRO);
    }
}