package edu.btu.annotating.morphology.structure.syntax;

import edu.btu.annotating.morphology.algo.*;
import edu.btu.annotating.morphology.structure.syntax.function.Dependent;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.structure.*;
import edu.btu.annotating.morphology.structure.syntax.structure.base.Base;
import edu.btu.annotating.morphology.structure.syntax.structure.base.BaseMaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class TreeGenerator implements Runnable {

    private Analyzer analyzer;
    private MorphoAnalysis[] path;
    private HashMap<String, Function> tempTree;
    private int blockLimit;
    private int pathNo;

    public TreeGenerator(Analyzer analyzer, MorphoAnalysis[] path, HashMap<String, Function> tempTree, int blockLimit, int pathNo) {
        this.analyzer = analyzer;
        this.path = path;
        this.tempTree = tempTree;
        this.blockLimit = blockLimit;
        this.pathNo = pathNo;
    }

    @Override
    public void run() {
        getTree();
    }

    private class Segment {

        Function item;
        String lex;
        Morpheme[] mList;
        int serialNum;

        public Segment(Function item, String lex, Morpheme[] mList, int serialNum) {
            this.item = item;
            this.lex = lex;
            this.mList = mList;
            this.serialNum = serialNum;
        }
    }

    private class Block {

        ArrayList<Segment> list;
        int position;

        public Block(ArrayList<Segment> list, int position) {
            this.list = list;
            this.position = position;
        }
    }

    private void getTree() {
        ArrayList<Segment> list = new ArrayList();
        Clause.staticNum = 0;

        int count1 = 0;
        int ii = 0;
        while (ii < path.length) {
            MorphoAnalysis current = path[ii];
            MorphoAnalysis next = null, nextnext = null;
            if (ii < path.length - 1) {
                next = path[ii + 1];
            }
            if (ii < path.length - 2) {
                nextnext = path[ii + 2];
            }
            //~~~~~~~~auxiliary verb~~~~~~~~~~~~~
            if (current.getPos().getMajor().equals("Aux")) {
                Function f = BaseMaker.make(current.getStem());
                list.add(new Segment(f, current.getMorphemesLexical(), current.getMorpheme(), count1));
                count1++;
                Leaf leaf = current.getLeaf();
                if (leaf != null) {
                    for (Morpheme m : leaf.getMorpheme()) {
                        if (Arrays.asList(m.getTag().getElement()).contains("V'")) {
                            Morpheme[] morpheme = {new Morpheme("B","<>","<i>",new MorphoTag("V"),"_","v0","NOM1234:#0","_","_","_")};
                            Stem stem = new Stem(morpheme);
                            f = BaseMaker.make(stem);
                            list.add(new Segment(f, "<i>", morpheme, count1));
                            count1++;
                        }
                        f = Suffix.make(m);
                        if (f == null) {
                            return;
                        }
                        list.add(new Segment(f, current.getMorphemesLexical(), current.getMorpheme(), count1));
                        count1++;
                    }
                }
                ii++;
                continue;
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            //+++++++++++compound verb+++++++++++
//            if (next != null && (next.getPos().getMajor().equals("V") || next.getPos().getMinor().equals("PAR") || next.getPos().getMinor().equals("GER") || next.getPos().getMinor().equals("INF"))) {
//                ArrayList<LexicalEntry> entryList = analyzer.getEntryList(current.getSurface() + " " + next.getStem().getSurface());
//                if (entryList != null) {
//                    Function f = CompoundVerb.make(entryList.get(0), current, next.getStem());
//                    list.add(new Segment(f, current.getMorphemesLexical() + "/" + next.getMorphemesLexical(), ArrayUtils.addAll(current.getMorpheme(), next.getMorpheme()), count1));
//                    count1++;
//                    Leaf leaf = next.getLeaf();
//                    if (leaf != null) {
//                        for (Morpheme m : leaf.getMorpheme()) {
//                            f = Suffix.make(m);
//                            if (f == null) {
//                                return;
//                            }
//                            list.add(new Segment(f, next.getMorphemesLexical(), next.getMorpheme(), count1));
//                            count1++;
//                        }
//                    }
//                    ii += 2;
//                    continue;
//                }
//            }
            //+++++++++++++++++++++++++++++++++++

            //%%%%compound adjectives/adverbs%%%%
//            if (next != null) {
//                ArrayList<LexicalEntry> entryList = analyzer.getEntryList(current.getSurface() + " " + next.getStem().getSurface());
//                if (entryList != null) {
//                    Function f = CompoundMaker.make(entryList.get(0), current, next.getStem());
//                    list.add(new Segment(f, current.getMorphemesLexical() + "/" + next.getMorphemesLexical(), ArrayUtils.addAll(current.getMorpheme(), next.getMorpheme()), count1));
//                    count1++;
//                    Leaf leaf = next.getLeaf();
//                    if (leaf != null) {
//                        for (Morpheme m : leaf.getMorpheme()) {
//
//                            f = Suffix.make(m);
//                            if (f == null) {
//                                return;
//                            }
//                            list.add(new Segment(f, next.getMorphemesLexical(), next.getMorpheme(), count1));
//                            count1++;
//                        }
//                    }
//                }
//            }
            //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

            Stem stem = current.getStem();
            Function f = BaseMaker.make(stem);
            if (f == null) {
                return;
            }
            list.add(new Segment(f, current.getMorphemesLexical(), current.getMorpheme(), count1));
            count1++;
            Leaf leaf = current.getLeaf();
            if (leaf != null) {
                for (Morpheme m : leaf.getMorpheme()) {
                    f = Suffix.make(m);
                    if (f == null) {
                        return;
                    }
                    list.add(new Segment(f, current.getMorphemesLexical(), current.getMorpheme(), count1));
                    count1++;
                }
            }
            // YA~Adv+GER eki yalnizca ikilemelerde gozlenebilir
            if (next != null && !current.equals(next) && current.search("YA~Adv+GER") && !next.search("YA~Adv+GER")) {
                return;
            }
            ii++;
        }

        // kural incelemesi
        // YA~Adv+GER eki yalnizca ikilemelerde gozlenebilir
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).item instanceof Suffix && ((Suffix) list.get(i).item).getMorpheme().getTag().toString().equals("YA~Adv+GER")) {
                for (Morpheme m : list.get(i + 1).mList) {
                    if (m.getTag().toString().equals("YA~Adv+GER")) {

                    }
                }
            }
        }

        // compound expression'larin tespit edilmesi
        // 1-compound verb
//        int i = 0;
//        for (Segment seg : list) {
//            if (i > 0 && seg.item instanceof BaseVerb) {
//
//            }
//            ArrayList<LexicalEntry> lle = analyzer.getEntryList("");
//            i++;
//        }

        // end-kural incelemesi
        // once tumcedeki her bir eylem, eylem obegi kurar
        ArrayList<Segment> list2 = new ArrayList();
        for (Segment seg : list) {
            Function temp = Clause.make(seg.item, path, pathNo);
            if (seg.lex.equals("<i>")) {
                ((Clause)temp).setAux(true);
            }
            if (temp != null) {
                list2.add(new Segment(temp, seg.lex, seg.mList, seg.serialNum));
            } else {
                list2.add(seg);
            }

        }

        //***********************>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>**
        // eger tumlec talep eden bir sifat varsa SuperAdjective yapisi kurulur
        int i = 0;
        ArrayList<Segment> tempL = new ArrayList();
        while (i < list2.size()) {
            Function f = list2.get(i).item;
            if (f instanceof Base && !((Base) f).getSubcat().equals("_")) {
                Function temp = SuperAdjective.make(f, path, pathNo);
                if (temp != null) {
                    tempL.add(new Segment(temp, list2.get(i).lex, list2.get(i).mList, list2.get(i).serialNum));
                } else {
                    tempL.add(list2.get(i));
                }
            } else {
                tempL.add(list2.get(i));
            }
            i++;
        }
        list2 = tempL;

        // eger tumlec talep eden bir belirtec varsa SuperAdverb yapisi kurulur
        i = 0;
        tempL = new ArrayList();
        while (i < list2.size()) {
            Function f = list2.get(i).item;
            if (f instanceof Base && !((Base) f).getSubcat().equals("_")) {
                Function temp = SuperAdverb.make(f, path, pathNo);
                if (temp != null) {
                    tempL.add(new Segment(temp, list2.get(i).lex, list2.get(i).mList, list2.get(i).serialNum));
                } else {
                    tempL.add(list2.get(i));
                }
            } else {
                tempL.add(list2.get(i));
            }
            i++;
        }
        list2 = tempL;

        // eger tumlec talep eden bir ad varsa SuperNoun yapisi kurulur
        i = 0;
        tempL = new ArrayList();
        while (i < list2.size()) {
            Function f = list2.get(i).item;
            if (f instanceof Base && !((Base) f).getSubcat().equals("_")) {
                Function temp = SuperNoun.make(f, path, pathNo);
                if (temp != null) {
                    tempL.add(new Segment(temp, list2.get(i).lex, list2.get(i).mList, list2.get(i).serialNum));
                } else {
                    tempL.add(list2.get(i));
                }
            } else {
                tempL.add(list2.get(i));
            }
            i++;
        }
        list2 = tempL;
        //***********************>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>**

        // eylem obeginin yaninda eylem cekim ekleri varsa bunlar birlestirilir
        i = 0;
        while (i < list2.size() - 1) {
            Function f1 = list2.get(i).item;
            Function f2 = list2.get(i + 1).item;
            if (f1 instanceof Clause && f2 instanceof Dependent) {
                ArrayList<Clause> tempList = Clause.update((Clause) f1, f2);
                if (tempList.size() > 0) {
                    list2 = conjoin(list2, new Segment(tempList.get(0), list2.get(i).lex, list2.get(i).mList, list2.get(i).serialNum), i);
                    i--;
                }
            }
            i++;
        }

        // eger eylem obeginin saginda bir participle, infinitive veya gerundium eki varsa eylem obeginin cType degeri "Infinite" olarak degistirilir.
        i = 0;
        while (i < list2.size() - 1) {
            Function f1 = list2.get(i).item;
            Function f2 = list2.get(i + 1).item;
            if (f1 instanceof Clause && f2 instanceof Dependent) {
                String st = ((Suffix)f2).getMorpheme().getState();
                if (st.equals("par1") || st.equals("par2") || st.equals("inf1") || st.equals("inf2") || st.equals("ger")) {
                    Clause clause = (Clause) f1;
                    clause.setcType("Infinite");
                }
            }
            i++;
        }

        // cogul eki solundaki ogeyle birlestirilir
/*        i = 0;
        while (i < list2.size() - 1) {
            Function f1 = list2.get(i).item;
            Function f2 = list2.get(i + 1).item;
            if (!(f1 instanceof Dependent) && f2 instanceof Dependent) {
                Function temp = PluralNoun.make(f1, f2);
                if (temp != null) {
                    list2 = conjoin(list2, new Segment(temp, list2.get(i).lex, list2.get(i).mList, list2.get(i).serialNum), i);
                }
            }
            i++;
        }*/

        ArrayList<Block> blockList = new ArrayList();
        blockList.add(new Block(list2, 0));

        int count = 0;
        do {
            ArrayList<Segment> list3 = blockList.get(count).list;
            i = blockList.get(count).position;
            segmentLoop:
            do {
                if (SyntaxAnalysis.numBlock + blockList.size() > blockLimit) {
                    tempTree.clear();///
                    return;
                }
                if (i >= list3.size() - 1) {
                    break;
                }

                int r = i + 1;
                int q = 0;
                if (i > 0) {
                    q = i - 1;
                }

                Function f1 = list3.get(i).item;
                Function f2 = list3.get(i + 1).item;
                if (!(f1 instanceof Dependent) && f2 instanceof Dependent) {
                    ///////////////////////////////////////////////////////////////////////
                    Function temp = PluralNoun.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    ///////////////////////////////////////////////////////////////////////
                    temp = Adjective_ki.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = CasedNoun.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = GenitiveNoun.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = PluralNoun.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = PossessiveNoun.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = Participle.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = Infinitive.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = Gerundium.make(f1, f2);
                    if (temp != null) {
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    if (f1 instanceof Clause) {
                        ArrayList<Clause> tempList = Clause.update((Clause) f1, f2);

                        boolean isExist = false;
                        for (Function f : tempList) {
                            if (f != null) {
                                isExist = true;
                                blockList.add(new Block(conjoin(list3, new Segment(f, list3.get(i).lex, list3.get(i).mList, list3.get(i).serialNum), i), q));
                            }
                        }

                        if (isExist) {
                            break segmentLoop;
                        }
                    }
                }
                if (!(f1 instanceof Dependent) && !(f2 instanceof Dependent)) {
                    Function temp = null;
                    try {
                        temp = NounPhrase.make(f1, f2);
                    } catch (Exception e) {
                        System.out.println("");
                    }
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = PostpositionalPhrase.make(f1, f2);
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = PostpositionalPhraseAdj.make(f1, f2);
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = ConjunctionPhrase.make(f1, f2);
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    /*temp = ChainPhrase.make(f1, f2);
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }*/
                    temp = Transparent.make(f1, f2);
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = AdjectivePhrase.make(f1, f2);
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }
                    temp = AdverbPhrase.make(f1, f2);
                    if (temp != null) {
                        if (r < list3.size() - 1) {
                            blockList.add(new Block(list3, r));
                        }
                        list3 = conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i);
                        blockList.get(count).list = list3;
                        if (i > 0) {
                            i--;
                        }
                        continue segmentLoop;
                    }

                    if (f2 instanceof SuperAdjective) {
                        temp = SuperAdjective.update((SuperAdjective) f2, f1);
                        boolean isExist = false;
                        if (temp != null) {
                            isExist = true;
                            if (r < list3.size() - 1) {
                                blockList.add(new Block(list3, r));
                            }
                            blockList.add(new Block(conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i), q));
                        }
                        if (isExist) {
                            break segmentLoop;
                        }
                    }

                    if (f2 instanceof SuperAdverb) {
                        temp = SuperAdverb.update((SuperAdverb) f2, f1);
                        boolean isExist = false;
                        if (temp != null) {
                            isExist = true;
                            if (r < list3.size() - 1) {
                                blockList.add(new Block(list3, r));
                            }
                            blockList.add(new Block(conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i), q));
                        }
                        if (isExist) {
                            break segmentLoop;
                        }
                    }

                    if (f2 instanceof SuperNoun) {
                        temp = SuperNoun.update((SuperNoun) f2, f1);
                        boolean isExist = false;
                        if (temp != null) {
                            isExist = true;
                            if (r < list3.size() - 1) {
                                blockList.add(new Block(list3, r));
                            }
                            blockList.add(new Block(conjoin(list3, new Segment(temp, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i), q));
                        }
                        if (isExist) {
                            break segmentLoop;
                        }
                    }

                    if (f2 instanceof Clause) {
                        ArrayList<Clause> tempList = Clause.update((Clause) f2, f1);
                        boolean isExist = false;
                        for (Function f : tempList) {
                            if (f != null) {
                                isExist = true;
                                if (r < list3.size() - 1) {
                                    blockList.add(new Block(list3, r));
                                }
                                blockList.add(new Block(conjoin(list3, new Segment(f, list3.get(i).lex + " " + list3.get(i + 1).lex, list3.get(i).mList, list3.get(i).serialNum), i), q));
                            }
                        }
                        if (isExist) {
                            break segmentLoop;
                        }
                    }
                }
                i++;
            } while (i < list3.size() - 1);
            count++;
            SyntaxAnalysis.numBlock++;
        } while (count < blockList.size());

        //SyntaxAnalysisMultithreading.numBlock += count;

        for (Block block : blockList) {
            ArrayList<Segment> list_ = block.list;
            if (list_.size() == 1) {
                if (list_.get(0).item/*.getStructure()*/ instanceof Clause) {

                    Clause c = (Clause) list_.get(0).item.getStructure();
                    if (c.checkMandatory()) {
                        if (c.getSubcat().isOk() || c.getSubcat().isHalfness()/* && c.checkSubcat()*/) {
                            tempTree.put(c.toString(), c);

                        } else if (c.getComplement().isEmpty() && c.checkSubcat()) {
                            tempTree.put(c.toString(), c);
                        }
                    }
                }
                ///////////*****************///////////////////**************//////////////////*****************************
                ///else if (list_.get(0).item/*.getStructure()*/ instanceof NounPhrase) { // BU KOD BLOGU DENEME AMACLI EKLENDI
                /// tempTree.put(list_.get(0).item.getStructure().toString(), (NounPhrase) list_.get(0).item.getStructure());
                ///}
                ///////////*****************///////////////////**************//////////////////*****************************
            }
        }
    }

    /*private boolean checkSuperTypes(Function f) {
        if (f instanceof Clause) {
            Clause c = (Clause) f;
            Subject subject = c.getSubject();
            ArrayList<Complement> complements = c.getComplement();
            ArrayList<Adjunct> adjuncts = c.getAdjunct();
            boolean b = checkSuperTypes(subject.getContent());
            if (!b) return b;
            for (Complement complement : complements) {
                b = checkSuperTypes(complement.getContent());
                if (!b) return b;
            }
            for (Adjunct adjunct : adjuncts) {
                b = checkSuperTypes(adjunct.getContent());
                if (!b) return b;
            }
        } else if (f instanceof Phrase) {
            Phrase phrase = (Phrase) f;
            boolean b = checkSuperTypes((Function) phrase.getHead());
            if (!b) return b;
            b = checkSuperTypes(phrase.getComp());
            if (!b) return b;
        } else if (f instanceof Inflected) {
            Inflected inflected = (Inflected) f;
            boolean b = checkSuperTypes(inflected.getBase());
            if (!b) return b;
        } else if (f instanceof SuperAdjective) {
            SuperAdjective sa = (SuperAdjective) f;
            return sa.getSubcat().isOk();
        } else if (f instanceof Base) {
            return true;
        }
        return true;
    }*/

    private ArrayList<Segment> conjoin(ArrayList<Segment> list, Segment seg, int i) {
        ArrayList<Segment> copyList = (ArrayList<Segment>) list.clone();
        copyList.add(i, seg);
        if (i + 1 < copyList.size()) {
            copyList.remove(i + 1);
        }
        if (i + 1 < copyList.size()) {
            copyList.remove(i + 1);
        }
        return copyList;
    }
}