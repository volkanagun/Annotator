package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.algo.Morpheme;
import edu.btu.annotating.morphology.structure.syntax.function.Dependent;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Suffix extends Structure implements Dependent {
    private Morpheme morpheme;

    private Suffix(Morpheme morpheme) {
        this.morpheme = morpheme;
    }

    public static Function make(Morpheme morpheme) {
        return new Suffix(morpheme);
    }

    public Morpheme getMorpheme() {
        return morpheme;
    }

    public void setMorpheme(Morpheme morpheme) {
        this.morpheme = morpheme;
    }

    public String getLexical() {
        return morpheme.getLexical();
    }

    public String getTagString() {
        return morpheme.getTag().toString();
    }

    @Override
    public Function duplicate() {
        return new Suffix(morpheme);
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[Suffix " + morpheme.getLexical() + "~" + morpheme.getTag().toString() + "]";
        } else if (Settings.printCode == 1) {
            return morpheme.getLexical() + "~" + morpheme.getTag().toString();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return morpheme.getSurface();
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
        return this;
    }

    @Override
    public boolean checkMandatory() {
        return true;
    }
}
