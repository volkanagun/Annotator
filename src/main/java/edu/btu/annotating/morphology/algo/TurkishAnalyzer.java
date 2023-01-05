package edu.btu.annotating.morphology.algo;

import java.util.ArrayList;
import java.util.List;

public class TurkishAnalyzer {
    int sortMode = 1;
    Analyzer analyzer = new Analyzer(sortMode);

    public List<String> analyze(String token) {
        MorphoAnalysis[] analyses = analyzer.getAnalysis(token);
        List<String> analysisList = new ArrayList<>();
        for(MorphoAnalysis analysis:analyses){
            String subList = analysis.getStem().getLexical();
            for(Morpheme morpheme:analysis.getMorpheme()){
                subList +="|" + (morpheme.getSurface() + ":" + morpheme.getTag().toString());
            }

            analysisList.add(subList);
        }

        return analysisList;
    }

    public static void main(String[] args) {
        TurkishAnalyzer tr = new TurkishAnalyzer();
        for(String analysis:tr.analyze("karşısındakiler")){
            System.out.println(analysis);
        }
    }
}
