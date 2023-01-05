package edu.btu.annotating;

import edu.btu.annotating.morphology.algo.TurkishAnalyzer;
import javafx.scene.control.ProgressBar;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.tokenization.Token;
import zemberek.tokenization.TurkishSentenceExtractor;
import zemberek.tokenization.TurkishTokenizer;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class AnalysisIO {

    private String filename = "resources/mysentences.txt";
    private String analysisFolder = "resources/analysis/";
    private List<String> paragraphs = new ArrayList<>();
    private List<AnalysisParagraph> analysisParagraphs = new ArrayList<>();
    private int currentSentence;
    private int current;

    private Map<Integer, AnalysisParagraph> analyzedMap = new HashMap<>();
    private TurkishMorphology morphology;
    private TurkishAnalyzer analyzer;

    public AnalysisIO initialize(ProgressBar progressBar, int size) {

        morphology = TurkishMorphology.createWithDefaults();
        analyzer = new TurkishAnalyzer();
       new Thread() {
            @Override
            public void run() {
                readXML();

                double progress = 0.0;

                //read sentences
                List<String> paragraphList = new TextFile(filename).readLines();
                double movement = 1.0/size;
                int count = 0;
                for (int i = 0; i < paragraphList.size(); i++) {
                    String paragraph = paragraphList.get(i);
                    int key = paragraph.hashCode();
                    if (!new File(analysisFolder + key).exists()) {
                        paragraphs.add(paragraph);
                        progress += movement;
                        progressBar.setProgress(progress);
                        count++;
                    }

                    if(count >= size) {break;}

                }
            }
        }.start();


        return this;
    }

    public List<Analysis> analyzeByZemberek(int i, Token word) {
        List<Analysis> analysisList = new ArrayList<>();
        WordAnalysis results = morphology.analyze(word);
        results.forEach(result -> {
            Analysis analysis = new Analysis(i, result.formatLexical(), word.normalized);
            analysisList.add(analysis);
        });
        char[] chars = word.content.toCharArray();
        if (Character.isUpperCase(chars[0]) || analysisList.isEmpty()) {
            String item = "[" + word.content + ":Noun] Noun+A3sg";
            Analysis analysis = new Analysis(i, item, word.content);
            analysisList.add(analysis);
        }
        return analysisList;
    }

    public List<Analysis> analyzeByOzkan(int i, Token word) {
        List<Analysis> analysisList = new ArrayList<>();
        List<String> results = analyzer.analyze(word.normalized);
        results.forEach(result -> {
            Analysis analysis = new Analysis(i, result, word.normalized);
            analysisList.add(analysis);
        });

        char[] chars = word.content.toCharArray();
        if (Character.isUpperCase(chars[0]) || analysisList.isEmpty()) {
            String item = word.content + ":Noun|A3sg";
            Analysis analysis = new Analysis(i, item, word.content);
            analysisList.add(analysis);
        }
        return analysisList;
    }

    public AnalysisSequence analyzeSentence(int pindex, int index, String sentence) {
        TurkishTokenizer tokenizer = TurkishTokenizer.ALL;
        List<Token> tokenList = tokenizer.tokenize(sentence);
        List<List<Analysis>> wordList = new ArrayList<>();
        List<Analysis> selectedList = new ArrayList<>();
        List<String> wordStringList = new ArrayList<>();
        AnalysisSequence analysisSequence = new AnalysisSequence(pindex, index, sentence);
        int wordIndex = 0;
        for (int i = 0; i < tokenList.size(); i++) {
            Token token = tokenList.get(i);
            if (!token.normalized.trim().isEmpty()) {
                wordStringList.add(token.normalized);
                if (token.getType().name().equals("Word")) {
                    List<Analysis> analyzed = analyzeByOzkan(wordIndex, token);
                    wordList.add(analyzed);
                    selectedList.add(analyzed.get(0));
                } else {
                    List<Analysis> analysisList = new ArrayList<>();
                    Analysis singleAnalysis = new Analysis(wordIndex, token.normalized, token.normalized);
                    analysisList.add(singleAnalysis);
                    wordList.add(analysisList);
                    selectedList.add(singleAnalysis);
                }

                wordIndex++;
            }
        }

        analysisSequence.setSuggestedList(wordList);
        analysisSequence.setSelectedList(selectedList);
        analysisSequence.setWords(wordStringList);
        return analysisSequence;
    }

    public AnalysisParagraph analyzeParagraph(String text){
        TurkishSentenceExtractor extractor = TurkishSentenceExtractor.DEFAULT;
        int pindex = text.hashCode();
        List<String> sentences = extractor.fromParagraph(text);
        List<AnalysisSequence> sequences = new ArrayList<>();
        for(String sentence:sentences){
            AnalysisSequence sequence = analyzeSentence(pindex, sentence.hashCode(), sentence);
            sequences.add(sequence);
        }

        return new AnalysisParagraph(pindex, text, sequences);
    }

    public AnalysisSequence current() {
        String paragraph = paragraphs.get(current);

        if (analyzedMap.containsKey(paragraph.hashCode())) {
            return analyzedMap.get(paragraph.hashCode()).get(currentSentence);
        } else {
            AnalysisParagraph sequence = analyzeParagraph(paragraph);
            analyzedMap.put(paragraph.hashCode(), sequence);
            analysisParagraphs.add(sequence);
            return sequence.get(currentSentence);
        }
    }

    public void check(int paragraphKey, int sentenceKey, Analysis selectedAnalysis) {
        if (analyzedMap.containsKey(paragraphKey)) {
            AnalysisParagraph paragraph = analyzedMap.get(paragraphKey);
            paragraph.setAnalysis(sentenceKey, selectedAnalysis);
        }
    }

    public boolean isChecked(int paragraphKey, int sentenceKey, Analysis selectedAnalysis) {
        if (analyzedMap.containsKey(paragraphKey)) {
            return analyzedMap.get(paragraphKey).equalsAnalysis(sentenceKey, selectedAnalysis);
        } else return false;
    }

    public AnalysisSequence next() {
        if (hasCurrent()  && hasNextSentence()) {
            currentSentence++;
            return current();
        }else if(hasCurrent() && hasNext()) {
            currentSentence = 0;
            current++;
            return current();
        }
        else return current();
    }

    public AnalysisSequence previous() {
        if (hasCurrent() && hasPreviousSentence()) {
            currentSentence--;
            return current();
        } else if(hasCurrent() && hasPrevious()) {
            currentSentence = 0;
            current--;
            return current();
        }
        else return current();
    }

    public void saveXML() {
        List<String> files = Arrays.stream(new File(analysisFolder).listFiles()).map(f -> f.getName())
                .collect(Collectors.toList());
        Serializer serializer = new Persister();
        for (Map.Entry<Integer, AnalysisParagraph> paragraph : analyzedMap.entrySet()) {
            if (!files.contains(paragraph.getKey().toString())) {
                File f = new File(analysisFolder + paragraph.getKey());
                try {
                    serializer.write(paragraph.getValue(), f);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void readXML() {
        File[] files = new File(analysisFolder).listFiles();
        for (File f : files) {
            Integer key = Integer.parseInt(f.getName());
            if (!analyzedMap.containsKey(key)) {
                Serializer serializer = new Persister();
                try {
                    AnalysisParagraph example = serializer.read(AnalysisParagraph.class, f);
                    analyzedMap.put(key, example);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    public boolean hasNext() {
        return current < (paragraphs.size() - 1);
    }

    public boolean hasNextSentence() {
        return currentSentence < analysisParagraphs.get(current).sequenceList.size();
    }

    public boolean hasPrevious() {
        return current > 0;
    }

    public boolean hasPreviousSentence() {
        return currentSentence > 0;
    }

    public boolean hasCurrent(){
        return analysisParagraphs.size() > current;
    }
}
