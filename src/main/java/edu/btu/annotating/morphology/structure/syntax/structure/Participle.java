package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Adjective;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Participle extends Inflected implements Adjective {

    public Participle(Function base, Function suffix) {
        super(base, suffix);
    }

    public static Function make(Function base, Function suffix) {
        if (!((Structure) base).isInflectionLocked()) {
            String sufState = ((Suffix) suffix).getMorpheme().getState();
            if (base instanceof Clause && (sufState.equals("par1") || sufState.equals("par2"))) {
                Clause vp = (Clause) base;
                vp.setZero();
                //vp.setcType("Infinite");
                if (vp.getSubject() != null) {
                    return null;
                }

                return new Participle(base, suffix);
            }
        }
        return null;
    }

    @Override
    public Function duplicate() {
        return new Participle(super.getBase().duplicate(), super.getSuffix().duplicate());
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[Participle " + super.getBase() + " " + super.getSuffix() + "]";
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
