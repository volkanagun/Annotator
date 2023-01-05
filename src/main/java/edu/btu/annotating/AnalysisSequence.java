package edu.btu.annotating;

import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.List;

@Root
public class AnalysisSequence  {
    @Attribute
    public int key;

    @Attribute
    public int paragraphKey;

    @Element
    public String sentence;

    public List<String> words = new ArrayList<>();
    @ElementList
    public List<Analysis> selectedList = new ArrayList<>();

    public List<List<Analysis>> suggestedList= new ArrayList<>();

    public AnalysisSequence() {
    }

    public AnalysisSequence(@Attribute(name="paragraphKey") int paragraphKey, @Attribute(name="key") int key, @Element(name="sentence") String sentence) {
        this.paragraphKey = paragraphKey;
        this.key = key;
        this.sentence = sentence;
    }

    public AnalysisSequence(@Attribute(name="paragraphKey") int paragraphKey, @Attribute(name="key") int key, @Element(name="sentence") String sentence, @ElementList(name="selectedList") List<Analysis> selectedList) {
        this.paragraphKey = paragraphKey;
        this.key = key;
        this.sentence = sentence;
        this.selectedList = selectedList;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<Analysis> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(List<Analysis> selectedList) {
        this.selectedList = selectedList;
    }

    public List<List<Analysis>> getSuggestedList() {
        return suggestedList;
    }

    public void setSuggestedList(List<List<Analysis>> suggestedList) {
        this.suggestedList = suggestedList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisSequence sequence = (AnalysisSequence) o;

        return sentence.equals(sequence.sentence);
    }

    @Override
    public int hashCode() {
        return sentence.hashCode();
    }
}
