package com.recomendacao;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import java.util.*;

public class SlopeOne extends AppCompatActivity {

    private Map<Produto,Map<Produto,Double>> matrizDiferenca;
    private Map<Produto,Map<Produto,Integer>> matrizFrequencia;

    private static List<Produto> todosItens;

    Usuario ana, jose, maria, joao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_one);

        /** Início - Simulando a leitura dos dados do sistema para o cálculo das recomendações */
        //Cração dos usuários
        ana = new Usuario("Ana");
        jose = new Usuario("José");
        maria = new Usuario("Maria");
        joao = new Usuario("João");

        //Criação dos Produtos
        Produto item1 = new Produto("        maçã");
        Produto item2 = new Produto("refrigerante");
        Produto item3 = new Produto("       arroz");
        Produto item4 = new Produto("       sabão");
        Produto item5 = new Produto("creme dental");
        Produto item6 = new Produto("       leite");
        todosItens = new ArrayList<>(); // ({item1, item2, item3, item4, item5, item6});
        todosItens.add(item1);
        todosItens.add(item2);
        todosItens.add(item3);
        todosItens.add(item4);
        todosItens.add(item5);
        todosItens.add(item6);

       //Criação da lista de notas dadas pelos usuários aos produtos
        HashMap<Produto,Double> notasUsuario1 = new HashMap<Produto,Double>();
        HashMap<Produto,Double> notasUsuario2 = new HashMap<Produto,Double>();
        HashMap<Produto,Double> notasUsuario3 = new HashMap<Produto,Double>();
        HashMap<Produto,Double> notasUsuario4 = new HashMap<Produto,Double>();

        Map<Usuario,Map<Produto,Double>> data = new HashMap<Usuario,Map<Produto,Double>>();
        //Mapeamento dos dados em geral
        mapeamentoUser1(data, item1, item2, item4, notasUsuario1);
        mapeamentoUser2(data, item1, item3, item6, notasUsuario2);
        mapeamentoUser3(data, item1, item2, item3, item4, notasUsuario3);
        mapeamentoUser4(data, item1, item4, item5, notasUsuario4);
        /** Fim - Simulando a leitura dos dados do sistema para o cálculo das recomendações */

        calculoPredicao(data);
    }

    private void calculoPredicao(Map<Usuario, Map<Produto, Double>> data) {
        System.out.println("****** todosItens ... " + todosItens.get(2).getNome());
        calculaRecomendacoes(data, ana); // Simulando o Usuário Logado
    }

    private void calculaRecomendacoes(Map<Usuario,Map<Produto,Double>> data, Usuario usuarioLogado) {
        criarMatrizDiferenca(data);
        System.out.println(" ");
        System.out.println(" --------------------  INÍCIO - EXECUÇÃO DO PROTÓTIPO --------------------");
        printData(data);
        System.out.println(" ");
        System.out.println("Lendo... " + usuarioLogado.toString());
        print(data.get(usuarioLogado));
        System.out.println(" ");
        System.out.println("Calculando PREDICT... " + usuarioLogado.toString());
        printRecomendacao(predict(data.get(usuarioLogado)), usuarioLogado);
        /*System.out.println("Calculando WEIGHTLESSPREDICT... " + usuarioLogado.toString());
        printRecomendacao(weightlesspredict(data.get(usuarioLogado)), usuarioLogado);*/
        System.out.println(" --------------------  FIM - EXECUÇÃO DO PROTÓTIPO --------------------");
        System.out.println(" ");
    }

    private void mapeamentoUser4(Map<Usuario, Map<Produto, Double>> data, Produto item1, Produto item4, Produto item5, HashMap<Produto, Double> notasUsuario4) {
        notasUsuario4.put(item1,0.1);
        notasUsuario4.put(item4,1.0);
        notasUsuario4.put(item5,0.4);
        data.put(ana,notasUsuario4);
    }

    private void mapeamentoUser3(Map<Usuario, Map<Produto, Double>> data, Produto item1, Produto item2, Produto item3, Produto item4, HashMap<Produto, Double> notasUsuario3) {
        notasUsuario3.put(item1,0.9);
        notasUsuario3.put(item2,0.4);
        notasUsuario3.put(item3,0.5);
        notasUsuario3.put(item4,0.1);
        data.put(jose,notasUsuario3);
    }

    private void mapeamentoUser2(Map<Usuario, Map<Produto, Double>> data, Produto item1, Produto item3, Produto item6, HashMap<Produto, Double> notasUsuario2) {
        notasUsuario2.put(item1,1.0);
        notasUsuario2.put(item3,0.5);
        notasUsuario2.put(item6,0.3);
        data.put(maria,notasUsuario2);
    }

    private void mapeamentoUser1(Map<Usuario, Map<Produto, Double>> data, Produto item1, Produto item2, Produto item4, HashMap<Produto, Double> notasUsuario1) {
        notasUsuario1.put(item1,1.0);
        notasUsuario1.put(item2,0.5);
        notasUsuario1.put(item4,0.1);
        data.put(joao,notasUsuario1);
    }

    public Map<Produto,Double> predict(Map<Produto,Double> notasUsuario) {
        HashMap<Produto,Double> predictions = new HashMap<Produto,Double>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : matrizDiferenca.keySet()) {
            frequencies.put(j,0);
            predictions.put(j,0.0);
        }
        for (Produto j : notasUsuario.keySet()) {
            for (Produto k : matrizDiferenca.keySet()) {
                try {
                    Double novoValor = ( matrizDiferenca.get(k).get(j).doubleValue() + notasUsuario.get(j).doubleValue() ) * matrizFrequencia.get(k).get(j).intValue();
                    predictions.put(k, predictions.get(k)+novoValor);
                    frequencies.put(k, frequencies.get(k)+ matrizFrequencia.get(k).get(j).intValue());
                } catch(NullPointerException e) {}
            }
        }
        HashMap<Produto,Double> cleanpredictions = new HashMap<Produto,Double>();
        for (Produto j : predictions.keySet()) {
            if (frequencies.get(j)>0) {
                cleanpredictions.put(j, predictions.get(j).doubleValue()/frequencies.get(j).intValue());
            }
        }
        for (Produto j : notasUsuario.keySet()) {
            cleanpredictions.put(j,notasUsuario.get(j));
        }
        return cleanpredictions;
    }

    public Map<Produto,Double> weightlesspredict(Map<Produto,Double> notasUsuario) {
        HashMap<Produto,Double> predictions = new HashMap<Produto,Double>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : matrizDiferenca.keySet()) {
            predictions.put(j,0.0);
            frequencies.put(j,0);
        }
        for (Produto j : notasUsuario.keySet()) {
            for (Produto k : matrizDiferenca.keySet()) {
                try {
                //System.out.println("Average diff between "+j+" and "+ k + " is "+matrizDiferenca.get(k).get(j).DoubleValue()+" with n = "+matrizFrequencia.get(k).get(j).DoubleValue());
                Double novoValor = ( matrizDiferenca.get(k).get(j).doubleValue() + notasUsuario.get(j).doubleValue() ) ;
                predictions.put(k, predictions.get(k)+novoValor);
                } catch(NullPointerException e) {}
            }
        }
        for (Produto j : predictions.keySet()) {
            predictions.put(j, predictions.get(j).doubleValue()/notasUsuario.size());
        }
        for (Produto j : notasUsuario.keySet()) {
            predictions.put(j,notasUsuario.get(j));
        }
        return predictions;
    }

    public void printData(Map<Usuario,Map<Produto,Double>> data) {
        System.out.println(" ");
        System.out.println("************ Notas Dadas pelos Usuários *********");
        for(Usuario usuario : data.keySet()) {
            System.out.println(usuario);
            print(data.get(usuario));
        }
        System.out.println(" ");
        System.out.println("************ Matriz Diferença [Itens x (Média das diferenças das notas e Frequências dos itens juntos) ] ************");
        System.out.print("             |");
        for (int g = 0; g< todosItens.size(); g++) { // todosItens[i]
            System.out.format("%21s", todosItens.get(g) + "       |");
        }
        System.out.println(" ");
        System.out.print("             |");
        for (int h = 0; h< todosItens.size(); h++) { // todosItens[i]
            System.out.print("  Notas    Frequenc |");
        }
        System.out.println(" ");
        for (int i = 0; i< todosItens.size(); i++) {
            System.out.print("\n" + todosItens.get(i) + ":|");
            printMatrizes(matrizDiferenca.get(todosItens.get(i)), matrizFrequencia.get(todosItens.get(i)));
        }
    }

    private void printMatrizes(Map<Produto,Double> notas,
                               Map<Produto,Integer> frequencies) {
        for (int j = 0; j< todosItens.size(); j++) {
            System.out.format("%10.3f", notas.get(todosItens.get(j)) );
            System.out.print(" ");
            System.out.format("%10s", frequencies.get(todosItens.get(j)) + " |");
        }
        System.out.println();
    }

    public static void print(Map<Produto,Double> notasUsuario) {
        for (Produto j : notasUsuario.keySet()) {
            System.out.println(" "+ j+ " --> "+notasUsuario.get(j).doubleValue());
        }
    }

    public static void printRecomendacao(Map<Produto,Double> notasUsuario, Usuario usuario) {
        ordenarCompare(notasUsuario, usuario);
        System.out.println(" ");
    }

    public static void ordenarCompare(Map<Produto, Double> map, Usuario usuario) {
        System.out.println(" ");
        System.out.println("************ ORDENA PELO COMPARADOR ( " + usuario.toString() + " ) *********");

        Comparador comparador = new Comparador(map);
        Map<Produto, Double> sorted_map = new TreeMap<Produto, Double>(comparador);

        System.out.println("unsorted map: " + map);
        sorted_map.putAll(map);
        System.out.println("results: " + sorted_map);
        System.out.println("&&&&&&&&&&&&&&&&& sorted map (get2): " + sorted_map.keySet());
        System.out.println(" ");
    }

    public void criarMatrizDiferenca(Map<Usuario,Map<Produto,Double>> data) {
        matrizDiferenca = new HashMap<Produto, Map<Produto, Double>>();
        matrizFrequencia = new HashMap<Produto, Map<Produto, Integer>>();
        // first iterate through users
        for (Map<Produto, Double> user : data.values()) {
            // then iterate through user data
            for (Map.Entry<Produto, Double> entry : user.entrySet()) {
                if (!matrizDiferenca.containsKey(entry.getKey())) {
                    matrizDiferenca.put(entry.getKey(), new HashMap<Produto, Double>());
                    matrizFrequencia.put(entry.getKey(), new HashMap<Produto, Integer>());
                }
                for (Map.Entry<Produto, Double> entry2 : user.entrySet()) {
                    int oldcount = 0;
                    if (matrizFrequencia.get(entry.getKey()).containsKey(entry2.getKey()))
                        oldcount = matrizFrequencia.get(entry.getKey()).get(entry2.getKey()).intValue();
                    Double olddiff = 0.0;
                    if (matrizDiferenca.get(entry.getKey()).containsKey(entry2.getKey()))
                        olddiff = matrizDiferenca.get(entry.getKey()).get(entry2.getKey()).doubleValue();
                    Double observeddiff = entry.getValue() - entry2.getValue();
                    matrizFrequencia.get(entry.getKey()).put(entry2.getKey(), oldcount + 1);
                    matrizDiferenca.get(entry.getKey()).put(entry2.getKey(), olddiff + observeddiff);
                }
            }
        }
        for (Produto j : matrizDiferenca.keySet()) {
            for (Produto i : matrizDiferenca.get(j).keySet()) {
                Double oldvalue = matrizDiferenca.get(j).get(i).doubleValue();
                int count = matrizFrequencia.get(j).get(i).intValue();
                matrizDiferenca.get(j).put(i, oldvalue / count);
            }
        }

    }
}