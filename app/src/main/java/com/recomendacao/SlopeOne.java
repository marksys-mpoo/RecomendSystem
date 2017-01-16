package com.recomendacao;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import java.util.*;

public class SlopeOne extends AppCompatActivity {

    //SlopeOne collectionsTeste = new SlopeOne();

    private Map<Usuario,Map<Produto,Float>> mData;
    private Map<Produto,Map<Produto,Float>> mDiffMatrix;
    private Map<Produto,Map<Produto,Integer>> mFreqMatrix;

    private static Produto[] mAllItems;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_one);

        // this is my data base
        Map<Usuario,Map<Produto,Float>> data = new HashMap<Usuario,Map<Produto,Float>>();
        // items
        Produto item1 = new Produto("       candy");
        Produto item2 = new Produto("         dog");
        Produto item3 = new Produto("         cat");
        Produto item4 = new Produto("         war");
        Produto item5 = new Produto("strange food");
        Produto item6 = new Produto("       leite");

        mAllItems = new Produto[]{item1, item2, item3, item4, item5, item6};

        //I'm going to fill it in
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
        data.put(new Usuario("Bob"),user1);
        user2.put(item1,1.0f);
        user2.put(item3,0.5f);
        user2.put(item4,0.2f);
        user2.put(item6,0.3f);
        data.put(new Usuario("Jane"),user2);
        user3.put(item1,0.9f);
        user3.put(item2,0.4f);
        user3.put(item3,0.5f);
        user3.put(item4,0.1f);
        data.put(new Usuario("Jo"),user3);
        user4.put(item1,0.1f);
        //user4.put(item2,0.4f);
        //user4.put(item3,0.5f);
        user4.put(item4,1.0f);
        user4.put(item5,0.4f);
        data.put(new Usuario("StrangeJo"),user4);
        // next, I create my predictor engine
        //SlopeOne so = new SlopeOne(data);
        buildDiffMatrix(data);
        System.out.println(" ");
        System.out.println(" --------------------  INÍCIO - EXECUÇÃO DO PROTÓTIPO --------------------");
        System.out.println("Here's the data I have accumulated...");
        //so.printData();
        printData(data);
        // then, I'm going to test it out...
        System.out.println("Inputting... User2");
        print(user2);
        System.out.println("Getting... User2");
        printRecomendacao(predict(user2));
    }

    /**
     * Based on existing data, and using weights,
     * try to predict all missing ratings.
     * The trick to make this more scalable is to consider
     * only mDiffMatrix entries having a large  (>1) mFreqMatrix
     * entry.
     *
     * It will output the prediction 0 when no prediction is possible.
     */
    public Map<Produto,Float> predict(Map<Produto,Float> user) {
        HashMap<Produto,Float> predictions = new HashMap<Produto,Float>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : mDiffMatrix.keySet()) {
            frequencies.put(j,0);
            predictions.put(j,0.0f);
        }
        for (Produto j : user.keySet()) {
            for (Produto k : mDiffMatrix.keySet()) {
                try {
                    float newval = ( mDiffMatrix.get(k).get(j).floatValue() + user.get(j).floatValue() ) * mFreqMatrix.get(k).get(j).intValue();
                    predictions.put(k, predictions.get(k)+newval);
                    frequencies.put(k, frequencies.get(k)+mFreqMatrix.get(k).get(j).intValue());
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

    /**
     * Based on existing data, and not using weights,
     * try to predict all missing ratings.
     * The trick to make this more scalable is to consider
     * only mDiffMatrix entries having a large  (>1) mFreqMatrix
     * entry.
     */
    public Map<Produto,Float> weightlesspredict(Map<Produto,Float> user) {
        HashMap<Produto,Float> predictions = new HashMap<Produto,Float>();
        HashMap<Produto,Integer> frequencies = new HashMap<Produto,Integer>();
        for (Produto j : mDiffMatrix.keySet()) {
            predictions.put(j,0.0f);
            frequencies.put(j,0);
        }
        for (Produto j : user.keySet()) {
            for (Produto k : mDiffMatrix.keySet()) {
                //System.out.println("Average diff between "+j+" and "+ k + " is "+mDiffMatrix.get(k).get(j).floatValue()+" with n = "+mFreqMatrix.get(k).get(j).floatValue());
                float newval = ( mDiffMatrix.get(k).get(j).floatValue() + user.get(j).floatValue() ) ;
                predictions.put(k, predictions.get(k)+newval);
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
        System.out.println("************ Votos Dados pelos Usuários *********");
        for(Usuario user : data.keySet()) {
            System.out.println(user);
            print(data.get(user));
        }

        System.out.println(" ");
        System.out.println("************ Matriz Diferença [Itens x (Média das diferenças dos votos e Frequências dos itens juntos) ] ************");
        System.out.print("             |");
        for (int g=0; g<mAllItems.length; g++) { // mAllItems[i]
            System.out.format("%21s", mAllItems[g] + "       |");
        }
        System.out.println(" ");
        System.out.print("             |");
        for (int h=0; h<mAllItems.length; h++) { // mAllItems[i]
            System.out.print("  Votos    Frequenc |");
        }
        System.out.println(" ");
        for (int i=0; i<mAllItems.length; i++) {
            System.out.print("\n" + mAllItems[i] + ":|");
            printMatrixes(mDiffMatrix.get(mAllItems[i]), mFreqMatrix.get(mAllItems[i]));
        }
    }

    private void printMatrixes(Map<Produto,Float> ratings,
                               Map<Produto,Integer> frequencies) {
        for (int j=0; j<mAllItems.length; j++) {
            System.out.format("%10.3f", ratings.get(mAllItems[j]) );
            System.out.print(" ");
            System.out.format("%10s", frequencies.get(mAllItems[j]) + " |");
        }
        System.out.println();
    }

    public static void print(Map<Produto,Float> user) {
        for (Produto j : user.keySet()) {
            System.out.println(" "+ j+ " --> "+user.get(j).floatValue());
        }
    }

    public static void printRecomendacao(Map<Produto,Float> user) {
        Map<Float,Produto> mapSemOrdem = new HashMap<>();
        Map<Produto, Float> mapSemOrdemCompare = new HashMap<>();
        for (Produto j : user.keySet()) {
            System.out.println(" TESTE2 "+ j+ " --> "+user.get(j).floatValue());
            mapSemOrdem.put(user.get(j).floatValue(), j);
            mapSemOrdemCompare.put(j, user.get(j).floatValue());
        }
        ordenar(mapSemOrdem);
        ordenarSort(mapSemOrdem);
        ordenarCompare(mapSemOrdemCompare);
        System.out.println(" --------------------  FIM - EXECUÇÃO DO PROTÓTIPO --------------------");
        System.out.println(" ");
    }

    public static void ordenarCompare(Map<Produto, Float> map) {
        System.out.println(" ");
        System.out.println("************ ORDENA COMPARE (Usuário Logado) *********");

        ValueComparator bvc = new ValueComparator(map);
        Map<Produto, Float> sorted_map = new TreeMap<Produto, Float>(bvc);

        System.out.println("unsorted map: " + map);
        sorted_map.putAll(map);
        System.out.println("results: " + sorted_map);
        System.out.println(" ");

    }

    public static void ordenarSort(Map<Float,Produto> map) {
        System.out.println(" ");
        System.out.println("************ Produtos Ordenados pela Classificação (Usuário Logado) *********");
        List<Combine> listCombinada = new ArrayList<Combine>();

        for (Map.Entry<Float,Produto> value : map.entrySet()) {
            Combine a = new Combine(value.getValue(), value.getKey());
            listCombinada.add(a);
        }
        System.out.println(" ");
        Collections.sort(listCombinada);
        System.out.println("************ TESTES COMBINADA (Recomendação para o Usuário Logado) *********");
        for (int i = 0; i < listCombinada.size(); i++) {
            System.out.println(listCombinada.get(i));
        }
        System.out.println(" ");
        /*
        System.out.println(" ");
        System.out.println("************ TESTES (Recomendação para o Usuário Logado) *********");
        for (Produto s : listCombinada)
        {
            System.out.println("" + s);
        }
        System.out.println(" --------------------  TESTES - EXECUÇÃO DO PROTÓTIPO --------------------");
        System.out.println(" ");*/

    }

    public static void ordenar(Map<Float,Produto> map) {
        System.out.println(" ");
        System.out.println("************ Produtos Ordenados pela Classificação (Usuário Logado) *********");
        List<Produto> listProdutosRecomendados = new ArrayList<Produto>();
        Map<Float,Produto> treeMap = new TreeMap<Float,Produto>(map);
        for (Map.Entry<Float,Produto> entry : treeMap.entrySet() ) {
            System.out.println("" + entry );
            listProdutosRecomendados.add(entry.getValue());
        }
        Collections.reverse(listProdutosRecomendados);
        System.out.println(" ");
        System.out.println("************ Lista Decrescente de Produtos (Recomendação para o Usuário Logado) *********");
        for (Produto s : listProdutosRecomendados)
        {
            System.out.println("" + s);
        }
        System.out.println(" ");

    }

    public void buildDiffMatrix(Map<Usuario,Map<Produto,Float>> data) {
        mDiffMatrix = new HashMap<Produto, Map<Produto, Float>>();
        mFreqMatrix = new HashMap<Produto, Map<Produto, Integer>>();
        // first iterate through users
        for (Map<Produto, Float> user : data.values()) {
            // then iterate through user data
            for (Map.Entry<Produto, Float> entry : user.entrySet()) {
                if (!mDiffMatrix.containsKey(entry.getKey())) {
                    mDiffMatrix.put(entry.getKey(), new HashMap<Produto, Float>());
                    mFreqMatrix.put(entry.getKey(), new HashMap<Produto, Integer>());
                }
                for (Map.Entry<Produto, Float> entry2 : user.entrySet()) {
                    int oldcount = 0;
                    if (mFreqMatrix.get(entry.getKey()).containsKey(entry2.getKey()))
                        oldcount = mFreqMatrix.get(entry.getKey()).get(entry2.getKey()).intValue();
                    float olddiff = 0.0f;
                    if (mDiffMatrix.get(entry.getKey()).containsKey(entry2.getKey()))
                        olddiff = mDiffMatrix.get(entry.getKey()).get(entry2.getKey()).floatValue();
                    float observeddiff = entry.getValue() - entry2.getValue();
                    mFreqMatrix.get(entry.getKey()).put(entry2.getKey(), oldcount + 1);
                    mDiffMatrix.get(entry.getKey()).put(entry2.getKey(), olddiff + observeddiff);
                }
            }
        }
        for (Produto j : mDiffMatrix.keySet()) {
            for (Produto i : mDiffMatrix.get(j).keySet()) {
                float oldvalue = mDiffMatrix.get(j).get(i).floatValue();
                int count = mFreqMatrix.get(j).get(i).intValue();
                mDiffMatrix.get(j).put(i, oldvalue / count);
            }
        }

    }
}

