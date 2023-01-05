package edu.btu.annotating.morphology.structure.syntax.function;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public interface Function {
    Function duplicate();
    Structure getStructure();
    Structure getDelegate();
    boolean checkMandatory();
}
