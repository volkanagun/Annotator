package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.InflectedNoun;
import edu.btu.annotating.morphology.structure.syntax.function.Noun;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

import java.util.Arrays;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class PossessiveNoun extends Inflected implements InflectedNoun {

    public PossessiveNoun(Function base, Function suffix) {
        super(base, suffix);
    }

    public static Function make(Function base, Function suffix) {
        if (!((Structure) base).isInflectionLocked() && !((Structure) base).isPossessiveLocked()) {
            String suff = ((Suffix) suffix).getMorpheme().getTag().toString();
            String[] possArr = {"POS1S", "POS2S", "POS3S", "POS1P", "POS2P", "POS3P"};
            if ((base instanceof Noun /*|| base instanceof Infinitive*/ || base instanceof Participle) && Arrays.asList(possArr).contains(suff)) {
                return (InflectedNoun) new PossessiveNoun(base, suffix);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[PossessiveNoun " + super.getBase() + " " + super.getSuffix() + "]";
        } else if (Settings.printCode == 1) {
            return super.getBase() + " " + super.getSuffix();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getBase().toString() + super.getSuffix().toString();
        } else {
            return "";
        }
    }

    @Override
    public Function duplicate() {
        return new PossessiveNoun(super.getBase().duplicate(), super.getSuffix().duplicate());
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
