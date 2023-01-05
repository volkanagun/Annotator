package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.base.BaseConjunction;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class ConjunctionPhrase extends Phrase implements Function {

    public ConjunctionPhrase(Function comp, Object head) {
        super(comp, head);
    }

    public static Function make(Function comp, Function head) {
        if (head instanceof BaseConjunction && ((Stem)((BaseConjunction) head).getHead()).getState().equals("cnj2") && comp instanceof Function) {
            ConjunctionPhrase conj = new ConjunctionPhrase(comp, head);
            if (conj != null) {
                conj.setIncomplete(true);
            }
            return conj;
        }
        if (head instanceof Function && comp instanceof ConjunctionPhrase && ((ConjunctionPhrase)comp).isIncomplete()) {
            Function f1 = ((ConjunctionPhrase)comp).getComp();
            Function f2 = head;
            if (!Controller.checkIsomorphism(f1, f2)) {
                return null;
            }
            ConjunctionPhrase conj = new ConjunctionPhrase(comp, head);
            if (conj != null) {
                conj.setIncomplete(false);
            }
            return conj;
        }

        return null;
    }

    @Override
    public Function duplicate() {
        return new ConjunctionPhrase(super.getComp(), super.getHead());
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[ConjunctionPhrase " + super.getComp() + " " + super.getHead() + "]";
        } else if (Settings.printCode == 1) {
            return super.getComp() + " " + super.getHead();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getComp().toString() + " " +  super.getHead().toString();
        } else {
            return "";
        }
    }

    @Override
    public Structure getStructure() {
        if (!this.isIncomplete()) {
            return ((Function)super.getHead()).getStructure();
        } else {
            return null;
        }
    }

    @Override
    public boolean doesItContainParticiple() {
        if (!this.isIncomplete() && super.getHead() instanceof PossessiveNoun && ((PossessiveNoun)super.getHead()).getBase() instanceof Participle) {
            return true;
        }
        return false;
    }

    @Override
    public Structure getDelegate() {
        if (this.isIncomplete()) {
            return ((Function)super.getComp()).getDelegate();
        } else {
            return ((Function)super.getHead()).getDelegate();
        }
    }

    @Override
    public boolean checkMandatory() {
        return getComp().checkMandatory() && ((Function) getHead()).checkMandatory();
    }
}
