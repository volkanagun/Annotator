package edu.btu.annotating;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

public class AnalysisParagraph {

    @Attribute
    public int pindex;
    @Element
    public String text;

    @ElementList
    public List<AnalysisSequence> sequenceList;

    public AnalysisParagraph(@Attribute(name="pindex") int pindex, @Element(name = "text") String text, @ElementList(name="sequenceList") List<AnalysisSequence> sequenceList) {
        this.pindex = pindex;
        this.text = text;
        this.sequenceList = sequenceList;
    }

    public AnalysisParagraph(int pindex, String text) {
        this.pindex = pindex;
        this.text = text;
        this.sequenceList = new ArrayList<>();
    }

    public boolean isNotEmpty(){
        return !sequenceList.isEmpty();
    }

    public void setAnalysis(int sentenceKey, Analysis analysis){
        int i = 0;
        while(i < sequenceList.size()){
            if(sequenceList.get(i).key == sentenceKey) break;
            i++;
        }

        if(i < sequenceList.size()) sequenceList.get(i).selectedList.set(analysis.index, analysis);
    }

    public AnalysisSequence get(int index){
        if(sequenceList.size() > index) return sequenceList.get(index);
        else return sequenceList.get(0);
    }

    public boolean equalsAnalysis(int sentenceKey, Analysis analysis){
        int i = 0;
        while(i < sequenceList.size()){
            if(sequenceList.get(i).key == sentenceKey) break;
            i++;
        }

        if(i < sequenceList.size()) return sequenceList.get(i).selectedList.get(analysis.index).equals(analysis);
        else  return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisParagraph that = (AnalysisParagraph) o;

        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    public int getPindex() {
        return pindex;
    }

    public String getText() {
        return text;
    }

    public List<AnalysisSequence> getSequenceList() {
        return sequenceList;
    }
}
