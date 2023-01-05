/*
 * Copyright (C) 2021 Ozkan Aslan euzkan@gmail.com.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package edu.btu.annotating.morphology.algo;

import edu.btu.annotating.morphology.structure.POS;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class MorphoAnalysis implements Comparable {
    private Morpheme[] morpheme;
    private Stem stem;
    private Leaf leaf;
    private String totalTag;
    private POS pos;
    private Channel channel;
    private int sortMode;
    private int probCat;
    private final int numMorpheme; // number of morpheme
    private int stemPoint;

    public MorphoAnalysis(Morpheme[] morpheme, int sortMode, boolean imitation) {
        this.morpheme = preProcess(morpheme);
        numMorpheme = morpheme.length;
        this.sortMode = sortMode;
        if (!imitation) {
            this.stem = detectStem();
        }
        this.leaf = detectLeaf();
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getTag() + "+";
        }
        temp = temp.substring(0, temp.length() - 1);
        this.totalTag = temp;
        this.pos = detectPOS();
        this.channel = processChannel(new Channel());
        arrangeSubcat();
        this.probCat = calculateProbCat();
    }

    public String getSurface() {
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getSurface();
        }
        return temp;
    }

    public Morpheme[] preProcess(Morpheme[] m) {
        // mAK ekinin i ve e hallerinin mayi meyi olmasi sorununu boyle cozuyoruz.
        for (int i = 0; i < m.length; i++) {
            if (i < m.length - 1 && m[i].getLexical().equals("mA") && (m[i + 1].getLexical().equals("NH") || m[i + 1].getLexical().equals("NA"))) {
                m[i].setLexical("mAK");
                if (m[i].getSurface().endsWith("a")) {
                    m[i].setSurface("may");
                } else if (m[i].getSurface().endsWith("e")) {
                    m[i].setSurface("mey");
                }
                m[i + 1].setSurface(m[i + 1].getSurface().substring(1));
            }
        }
        return m;
    }

    public Morpheme[] getMorpheme() {
        return morpheme;
    }

    public Stem getStem() {
        return stem;
    }

    public String getTotalTag() {
        return totalTag;
    }

    public POS getPos() {
        return pos;
    }

    public Channel getChannel() {
        return channel;
    }

    public int getSortMode() {
        return sortMode;
    }

    public int getNumMorpheme() {
        return numMorpheme;
    }

    public Leaf getLeaf() {
        return leaf;
    }

    public int getProbCat() {
        return probCat;
    }

    private int calculateProbCat() {
        Morpheme last = morpheme[morpheme.length - 1];

        for (int i = 0; i < morpheme.length - 1; i++) {
            Morpheme m1 = morpheme[i];
            Morpheme m2 = morpheme[i + 1];
            String stt1 = m1.getState();
            String stt2 = m2.getState();
            String lex1 = m1.getLexical();
            String lex2 = m2.getLexical();

            if (stt2.equals("case1") || stt2.equals("case2")) {
                if (stt1.equals("par1")) {
                    if (lex1.equals("YAcAK") || lex1.equals("YAn") || lex1.equals("YAsI") || lex1.equals("mHş")) {
                        return 1;
                    } else if (lex1.equals("DHK")) {
                        return 2;
                    }
                } else if (stt1.equals("par2")) {
                    if (lex1.equals("z")) {
                        return 1;
                    }
                }
            } else if (stt2.equals("aux4") || stt2.equals("aux5") || stt2.equals("aux6") || stt2.equals("aux7") || stt2.equals("aux8") || stt2.equals("per12") || stt2.equals("advs")) {
                if (m1.getState().equals("par1")) {
                    if (lex1.equals("YAcAK") || lex1.equals("DHK") || lex1.equals("mHş")) {
                        return 2;
                    }
                } else if (stt1.equals("par2")) {
                    if (lex1.equals("z")) {
                        return 2;
                    }
                } else if (stt1.equals("case1")) {
                    if (lex1.equals("NH") || lex1.equals("NA")) {
                        return 1;
                    }
                }
            }

            // ek ardisikligi denetimi
            if (lex1.equals("lE") && lex2.equals("Xr")) {
                return 2;
            }
            // end-ek ardisikligi denetimi
        }

        // sonda yer alma denetimi
        if (last.getState().equals("par1")) {
            if (last.getLexical().equals("DHK")) {
                return 2;
            }
        }
        // end-sonda yer alma denetimi

        // default
        return 0;
    }

    private void arrangeSubcat() {
        for (int i = 0; i < morpheme.length; i++) {
            if (!morpheme[i].getSubcat().equals("_")) {
                if (morpheme[i].getSubcat().contains("|")) {
                    morpheme[i].setSubcat(trimSubcat(morpheme[i].getSubcat()));
                }
            }
        }
        /*if (!this.stem.getSubcat().equals("_")) {
         this.stem.setSubcat(trimSubcat(this.stem.getSubcat()));
         }*/
    }

    private String trimSubcat(String str) {
        if (str.equals("?")) {
            return str;
        }
        String[] sub = str.split("[|]");
        HashSet<String> set = new HashSet();
        for (String s : sub) {
            if (!s.equals("?")) {
                set.add(s);
            }
        }
        String[] sub2 = set.toArray(new String[set.size()]);
        String temp = "";
        for (String s : sub2) {
            temp += s + "|";
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    private Channel processChannel(Channel channel) {
        final String[] voiceTags = {"REF", "REC", "PROC", "CAUS", "FACT", "PASS"};
        final String[] polarityTags = {"NEG"};
        final String[] compoundTags = {"ABIL", "QUICK", "NEAR", "DUR", "INAB"};
        final String[] tenseTags = {"IMP", "COND", "PAST", "FUT", "NEC", "CONTI", "AOR", "OPT"};
        final String[] pastTypeTags = {"OBS", "NARR"};
        final String[] personTags = {"PER1S", "PER2S", "PER3S", "PER1P", "PER2P", "PER3P"};

        final String[] pluralityTags = {"PLU"};
        final String[] possessiveTags = {"POS1S", "POS2S", "POS3S", "POS1P", "POS2P", "POS3P"};
        final String[] caseTags = {"ACC", "DAT", "ABL", "INS", "EQU", "GEN", "LOC", "ILE"};

        final String[] copulaTenseStates = {"aux1", "aux2", "aux3", "aux4", "aux5", "aux6", "aux7", "aux8", "aux9", "per12"}; // per12 istisna

        boolean foundCopula = false;
        for (int i = 0; i < morpheme.length; i++) {
            for (int j = 0; j < morpheme[i].getTag().getNumElement(); j++) {
                if (Arrays.asList(voiceTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setVoice(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(polarityTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setPolarity(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(compoundTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setCompound(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(tenseTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setTense(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(personTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setPerson(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(pluralityTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setPlurality(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(possessiveTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setPossessive(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(caseTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setCase_(morpheme[i].getTag().getElement()[j]);
                }
                if (Arrays.asList(pastTypeTags).contains(morpheme[i].getTag().getElement()[j])) {
                    channel.setPastType(morpheme[i].getTag().getElement()[j]);
                }
                // copula
                if (Arrays.asList(copulaTenseStates).contains(morpheme[i].getState())) {
                    foundCopula = true;
                    if (Arrays.asList(tenseTags).contains(morpheme[i].getTag().getElement()[j])) {
                        channel.setCopulaTense(morpheme[i].getTag().getElement()[j]);
                    }
                    if (Arrays.asList(pastTypeTags).contains(morpheme[i].getTag().getElement()[j])) {
                        channel.setCopulaPastType(morpheme[i].getTag().getElement()[j]);
                    }
                    if (Arrays.asList(personTags).contains(morpheme[i].getTag().getElement()[j])) {
                        channel.setCopulaPerson(morpheme[i].getTag().getElement()[j]);
                    }
                }
                if (foundCopula) {
                    if (Arrays.asList(personTags).contains(morpheme[i].getTag().getElement()[j])) {
                        channel.setCopulaPerson(morpheme[i].getTag().getElement()[j]);
                    }
                }
                // end-copula
            }
        }

        // eger analizin POS'u verb ise noun channel'larini null yap noun veya turevi ise verb channel'larini null yap
        // eger analizin POS'u copula ise yani major=Auxiliary-Verb ise ...
        if (this.pos.getMajor().equals("Aux")) {
            channel.setVoice("NULL");
            channel.setPolarity("NULL");
            channel.setCompound("NULL");
            channel.setTense("NULL");
            channel.setPastType("NULL");
            channel.setPerson("NULL");
        } else if (this.pos.getMajor().equals("INF") || this.pos.getMajor().equals("PAR") || this.pos.getMajor().equals("GER")) {
            channel.setTense("NULL");
            channel.setPastType("NULL");
            channel.setPerson("NULL");
        } else if (this.pos.getMajor().equals("V")) {
            channel.setPlurality("NULL");
            channel.setPossessive("NULL");
            channel.setCase_("NULL");
        } else {
            channel.setVoice("NULL");
            channel.setPolarity("NULL");
            channel.setCompound("NULL");
            channel.setTense("NULL");
            channel.setPastType("NULL");
            channel.setPerson("NULL");
        }

        return channel;
    }

    private POS detectPOS() {
        final String[] nounAux = {"aux4", "aux5", "aux6", "aux7", "aux8", "aux9"};
        final String[] verbAux = {"aux1", "aux2", "aux3"};
        String major = "", minor = "";
        for (int i = morpheme.length - 1; i >= 0; i--) {
            String t[] = morpheme[i].getTag().getElement();

            // istisnalar
            // iken icin istisna tanimlamasi: evdeyken, ogrenciyken
            if (morpheme[i].getState().equals("advs") && t[0].equals("V'") && t[1].equals("Adv")) {
                major = "Adv";
                minor = "Aux";
                break;
            }
            if (morpheme[i].getState().equals("ger") && t[0].equals("V'") && t[1].equals("Adv")) {
                major = "Adv";
                minor = "GER"; // yapmisken, gidiyorken bunlar icin GER dedik ama tartismali?
                break;
            }
            // end-istisnalar

            if (Arrays.asList(nounAux).contains(morpheme[i].getState())) {
                major = "Aux";
                break;
            }
            if (Arrays.asList(verbAux).contains(morpheme[i].getState())) {
                //
                continue;
            }
            if (morpheme[i].getTag().getNumElement() > 1 && t[1].equals("SC")) {
                if (major.isEmpty()) {
                    major = t[0];
                } else {
                    minor = t[0];
                }
                break;
            }
            if (morpheme[i].getTag().getNumElement() > 1 && t[1].equals("INF")) {
                major = "N";//t[1];
                minor = t[1];
                break;
            }
            if (morpheme[i].getTag().getNumElement() > 1 && t[1].equals("PAR")) {
                major = "Adj";//t[1];
                minor = t[1];
                break;
            }
            if (morpheme[i].getTag().getNumElement() > 1 && t[1].equals("GER")) {
                major = "Adv";//t[1];
                minor = t[1];
                break;
            }
            if (isPOS(t[0])) {
                if (major.isEmpty()) {
                    major = t[0];
                } else {
                    minor = t[0];
                }
            }
        }

        return new POS(major, minor);
    }

    private boolean isPOS(String tag) {
        final String[] pos = {"V", "N", "Adj", "Adv", "Pro", "Pp", "Int", "Cnj", "Com", "IP", "Sym", "Num", "Abb", "Eos", "Is", "V+REC", "V+PASS", "V+REF", "V+PROC", "V+CAUS", "V+FACT"};
        return Arrays.asList(pos).contains(tag);
    }

    private Leaf detectLeaf() {
        Morpheme[] m = new Morpheme[morpheme.length - stemPoint - 1];
        int c = 0;
        for (int i = stemPoint + 1; i < morpheme.length; i++) {
            m[c] = morpheme[i];
            c++;
        }
        return new Leaf(m);
    }

    private Stem detectStem() {
        final String[] rootStop = {"v1", "v2", "v3", "v4", "v5", "proc1", "proc2", "proc3", "proc4", "proc5", "rec", "ref1", "ref2", "ref3", "n", "adj1", "adj2", "adv"};
        final String[] rootStop2 = {"pn", "cnj", "cnj1", "cnj2", "com", "pp1", "pp2", "pp3", "pro", "pro2", "pro3", "abb", "on", "ip", "int", "sym", "oth", "nmr", "punc"};
        final String[] rootStop3 = {"poss1"};
        //final String[] stemStop = {"fact1", "fact2", "caus1", "caus2", "pass", "vs1", "ns1", "ns2", "ns3", "ns4", "adj3", "adjs1", "adjs2", "adjs3", "adjs4", "adjs5", "adjs7", "plu1", "emp2" /*, "adjs6"*/}; // emp2 added
        // dikkat!!! asagidaki satir yeni, ustteki orijinal.
        final String[] stemStop = {"vs1", "ns1", "ns2", "ns3", "ns4", "adj3", "adjs1", "adjs2", "adjs3", "adjs4", "adjs5", "adjs7" /*, "adjs6"*/}; // emp2 added
        final String[] verbalStop = {"ger", "inf1", "inf2", "par1", "par2"};
        Morpheme[] m = null;
        String sur = "", lex = "", tagDesc = "", stt = "", sub = "", tag = "";
        boolean found = false;
        boolean innerFound = false;///
        for (int i = morpheme.length - 1; i >= 0; i--) {
            if (Arrays.asList(rootStop).contains(morpheme[i].getState()) || Arrays.asList(rootStop2).contains(morpheme[i].getState())
                    || Arrays.asList(stemStop).contains(morpheme[i].getState())
                || (Arrays.asList(rootStop3).contains(morpheme[i].getState()) && i == 0/*morpheme[0].getLexical().contains(" ")*/)/*|| Arrays.asList(verbalStop).contains(morpheme[i].getState())*/) {
                found = true;
                m = new Morpheme[i + 1];
                for (int j = 0; j < i + 1; j++) {
                    m[j] = morpheme[j];
                }
                stemPoint = i;
                break;
            }
            if (morpheme[i].getState().equals("<inner>")) {
                innerFound = true;
                continue;
            }
            if (innerFound) {
                found = true;
                m = new Morpheme[i + 1];
                for (int j = 0; j < i + 1; j++) {
                    m[j] = morpheme[j];
                }
                stemPoint = i;
                break;
            }
            /// ekleme (problem: "uretenler")
            if (i == 0) {
                m = new Morpheme[i + 1];
                for (int j = 0; j < i + 1; j++) {
                    m[j] = morpheme[j];
                }
                stemPoint = i;
                break;
            }
        }
//        if (!found) {
//            debug = 1;
//        }
        return new Stem(m);
    }

    @Override
    public String toString() {
        String temp1 = "", temp2 = "", temp3 = "", temp4 = "";
        for (Morpheme m : morpheme) {
            temp1 += m.getSurface() + "/";
            temp2 += m.getLexical() + "/";
            temp3 += m.getTag() + "/";
            temp4 += m.getState() + "/";
        }
        temp1 = temp1.substring(0, temp1.length() - 1);
        temp2 = temp2.substring(0, temp2.length() - 1);
        temp3 = temp3.substring(0, temp3.length() - 1);
        temp4 = temp4.substring(0, temp4.length() - 1);
        return "surface\t" + temp1 + "\nlexical\t" + temp2 + "\ntag\t" + temp3 + "\nstate\t" + temp4 + "\nstem\n\tlexical\t" + stem.getLexical() + "\n\ttotTag\t" + stem.getTotalTag() + "\n\tMtoStr\t" + stem.getMorphemeToString() + "\n\tsubcat\t" + stem.getSubcat() + "\n\tPOS\n\t\tmajor\t" + stem.getPos().getMajor() + "\n\t\tminor\t" + stem.getPos().getMinor() + "\nleaf\n\ttotTag\t" + leaf.getTotalTag() + "\nPOS\n\tmajor\t" + pos.getMajor() + "\n\tminor\t" + pos.getMinor() + "\ntotTag\t" + totalTag + "\nchannel\t" + channel + "\nprobCat\t" + probCat;
    }

    public String toStringShort() {
        String temp2 = "", temp3 = "";
        for (Morpheme m : morpheme) {
            temp2 += m.getLexical() + "/";
            temp3 += m.getTag() + "/";
        }
        temp2 = temp2.substring(0, temp2.length() - 1);
        temp3 = temp3.substring(0, temp3.length() - 1);
        return temp2 + "\t" + temp3;
    }

    public String getMorphemesLexical() {
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getLexical() + "/";
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    public String getMorphemesTag() {
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getTag() + "/";
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    @Override
    public int compareTo(Object o) {
        MorphoAnalysis otherAnalysis = (MorphoAnalysis) o;
        if (sortMode == 1) {
            if (otherAnalysis.getStem().getSurface().length() < this.getStem().getSurface().length()) {
                return -1;
            } else if (otherAnalysis.getStem().getSurface().length() > this.getStem().getSurface().length()) {
                return 1;
            } else {
                if (otherAnalysis.getStem().getLexical().length() < this.getStem().getLexical().length()) {
                    return -1;
                } else if (otherAnalysis.getStem().getLexical().length() > this.getStem().getLexical().length()) {
                    return 1;
                } else {
                    if (otherAnalysis.getStem().getMorphemeToString().split("/").length < this.getStem().getMorphemeToString().split("/").length) {
                        return -1;
                    } else if (otherAnalysis.getStem().getMorphemeToString().split("/").length > this.getStem().getMorphemeToString().split("/").length) {
                        return 1;
                    } else {
                        int r = otherAnalysis.getTotalTag().compareTo(this.getTotalTag());
                        if (r == 0) {
                            r = otherAnalysis.getStem().getMorphemeToString().compareTo(this.getStem().getMorphemeToString());
                        }
                        return r;
                    }
                }
            }
        } else if (sortMode == 2) {
            if (otherAnalysis.morpheme.length < this.morpheme.length) {
                return -1;
            } else if (otherAnalysis.morpheme.length > this.morpheme.length) {
                return 1;
            } else {
                return 0;
            }
        } else if (sortMode == 3) {
            if (Math.random() < 0.5) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }

    // returns true if String value of tag of a morpheme is included in the morpheme[] else returns false
    public boolean search(String s) {
        for (Morpheme m : morpheme) {
            String ms = m.getLexical() + "~" + m.getTag().toString();
            if (ms.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
