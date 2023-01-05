package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Inflected extends Structure {
    private Function base;
    private Function suffix;
    private boolean containParticiple;

    public Inflected(Function base, Function suffix) {
        this.base = base;
        this.suffix = suffix;
    }

    public Function getBase() {
        return base;
    }

    public void setBase(Function base) {
        this.base = base;
    }

    public Function getSuffix() {
        return suffix;
    }

    public void setSuffix(Function suffix) {
        this.suffix = suffix;
    }

    public Structure getDelegate() {
        return null;
    }

    public boolean doesItContainParticiple() {
        //if (base instanceof Inflected && ((Inflected)base).doesItContainParticiple()) {
        if (base instanceof Inflected && ((Inflected)base) instanceof Participle) {
            containParticiple = true;
        } else if (base instanceof Participle) {
            containParticiple = true;
        }
        return containParticiple;
    }
}
