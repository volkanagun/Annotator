package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Adjective;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.Noun;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class BaseAdjective extends Base implements Adjective, Noun {

    public BaseAdjective(Object head) {
        super(head);
    }

    @Override
    public Function duplicate() {
        return new BaseAdjective((Stem) super.getHead());
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
