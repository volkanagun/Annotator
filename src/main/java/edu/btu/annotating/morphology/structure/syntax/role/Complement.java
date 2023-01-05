package edu.btu.annotating.morphology.structure.syntax.role;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.Noun;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.CasedNoun;
import edu.btu.annotating.morphology.structure.syntax.structure.PossessiveNoun;
import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.structure.syntax.structure.Suffix;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Complement extends Role {

    private Suffix suffix;

    private Complement(Function content, int serialNumber, Suffix suffix) {
        super(content, serialNumber);
        this.suffix = suffix;
    }

    public static Role make(Function content, int serialNumber) {
        if (content.getStructure() instanceof Noun || content.getStructure() instanceof PossessiveNoun) {
            return new Complement(content, serialNumber, null);
        } else if (content.getStructure() instanceof CasedNoun) {
            return new Complement(content, serialNumber, (Suffix) ((CasedNoun) content.getStructure()).getSuffix());
        }

        return null;
    }

    @Override
    public String toString() {
        if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getContent().toString();
        } else {
            return "[Complement " + super.getContent() + "]";
        }
    }

    @Override
    public Function duplicate() {
        if (suffix == null) {
            return new Complement(super.getContent().duplicate(), super.getSerialNumber(), null);
        } else {
            return new Complement(super.getContent().duplicate(), super.getSerialNumber(), (Suffix) suffix.duplicate());
        }
    }

    @Override
    public String getString() {
        return super.getContent().toString();
    }

    @Override
    public Structure getStructure() {
        return null;
    }
}
