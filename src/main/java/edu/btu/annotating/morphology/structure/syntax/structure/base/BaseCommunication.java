package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Communication;
import edu.btu.annotating.morphology.structure.syntax.function.Function;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class BaseCommunication extends Base implements Communication  {

    public BaseCommunication(Object head) {
        super(head);
    }

    @Override
    public Function duplicate() {
        return new BaseCommunication((Stem) super.getHead());
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
