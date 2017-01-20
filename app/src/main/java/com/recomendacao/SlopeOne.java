package com.recomendacao;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import java.util.*;

public class SlopeOne extends AppCompatActivity {

    private Map<Produto,Map<Produto,Float>> matrizDiferenca;
    private Map<Produto,Map<Produto,Integer>> matrizFrequencia;

    private static Produto[] todosItens;

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
        todosItens = new Produto[]{item1, item2, item3, item4, item5, item6};

        //Criação da lista de notas dadas pelos usuários aos produtos
        HashMap<Produto,Float> notasUsuario1 = new HashMap<Produto,Float>();
        HashMap<Produto,Float> notasUsuario2 = new HashMap<Produto,Float>();
        HashMap<Produto,Float> notasUsuario3 = new HashMap<Produto,Float>();
        HashMap<Produto,Float> notasUsuario4 = new HashMap<Produto,Float>();

        Map<Usuario,Map<Produto,Float>> data = new HashMap<Usuario,Map<Produto,Float>>();
        //Mapeamento dos dados em geral
        mapeamentoUser1(data, item1, item2, item4, notasUsuario1);
        mapeamentoUser2(data, item1, item3, item6, notasUsuario2);
        mapeamentoUser3(data, item1, item2, item3, item4, notasUsuario3);
        mapeamentoUser4(data, item1, item4, item5, notasUsuario4);
        /** Fim - Simulando a leitura dos dados do sistema para o cálculo das recomendações */

        calculaRecomendacoes(data, jose); // Supondo que jose é o Usuário Logado (neste caso)
    }

    private void calculaRecomendacoes(Map<Usuario,Map<Produto,Float>> data, Usuario usuario) {
        criarMatrizDiferenca(data);
        System.out.println(" ");
        System.out.println(" --------------------  INÍCIO - EXECUÇÃO DO PROTÓTIPO --------------------");
        printData(data);
        System.out.println(" ");
        System.out.println("Lendo... " + usuario.toString());
        print(data.get(usuario));
        System.out.println(" ");
        System.out.println("Calculando PREDICT... " + usuario.toString());
        printRecomendacao(predict(data.get(usuario)), usuario);
        System.out.println("Calculando WEIGHTLESSPREDICT... " + usuario.toString());
        printRecomendacao(weightlesspredict(data.get(usuario)), usuario);
        System.out.println(" --------------------  FIM - EXECUÇÃO DO PROTÓTIPO --------------------");
        System.out.println(" ");
    }

    private void mapeamentoUser4(Map<Usuario, Map<Produto, Float>> data, Produto item1, Produto item4, Produto item5, HashMap<Produto, Float> notasUsuario4) {
        notasUsuario4.put(item1,0.1f);
        notasUsuario4.put(item4,1.0f);
        notasUsuario4.put(item5,0.4f);
        data.put(ana,notasUsuario4);
    }

    private void mapeamentoUser3(Map<Usuario, Map<Produto, Float>> data, Produto item1, Produto item2, Produto item3, Produto item4, HashMap<Produto, Float> notasUsuario3) {
        notasUsuario3.put(item1,0.9f);
        notasUsuario3.put(item2,0.4f);
        notasUsuario3.put(item3,0.5f);
        notasUsuario3.put(item4,0.1f);
        data.put(jose,notasUsuario3);
    }

    private void mapeamentoUser2(Map<Usuario, Map<Produto, Float>> data, Produto item1, Produto item3, Produto item6, HashMap<Produto, Float> notasUsuario2) {
        notasUsuario2.put(item1,1.0f);
        notasUsuario2.put(item3,0.5f);
        notasUsuario2.put(item6,0.3f);
        data.put(maria,notasUsuario2);
    }

    private void mapeamentoUser1(Map<Usuario, Map<Produto, Float>> data, Produto item1, Produto item2, Produto item4, HashMap<Produto, Float> notasUsuario1) {
        notasUsuario1.put(item1,1.0f);
        notasUsuario1.put(item2,0.5f);
        notasUsuario1.put(item4,0.1f);
        data.put(joao,notasUsuario1);
    }

    public Map<Produto,Float> predict(Map<Produto,Float> notasUsuario) {
        HashMap<Produto,Float> predictions = new HashMap<Produto,Float>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : matrizDiferenca.keySet()) {
            frequencies.put(j,0);
            predictions.put(j,0.0f);
        }
        for (Produto j : notasUsuario.keySet()) {
            for (Produto k : matrizDiferenca.keySet()) {
                try {
                    float novoValor = ( matrizDiferenca.get(k).get(j).floatValue() + notasUsuario.get(j).floatValue() ) * matrizFrequencia.get(k).get(j).intValue();
                    predictions.put(k, predictions.get(k)+novoValor);
                    frequencies.put(k, frequencies.get(k)+ matrizFrequencia.get(k).get(j).intValue());
                } catch(NullPointerException e) {}
            }
        }
        HashMap<Produto,Float> cleanpredictions = new HashMap<Produto,Float>();
        for (Produto j : predictions.keySet()) {
            if (frequencies.get(j)>0) {
                cleanpredictions.put(j, predictions.get(j).floatValue()/frequencies.get(j).intValue());
            }
        }
        for (Produto j : notasUsuario.keySet()) {
            cleanpredictions.put(j,notasUsuario.get(j));
        }
        return cleanpredictions;
    }

    public Map<Produto,Float> weightlesspredict(Map<Produto,Float> notasUsuario) {
        HashMap<Produto,Float> predictions = new HashMap<Produto,Float>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : matrizDiferenca.keySet()) {
            predictions.put(j,0.0f);
            frequencies.put(j,0);
        }
        for (Produto j : notasUsuario.keySet()) {
            for (Produto k : matrizDiferenca.keySet()) {
                try {
                //System.out.println("Average diff between "+j+" and "+ k + " is "+matrizDiferenca.get(k).get(j).floatValue()+" with n = "+matrizFrequencia.get(k).get(j).floatValue());
                float novoValor = ( matrizDiferenca.get(k).get(j).floatValue() + notasUsuario.get(j).floatValue() ) ;
                predictions.put(k, predictions.get(k)+novoValor);
                } catch(NullPointerException e) {}
            }
        }
        for (Produto j : predictions.keySet()) {
            predictions.put(j, predictions.get(j).floatValue()/notasUsuario.size());
        }
        for (Produto j : notasUsuario.keySet()) {
            predictions.put(j,notasUsuario.get(j));
        }
        return predictions;
    }

    public void printData(Map<Usuario,Map<Produto,Float>> data) {
        System.out.println(" ");
        System.out.println("************ Notas Dadas pelos Usuários *********");
        for(Usuario usuario : data.keySet()) {
            System.out.println(usuario);
            print(data.get(usuario));
        }

        System.out.println(" ");
        System.out.println("************ Matriz Diferença [Itens x (Média das diferenças das notas e Frequências dos itens juntos) ] ************");
        System.out.print("             |");
        for (int g = 0; g< todosItens.length; g++) { // todosItens[i]
            System.out.format("%21s", todosItens[g] + "       |");
        }
        System.out.println(" ");
        System.out.print("             |");
        for (int h = 0; h< todosItens.length; h++) { // todosItens[i]
            System.out.print("  Notas    Frequenc |");
        }
        System.out.println(" ");
        for (int i = 0; i< todosItens.length; i++) {
            System.out.print("\n" + todosItens[i] + ":|");
            printMatrizes(matrizDiferenca.get(todosItens[i]), matrizFrequencia.get(todosItens[i]));
        }
    }

    private void printMatrizes(Map<Produto,Float> notas,
                               Map<Produto,Integer> frequencies) {
        for (int j = 0; j< todosItens.length; j++) {
            System.out.format("%10.3f", notas.get(todosItens[j]) );
            System.out.print(" ");
            System.out.format("%10s", frequencies.get(todosItens[j]) + " |");
        }
        System.out.println();
    }

    public static void print(Map<Produto,Float> notasUsuario) {
        for (Produto j : notasUsuario.keySet()) {
            System.out.println(" "+ j+ " --> "+notasUsuario.get(j).floatValue());
        }
    }

    public static void printRecomendacao(Map<Produto,Float> notasUsuario, Usuario usuario) {
        ordenarCompare(notasUsuario, usuario);
        System.out.println(" ");
    }

    public static void ordenarCompare(Map<Produto, Float> map, Usuario usuario) {
        System.out.println(" ");
        System.out.println("************ ORDENA PELO COMPARADOR ( " + usuario.toString() + " ) *********");

        Comparador comparador = new Comparador(map);
        Map<Produto, Float> sorted_map = new TreeMap<Produto, Float>(comparador);

        System.out.println("unsorted map: " + map);
        sorted_map.putAll(map);
        System.out.println("results: " + sorted_map);
        System.out.println(" ");
    }

    public void criarMatrizDiferenca(Map<Usuario,Map<Produto,Float>> data) {
        matrizDiferenca = new HashMap<Produto, Map<Produto, Float>>();
        matrizFrequencia = new HashMap<Produto, Map<Produto, Integer>>();
        // first iterate through users
        for (Map<Produto, Float> user : data.values()) {
            // then iterate through user data
            for (Map.Entry<Produto, Float> entry : user.entrySet()) {
                if (!matrizDiferenca.containsKey(entry.getKey())) {
                    matrizDiferenca.put(entry.getKey(), new HashMap<Produto, Float>());
                    matrizFrequencia.put(entry.getKey(), new HashMap<Produto, Integer>());
                }
                for (Map.Entry<Produto, Float> entry2 : user.entrySet()) {
                    int oldcount = 0;
                    if (matrizFrequencia.get(entry.getKey()).containsKey(entry2.getKey()))
                        oldcount = matrizFrequencia.get(entry.getKey()).get(entry2.getKey()).intValue();
                    float olddiff = 0.0f;
                    if (matrizDiferenca.get(entry.getKey()).containsKey(entry2.getKey()))
                        olddiff = matrizDiferenca.get(entry.getKey()).get(entry2.getKey()).floatValue();
                    float observeddiff = entry.getValue() - entry2.getValue();
                    matrizFrequencia.get(entry.getKey()).put(entry2.getKey(), oldcount + 1);
                    matrizDiferenca.get(entry.getKey()).put(entry2.getKey(), olddiff + observeddiff);
                }
            }
        }
        for (Produto j : matrizDiferenca.keySet()) {
            for (Produto i : matrizDiferenca.get(j).keySet()) {
                float oldvalue = matrizDiferenca.get(j).get(i).floatValue();
                int count = matrizFrequencia.get(j).get(i).intValue();
                matrizDiferenca.get(j).put(i, oldvalue / count);
            }
        }

    }
}