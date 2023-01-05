package edu.btu.annotating.morphology.structure.syntax;

import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;

import java.util.ArrayList;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class MorphoAnalysisMap {

    private Analyzer analyzer;
    private ArrayList<String> tokenList;
    private String[] token;
    private MorphoAnalysis[][] map;
    private long cartesianProduct;

    public MorphoAnalysisMap(Analyzer analyzer, String sentence) {
        // silinecek
    }

    public MorphoAnalysisMap(Analyzer analyzer, ArrayList<String> tokenList) {
        this.analyzer = analyzer;
        this.tokenList = tokenList;

        cartesianProduct = 0;
        if (!tokenList.isEmpty()) {
            cartesianProduct = 1;
            String[] tokens = tokenList.toArray(new String[tokenList.size()]);
            map = new MorphoAnalysis[tokens.length][];
            for (int i = 0; i < tokens.length; i++) {
                MorphoAnalysis[] analysis = null;
                try {
                    analysis = analyzer.getAnalysis(tokens[i]);
                } catch (Exception e) {
//***//                    cartesianProduct = 0;
//***//                    map = null;
//***//                    return;
                }
                //***////////////////////////////////////////////////////////////
                if (analysis == null || analysis.length == 0) {
                    analysis = analyzer.getAnalysis("~~yok");
                }
                //***////////////////////////////////////////////////////////////
                if (analysis.length > 0) {
                    ArrayList<MorphoAnalysis> aList = new ArrayList();
                    for (MorphoAnalysis an : analysis) {
//***//                        if (an.getProbCat() == 0) {//<== ATTENTION
                            aList.add(an);
//***//                        }
                    }
                    if (aList.isEmpty()) {
                        cartesianProduct = 0;
                        map = null;
                        return;
                    }
                    map[i] = aList.toArray(new MorphoAnalysis[aList.size()]);
                    cartesianProduct *= aList.size();
                } else {
                    cartesianProduct = 0;
                    map = null;
                    return;
                }
            }
            token = tokens;
        }
    }

    private String[] detectTokens(String s) {
        // anlaşmayı kabul etti
        String[] t = s.split(" ");
        ArrayList<String> list = new ArrayList<>();
        int i = 0;
        while (i < t.length) {
            String temp = check(t, i, i + 4);
            if (temp.isEmpty()) {
                temp = check(t, i, i + 3);
            }
            if (temp.isEmpty()) {
                temp = check(t, i, i + 2);
            }
            if (temp.isEmpty()) {
                list.add(t[i]);
            } else {
                list.add(temp);
                String[] nt = temp.split(" ");
                i += nt.length;
                continue;
            }
            i++;
        }
        String[] result = new String[list.size()];
        result = list.toArray(result);
        return result;
    }

    private String check(String[] arr, int s, int e) {
        if (e > arr.length) {
            return "";
        }
        String temp = merge(arr, s, e);
        if (analyzer.getAnalysis(temp).length > 0) {
            return temp;
        } else {
            return "";
        }
    }

    private String merge(String[] arr, int s, int e) {
        String result = "";
        for (int i = s; i < e; i++) {
            result += arr[i] + " ";
        }
        return result.substring(0, result.length() - 1);
    }

    public ArrayList<String> getTokenList() {
        return tokenList;
    }

    public String[] getToken() {
        return token;
    }

    public MorphoAnalysis[][] getMap() {
        if (map == null) {
            return null;
        }
        int[] counter = new int[map.length];
        int last = map.length - 1;
        MorphoAnalysis[][] pathArray = new MorphoAnalysis[Math.toIntExact(cartesianProduct)][map.length];
        int cc = 0;
        mainloop:
        while (1 == 1) {

            ArrayList<MorphoAnalysis> path = new ArrayList();
            for (int i = 0; i < counter.length; i++) {
                path.add(map[i][counter[i]]);
            }

            pathArray[cc] = path.toArray(new MorphoAnalysis[path.size()]);

            counter[last]++;
            for (int i = last; i >= 0; i--) {
                if (counter[i] == map[i].length) {
                    if (i == 0) {
                        break mainloop;
                    }
                    counter[i] = 0;
                    counter[i - 1]++;
                }
            }
            cc++;
        }
        return pathArray;
    }

    public void resetMap() {
        map = new MorphoAnalysis[token.length][];
    }

    public void setMap(MorphoAnalysis[][] map) {
        this.map = map;
    }

    public void setCartesianProduct(long cartesianProduct) {
        this.cartesianProduct = cartesianProduct;
    }

    public long getCartesianProduct() {
        return cartesianProduct;
    }

    public MorphoAnalysisMap duplicate() {
        MorphoAnalysisMap m = new MorphoAnalysisMap(this.analyzer, this.tokenList);
        m.token = this.token;
        m.map = this.map;
        m.cartesianProduct = this.cartesianProduct;
        return m;
    }
}