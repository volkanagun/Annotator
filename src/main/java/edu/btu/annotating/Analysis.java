package edu.btu.annotating;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root
public class Analysis implements Serializable {
    @Attribute
    public int index;
    @Element
    public String annotation;
    @Element
    public String word;



    public Analysis(@Attribute(name="index") int index, @Element(name="annotation") String annotation, @Element(name="word") String word) {
        this.index = index;
        this.annotation = annotation;
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Analysis analysis = (Analysis) o;

        if (index != analysis.index) return false;
        if (!annotation.equals(analysis.annotation)) return false;
        return word.equals(analysis.word);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + annotation.hashCode();
        result = 31 * result + word.hashCode();
        return result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
