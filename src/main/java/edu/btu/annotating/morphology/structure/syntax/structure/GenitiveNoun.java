package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.InflectedNoun;
import edu.btu.annotating.morphology.structure.syntax.function.Noun;
import edu.btu.annotating.morphology.structure.syntax.function.Pronoun;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class GenitiveNoun extends Inflected implements InflectedNoun {

    public GenitiveNoun(Function base, Function suffix) {
        super(base, suffix);
    }

    public static Function make(Function base, Function suffix) {
        if (!((Structure) base).isInflectionLocked()) {
            String suff = ((Suffix) suffix).getMorpheme().getTag().toString();
            if ((base.getStructure() instanceof Noun || base.getStructure() instanceof Pronoun || base.getStructure() instanceof PossessiveNoun /*|| base instanceof Infinitive*/ || base.getStructure() instanceof Participle
            ) && suff.equals("GEN")) {
                return (InflectedNoun) new GenitiveNoun(base, suffix);
            }
        }
        return null;
    }

    @Override
    public Function duplicate() {
        return new GenitiveNoun(super.getBase().duplicate(), super.getSuffix().duplicate());
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[GenitiveNoun " + super.getBase() + " " + super.getSuffix() + "]";
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
