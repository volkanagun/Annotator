package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.*;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

import java.util.Arrays;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class CasedNoun extends Inflected implements InflectedNoun, Adverb {

    public CasedNoun(Function base, Function suffix) {
        super(base, suffix);
    }

    public static Function make(Function base, Function suffix) {
        if (!((Structure) base).isInflectionLocked()) {
            String suff = ((Suffix) suffix).getMorpheme().getTag().toString();
            String[] caseArr = {"ABL", "ACC", "DAT", "EQU", "ILE", "INS", "LOC"};
            if ((base instanceof Noun || base instanceof Pronoun || base instanceof InflectedNoun /*|| base instanceof Adjective*/
                    || base instanceof ConjunctionPhrase || base instanceof Participle) && Arrays.asList(caseArr).contains(suff)) {
                return (InflectedNoun) new CasedNoun(base, suffix);
            }
        }
        return null;
    }

    @Override
    public Function duplicate() {
        return new CasedNoun(super.getBase().duplicate(), super.getSuffix().duplicate());
    }

    @Override
    public String toString() {

        if (Settings.printCode == 0) {
            return "[CasedNoun " + super.getBase() + " " + super.getSuffix() + "]";
        } else if (Settings.printCode == 1) {
            return super.getBase() + " " + super.getSuffix();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getBase().toString() + super.getSuffix().toString();
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
        return super.getBase().getDelegate();
    }

    @Override
    public boolean checkMandatory() {
        return getBase().checkMandatory();
    }
}
