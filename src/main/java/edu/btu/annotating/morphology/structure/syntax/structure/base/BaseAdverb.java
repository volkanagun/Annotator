package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Adverb;
import edu.btu.annotating.morphology.structure.syntax.function.Function;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class BaseAdverb extends Base implements Adverb {

    public BaseAdverb(Object head) {
        super(head);
    }

    @Override
    public Function duplicate() {
        return new BaseAdverb((Stem) super.getHead());
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
