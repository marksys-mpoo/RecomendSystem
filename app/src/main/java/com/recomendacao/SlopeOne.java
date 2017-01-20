package com.recomendacao;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import java.util.*;

public class SlopeOne extends AppCompatActivity {

    private Map<Produto,Map<Produto,Float>> matrizDiferenca;
    private Map<Produto,Map<Produto,Integer>> matrizFrequencia;

    private static Produto[] todosItens;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_one);

        Map<Usuario,Map<Produto,Float>> data = new HashMap<Usuario,Map<Produto,Float>>();
        Produto item1 = new Produto("        maçã");
        Produto item2 = new Produto("refrigerante");
        Produto item3 = new Produto("       arroz");
        Produto item4 = new Produto("       sabão");
        Produto item5 = new Produto("creme dental");
        Produto item6 = new Produto("       leite");

        todosItens = new Produto[]{item1, item2, item3, item4, item5, item6};

        HashMap<Produto,Float> user1 = new HashMap<Produto,Float>();
        HashMap<Produto,Float> user2 = new HashMap<Produto,Float>();
        HashMap<Produto,Float> user3 = new HashMap<Produto,Float>();
        HashMap<Produto,Float> user4 = new HashMap<Produto,Float>();
        mapeamento(data, item1, item2, item3, item4, item5, item6, user1, user2, user3, user4);
    }

    private void mapeamento(Map<Usuario, Map<Produto, Float>> data, Produto item1, Produto item2, Produto item3, Produto item4, Produto item5, Produto item6, HashMap<Produto, Float> user1, HashMap<Produto, Float> user2, HashMap<Produto, Float> user3, HashMap<Produto, Float> user4) {
        user1.put(item1,1.0f);
        user1.put(item2,0.5f);
        user1.put(item4,0.1f);
        data.put(new Usuario("João"),user1);
        user2.put(item1,1.0f);
        user2.put(item3,0.5f);
        user2.put(item6,0.3f);
        data.put(new Usuario("Maria"),user2);
        user3.put(item1,0.9f);
        user3.put(item2,0.4f);
        user3.put(item3,0.5f);
        user3.put(item4,0.1f);
        data.put(new Usuario("José"),user3);
        user4.put(item1,0.1f);
        user4.put(item4,1.0f);
        user4.put(item5,0.4f);
        data.put(new Usuario("Ana"),user4);
        criarMatrizDiferenca(data);
        System.out.println(" ");
        System.out.println(" --------------------  INÍCIO - EXECUÇÃO DO PROTÓTIPO --------------------");
        printData(data);
        System.out.println(" ");
        System.out.println("Lendo... User2 - Maria");
        print(user2);
        System.out.println(" ");
        System.out.println("Calculando PREDICT... User2 - Maria");
        printRecomendacao(predict(user2));
        System.out.println(" ");
        System.out.println("Calculando WEIGHTLESSPREDICT... User2 - Maria");
        printRecomendacao(weightlesspredict(user2));
    }

    public Map<Produto,Float> predict(Map<Produto,Float> user) {
        HashMap<Produto,Float> predictions = new HashMap<Produto,Float>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : matrizDiferenca.keySet()) {
            frequencies.put(j,0);
            predictions.put(j,0.0f);
        }
        for (Produto j : user.keySet()) {
            for (Produto k : matrizDiferenca.keySet()) {
                try {
                    float newval = ( matrizDiferenca.get(k).get(j).floatValue() + user.get(j).floatValue() ) * matrizFrequencia.get(k).get(j).intValue();
                    predictions.put(k, predictions.get(k)+newval);
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
        for (Produto j : user.keySet()) {
            cleanpredictions.put(j,user.get(j));
        }
        return cleanpredictions;
    }

    public Map<Produto,Float> weightlesspredict(Map<Produto,Float> user) {
        HashMap<Produto,Float> predictions = new HashMap<Produto,Float>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : matrizDiferenca.keySet()) {
            predictions.put(j,0.0f);
            frequencies.put(j,0);
        }
        for (Produto j : user.keySet()) {
            for (Produto k : matrizDiferenca.keySet()) {
                try {
                //System.out.println("Average diff between "+j+" and "+ k + " is "+matrizDiferenca.get(k).get(j).floatValue()+" with n = "+matrizFrequencia.get(k).get(j).floatValue());
                float newval = ( matrizDiferenca.get(k).get(j).floatValue() + user.get(j).floatValue() ) ;
                predictions.put(k, predictions.get(k)+newval);
                } catch(NullPointerException e) {}
            }
        }
        for (Produto j : predictions.keySet()) {
            predictions.put(j, predictions.get(j).floatValue()/user.size());
        }
        for (Produto j : user.keySet()) {
            predictions.put(j,user.get(j));
        }
        return predictions;
    }

    public void printData(Map<Usuario,Map<Produto,Float>> data) {
        System.out.println(" ");
        System.out.println("************ Notas Dados pelos Usuários *********");
        for(Usuario user : data.keySet()) {
            System.out.println(user);
            print(data.get(user));
        }

        System.out.println(" ");
        System.out.println("************ Matriz Diferença [Itens x (Média das diferenças dos votos e Frequências dos itens juntos) ] ************");
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

    private void printMatrizes(Map<Produto,Float> ratings,
                               Map<Produto,Integer> frequencies) {
        for (int j = 0; j< todosItens.length; j++) {
            System.out.format("%10.3f", ratings.get(todosItens[j]) );
            System.out.print(" ");
            System.out.format("%10s", frequencies.get(todosItens[j]) + " |");
        }
        System.out.println();
    }

    public static void print(Map<Produto,Float> user) {
        for (Produto j : user.keySet()) {
            System.out.println(" "+ j+ " --> "+user.get(j).floatValue());
        }
    }

    public static void printRecomendacao(Map<Produto,Float> user) {
        ordenarCompare(user);
        System.out.println(" --------------------  FIM - EXECUÇÃO DO PROTÓTIPO --------------------");
        System.out.println(" ");
    }

    public static void ordenarCompare(Map<Produto, Float> map) {
        System.out.println(" ");
        System.out.println("************ ORDENA COMPARE (User2 - Maria) *********");

        Comparador bvc = new Comparador(map);
        Map<Produto, Float> sorted_map = new TreeMap<Produto, Float>(bvc);

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