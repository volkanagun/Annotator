package edu.btu.annotating.morphology.structure.syntax.structure;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public abstract class Syntheme extends Structure {
    private Object head;

    public Syntheme(Object head) {
        this.head = head;
    }

    public Object getHead() {
        return head;
    }

    public void setHead(Object head) {
        this.head = head;
    }
}
