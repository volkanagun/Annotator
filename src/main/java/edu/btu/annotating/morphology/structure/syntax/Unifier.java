package edu.btu.annotating.morphology.structure.syntax;

import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.structure.syntax.function.Function;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Unifier {

    public static void main(String[] args) throws InterruptedException {
        Analyzer analyzer = new Analyzer(1);
        SyntaxAnalysis sa = Unifier.unify(analyzer, "Meseleyi idrak ettiğimizi ilan etti", 1, 5000, 10000);
        for (Function f : sa.getTreeList()) {
            System.out.println(f);
        }
    }

    public static SyntaxAnalysis unify(Analyzer analyzer, String sentence, int numThread, int blockLimit, long pathLimit) throws InterruptedException {
        Tokenizer tokenizer = new Tokenizer(analyzer, sentence);
        ArrayList<ArrayList<String>> tokenMatrix = tokenizer.tokenize();
        ArrayList<MorphoAnalysisMap> mamList = new ArrayList<>();
        long totalCartesian = 0;
        for (ArrayList<String> tokenList : tokenMatrix) {
            MorphoAnalysisMap m = new MorphoAnalysisMap(analyzer, tokenList);
            mamList.add(m);
            totalCartesian += m.getCartesianProduct();
        }
        if (totalCartesian > pathLimit) {
            return null;
        }
        SyntaxAnalysis sa = new SyntaxAnalysis(analyzer, mamList, numThread, blockLimit);
        return sa;
    }

    public static ArrayList<MorphoAnalysisMap> unify2(Analyzer analyzer, String sentence, long pathLimit) throws InterruptedException {
        Tokenizer tokenizer = new Tokenizer(analyzer, sentence);
        ArrayList<ArrayList<String>> tokenMatrix = tokenizer.tokenize();
        ArrayList<MorphoAnalysisMap> mamList = new ArrayList<>();
        long totalCartesian = 0;
        for (ArrayList<String> tokenList : tokenMatrix) {
            MorphoAnalysisMap m = new MorphoAnalysisMap(analyzer, tokenList);
            mamList.add(m);
            totalCartesian += m.getCartesianProduct();
        }
        if (totalCartesian > pathLimit) {
            return null;
        }
        return mamList;
    }

    public static MorphoAnalysisMap unify3(Analyzer analyzer, String sentence, long pathLimit) throws InterruptedException {
        ArrayList<String> tokenList = new ArrayList<>(Arrays.asList(sentence.split(" ")));
        MorphoAnalysisMap mam = new MorphoAnalysisMap(analyzer, tokenList);
        if (mam.getCartesianProduct() > pathLimit) {
            return null;
        }
        return mam;
    }
}
