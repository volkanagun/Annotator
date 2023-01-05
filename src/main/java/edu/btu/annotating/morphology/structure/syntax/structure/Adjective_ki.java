package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.*;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Adjective_ki extends Inflected implements Adjective {

    public Adjective_ki(Function base, Function suffix) {
        super(base, suffix);
    }

    public static Function make(Function base, Function suffix) {
        if (!((Structure) base).isInflectionLocked()) {
            String suff = ((Suffix) suffix).getMorpheme().getTag().toString();
            String lex = ((Suffix) suffix).getMorpheme().getLexical();
            if ((base instanceof Noun || base instanceof Pronoun || base instanceof InflectedNoun || base instanceof Adjective) && suff.equals("Adj+SC") && lex.equals("ki")) {
                return (Adjective) new Adjective_ki(base, suffix);
            }
        }
        return null;
    }

    @Override
    public Function duplicate() {
        return new Adjective_ki(super.getBase().duplicate(), super.getSuffix().duplicate());
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[Adjective_ki " + super.getBase() + " " + super.getSuffix() + "]";
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
