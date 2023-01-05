package edu.btu.annotating.morphology.structure.syntax.structure;

import edu.btu.annotating.morphology.algo.MorphoAnalysis;
import edu.btu.annotating.morphology.structure.syntax.function.*;
import edu.btu.annotating.morphology.structure.syntax.lexical.structure.Subcat;
import edu.btu.annotating.morphology.structure.syntax.role.Adjunct;
import edu.btu.annotating.morphology.structure.syntax.role.Complement;
import edu.btu.annotating.morphology.structure.syntax.role.Role;
import edu.btu.annotating.morphology.structure.syntax.role.Subject;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.base.Base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Clause extends Syntheme implements Verb {

    static int debug = 0;

    public static int staticNum;

    private MorphoAnalysis[] path;
    private int pathNo;
    private int num;
    private Subject subject;
    private ArrayList<Complement> complement;
    private ArrayList<Adjunct> adjunct;
    private Subcat subcat;
    private String cType;
    private boolean agreementSubjectPerson;
    private int serialNumber;
    private String polarity;
    private String compound;
    private String tense;
    private String pastType;
    private String person;
    private String copulaTense;
    private String copulaPastType;
    private String copulaPerson;
    private ArrayList<Suffix> suffixList;
    private boolean isAux;

    private Clause(Object head, MorphoAnalysis[] path, int pathNo) {
        super(head);
        this.path = path;
        this.pathNo = pathNo;
        num = ++staticNum;
        subcat = processSubcat();
        agreementSubjectPerson = true;
        complement = new ArrayList();
        adjunct = new ArrayList();
        cType = "Finite";
        serialNumber = 0;
        polarity = "POS";
        compound = "NULL";
        tense = "IMP";
        pastType = "NULL";
        person = "PER2S";
        copulaTense = "NULL";
        copulaPastType = "NULL";
        copulaPerson = "NULL";
        suffixList = new ArrayList();
        isAux = false;
    }

    private Subcat processSubcat() {
        String s = ((Base) super.getHead()).getSubcat();
        return new Subcat(s);
    }

    public Clause(Object head, MorphoAnalysis[] path, int pathNo, int num, Subject subject, ArrayList<Complement> complement, ArrayList<Adjunct> adjunct, Subcat subcat, String cType, boolean agreementSubjectPerson, int serialNumber, String polarity, String compound, String tense, String pastType, String person, String copulaTense, String copulaPastType, String copulaPerson, ArrayList<Suffix> suffixList, boolean isAux) {
        super(head);
        this.path = path;
        this.pathNo = pathNo;
        this.num = num;
        this.subject = subject;
        this.complement = complement;
        this.adjunct = adjunct;
        this.subcat = subcat;
        this.cType = cType;
        this.agreementSubjectPerson = agreementSubjectPerson;
        this.serialNumber = serialNumber;
        this.polarity = polarity;
        this.compound = compound;
        this.tense = tense;
        this.pastType = pastType;
        this.person = person;
        this.copulaTense = copulaTense;
        this.copulaPastType = copulaPastType;
        this.copulaPerson = copulaPerson;
        this.suffixList = suffixList;
        this.isAux = isAux;
    }

    public MorphoAnalysis[] getPath() {
        return path;
    }

    public int getPathNo() {
        return pathNo;
    }

    public int getNum() {
        return num;
    }

    public Subject getSubject() {
        return subject;
    }

    public ArrayList<Complement> getComplement() {
        return complement;
    }

    public ArrayList<Adjunct> getAdjunct() {
        return adjunct;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public Subcat getSubcat() {
        return subcat;
    }

    public static Function make(Object head, MorphoAnalysis[] path, int pathNo) {
        if (head instanceof Verb) {
            return new Clause(head, path, pathNo);
        }
        return null;
    }

    public void addComplement(Complement c) {
        complement.add(c);
    }

    public void addAdjunct(Adjunct a) {
        adjunct.add(a);
    }

    public void setZero() {
        this.tense = "NULL";
        this.pastType = "NULL";
        this.person = "NULL";
        this.copulaTense = "NULL";
        this.copulaPastType = "NULL";
        this.copulaPerson = "NULL";
    }

    public static ArrayList<Clause> update(Clause vp, Function item) {
        if (item instanceof ConjunctionPhrase) {
            debug = 1;
        }

        if (item instanceof Structure && ((Structure) item).isIncomplete()) {
            return new ArrayList();
        }
        ArrayList<Clause> list = new ArrayList();
        Clause copyVP = (Clause) vp.duplicate(); // forking copy
        if (item.getStructure() instanceof Pronoun) { // item instanceof Pronoun
            if (copyVP.subject == null) {
                Subject s = (Subject) Subject.make(item, ++copyVP.serialNumber);
                copyVP.subject = s;
                checkAgreement(copyVP);
                list.add(copyVP);
                copyVP = (Clause) vp.duplicate(); // refreshing
            }
        } else if (item.getStructure() instanceof Noun || item.getStructure() instanceof PossessiveNoun) {
            if (copyVP.subject == null) {
                Subject s = (Subject) Subject.make(item, ++copyVP.serialNumber);
                if (s != null) {
                    copyVP.subject = s;
                    checkAgreement(copyVP);
                    list.add(copyVP);
                    copyVP = (Clause) vp.duplicate(); // refreshing
                }
            }
            // belirtisiz nesne
            if (!(item.getStructure() instanceof PossessiveNoun) && copyVP.getHead() instanceof Base && Subcat.check(copyVP.getSubcat(), "NOM") && copyVP.serialNumber == 0) {
                Subcat.update(copyVP.getSubcat(), copyVP.getComplement(), "NOM");
                Complement c = (Complement) Complement.make(item, ++copyVP.serialNumber);
                copyVP.addComplement(c);
                checkAgreement(copyVP);
                list.add(copyVP);
                copyVP = (Clause) vp.duplicate(); // refreshing
            }
        } else if (item.getStructure() instanceof CasedNoun) {
            CasedNoun cn = (CasedNoun) item.getStructure();
            String suffTag = ((Suffix) cn.getSuffix()).getMorpheme().getTag().toString();
            if (copyVP.getHead() instanceof Base && Subcat.check(copyVP.getSubcat(), suffTag)) {//////////////////////
                Subcat.update(copyVP.getSubcat(), copyVP.getComplement(), suffTag);
                Complement c = (Complement) Complement.make(item, ++copyVP.serialNumber);
                copyVP.addComplement(c);
                checkAgreement(copyVP);
                list.add(copyVP);
                copyVP = (Clause) vp.duplicate(); // refreshing
            }
            // CasedNoun ayni zamanda adjunct da olabilir
            Adjunct a = (Adjunct) Adjunct.make(item, ++copyVP.serialNumber);
            if (a != null) {
                copyVP.addAdjunct(a);
                checkAgreement(copyVP);
                list.add(copyVP);
                copyVP = (Clause) vp.duplicate(); // refreshing
            }
        } else if (item.getStructure() instanceof Adverb) {
            // kural: Base yapisindaki ya da AdverbPhrase bir Adverb eylemin hemen yaninda olmalidir, aksi halde tumce kuralsiz olur.
            // isini duzgun yapti *duzgun isini yapti | isini cok iyi yapti *cok iyi isini yapti
            if (item instanceof Base || (item instanceof ChainPhrase && ((ChainPhrase) item).getDelegate() instanceof Base) || (item instanceof ConjunctionPhrase && ((ConjunctionPhrase) item).getDelegate() instanceof Base)) {
                //if (copyVP.serialNumber == 0) {
                    Adjunct a = (Adjunct) Adjunct.make(item, ++copyVP.serialNumber);
                    copyVP.addAdjunct(a);
                    checkAgreement(copyVP);
                    list.add(copyVP);
                    copyVP = (Clause) vp.duplicate(); // refreshing
                //}
            } else {
                Adjunct a = (Adjunct) Adjunct.make(item, ++copyVP.serialNumber);
                copyVP.addAdjunct(a);
                checkAgreement(copyVP);
                list.add(copyVP);
                copyVP = (Clause) vp.duplicate(); // refreshing
            }
        } else if (item.getStructure() instanceof Dependent) {
            Suffix suff = (Suffix) item;
            String[] tags = suff.getMorpheme().getTag().getElement();
            String state = suff.getMorpheme().getState();
            final String[] polarityTags = {"NEG"};
            final String[] compoundTags = {"ABIL", "QUICK", "NEAR", "DUR", "INAB"};
            final String[] tenseTags = {"IMP", "COND", "PAST", "FUT", "NEC", "CONTI", "AOR", "OPT"};
            final String[] pastTypeTags = {"OBS", "NARR"};
            final String[] personTags = {"PER1S", "PER2S", "PER3S", "PER1P", "PER2P", "PER3P"};
            final String[] copulaTenseTags = {"PAST", "COND", "AOR"};

            ///****///****///****///****///****///****///****///****///****///****///****///****
//            if (tags[0].equals("PASS")) {
//                Subcat sb = copyVP.subcat;
//                ArrayList<Template> tList = sb.getList();
//                ArrayList<Template> newTList = new ArrayList<>();
//                for (Template template : tList) {
//                    ArrayList<Cell> cList = template.getList();
//                    ArrayList<Cell> newCList = new ArrayList<>();
//                    for (Cell cell : cList) {
//                        if (!cell.getS().equals("NOM")) {
//                            newCList.add(new Cell(cell.getS()));
//                        }
//                    }
//                    if (!newCList.isEmpty()) {
//                        newTList.add(new Template(newCList, new String[]{}));
//                    }
//                }
//                copyVP.subcat.setList(newTList);
//            }
            ///****///****///****///****///****///****///****///****///****///****///****///****

            if (state.equals("par1") || state.equals("par2") || state.equals("inf1") || state.equals("inf2") || state.equals("ger")) {
                copyVP.cType = "Infinite";
            }

            if (!state.equals("par1") && !state.equals("par2") && !state.equals("inf1") && !state.equals("inf2") && !state.equals("ger")) {
                int i;
                if ((i = isAnyExist(tags, polarityTags)) > -1) {
                    copyVP.polarity = polarityTags[i];
                }
                if ((i = isAnyExist(tags, compoundTags)) > -1) {
                    copyVP.compound = compoundTags[i];
                }
                if (/*!tags[0].equals("V'") &&*/ (i = isAnyExist(tags, tenseTags)) > -1) {
                    copyVP.tense = tenseTags[i];
                }
                if ((i = isAnyExist(tags, personTags)) > -1) {
                    copyVP.person = personTags[i];
                }
                if (/*!tags[0].equals("V'") &&*/ (i = isAnyExist(tags, pastTypeTags)) > -1) {
                    copyVP.pastType = pastTypeTags[i];
                }
                if (tags[0].equals("V'") && (i = isAnyExist(tags, copulaTenseTags)) > -1) {
                    copyVP.copulaTense = copulaTenseTags[i];
                }
                if (tags[0].equals("V'") && (i = isAnyExist(tags, pastTypeTags)) > -1) {
                    copyVP.copulaPastType = pastTypeTags[i];
                }
                checkAgreement(copyVP);
                list.add(copyVP);
                copyVP.suffixList.add(suff);
                copyVP = (Clause) vp.duplicate(); // refreshing
            }

        }
        ArrayList<Clause> list2 = new ArrayList();
        for (Clause phrase : list) {
            if (phrase.agreementSubjectPerson) {
                list2.add(phrase);
            }
        }
        return list2;
    }

    private static void checkAgreement(Clause vp) {
        if (vp.isAux) {
            return;
        }
        if (vp.subject == null) {
            return;
        }
        if (vp.cType.equals("Infinite")) {
            return;
        }
        String subj = "";
        if (vp.subject.getContent() instanceof Transparent) {
            subj = vp.subject.getContent().getStructure().toString();
        } else {
            subj = vp.subject.getString();
        }

        if (!subj.equals("[BasePronoun ben]") && vp.person.equals("PER1S")) {
            vp.agreementSubjectPerson = false;
        } else if (!subj.equals("[BasePronoun sen]") && vp.person.equals("PER2S")) {
            vp.agreementSubjectPerson = false;
        } else if (!subj.equals("[BasePronoun biz]") && vp.person.equals("PER1P")) {
            vp.agreementSubjectPerson = false;
        } else if (!subj.equals("[BasePronoun siz]") && vp.person.equals("PER2P")) {
            vp.agreementSubjectPerson = false;
        } else if ((vp.person.equals("PER3S") || vp.person.equals("PER3P")) && (subj.equals("[BasePronoun ben]") || subj.equals("[BasePronoun sen]") || subj.equals("[BasePronoun biz]") || subj.equals("[BasePronoun siz]"))) {
            vp.agreementSubjectPerson = false;
        }
    }

    private static int isAnyExist(String[] arr1, String[] arr2) {
        int i = 0;
        for (String a : arr1) {
            int j = 0;
            for (String b : arr2) {
                if (a.equals(b)) {
                    return j;
                }
                j++;
            }
            i++;
        }
        return -1;
    }



    @Override
    public boolean checkSubcat() {
        //--
        if (subcat.isMandatory()) {
            return false;
        }
        //--
        if (subcat.getActiveTemplateLength() == complement.size()) {
            return true;
        }
        return false;
    }

    @Override
    public Function duplicate() {
        Subject copySubject = null;
        ArrayList<Complement> copyComplement = null;
        ArrayList<Adjunct> copyAdjunct = null;
        Subcat copySubcat = null;

        if (subject != null) {
            copySubject = (Subject) subject.duplicate();
        }
        if (complement != null) {
            copyComplement = new ArrayList();
            for (Complement c : complement) {
                if (c == null) {
                    copyComplement.add(null);
                } else {
                    copyComplement.add((Complement) c.duplicate());
                }
            }
        }
        if (adjunct != null) {
            copyAdjunct = new ArrayList();
            for (Adjunct a : adjunct) {
                if (a == null) {
                    copyAdjunct.add(null);
                } else {
                    copyAdjunct.add((Adjunct) a.duplicate());
                }
            }
        }
        if (subcat != null) {
            copySubcat = (Subcat) subcat.duplicate();
        }

        return new Clause(super.getHead(), this.path, this.pathNo, this.num, copySubject, copyComplement, copyAdjunct, copySubcat, cType, this.agreementSubjectPerson, this.serialNumber, this.polarity, this.compound, this.tense, this.pastType, this.person, this.copulaTense, this.copulaPastType, this.copulaPerson, this.suffixList, this.isAux);
    }

    @Override
    public String toString() {
        String sSubject, sComplement, sAdjunct;
        if (subject == null) {
            sSubject = "[Subject null]";
        } else {
            sSubject = subject.toString();
        }
        if (complement.isEmpty()) {
            sComplement = "[Complement null]";
        } else if (complement.size() == 1) {
            sComplement = complement.get(0).toString();
        } else {
            sComplement = complement.toString();
        }
        if (adjunct.isEmpty()) {
            sAdjunct = "[Adjunct null]";
        } else if (adjunct.size() == 1) {
            sAdjunct = adjunct.get(0).toString();
        } else {
            sAdjunct = adjunct.toString();
        }

        if (Settings.printCode == 0) {
            return "[Clause " + sSubject + ", " + sComplement + ", " + sAdjunct + ", " + "[Verb " + super.getHead() + "] " + this.polarity + " " + this.compound + " " + this.tense + " " + this.person + "]";
        } else if (Settings.printCode == 1) {
            if (subject == null) {
                sSubject = "";
            } else {
                sSubject += " ";
            }
            if (complement.isEmpty()) {
                sComplement = "";
            } else {
                sComplement += " ";
            }
            if (adjunct.isEmpty()) {
                sAdjunct = "";
            } else {
                sAdjunct += " ";
            }
            return sSubject + sComplement + sAdjunct + super.getHead();
        } else if (Settings.printCode == 2) {
            String separ = " | ", sub = "Subject:", comp = "Complement:", ad = "Adjunct:", v = "Verb:";
            if (this.tense.equals("NULL")) {
                separ = " ";
                sub = "";
                comp = "";
                ad = "";
                v = "";
            }
            ArrayList<Role> roleList = new ArrayList();

            if (subject != null) {
                roleList.add(subject);
            }
            if (!complement.isEmpty()) {
                for (Complement c : complement) {
                    roleList.add(c);
                }
            }
            if (!adjunct.isEmpty()) {
                for (Adjunct a : adjunct) {
                    roleList.add(a);
                }
            }

            Collections.sort(roleList, new Comparator<Role>() {
                @Override
                public int compare(Role role1, Role role2) {
                    if (role1.getSerialNumber() > role2.getSerialNumber()) {
                        return -1;
                    } else if (role1.getSerialNumber() < role2.getSerialNumber()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            String result = "";
            for (Role r : roleList) {
                if (r instanceof Subject) {
                    result += sub + r + separ;
                } else if (r instanceof Complement) {
                    result += comp + r + separ;
                } else if (r instanceof Adjunct) {
                    result += ad + r + separ;
                }
            }

            String suffixes = "";
            for (Suffix suff : this.suffixList) {
                suffixes += suff.getMorpheme().getSurface();
            }

            return result + v + super.getHead().toString() + suffixes;
        }  else if (Settings.printCode == 3) {
            String separ = " ", sub = "(S)", comp = "(C)", ad = "(A)", v = "(V)";
            ArrayList<Role> roleList = new ArrayList();

            if (!this.cType.equals("Finite")) {
                separ = " ";
                sub = "";
                comp = "";
                ad = "";
                v = "";
            }

            if (subject != null) {
                roleList.add(subject);
            }
            if (!complement.isEmpty()) {
                for (Complement c : complement) {
                    roleList.add(c);
                }
            }
            if (!adjunct.isEmpty()) {
                for (Adjunct a : adjunct) {
                    roleList.add(a);
                }
            }

            Collections.sort(roleList, new Comparator<Role>() {
                @Override
                public int compare(Role role1, Role role2) {
                    if (role1.getSerialNumber() > role2.getSerialNumber()) {
                        return -1;
                    } else if (role1.getSerialNumber() < role2.getSerialNumber()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            String result = "";
            for (Role r : roleList) {
                if (r instanceof Subject) {
                    result += sub + r + separ;
                } else if (r instanceof Complement) {
                    result += comp + r + separ;
                } else if (r instanceof Adjunct) {
                    result += ad + r + separ;
                }
            }

            String suffixes = "";
            for (Suffix suff : this.suffixList) {
                suffixes += suff.getMorpheme().getSurface();
            }

            return result + v + super.getHead().toString() + suffixes;
        } else if (Settings.printCode == 4) {
            String separ = "|", sub = "", comp = "", ad = "", v = "";
            if (this.tense.equals("NULL")) {
                separ = " ";
                sub = "";
                comp = "";
                ad = "";
                v = "";
            }
            ArrayList<Role> roleList = new ArrayList();

            if (subject != null) {
                roleList.add(subject);
            }
            if (!complement.isEmpty()) {
                for (Complement c : complement) {
                    roleList.add(c);
                }
            }
            if (!adjunct.isEmpty()) {
                for (Adjunct a : adjunct) {
                    roleList.add(a);
                }
            }

            Collections.sort(roleList, new Comparator<Role>() {
                @Override
                public int compare(Role role1, Role role2) {
                    if (role1.getSerialNumber() > role2.getSerialNumber()) {
                        return -1;
                    } else if (role1.getSerialNumber() < role2.getSerialNumber()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            String result = "";
            for (Role r : roleList) {
                if (r instanceof Subject) {
                    result += sub + r + separ;
                } else if (r instanceof Complement) {
                    result += comp + r + separ;
                } else if (r instanceof Adjunct) {
                    result += ad + r + separ;
                }
            }

            String suffixes = "";
            for (Suffix suff : this.suffixList) {
                suffixes += suff.getMorpheme().getSurface();
            }

            return result + v + super.getHead().toString() + suffixes;
        } else {
            return "";
        }
    }

    /*@Override
     public String getSubcat() {
     return ((Base) super.getHead()).getSubcat();
     }*/
    @Override
    public Structure getStructure() {
        return this;
    }

    @Override
    public Structure getDelegate() {
        return ((Function) super.getHead()).getDelegate();
    }

    @Override
    public boolean checkMandatory() {
        if (subject != null && !subject.getContent().checkMandatory()) {
            return false;
        }
        if (complement != null) {
            for (Complement comp : complement) {
                if (!comp.getContent().checkMandatory()) {
                    return false;
                }
            }
        }
        if (adjunct != null) {
            for (Adjunct ad : adjunct) {
                if (!ad.getContent().checkMandatory()) {
                    return false;
                }
            }
        }
        if (!((Function) getHead()).checkMandatory()) {
            return false;
        }
        if (subcat.isMandatory() && !subcat.isOk()) {
            return false;
        }
        return true;
    }

    public String getPolarity() {
        return polarity;
    }

    public String getCompound() {
        return compound;
    }

    public String getTense() {
        return tense;
    }

    public String getPastType() {
        return pastType;
    }

    public String getPerson() {
        return person;
    }

    public String getCopulaTense() {
        return copulaTense;
    }

    public String getCopulaPastType() {
        return copulaPastType;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public void setAux(boolean aux) {
        isAux = aux;
    }
}
