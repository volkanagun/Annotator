package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.base.BaseConjunction;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Transparent extends Phrase implements Function {

    public Transparent(Function comp, Object head) {
        super(comp, head);
    }

    public static Function make(Function comp, Function head) {
        if (head instanceof BaseConjunction && ((Stem)((BaseConjunction) head).getHead()).getState().equals("cnj1") && comp instanceof Function) {
            return new Transparent(comp, head);
        }
        return null;
    }

    @Override
    public Function duplicate() {
        return new Transparent(super.getComp(), super.getHead());
    }

    @Override
    public Structure getStructure() {
        return ((Function)super.getComp()).getStructure();
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[Transparent " + super.getComp() + " " + super.getHead() + "]";
        } else if (Settings.printCode == 1) {
            return super.getComp() + " " + super.getHead();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getComp().toString() + " " + super.getHead().toString();
        } else {
            return "";
        }
    }

    @Override
    public Structure getDelegate() {
        return ((Function)super.getComp()).getDelegate();
    }

    @Override
    public boolean checkMandatory() {
        return getComp().checkMandatory() && ((Function) getHead()).checkMandatory();
    }

}
