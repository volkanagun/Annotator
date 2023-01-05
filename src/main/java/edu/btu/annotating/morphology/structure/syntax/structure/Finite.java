package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.InflectedVerb;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Finite extends Inflected implements InflectedVerb {

    public Finite(Function base, Function suffix) {
        super(base, suffix);
    }

    public static Function make(Function base, Function suffix) {
        String[] tags = ((Suffix)suffix).getMorpheme().getTag().getElement();
        String[] compoundTags = {"ABIL", "QUICK", "NEAR", "DUR", "INAB"};
        String[] tenseTags = {"IMP", "COND", "PAST", "FUT", "NEC", "CONTI", "AOR", "OPT"};
        String[] personTags = {"PER1S", "PER2S", "PER3S", "PER1P", "PER2P", "PER3P"};
        if ((base instanceof Clause || base instanceof Finite) && isAnyExist(tags, compoundTags)) {
            return (InflectedVerb) new Finite(base, suffix);
        }
        if ((base instanceof Clause || base instanceof Finite) && isAnyExist(tags, tenseTags)) {
            return (InflectedVerb) new Finite(base, suffix);
        }
        if ((base instanceof Clause || base instanceof Finite) && isAnyExist(tags, personTags)) {
            return (InflectedVerb) new Finite(base, suffix);
        }

        return null;
    }

    private static boolean isAnyExist(String[] arr1, String[] arr2) {
        for (String a : arr1) {
            for (String b : arr2) {
                if (a.equals(b)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Function duplicate() {
        return new Finite(super.getBase().duplicate(), super.getSuffix().duplicate());
    }

    @Override
    public String toString() {

        if (Settings.printCode == 0) {
            return "[Finite " + super.getBase() + " " + super.getSuffix() + "]";
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
