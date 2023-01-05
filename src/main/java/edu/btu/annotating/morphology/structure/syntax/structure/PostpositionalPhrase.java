package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.*;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.base.Base;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class PostpositionalPhrase extends Phrase implements Adverb {

    public PostpositionalPhrase(Function comp, Object head) {
        super(comp, head);
    }

    public static Function make(Function comp, Object head) {
        if (head instanceof Postposition && (comp instanceof Noun || comp instanceof Pronoun)) {
            Base base = (Base) head;
            if (base.getSubcat().contains("NOM")) {
                // comp yalin halde ise
                return new PostpositionalPhrase(comp, head);
            }
        } else if (head instanceof Postposition && comp instanceof CasedNoun) {
            Base base = (Base) head;
            CasedNoun cn = (CasedNoun) comp;
            Suffix tSuf = (Suffix) cn.getSuffix();
            if (base.getSubcat().contains(tSuf.getMorpheme().getTag().toString())) {
                // comp yalin halde degilse
                return new PostpositionalPhrase(comp, head);
            }
        }

        return null;
    }

    @Override
    public Function duplicate() {
        return new PostpositionalPhrase(super.getComp(), super.getHead());
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[PostpositionalPhrase " + super.getComp() + " " + super.getHead() + "]";
        } else if (Settings.printCode == 1) {
            return super.getComp() + " " + super.getHead();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getComp().toString() + " " + super.getHead().toString();
        } else {
            return "";
        }
    }

    @Override
    public Structure getStructure() {
        return this;
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
