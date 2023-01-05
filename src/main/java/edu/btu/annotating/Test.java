package edu.btu.annotating;

import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.AnalysisFormatters;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;

public class Test {
    public static void main(String[] args) {

        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        String word = "kalemi";


        WordAnalysis results = morphology.analyze(word);
        for (SingleAnalysis result : results) {

        }
    }

}
