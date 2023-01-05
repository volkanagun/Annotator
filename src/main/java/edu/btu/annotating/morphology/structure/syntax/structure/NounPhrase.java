package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.algo.Morpheme;
import edu.btu.annotating.morphology.algo.Stem;
import edu.btu.annotating.morphology.structure.syntax.function.*;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.base.Base;
import edu.btu.annotating.morphology.structure.syntax.structure.base.BaseAdjective;
import edu.btu.annotating.morphology.structure.syntax.structure.base.BaseNoun;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class NounPhrase extends Phrase implements Noun, Noun2 {

    public NounPhrase(Function comp, Object head) {
        super(comp, head);
    }

    public static Function make(Function comp, Function head) {
         if (head instanceof PossessiveNoun && (comp.getStructure() instanceof Adjective)) {
            // sifat tamlamasi fakat bas PossessiveNoun
            if (((PossessiveNoun)head).getBase() instanceof Infinitive && ((Clause)((Infinitive)((PossessiveNoun)head).getBase()).getBase()).getSerialNumber() > 0) {
                 return null;
            }
            if (((PossessiveNoun)head).getBase() instanceof Participle && ((Clause)((Participle)((PossessiveNoun)head).getBase()).getBase()).getSerialNumber() > 0) {
                 return null;
            }
            if (comp instanceof Structure && ((Structure)comp).isIncomplete()) {
                return null;
            }
            if (comp instanceof ChainPhrase) {
                return null;
            }
            ////////System.out.println("***NounPhrase RuleSet1***");
            ///return new NounPhrase(comp, head);
        } else if (head instanceof Noun && (comp.getStructure() instanceof Adjective
                || (comp instanceof Phrase && ((Phrase)comp).doesItContainParticiple())
                || (comp instanceof Inflected && ((Inflected)comp).doesItContainParticiple()))) {
            // sifat tamlamasi
            if (head instanceof Infinitive) {
                return null;
            }
            if (head instanceof NounPhrase) {
                NounPhrase np = (NounPhrase) head;
                if (/*np.getHead() instanceof PossessiveNoun &&*/ np.getComp() instanceof GenitiveNoun) {
                    return null;
                }
            }
            if (head instanceof BaseAdjective/* && ((Stem)((BaseAdjective)head).getHead()).getState().equals("adj2")*/) {
                return null;
            }
            // exception
            if (head instanceof BaseNoun && ((BaseNoun)head).getBaseLexical().equals("bir")) {
                return null;
            }
            // end-exception
            if (comp instanceof Structure && ((Structure)comp).isIncomplete()) {
                return null;
            }
            if (comp instanceof ChainPhrase) {
                return null;
            }
             ////////System.out.println("***NounPhrase RuleSet2***");
            return new NounPhrase(comp, head);
        } else if (head.getStructure() instanceof PossessiveNoun && comp instanceof GenitiveNoun) {
            // belirtili ad tamlamasi
            Suffix s = (Suffix) ((PossessiveNoun) head.getStructure()).getSuffix();
            Morpheme m = s.getMorpheme();
            String ss = m.getLexical() + "~" + m.getTag();
            if (((GenitiveNoun) comp).getBase() instanceof Base) {
                Base b = (Base) ((GenitiveNoun) comp).getBase();
                Stem st = (Stem) b.getHead();
                String bb = st.getLexical() + "~" + st.getPos().getMajor();
                if (ss.equals("Un~POS2S") && !bb.equals("sen~Pro")) {
                    return null;
                }
                if (bb.equals("sen~Pro") && !ss.equals("Un~POS2S")) {
                    return null;
                }
                if (ss.equals("Um~POS1S") && !bb.equals("ben~Pro")) {
                    return null;
                }
                if (bb.equals("ben~Pro") && !ss.equals("Um~POS1S")) {
                    return null;
                }
                if (ss.equals("UmHz~POS1P") && !bb.equals("biz~Pro")) {
                    return null;
                }
                if (bb.equals("biz~Pro") && !ss.equals("UmHz~POS1P")) {
                    return null;
                }
                if (ss.equals("UnHz~POS2P") && !bb.equals("siz~Pro")) {
                    return null;
                }
                if (bb.equals("siz~Pro") && !ss.equals("UnHz~POS2P")) {
                    return null;
                }
            } else {
                if (ss.equals("Um~POS1S") || ss.equals("Un~POS2S") || ss.equals("UmHz~POS1P") || ss.equals("UnHz~POS2P") || ss.equals("lArI~POS3P")) {
                    return null;
                }
            }
             ////////System.out.println("***NounPhrase RuleSet3***");
            return new NounPhrase(comp, head);
        } else if (head instanceof PossessiveNoun && comp instanceof Noun) {
            // belirtisiz ad tamlamasi
            if (comp instanceof Adjective) {
                return null;
            }
            if (((PossessiveNoun)head).getBase() instanceof Infinitive && ((Clause)((Infinitive)((PossessiveNoun)head).getBase()).getBase()).getSerialNumber() > 0) {
                return null;
            }
            if (((PossessiveNoun)head).getBase() instanceof Participle /*&& ((Clause)((Participle)((PossessiveNoun)head).getBase()).getBase()).getSerialNumber() > 0*/) {
                return null;
            }
            if (comp instanceof NounPhrase && ((NounPhrase)comp).getHead() instanceof PossessiveNoun &&
                    !((PossessiveNoun)((NounPhrase)comp).getHead()).getSuffix().toString().equals("[Suffix SH~POS3S]") && !((PossessiveNoun)((NounPhrase)comp).getHead()).getSuffix().toString().equals("[Suffix lArI~POS3P]")) {
                return null;
            }
            PossessiveNoun pn = (PossessiveNoun) head;
            String tag = ((Suffix) pn.getSuffix()).getMorpheme().getTag().toString();
            /*if (!(tag.equals("POS3S") || tag.equals("POS3S"))) {
             return null;
             }*/
             ////////System.out.println("***NounPhrase RuleSet4***");
            return new NounPhrase(comp, head);
        } else if (head instanceof Noun && comp instanceof GenitiveNoun && ((GenitiveNoun) comp).getBase() instanceof Pronoun) {
            // GenitiveNoun yapisinin base'i Pronoun ise sagdaki yapi PossessiveNoun olmak zorunda degil: "bizim ev"
            // Fakat bu yapi ucuncu kisi Pronoun'lar icin uygun degil: onun kitap
            // Head Infinitive olamaz, istisnasi YHs eki. Ancak Infinitive'in yalnizca eylemden olusmasi gerekiyor.
            if (head instanceof Infinitive && ((Clause) ((Infinitive) head).getBase()).getSerialNumber() > 0) {
                return null;
            }
            if (head instanceof NounPhrase && ((NounPhrase)head).getComp() instanceof GenitiveNoun) {
                return null;
            }
            if (!((GenitiveNoun) comp).getBase().toString().equals("[BasePronoun ben]") && !((GenitiveNoun) comp).getBase().toString().equals("[BasePronoun sen]") && !((GenitiveNoun) comp).getBase().toString().equals("[BasePronoun biz]") && !((GenitiveNoun) comp).getBase().toString().equals("[BasePronoun siz]")) {
                return null;
            }
            NounPhrase np = new NounPhrase(comp, head);
            np.setPossessiveLocked(true);
             ////////System.out.println("***NounPhrase RuleSet5***");
            return np;
        } else if (head instanceof NounPhrase && !(((NounPhrase) head).getComp() instanceof GenitiveNoun) && ((NounPhrase) head).getHead() instanceof PossessiveNoun && comp instanceof GenitiveNoun) {
            // ic ice ad tamlamasi: gazetecinin calisma odasi
            if (((GenitiveNoun)comp).getBase() instanceof Infinitive || (((GenitiveNoun)comp).getBase() instanceof PossessiveNoun && ((PossessiveNoun)((GenitiveNoun)comp).getBase()).getBase() instanceof Infinitive)) {
                return null;
            }
             if (((GenitiveNoun)comp).getBase() instanceof Participle || (((GenitiveNoun)comp).getBase() instanceof PossessiveNoun && ((PossessiveNoun)((GenitiveNoun)comp).getBase()).getBase() instanceof Participle)) {
                 return null;
             }
            String sf = ((Suffix)((PossessiveNoun)((NounPhrase) head).getHead()).getSuffix()).getMorpheme().getTag().toString();
            if (!sf.equals("POS3S") && !sf.equals("POS3P")) {
                return null;
            }
             ////////System.out.println("***NounPhrase RuleSet6***");
            return new NounPhrase(comp, head);
        }

        return null;
    }

    @Override
    public Function duplicate() {
        return new NounPhrase(super.getComp(), super.getHead());
    }

    @Override
    public String toString() {
        if (Settings.printCode == 0) {
            return "[NounPhrase " + super.getComp() + " " + super.getHead() + "]";
        } else if (Settings.printCode == 1) {
            return super.getComp() + " " + super.getHead();
        } else if (Settings.printCode == 2 || Settings.printCode == 3 || Settings.printCode == 4) {
            return super.getComp().toString() + " " + super.getHead().toString();
        } else {
            return "";
        }
    }

    @Override
    public Structure getStructure() {
        return this;
    }

    @Override
    public boolean doesItContainParticiple() {
        if (super.getHead() instanceof PossessiveNoun && ((PossessiveNoun)super.getHead()).getBase() instanceof Participle) {
            return true;
        }
        return false;
    }

    @Override
    public Structure getDelegate() {
        return ((Function)super.getHead()).getDelegate();
    }

    @Override
    public boolean checkMandatory() {
        return getComp().checkMandatory() && ((Function) getHead()).checkMandatory();
    }
}
