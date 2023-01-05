package edu.btu.annotating.morphology.structure.syntax.role;

import edu.btu.annotating.morphology.structure.syntax.function.Adverb;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.CasedNoun;
import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.structure.syntax.structure.Suffix;
import edu.btu.annotating.morphology.algo.MorphoTag;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Adjunct extends Role {

    private Adjunct(Function content, int serialNumber) {
        super(content, serialNumber);
    }

    public static Role make(Function content, int serialNumber) {
        if (content.getStructure() instanceof Adverb) {
            if (content.getStructure() instanceof CasedNoun) {
                CasedNoun cn = (CasedNoun) content.getStructure();
                MorphoTag t = ((Suffix) cn.getSuffix()).getMorpheme().getTag();
                if (!t.toString().equals("ILE") && !t.toString().equals("INS") && !t.toString().equals("LOC") && !t.toString().equals("ABL")) {
                    return null;
                }
            }
            return new Adjunct(content, serialNumber);
        }

        return null;
    }

    @Override
    public String toString() {
        if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getContent().toString();
        } else {
            return "[Adjunct " + super.getContent() + "]";
        }
    }

    @Override
    public Function duplicate() {
        return new Adjunct(super.getContent().duplicate(), super.getSerialNumber());
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
