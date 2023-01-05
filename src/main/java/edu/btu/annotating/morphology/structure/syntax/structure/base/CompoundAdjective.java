package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.structure.syntax.structure.Structure;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;
import edu.btu.annotating.morphology.structure.lang.LexicalEntry;
import edu.btu.annotating.morphology.structure.syntax.function.Adjective;
import edu.btu.annotating.morphology.structure.syntax.function.Function;

import java.util.ArrayList;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class CompoundAdjective extends Base implements Adjective {
    private ArrayList<MorphoAnalysis> member;
    private LexicalEntry entry;

    public CompoundAdjective(LexicalEntry entry, ArrayList<MorphoAnalysis> member, Object head) {
        super(head);
        this.entry = entry;
        this.member =  member;
    }

    @Override
    public Function duplicate() {
        return null;
    }

    @Override
    public Structure getStructure() {
        return null;
    }

    @Override
    public Structure getDelegate() {
        return null;
    }

    @Override
    public boolean checkMandatory() {
        return false;
    }
}
