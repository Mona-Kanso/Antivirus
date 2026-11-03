import java.util.ArrayList;
import java.util.Random;


public class Tabuleiro {

    private static final int TAMANHO = 5;
    private static final int CENTRO = 3;

    // Layout do setor ampliado: largura interna da caixa e espaco de cada coluna.
    private static final int LARGURA_SETOR = 13;
    private static final int LARGURA_COLUNA = 22;

    // Portas Leste-Oeste: linha y (1..5), caractere x diz se ha porta entre a
    // coluna x+1 e a coluna x+2.
    private static final String[] MAPA_LR = {
        "1011",
        "1101",
        "1111",
        "0110",
        "1011"
    };

    // Portas Norte-Sul: linha y (entre y+1 e y+2), caractere x = coluna x+1.
    private static final String[] MAPA_UD = {
        "10110",
        "01101",
        "10101",
        "01011"
    };

    // N = normal, O = oculto, P = privado (o centro deve ser normal).
    private static final String[] MAPA_TIPOS = {
        "NOPNN",
        "NNNOP",
        "PNNNO",
        "NONPN",
        "NNONP"
    };

    private final Setor[][] setores = new Setor[TAMANHO][TAMANHO];          // [y][x]
    private final boolean[][] visitados = new boolean[TAMANHO][TAMANHO];    // [y][x]
    private final boolean[][] portasLR = new boolean[TAMANHO][TAMANHO - 1]; // [y][x]
    private final boolean[][] portasUD = new boolean[TAMANHO - 1][TAMANHO]; // [y][x]
    private final Random aleatorio = new Random();
    private final int fonteX;
    private final int fonteY;

    public Tabuleiro() {
        carregaPortas();
        criaSetores();
        ligaSetores();

        // Os jogadores comecam no centro, que ja conta como visitado (sem inimigos).
        visitados[CENTRO - 1][CENTRO - 1] = true;

        // Fonte de infeccao: qualquer posicao, menos o centro.
        int x;
        int y;
        do {
            x = aleatorio.nextInt(TAMANHO) + 1;
            y = aleatorio.nextInt(TAMANHO) + 1;
        } while (x == CENTRO && y == CENTRO);
        this.fonteX = x;
        this.fonteY = y;
    }

    // ---------------------------------------------------------------- montagem
    private void carregaPortas() {
        for (int y = 0; y < TAMANHO; ++y) {
            for (int x = 0; x < TAMANHO - 1; ++x) {
                portasLR[y][x] = MAPA_LR[y].charAt(x) == '1';
            }
        }
        for (int y = 0; y < TAMANHO - 1; ++y) {
            for (int x = 0; x < TAMANHO; ++x) {
                portasUD[y][x] = MAPA_UD[y].charAt(x) == '1';
            }
        }
    }

    
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
                Setor s = getSetor(x, y);

                s.setPortaNorte(temPorta(x, y, 'w'));
                s.setPortaSul(temPorta(x, y, 's'));
                s.setPortaOeste(temPorta(x, y, 'a'));
                s.setPortaLeste(temPorta(x, y, 'd'));

                // setVizinho ignora null, entao so ligamos onde ha porta.
                if (temPorta(x, y, 'w')) s.setVizinhoNorte(getSetor(x, y - 1));
                if (temPorta(x, y, 's')) s.setVizinhoSul(getSetor(x, y + 1));
                if (temPorta(x, y, 'a')) s.setVizinhoOeste(getSetor(x - 1, y));
                if (temPorta(x, y, 'd')) s.setVizinhoLeste(getSetor(x + 1, y));
            }
        }
    }

    // ---------------------------------------------------------------- consultas
    public boolean coordenadaValida(int x, int y) {
        return x >= 1 && x <= TAMANHO && y >= 1 && y <= TAMANHO;
    }

    public Setor getSetor(int x, int y) {
        if (!coordenadaValida(x, y)) {
            throw new IllegalArgumentException("Coordenada invalida: [" + x + "," + y + "]");
        }
        return setores[y - 1][x - 1];
    }

    public Setor getSetorCentral() {
        return getSetor(CENTRO, CENTRO);
    }

    // Verifica se tem porta saindo do setor (x,y) na direcao d ('w','a','s','d').
    // Os limites externos do tabuleiro nunca tem porta.
    public boolean temPorta(int x, int y, char d) {
        if (!coordenadaValida(x, y)) {
            return false;
        }
        int ix = x - 1;
        int iy = y - 1;

        switch (d) {
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

    public boolean foiVisitado(Setor setor) {
        return visitados[setor.y - 1][setor.x - 1];
    }

    // ---------------------------------------------------------------- jogo
    // Chamar depois de Jogador.movimentar() bem-sucedido: na primeira visita marca o
    // setor como visitado e gera de 1 a 3 inimigos. Devolve true se era a primeira visita.
    public boolean entrarNoSetor(Setor setor) {
        int ix = setor.x - 1;
        int iy = setor.y - 1;
        if (visitados[iy][ix]) {
            return false;
        }
        visitados[iy][ix] = true;
        gerarInimigos(setor);
        return true;
    }

    private void gerarInimigos(Setor setor) {
        int quantidade = aleatorio.nextInt(3) + 1;          // 1 a 3 inimigos
        for (int i = 0; i < quantidade; ++i) {
            int forca = aleatorio.nextInt(3) + 1;           // ATK = DEF, entre 1 e 3
            Inimigo inimigo = new Inimigo();
            inimigo.setATK(forca);
            inimigo.setDEF(forca);
            setor.inimigos.add(inimigo);                    // protected: acessivel no mesmo pacote
        }
    }

    // ---------------------------------------------------------------- desenho
    // Imprime o tabuleiro inteiro. p1 e p2 podem ser null (jogador eliminado):
    // nesse caso ele simplesmente nao aparece no desenho.
    public void imprimeTabuleiro(Jogador p1, Jogador p2) {
        StringBuilder sb = new StringBuilder();

        sb.append("    ");
        for (int x = 1; x <= TAMANHO; ++x) {
            sb.append("  ").append(x).append(" "); // linha de numeros x
        }
        sb.append("\n");

        sb.append(linhaHorizontal(0));
        for (int y = 1; y <= TAMANHO; ++y) {
            sb.append(y).append("   |");
            for (int x = 1; x <= TAMANHO; ++x) {
                sb.append(conteudoCelula(x, y, p1, p2));
                boolean porta = x < TAMANHO && portaVisivel(x, y, 'd');
                sb.append(porta ? '*' : '|');
            }
            sb.append("\n");
            sb.append(linhaHorizontal(y));
        }
        System.out.print(sb);
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
        if (distintos.isEmpty()) {
            return;
        }

        ArrayList<String[]> blocos = new ArrayList<>();
        for (Setor s : distintos) {
            blocos.add(montaSetor(s, p1, p2));
        }

        StringBuilder sb = new StringBuilder();
        for (int linha = 0; linha < blocos.get(0).length; ++linha) {
            for (String[] bloco : blocos) {
                sb.append(String.format("%-" + LARGURA_COLUNA + "s", bloco[linha]));
            }
            sb.append("\n");
        }
        System.out.print(sb);
    }

    // Monta as linhas (todas com a mesma largura) da caixa de um setor.
    private String[] montaSetor(Setor s, Jogador p1, Jogador p2) {
        int x = s.x;
        int y = s.y;
        boolean temP1 = p1 != null && p1.getSetor() == s;
        boolean temP2 = p2 != null && p2.getSetor() == s;

        StringBuilder inimigos = new StringBuilder();
        for (Inimigo i : s.inimigos) {
            if (inimigos.length() > 0) {
                inimigos.append(' ');
            }
            inimigos.append(i.getATK()).append('/').append(i.getDEF());
        }

        String nomeP1 = temP1 ? "P1" : "";
        String nomeP2 = temP2 ? "P2" : "";
        String statusP1 = temP1 ? p1.getATK() + "/" + p1.getDEF() : "";
        String statusP2 = temP2 ? p2.getATK() + "/" + p2.getDEF() : "";

        String vazio = String.format("%" + LARGURA_SETOR + "s", "");
        String tipo = s.getClass().getSimpleName().replace("Setor", "");

        String[] linhas = new String[9];
        linhas[0] = "Setor [" + x + "," + y + "] " + tipo;
        linhas[1] = bordaHorizontal(portaVisivel(x, y, 'w'));
        linhas[2] = String.format("| %-" + (LARGURA_SETOR - 1) + "s|", inimigos);
        linhas[3] = "|" + vazio + "|";
        linhas[4] = (portaVisivel(x, y, 'a') ? "*" : "|") + vazio
                  + (portaVisivel(x, y, 'd') ? "*" : "|");
        linhas[5] = "|" + vazio + "|";
        linhas[6] = "| " + String.format("%-4s", nomeP1) + "   " + String.format("%4s", nomeP2) + " |";
        linhas[7] = "| " + String.format("%-4s", statusP1) + "   " + String.format("%4s", statusP2) + " |";
        linhas[8] = bordaHorizontal(portaVisivel(x, y, 's'));
        return linhas;
    }

    // "|------*------|" (com porta) ou "|-------------|" (parede)
    private String bordaHorizontal(boolean porta) {
        String metade = String.format("%" + (LARGURA_SETOR / 2) + "s", "").replace(' ', '-');
        return "|" + metade + (porta ? "*" : "-") + metade + "|";
    }

    // Linha entre a linha y e y+1 (y = 0 e a borda de cima, y = 5 a de baixo).
    private String linhaHorizontal(int y) {
        StringBuilder sb = new StringBuilder("    |");
        for (int x = 1; x <= TAMANHO; ++x) {
            boolean porta = y >= 1 && y < TAMANHO && portaVisivel(x, y, 's');
            sb.append(porta ? "-*-" : "---").append('|');
        }
        sb.append("\n");
        return sb.toString();
    }

    private boolean portaVisivel(int x, int y, char d) {
        if (!temPorta(x, y, d)) {
            return false;
        }
        int vx = x + (d == 'd' ? 1 : d == 'a' ? -1 : 0);
        int vy = y + (d == 's' ? 1 : d == 'w' ? -1 : 0);
        return visitados[y - 1][x - 1] || visitados[vy - 1][vx - 1];
    }

    // Conteudo de 3 caracteres: ambos = " P ", so P1 = "P1 ", so P2 = "P2 ",
    // senao centro (C), fonte (X) ou vazio.
    private String conteudoCelula(int x, int y, Jogador p1, Jogador p2) {
        Setor setor = getSetor(x, y);
        boolean temP1 = p1 != null && p1.getSetor() == setor;
        boolean temP2 = p2 != null && p2.getSetor() == setor;
        if (temP1 && temP2) return " P ";
        if (temP1)          return "P1 ";
        if (temP2)          return "P2 ";
        if (x == CENTRO && y == CENTRO) return " C ";
        if (x == fonteX && y == fonteY) return " X ";
        return "   ";
    }
}