import java.util.ArrayList;
import java.util.Scanner;

public class Principal{

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        //Mudar isso depois
        Setor setorInicial = new SetorNormal(3, 3);
        
        ArrayList<Jogador> jogadores = new ArrayList<Jogador>();

        jogadores.set(1, new Simples(setorInicial));
        jogadores.set(2, new Suporte(setorInicial, jogadores.get(1)));


        int jogadoresComVida = 2;
        int ciclo = 25;
        int acao;
        boolean acaoValida;
        char direcao = ' ';
        int indiceInimigoAtacado;
        Inimigo inimigoAtacado;
        boolean movimentoRealizado;

        //Inserir função que imprime e inicializa o tabuleiro

        while(ciclo > 0 && jogadoresComVida > 0){
            
            System.out.printf("==================== CICLO %d ====================%n", ciclo);
            for(Jogador jogador : jogadores){
                if(jogador.getVida()){
                    //Movimentar o jogador
                    direcao = ' ';
                    if(jogador.setor.temInimigo()){
                        System.out.print("Jogador ");
                        jogador.imprimeJogador();
                        System.out.println(" não pode se movimentar. Ainda há inimigos no setor!");
                    } 
                    
                    else{

                        movimentoRealizado = false;

                        while(!movimentoRealizado){

                            while (direcao != 'a' || direcao != 'd' || direcao != 'w' || direcao != 's'){
                                System.out.print("Digite a direção do movimento: ");
                                direcao = scanner.next().charAt(0);
                                direcao = Character.toLowerCase(direcao);
                            }

                            movimentoRealizado = jogador.movimentar(direcao);
                        }


                    }

                    //Realizar 2 ações do jogador
                    for(int i = 0; i < 2; i++){
                        acaoValida = false;
                        while(!acaoValida){
                            System.out.print("Qual ação você gostaria de realizar? ");
                            acao = scanner.nextInt();
                            
                            switch(acao){
                                //Ação de atacar
                                case 1:
                                    if(jogador.setor.temInimigo()){
                                        jogador.setor.listarInimigos();
                                        inimigoAtacado = null;

                                        while(inimigoAtacado == null){
                                            System.out.print("Qual inimigo você gostaria de atacar? ");
                                            indiceInimigoAtacado = scanner.nextInt();
                                            inimigoAtacado = jogador.setor.retornarInimigo(indiceInimigoAtacado); 
                                            if(inimigoAtacado == null){
                                                System.out.println("Inimigo inválido");
                                            }
                                        }

                                        jogador.atacar(inimigoAtacado);
                                        acaoValida = true;
                                    }

                                    else{
                                        System.out.println("Não há inimimgos no setor");
                                    }

                                    break;
                                    
                                case 2:
                                    jogador.procurar();
                                    acaoValida = true;
                                    break;

                                case 3:
                                    if(jogador.setor.jogadoresNoMesmoSetor()){
                                        System.out.println("[1] P1");
                                        System.out.println("[2] P2");
                                        int indiceJogador = 0;
                                        while(indiceJogador != 1 || indiceJogador != 2){
                                            System.out.print("De qual jogador você gostaria de recuperar a defesa?");
                                            indiceJogador = scanner.nextInt();
                                        } 
                                        acaoValida = jogador.recuperarDefesa(indiceJogador);
                                    }
                                    acaoValida = jogador.recuperarDefesa(2);

                                    break;
                                
                                default:
                                    System.out.println("Opção inválida");
                                    acaoValida = false;

                                    break;
                            }
                        }

                    }

                    if(!jogador.getVida()){
                        jogadoresComVida--;
                    }
                }

                }
                
            ciclo--;
        }
    }
        
}
