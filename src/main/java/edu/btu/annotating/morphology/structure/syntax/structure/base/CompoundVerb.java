package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.lang.LexicalEntry;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.Verb;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class CompoundVerb extends Base implements Verb {

    private MorphoAnalysis member;
    private LexicalEntry entry;

    public CompoundVerb(LexicalEntry entry, MorphoAnalysis member, Object head) {
        super(head);
        this.entry = entry;
        this.member =  member;
    }

    public static Function make(LexicalEntry entry, MorphoAnalysis member, Object head) {
        return new CompoundVerb(entry, member, head);
    }

    public String getSubcat() {
        //return ((Stem) super.getHead()).getSubcat();
        return entry.getSubcat();
    }

    public MorphoAnalysis getMember() {
        return member;
    }

    public void setMember(MorphoAnalysis member) {
        this.member = member;
    }

    public LexicalEntry getEntry() {
        return entry;
    }

    public void setEntry(LexicalEntry entry) {
        this.entry = entry;
    }

    @Override
    public boolean checkSubcat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Function duplicate() {
        return new CompoundVerb(getEntry(), getMember(), super.getHead());
    }

    @Override
    public Structure getStructure() {
        return this;
    }

    @Override
    public Structure getDelegate() {
        return ((Function)super.getHead()).getDelegate();
    }

    @Override
    public boolean checkMandatory() {
        return true;
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[CompoundVerb " + member.getMorphemesLexical() + " " + ((Stem) super.getHead()).getLexical() + "]";
        } else if (Settings.printCode == 1) {
            return member.getMorphemesLexical() + " " + ((Stem) super.getHead()).getLexical();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return member.getSurface() + " " + ((Stem) super.getHead()).getSurface();
        } else {
            return "";
        }
    }
}
