package edu.btu.annotating.morphology.structure.syntax;

import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.role.Adjunct;
import edu.btu.annotating.morphology.structure.syntax.role.Complement;
import edu.btu.annotating.morphology.structure.syntax.structure.Clause;
import edu.btu.annotating.morphology.structure.syntax.structure.Inflected;
import edu.btu.annotating.morphology.structure.syntax.structure.Phrase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class SyntaxAnalysis {

    private Analyzer analyzer;
    private ArrayList<MorphoAnalysisMap> morphoAnalysisMapList;
    private int numThread;
    private int blockLimit;
    private ArrayList<Function> treeList;
    private ArrayList<ArrayList<Clause>> clauseList;
    private HashMap<String, Function> tempTree;
    private int numTree;
    public static volatile int numBlock;
    private long totalCartesianProduct;

    public SyntaxAnalysis(Analyzer analyzer, MorphoAnalysisMap morphoAnalysisMap, int numThread, int blockLimit) throws InterruptedException {
        // silinecek
    }

    public SyntaxAnalysis(Analyzer analyzer, ArrayList<MorphoAnalysisMap> morphoAnalysisMapList, int numThread, int blockLimit) throws InterruptedException {
        this.analyzer = analyzer;
        this.morphoAnalysisMapList = morphoAnalysisMapList;
        this.numThread = numThread;
        this.blockLimit = blockLimit;
        process2();
    }

    public SyntaxAnalysis(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public SyntaxAnalysis() {
    }

    public int getNumTree() {
        return numTree;
    }

    public ArrayList<Function> getTreeList() {
        return treeList;
    }

    public ArrayList<Clause> getAllClauseList() {
        ArrayList<Clause> result = new ArrayList();
        for (ArrayList<Clause> list : clauseList) {
            for (Clause c : list) {
                result.add(c);
            }
        }
        return result;
    }

    public ArrayList<ArrayList<Clause>> getClauseList() {
        return clauseList;
    }

    public ArrayList<MorphoAnalysisMap> getMorphoAnalysisMapList() {
        return morphoAnalysisMapList;
    }

    public long getTotalCartesianProduct() {
        return totalCartesianProduct;
    }

    public int getNumMam() {
        return morphoAnalysisMapList.size();
    }

    public void processSingle(MorphoAnalysis[] path, int blockLimit, int pathNo) {
        treeList = new ArrayList();
        tempTree = new HashMap();
        numBlock = 0;

        TreeGenerator tg = new TreeGenerator(analyzer, path, tempTree, blockLimit, pathNo);
        tg.run();
        treeList.addAll(tempTree.values());
        numTree = treeList.size();
    }

    /*private void process() throws InterruptedException {
        treeList = new ArrayList();
        tempTree = new HashMap();
        numBlock = 0;

        if (morphoAnalysisMap.getMap() == null) {
            return;
        }

        MorphoAnalysis[][] pathArray = morphoAnalysisMap.getMap();

        Thread[] thread = new Thread[numThread];
        int cc = 0;
        do {
            for (int j = 0; j < numThread; j++) {
                if (cc == pathArray.length) {
                    break;
                }
                if (thread[j] == null || !thread[j].isAlive()) {
                    thread[j] = new Thread(new TreeGenerator(analyzer, pathArray[cc], tempTree, blockLimit, cc));
                    thread[j].start();
                    cc++;
                }
            }
        } while (cc < pathArray.length);

        for (int j = 0; j < numThread; j++) {
            if (thread[j] != null) {
                thread[j].join();
            }
        }

        treeList.addAll(tempTree.values());
        numTree = treeList.size();
    }*/

    // without threads
    private void process2() {
        treeList = new ArrayList();
        tempTree = new HashMap();
        numBlock = 0;

//        if (morphoAnalysisMap.getMap() == null) {
//            return;
//        }


        MorphoAnalysis[][] pathArray = combine(morphoAnalysisMapList);
        for (MorphoAnalysisMap mam : morphoAnalysisMapList) {
            totalCartesianProduct += mam.getCartesianProduct();
        }


        int cc = 0;
        do {
            if (cc == pathArray.length) {
                break;
            }
            TreeGenerator tg = new TreeGenerator(analyzer, pathArray[cc], tempTree, blockLimit, cc);
            tg.run();
            cc++;

        } while (cc < pathArray.length);

        treeList.addAll(tempTree.values());
        numTree = treeList.size();
    }

    private MorphoAnalysis[][] combine(ArrayList<MorphoAnalysisMap> list) {
        ArrayList<MorphoAnalysis[]> lst = new ArrayList<>();
        for (MorphoAnalysisMap mam : list) {
            MorphoAnalysis[][] p = mam.getMap();
            try {
                lst.addAll(new ArrayList<>(Arrays.asList(p)));
            } catch (Exception e) {

            }
        }
        MorphoAnalysis[][] result = new MorphoAnalysis[lst.size()][];
        result = lst.toArray(result);
        return result;
    }

    public ArrayList<ArrayList<Clause>> makeClauseList() {
        clauseList = new ArrayList();
        for (Function f : treeList) {
            ArrayList<Clause> temp = new ArrayList();
            pickClause(f, temp);
            clauseList.add(temp);
        }
        return clauseList;
    }

    private ArrayList<Clause> pickClause(Function f, ArrayList<Clause> clauseList) {
        if (f instanceof Phrase) {
            pickClause((Function) ((Phrase) f).getHead(), clauseList);
            pickClause((Function) ((Phrase) f).getComp(), clauseList);
        } else if (f instanceof Inflected) {
            pickClause(((Inflected) f).getBase(), clauseList);
        } else if (f instanceof Clause) {
            Clause c = (Clause) f;
            clauseList.add(c);
            if (c.getSubject() != null) {
                pickClause(c.getSubject().getContent(), clauseList);
            }
            if (!c.getComplement().isEmpty()) {
                for (Complement comp : c.getComplement()) {
                    pickClause(comp.getContent(), clauseList);
                }
            }
            if (!c.getAdjunct().isEmpty()) {
                for (Adjunct a : c.getAdjunct()) {
                    pickClause(a.getContent(), clauseList);
                }
            }
        }
        return clauseList;
    }
}
