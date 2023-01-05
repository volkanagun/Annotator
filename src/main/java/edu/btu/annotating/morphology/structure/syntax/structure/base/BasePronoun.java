package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.Pronoun;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class BasePronoun extends Base implements Pronoun {

    public BasePronoun(Object head) {
        super(head);
    }

    @Override
    public Function duplicate() {
        return new BasePronoun((Stem) super.getHead());
    }

    @Override
    public Structure getStructure() {
        return this;
    }

    @Override
    public Structure getDelegate() {
        return this;
    }

    @Override
    public boolean checkMandatory() {
        return true;
    }
}
