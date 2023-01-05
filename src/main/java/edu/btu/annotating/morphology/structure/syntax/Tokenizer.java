package edu.btu.annotating.morphology.structure.syntax;

import edu.btu.annotating.morphology.algo.Analyzer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Tokenizer {
    private Analyzer analyzer;
    private String sentence;

    public Tokenizer(Analyzer analyzer, String sentence) {
        this.analyzer = analyzer;
        this.sentence = clear(sentence);
    }

    private String clear(String sentence) {
        String result = sentence;
        while (result.contains("  ")) {
            result = result.replaceAll("  ", " ");
        }
        if (result.startsWith(" ")) {
            result = result.substring(1);
        }
        if (result.endsWith(" ")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer(1);
        Tokenizer tokenizer = new Tokenizer(analyzer, "Misafirleri kabul ettiğini ilan etti");
        ArrayList<ArrayList<String>> r = tokenizer.tokenize();
        for (ArrayList<String> l : r) {
            for (String s : l) {
                System.out.print(s + "//");
            }
            System.out.println();
        }
    }

    public ArrayList<ArrayList<String>> tokenize() {
        String[] t = sentence.split(" ");
        ArrayList<String> inputTokenList = new ArrayList<>(Arrays.asList(t));
        class Box {
            ArrayList<String> tList;
            int index;
            public Box(ArrayList<String> tList, int index) {
                this.tList = tList;
                this.index = index;
            }
        }
        ArrayList<Box> list = new ArrayList<>();
        list.add(new Box(inputTokenList, 0));

        int c = 0;
        do {
            if (c == list.size()) {
                break;
            }
            Box b = list.get(c);
            ArrayList<String> outputTokenList = b.tList;
            int index = b.index;
            for (int i = index; i < outputTokenList.size(); i++) {
                for (int j = 2; j < 4; j++) {
                    String temp = merge(outputTokenList, i, i + j);
                    if (analyzer.getAnalysis(temp).length > 0) {
                        ArrayList<String> tempTokenList = (ArrayList<String>) outputTokenList.clone();
                        tempTokenList.add(i, temp);
                        for (int k = 0; k < j; k++) {
                            tempTokenList.remove(i + 1);
                        }
                        list.add(new Box(tempTokenList, i + 1));
                    }
                }
            }
            c++;
        } while (1 == 1);
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (Box b : list) {
            result.add(b.tList);
        }
        return result;
    }

    private String merge(ArrayList<String> list, int s, int e) {
        if (s >= e || e > list.size()) {
            return "";
        }
        String result = "";
        for (int i = s; i < e; i++) {
            result += list.get(i) + " ";
        }
        return result.substring(0, result.length() - 1);
    }
}
