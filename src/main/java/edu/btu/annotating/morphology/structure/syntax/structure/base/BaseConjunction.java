package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Conjunction;
import edu.btu.annotating.morphology.structure.syntax.function.Function;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class BaseConjunction extends Base implements Conjunction {

    public BaseConjunction(Object head) {
        super(head);
    }

    @Override
    public Function duplicate() {
        return new BaseConjunction((Stem) super.getHead());
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
