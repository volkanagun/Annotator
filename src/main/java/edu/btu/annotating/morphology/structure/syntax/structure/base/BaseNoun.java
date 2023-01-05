package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.Noun;
import edu.btu.annotating.morphology.structure.syntax.function.Noun2;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class BaseNoun extends Base implements Noun, Noun2 {

    public BaseNoun(Object head) {
        super(head);
    }

    @Override
    public Function duplicate() {
        return new BaseNoun((Stem) super.getHead());
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
