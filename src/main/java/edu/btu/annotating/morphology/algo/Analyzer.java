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

import edu.btu.annotating.morphology.structure.State;
import edu.btu.annotating.morphology.structure.lang.LexicalEntry;
import edu.btu.annotating.morphology.structure.lang.LexiconReader;
import edu.btu.annotating.morphology.structure.lang.SuffixEntry;
import edu.btu.annotating.morphology.structure.lang.SuffixReader;
import edu.btu.annotating.morphology.utils.FileReader;
import edu.btu.annotating.morphology.utils.ReadFile;

import java.util.*;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Analyzer {
    private int debug;
    private final ArrayList<LexicalEntry> lexicon;
    private final ArrayList<SuffixEntry> suffixList;
    private final HashMap<String, HashSet<SuffixEntry>> suffixIndex;
    private final HashSet<String> morphotactics;
    private final HashSet<String> states;
    private HashMap<String, ArrayList<LexicalEntry>> rootMap;
    private Synthesizer synthesizer;
    private int sortMode;
    private int numStem;

    public ArrayList<LexicalEntry> getEntryList(String s) {
        return rootMap.get(s);
    }



    public Analyzer(int sortMode) {
        this.sortMode = sortMode;
        // initialization
        // constructing a Synthesizer object
        synthesizer = new Synthesizer();

        // reading the entries from lexicon
        LexiconReader lReader = new LexiconReader();
        ReadFile instance = new ReadFile();

        String trlexFile = instance.getFile("resources/_lang/TrLex").getPath();
        lexicon = lReader.read(trlexFile);

        // reading the morphotactics
        FileReader fReader = new FileReader();
        String morphFile = instance.getFile("resources/_lang/Morphotactics").getPath();
        ArrayList<String> tempList = fReader.read(morphFile);
        for (int i = 0; i < tempList.size(); i++) {
            String s = tempList.get(i);
            String part[] = s.split("\t");
            Set<String> mySet = new HashSet<>(Arrays.asList(part));

            if (mySet.contains("0")) {
                tempList.set(i, s.replace("\t0", "\t"));
                for (int j = 0; j < tempList.size(); j++) {
                    String s2 = tempList.get(j);
                    String part2[] = s2.split("\t");
                    if (part[1].equals(part2[0])) {
                        String[] newArray = Arrays.copyOfRange(part2, 2, part2.length);
                        String r = part[0] + "\t" + part2[1];
                        for (String a : newArray) {
                            r += "\t" + a;
                        }
                        tempList.add(r);
                    }
                }
            } else {

            }
        }

        states = new HashSet();
        morphotactics = new HashSet();
        for (int i = 0; i < tempList.size(); i++) {
            String s = tempList.get(i);
            String part[] = s.split("\t");
            for (int j = 2; j < part.length; j++) {
                morphotactics.add(part[0] + "\t" + part[1] + "\t" + part[j]);
            }
            states.add(part[0]);
            states.add(part[1]);
        }
        // end-reading the morphotactics

        // reading the suffixes
        SuffixReader sReader = new SuffixReader();
        String suffixFile = instance.getFile("resources/_lang/SuffixList").getPath();
        suffixList = sReader.read(suffixFile);

        suffixIndex = new HashMap();
        for (String state : states) {
            for (SuffixEntry se : suffixList) {
                if (state.equals("n") && se.getlForm().getContent().equals("SH")) {
                    debug = 1;
                }
                if (morphotactics.contains(state + "\t" + se.getState().getContent() + "\t" + se.getlForm().getContent())) {
                    for (String sf : se.getsForm().getContents()) {
                        for (int i = 1; i <= sf.length(); i++) {
                            String key = state + "\t" + sf.substring(0, i);
                            HashSet<SuffixEntry> temp = suffixIndex.get(key);
                            if (temp == null) {
                                temp = new HashSet();
                            }
                            if (i == sf.length()) {
                                temp.add(se);
                            }
                            suffixIndex.put(key, temp);
                        }
                    }
                }
            }
        }
        // end-reading the suffixes

        // mapping the surface forms to the lexical entries
        rootMap = new HashMap();
        for (LexicalEntry le : lexicon) {
            for (String sForm : le.getsForm().getContents()) {
                ArrayList<LexicalEntry> entryList = rootMap.get(sForm);
                if (entryList == null) {
                    entryList = new ArrayList();
                }
                entryList.add(le);
                rootMap.put(sForm, entryList);
            }
        }
        // end-mapping the surface forms to the lexical entries

        // end-initialization
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode;
    }

    public MorphoAnalysis[] getAnalysis(String token) {
        String token2 = getLowercase(processAccents(token))/*.toLowerCase(new Locale("tr", "TR"))*/ + "#";

        class Candidate {

            LexicalEntry lex;
            ArrayList<SuffixEntry> suffixList;
            ArrayList<String> surfaceFormList;
            State state;
            String rest;

            public Candidate(LexicalEntry lex, ArrayList<String> surfaceFormList, State state, String rest) {
                this.lex = lex;
                this.surfaceFormList = surfaceFormList;
                this.state = state;
                this.rest = rest;
                suffixList = new ArrayList();
            }

            public Candidate(LexicalEntry lex, ArrayList<SuffixEntry> suffixList, ArrayList<String> surfaceFormList, State state, String rest) {
                this.lex = lex;
                this.suffixList = suffixList;
                this.surfaceFormList = surfaceFormList;
                this.state = state;
                this.rest = rest;
            }

            public void setState(State state) {
                this.state = state;
            }

            public void setRest(String rest) {
                this.rest = rest;
            }

            public void addSuffix(SuffixEntry se) {
                suffixList.add(se);
            }

            public void addSurfaceForm(String sf) {
                surfaceFormList.add(sf);
            }

            public Candidate duplicate() {
                ArrayList<SuffixEntry> copySuffixList = new ArrayList();
                for (SuffixEntry se : suffixList) {
                    copySuffixList.add((SuffixEntry) se.duplicate());
                }
                ArrayList<String> copySurfaceFormList = new ArrayList(surfaceFormList);
                return new Candidate((LexicalEntry) lex.duplicate(), copySuffixList, copySurfaceFormList, (State) state.duplicate(), rest);
            }

            @Override
            public String toString() {
                return "Candidate{" + "lex=" + lex + ", suffixList=" + suffixList + ", surfaceFormList=" + surfaceFormList + ", state=" + state + ", rest=" + rest + '}';
            }
        }

        // root finding
        ArrayList<Candidate> candidates = new ArrayList();
        for (int i = 1; i <= token2.length(); i++) {
            String cut = token2.substring(0, i);
            ArrayList<LexicalEntry> lexicalEntryList = rootMap.get(cut);
            if (lexicalEntryList != null) {
                for (LexicalEntry lex : lexicalEntryList) {
                    ArrayList<String> temp = new ArrayList();
                    temp.add(cut);
                    candidates.add(new Candidate(lex, temp, lex.getState(), token2.substring(i)));
                }
            }
        }
        // end-root finding

        // suffix searching
        loop:
        for (int i = 0; i < candidates.size(); i++) {
            Candidate candidate = candidates.get(i);
            String rest = candidate.rest;
            State state = candidate.state;

            if (!state.getContent().equals("end") && rest.isEmpty()) {
                rest = "#";
            }
            for (int j = 0; j < rest.length(); j++) {
                String cut = rest.substring(0, j + 1);
                HashSet<SuffixEntry> temp = suffixIndex.get(state.getContent() + "\t" + cut);
                if (temp == null) {
                    candidates.remove(i);
                    i--;
                    continue loop;
                } else if (!temp.isEmpty()) {
                    for (SuffixEntry se : temp.toArray(new SuffixEntry[temp.size()])) {
                        Candidate newC = candidate.duplicate();
                        newC.setState(se.getState());
                        newC.setRest(rest.substring(j + 1));
                        newC.addSuffix(se);
                        newC.addSurfaceForm(cut);
                        candidates.add(newC);
                    }
                }
            }

            if (!candidate.state.getContent().equals("end")) {
                candidates.remove(i);
                i--;
            }
        }
        // end-suffix searching

        // candidate checking
        for (int i = 0; i < candidates.size(); i++) {
            Candidate candidate = candidates.get(i);
            String s = candidate.lex.getlForm().getContent() + "+";
            for (int j = 0; j < candidate.suffixList.size() - 1; j++) {
                SuffixEntry se = candidate.suffixList.get(j);
                s += se.getlForm().getContent() + "+";
            }
            String result = clear(synthesizer.synthesis(s.toCharArray(), candidate.lex.getvCat().getContent(), '\t'), '\t') + "#";
            if (!token2.equals(result)) {
                candidates.remove(i);
                i--;
            }
        }
        // end-candidate checking

        // MorphoAnalysis constructing
        ArrayList<MorphoAnalysis> result = new ArrayList();
        for (int i = 0; i < candidates.size(); i++) {
            Candidate candidate = candidates.get(i);

            Morpheme[] mArr = new Morpheme[candidate.surfaceFormList.size() - 1];
            mArr[0] = new Morpheme("B", candidate.surfaceFormList.get(0), candidate.lex.getlForm().getContent(), candidate.lex.getTag(), "", candidate.lex.getState().getContent(), candidate.lex.getSubcat(), candidate.lex.getDeepA().getContent(), candidate.lex.getvCat().getContent(), "");
            for (int j = 1; j < mArr.length; j++) {
                mArr[j] = new Morpheme("S", candidate.surfaceFormList.get(j), candidate.suffixList.get(j - 1).getlForm().getContent(), candidate.suffixList.get(j - 1).getMTag(), "", candidate.suffixList.get(j - 1).getState().getContent(), "_", "_", "_", "");
            }
            mArr = processExceptions(mArr);
            MorphoAnalysis analysis = new MorphoAnalysis(mArr, sortMode, false);
            result.add(analysis);
        }

        ArrayList<MorphoAnalysis> result2 = removeMistakes(result);
        result2 = removeOverlapping(result2);

        MorphoAnalysis[] result3 = result2.toArray(new MorphoAnalysis[result2.size()]);
        Arrays.sort(result3);

        ////////////////////////////////////////////
        HashSet<String> stemSet = new HashSet();
        for (MorphoAnalysis a : result3) {
            stemSet.add(a.getStem().getLexical());
        }
        numStem = stemSet.size();
        ////////////////////////////////////////////

        return result3;
        // end-MorphoAnalysis constructing
    }

    private String processAccents(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            if (c == 'â') {
                result += 'a';
            } else if (c == 'î') {
                result += 'i';
            } else if (c == 'ô') {
                result += 'o';
            } else if (c == 'û') {
                result += 'u';
            } else {
                result += c;
            }
        }
        return result;
    }

    private String getLowercase(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            int posL = Synthesizer.LOWERCASE.indexOf(c);
            int posU = Synthesizer.UPPERCASE.indexOf(c);
            if (posL > -1) {
                result += c;
            } else if (posU > -1) {
                result += Synthesizer.LOWERCASE.charAt(posU);
            } else {
                result += c;
            }
        }
        return result;
    }

    private String clear(String s, char separator) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) != '0' && s.charAt(i) != separator)
                buf.append(s.charAt(i));
        return buf.toString();
    }

    private Morpheme[] processExceptions(Morpheme[] mArr) {
        ArrayList<Morpheme> mList = new ArrayList();
        for (Morpheme m : mArr) {
            if (m.getLexical().endsWith("|")) {
                // ban/a~ben/NA~Pro/DAT
                if (m.getLexical().equals("benim|")) {
                    debug = 1;
                }
                if (m.getDeepAnalysis().equals("_")) {
                    mList.add(new Morpheme("B", m.getSurface(), m.getLexical(), m.getTag(), "", "<inner>", "", "", "", ""));
                } else {

                    String[] b = m.getDeepAnalysis().split("~");
                    String[] sur = b[0].split("/");
                    String[] lex = b[1].split("/");
                    String[] tag = b[2].split("/");
                    String[] stt = null;
                    try {
                        stt = b[3].split("/");
                    } catch (Exception e) {
                        //System.out.println(m.getLexical());
                        stt = new String[sur.length];
                        for (int i = 0; i < sur.length; i++) {
                            stt[i] = m.getState();
                        }
                    }
                    String type = "", state = "<inner>";
                    for (int i = 0; i < sur.length; i++) {
                        if (i == 0) {
                            type = "B";
                        } else {
                            type = "S";
                        }
                        if (i == sur.length - 1) {
                            state = m.getState();
                        }
                        mList.add(new Morpheme(type, sur[i], lex[i], new MorphoTag(tag[i].split("[+]")), "", stt[i], "", m.getDeepAnalysis(), m.getVerbType(), m.getRealSurface()));
                    }
                    // Morpheme morpheme = new Morpheme(type, surface[j], lexical[j], new Tag(t), tagDesc[j], state[j], subcat2, baseAnalysis2);
                }
            } else {
                mList.add(m);
            }
        }
        return mList.toArray(new Morpheme[mList.size()]);
    }

    private ArrayList<MorphoAnalysis> removeMistakes(ArrayList<MorphoAnalysis> list) {
        ArrayList<MorphoAnalysis> result = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            MorphoAnalysis a = list.get(i);
            Morpheme[] morpheme = a.getMorpheme();
            if (a.getMorpheme().length == 1) {
                result.add(a);
            } else {
                boolean error = false;
                for (int j = 0; j < morpheme.length; j++) {
                    String sur = morpheme[j].getSurface();
                    String sur2 = "";
                    if (j < morpheme.length - 1) {
                        sur2 = morpheme[j + 1].getSurface();
                    }
                    String lex = morpheme[j].getLexical();
                    String lex2 = "";
                    if (j < morpheme.length - 1) {
                        lex2 = morpheme[j + 1].getLexical();
                    }
                    MorphoTag tag = morpheme[j].getTag();
                    MorphoTag tag2 = null;
                    if (j < morpheme.length - 1) {
                        tag2 = morpheme[j + 1].getTag();
                    }
                    String stt = morpheme[j].getState();
                    String stt2 = "";
                    if (j < morpheme.length - 1) {
                        stt2 = morpheme[j + 1].getState();
                    }
                    if (j < morpheme.length - 1 && tag.getElement()[0].equals("Pro") && stt.equals("pro") && stt2.equals("poss1")) {
                        // Kural: Zamirlerden sonra dogrudan iyelik eki gelemez. Ornek: benimiz, senleri, biziniz, bazi zamirlerde bu kural gecerli degil, onlarin state'i pro3. iyelik gelemeyenlerin pro
                        error = true;
                        break;
                    }
                    if (tag.getElement()[0].equals("Adj") &&
                            (sur.equals("bu") || sur.equals("şu") || sur.equals("o")) && stt2.equals("poss1")) {
                        // Kural: Isaret sifatlarindan sonra dogrudan iyelik eki gelemez. Ornek: bun, sun, on, bunumuz, sunumuz, onumuz
                        error = true;
                        break;
                    }
                    if (j == morpheme.length - 1 && lex.equals("DHK")) {
                        // Kural: DHK eki en sonda, kendinden sonra herhangi bir ek gelmeksizin gozlenemez. tanidik, bildik gibi bazi kalip sozcukler butun halde lexicon'da yer almak sartiyla
                        error = true;
                        break;
                    }
                    if (j < morpheme.length - 1 && "aeıioöuü".contains(lex.substring(lex.length()-1, lex.length())) && lex2.equals("sH")) {
                        // Kural: sH~similarity eki unlu ile biten bir govdeye gelemez. Ornek: kitapsi->dogru parasi->yanlis
                        error = true;
                        break;
                    }
                    if (j < morpheme.length - 1 && lex.equals("sH") && stt2.equals("poss1")) {
                        // Kural: sH~similarity ekinden sonra iyelik eki gelemez. Ornek: insan-si-n
                        error = true;
                        break;
                    }
                }
                if (!error) {
                    // eger hicbir hatali durum yoksa analizi sonuc listesine al
                    result.add(a);
                }
            }
        }

        return result;
    }

    private ArrayList<MorphoAnalysis> removeOverlapping(ArrayList<MorphoAnalysis> analysisArr) {

        HashMap<String, ArrayList<MorphoAnalysis>> totalTagMap = new HashMap();
        for (MorphoAnalysis analysis : analysisArr) {
            ArrayList<MorphoAnalysis> list = totalTagMap.get(analysis.getStem().getMorphemeToString() + "." + analysis.getPos() + "." + analysis.getChannel()); // <= analysis.getStem().getLexical()
            if (list == null) {
                list = new ArrayList();
            }
            list.add(analysis);
            totalTagMap.put(analysis.getStem().getMorphemeToString() + "." + analysis.getPos() + "." + analysis.getChannel(), list); // <= analysis.getStem().getLexical()
        }

        ArrayList<MorphoAnalysis> result = new ArrayList();
        Set keySet = totalTagMap.keySet();
        Iterator itr = keySet.iterator();
        int c = 0;
        while (itr.hasNext()) {
            String key = (String) itr.next();
            ArrayList<MorphoAnalysis> value = totalTagMap.get(key);
            if (!checkOverlapping(value)) {
                for (MorphoAnalysis analysis : value) {
                    result.add(analysis);
                }
            } else {
                int minNumMorphemeIndex = 0;
                for (int i = 0; i < value.size(); i++) {
                    if ((value.get(i).getStem().getMorphemeToString() + "." + value.get(i).getPos() + "." + value.get(i).getChannel()).equals(key)) { // <= getStem().getLexical()
                        if (value.get(i).getNumMorpheme() < value.get(minNumMorphemeIndex).getNumMorpheme()) {
                            minNumMorphemeIndex = i;
                        }
                    }
                }
                result.add(value.get(minNumMorphemeIndex));
            }
        }

        return result;
    }

    private boolean checkOverlapping(ArrayList<MorphoAnalysis> list) {
        if (list.size() == 1) {
            return false;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getNumMorpheme() == list.get(j).getNumMorpheme()) {
                    //return false;
                }
            }
        }

        return true;
    }
}
