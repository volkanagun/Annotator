package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Adverb;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class AdverbPhrase extends Phrase implements Adverb {

    public AdverbPhrase(Function comp, Object head) {
        super(comp, head);
    }

    public static Function make(Function comp, Function head) {
        if (comp instanceof CasedNoun) {
            return null;
        }
        if (comp instanceof Gerundium) {
            return null;
        }
        if (head instanceof CasedNoun) {
            return null;
        }
        if (head instanceof Adverb && comp instanceof Adverb) {
            return new AdverbPhrase(comp, head);
        }
        return null;
    }

    @Override
    public Function duplicate() {
        return new AdverbPhrase(super.getComp(), super.getHead());
    }

    @Override
    public Structure getStructure() {
        return this;
    }

    @Override
    public Structure getDelegate() {
        return ((Function)super.getHead()).getDelegate();
    }

    @Override
    public boolean checkMandatory() {
        return getComp().checkMandatory() && ((Function) getHead()).checkMandatory();
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[AdverbPhrase " + super.getComp() + " " + super.getHead() + "]";
        } else if (Settings.printCode == 1) {
            return super.getComp() + " " + super.getHead();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getComp().toString() + " " + super.getHead().toString();
        } else {
            return "";
        }
    }

}
