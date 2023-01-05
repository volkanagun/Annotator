package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public abstract class Phrase extends Syntheme {
    private Function comp;

    public Phrase(Function comp, Object head) {
        super(head);
        this.comp = comp;
    }

    public Function getComp() {
        return comp;
    }

    public void setComp(Function comp) {
        this.comp = comp;
    }

    public boolean doesItContainParticiple() {
        return false;
    }

    public Structure getDelegate() {
        return null;
    }
}
