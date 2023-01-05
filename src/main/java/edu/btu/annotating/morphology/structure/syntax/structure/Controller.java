package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.Noun2;
import edu.btu.annotating.morphology.structure.syntax.structure.base.*;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Controller {
    public static boolean checkIsomorphism(Function f1, Function f2) {
        if (f1.getStructure() instanceof Adjective_ki && f2.getStructure() instanceof Adjective_ki) {
            return true;
        }
        if (f1.getStructure() instanceof CasedNoun && f2.getStructure() instanceof CasedNoun) {
            CasedNoun cn1 = (CasedNoun) f1.getStructure();
            CasedNoun cn2 = (CasedNoun) f2.getStructure();
            String suff1 = ((Suffix) cn1.getSuffix()).getMorpheme().getTag().toString();
            String suff2 = ((Suffix) cn2.getSuffix()).getMorpheme().getTag().toString();
            if (suff1.equals(suff2)) {
                return true;
            }
        }
        if (f1.getStructure() instanceof GenitiveNoun && f2.getStructure() instanceof GenitiveNoun) {
            return true;
        }
        if (f1.getStructure() instanceof Gerundium && f2.getStructure() instanceof Gerundium) {
            return true;
        }
        if (f1.getStructure() instanceof Infinitive && f2.getStructure() instanceof Infinitive) {
            return true;
        }
        if (f1.getStructure() instanceof NounPhrase && f2.getStructure() instanceof NounPhrase) {
            return true;
        }
        if (f1.getStructure() instanceof Participle && f2.getStructure() instanceof Participle) {
            return true;
        }
        if (f1.getStructure() instanceof PluralNoun && f2.getStructure() instanceof PluralNoun) {
            return true;
        }
        if (f1.getStructure() instanceof PossessiveNoun && f2.getStructure() instanceof PossessiveNoun) {
            return true;
        }
        if (f1.getStructure() instanceof PostpositionalPhrase && f2.getStructure() instanceof PostpositionalPhrase) {
            return true;
        }
        ///
        if (f1.getStructure() instanceof BaseNoun && f2.getStructure() instanceof BaseNoun) {
            return true;
        }
        if (f1.getStructure() instanceof BaseAdjective && f2.getStructure() instanceof BaseAdjective) {
            return true;
        }
        if (f1.getStructure() instanceof BaseAdverb && f2.getStructure() instanceof BaseAdverb) {
            return true;
        }
        if (f1.getStructure() instanceof BaseCommunication && f2.getStructure() instanceof BaseCommunication) {
            return true;
        }
        if (f1.getStructure() instanceof BaseInterjection && f2.getStructure() instanceof BaseInterjection) {
            return true;
        }
        if (f1.getStructure() instanceof BaseNumber_ && f2.getStructure() instanceof BaseNumber_) {
            return true;
        }
        if (f1.getStructure() instanceof BasePronoun && f2.getStructure() instanceof BasePronoun) {
            return true;
        }
        if (f1.getStructure() instanceof BaseSymbol && f2.getStructure() instanceof BaseSymbol) {
            return true;
        }
        /////
        if (f1.getStructure() instanceof Clause && f2.getStructure() instanceof Clause) {
            return true;
        }
        ///
        if (f1.getStructure() instanceof Noun2 && f2.getStructure() instanceof PossessiveNoun) {
            return true;
        }
        /*if (f1.getStructure() instanceof PossessiveNoun && f2.getStructure() instanceof Noun2) {
            return true;
        }*/
        if (f1.getStructure() instanceof Noun2 && f2.getStructure() instanceof Noun2) {
            return true;
        }
        return false;
    }

    public static boolean checkIsomorphism2(Function f1, Function f2) {
        if (f1.getStructure() instanceof Adjective_ki && f2.getStructure() instanceof Adjective_ki) {
            return true;
        }
        if (f1.getStructure() instanceof CasedNoun && f2.getStructure() instanceof CasedNoun) {
            CasedNoun cn1 = (CasedNoun) f1.getStructure();
            CasedNoun cn2 = (CasedNoun) f2.getStructure();
            String suff1 = ((Suffix) cn1.getSuffix()).getMorpheme().getTag().toString();
            String suff2 = ((Suffix) cn2.getSuffix()).getMorpheme().getTag().toString();
            if (suff1.equals(suff2)) {
                return true;
            }
        }
        if (f1.getStructure() instanceof GenitiveNoun && f2.getStructure() instanceof GenitiveNoun) {
            return true;
        }
        if (f1.getStructure() instanceof Gerundium && f2.getStructure() instanceof Gerundium) {
            return true;
        }
        if (f1.getStructure() instanceof Infinitive && f2.getStructure() instanceof Infinitive) {
            return true;
        }
        if (f1.getStructure() instanceof NounPhrase && f2.getStructure() instanceof NounPhrase) {
            return true;
        }
        if (f1.getStructure() instanceof Participle && f2.getStructure() instanceof Participle) {
            return true;
        }
        if (f1.getStructure() instanceof PluralNoun && f2.getStructure() instanceof PluralNoun) {
            return true;
        }
        if (f1.getStructure() instanceof PossessiveNoun && f2.getStructure() instanceof PossessiveNoun) {
            return true;
        }
        if (f1.getStructure() instanceof PostpositionalPhrase && f2.getStructure() instanceof PostpositionalPhrase) {
            return true;
        }
        ///
        if (f1.getStructure() instanceof BaseNoun && f2.getStructure() instanceof BaseNoun) {
            return true;
        }
        if (f1.getStructure() instanceof BaseAdjective && f2.getStructure() instanceof BaseAdjective) {
            return true;
        }
        if (f1.getStructure() instanceof BaseAdverb && f2.getStructure() instanceof BaseAdverb) {
            return true;
        }
        if (f1.getStructure() instanceof BaseCommunication && f2.getStructure() instanceof BaseCommunication) {
            return true;
        }
        if (f1.getStructure() instanceof BaseInterjection && f2.getStructure() instanceof BaseInterjection) {
            return true;
        }
        if (f1.getStructure() instanceof BaseNumber_ && f2.getStructure() instanceof BaseNumber_) {
            return true;
        }
        if (f1.getStructure() instanceof BasePronoun && f2.getStructure() instanceof BasePronoun) {
            return true;
        }
        if (f1.getStructure() instanceof BaseSymbol && f2.getStructure() instanceof BaseSymbol) {
            return true;
        }
        /////
        if (f1.getStructure() instanceof Clause && f2.getStructure() instanceof Clause) {
            return true;
        }
        ///
        if (f1.getStructure() instanceof Noun2 && f2.getStructure() instanceof Noun2) {
            return true;
        }
        return false;
    }
}
