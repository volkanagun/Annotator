package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class ChainPhrase extends Phrase implements Function {

    public ChainPhrase(Function comp, Object head) {
        super(comp, head);
    }

    public static Function make(Function comp, Function head) {
        if ((head instanceof ConjunctionPhrase && !(comp instanceof ConjunctionPhrase)) ||
                (comp instanceof ConjunctionPhrase && !(head instanceof ConjunctionPhrase)) ||
                (head instanceof Transparent && !(comp instanceof Transparent)) ||
                (comp instanceof Transparent && !(head instanceof Transparent))) {
            return null;
        }
        if (head instanceof Function && comp instanceof ChainPhrase) {
            Function f1 = ((ChainPhrase) comp).getComp();
            Function f2 = head;
            if (!Controller.checkIsomorphism2(f1, f2)) {
                return null;
            }
            ChainPhrase chain = new ChainPhrase(comp, head);
            return chain;
        }
        if (head instanceof Function && comp instanceof Function) {
            if (!Controller.checkIsomorphism2(comp, head)) {
                return null;
            }
            ChainPhrase chain = new ChainPhrase(comp, head);
            return chain;
        }

        return null;
    }

    @Override
    public Function duplicate() {
        return new ChainPhrase(super.getComp(), super.getHead());
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[ChainPhrase " + super.getComp() + " " + super.getHead() + "]";
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
        return ((Function) super.getHead()).getStructure();
    }

    @Override
    public boolean doesItContainParticiple() {
        if (super.getHead() instanceof PossessiveNoun && ((PossessiveNoun)super.getHead()).getBase() instanceof Participle) {
            return true;
        }
        return false;
    }

    @Override
    public Structure getDelegate() {
        return ((Function)super.getHead()).getDelegate();
    }

    @Override
    public boolean checkMandatory() {
        return getComp().checkMandatory() && ((Function) getHead()).checkMandatory();
    }
}
