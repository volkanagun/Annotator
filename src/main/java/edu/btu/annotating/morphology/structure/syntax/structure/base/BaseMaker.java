package edu.btu.annotating.morphology.structure.syntax.structure.base;

import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.Function;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class BaseMaker {
    public static Function make(Stem stem) {

        if (stem.getPos().getMajor().equals("Adj")) {
            return new BaseAdjective(stem);
        } else if (stem.getPos().getMajor().equals("Adv")) {
            return new BaseAdverb(stem);
        } else if (stem.getPos().getMajor().equals("Com")) {
            return new BaseCommunication(stem);
        } else if (stem.getPos().getMajor().equals("Cnj")) {
            return new BaseConjunction(stem);
        } else if (stem.getPos().getMajor().equals("Int")) {
            return new BaseInterjection(stem);
        } else if (stem.getPos().getMajor().equals("N")) {
            return new BaseNoun(stem);
        } else if (stem.getPos().getMajor().equals("Num")) {
            return new BaseNumber_(stem);
        } else if (stem.getPos().getMajor().equals("Pp")) {
            return new BasePostposition(stem);
        } else if (stem.getPos().getMajor().equals("Pro")) {
            return new BasePronoun(stem);
        } else if (stem.getPos().getMajor().equals("Sym")) {
            return new BaseSymbol(stem);
        } else if (stem.getPos().getMajor().equals("V")) {
            return new BaseVerb(stem);
        } else if (stem.getPos().getMajor().equals("Is")) { // new
            return new BaseSymbol(stem);
        } else if (stem.getPos().getMajor().equals("Eos")) { // new
            return new BaseSymbol(stem);
        } else if (stem.getPos().getMajor().equals("IP")) { // new
            return new BaseQParticle(stem);
        } else if (stem.getPos().getMajor().equals("Abb")) { // new
            return new BaseSymbol(stem);
        }

        return null;
    }
}
