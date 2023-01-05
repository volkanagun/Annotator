package edu.btu.annotating.morphology.test.syntax;
//
//import disambiguation.SyntaxDisam;
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.Set;
//import languagemodel.LanguageModel;
//import morphology.algo.Analyzer;
//
///**
// *
// * @author ozkan
// */
//public class TestCompareSentences {
//
//    private static SyntaxDisam sd;
//
//    public static void main(String[] args) throws Exception {
//        methodLanguageModel();
//        //methodSyntaxDisam;
//    }
//
//    private static void methodSyntaxDisam() throws Exception {
//        Analyzer analyzer = new Analyzer(1);
//        LanguageModel lm = new LanguageModel(analyzer, 12, 200000, ";", 3);
//        lm.readMap("_model\\lm\\language_model_big1");
//
//        sd = new SyntaxDisam(12, 5000, analyzer, lm);
//        sd.readMapsFromfile("_model\\sd_model_big1");
//
//        Scanner scan = new Scanner(System.in, "UTF-8");
//        String input;
//        ArrayList<String> list = new ArrayList();
//        do {
//            System.out.print("please enter to read sentences to compare");
//            input = scan.nextLine();
//
//            if (input.equals("")) {
//                list = readFile("_sentences.txt");
//                LinkedHashMap<String, Double> result = sd.testList(list);
//                Set keySet = result.keySet();
//                Iterator it = keySet.iterator();
//                while (it.hasNext()) {
//                    String key = (String) it.next();
//                    Double value = result.get(key);
//                    System.out.println(key + "\t" + value);
//                }
//                list = new ArrayList();
//                System.out.println("");
//            }
//
//        } while (!input.equals("."));
//    }
//
//    private static void methodLanguageModel() throws Exception {
//        Analyzer analyzer = new Analyzer(1);
//        LanguageModel lm = new LanguageModel(analyzer, 12, 200000, ";", 3);
//        lm.readMap("_model\\lm\\language_model_big1");
//        sd = new SyntaxDisam(12, 5000, analyzer, lm);
//        sd.readMapsFromfile("_model\\sd_model_big1");
//
//        Scanner scan = new Scanner(System.in, "UTF-8");
//        String input;
//        ArrayList<String> list = new ArrayList();
//        do {
//            System.out.print("please enter to read sentences to compare");
//            input = scan.nextLine();
//
//            if (input.equals("")) {
//                list = readFile("_sentences.txt");
//
//                LinkedHashMap<String, Double> result = new LinkedHashMap();
//                for (String sent : list) {
//                    lm.test(sent);
//                    result.put(sent, lm.getProb("token", 0));
//                }
//                LinkedHashMap<String, Double> sorted = sortByValues(result);
//
//                Set keySet = sorted.keySet();
//                Iterator it = keySet.iterator();
//                while (it.hasNext()) {
//                    String key = (String) it.next();
//                    Double value = sorted.get(key);
//                    System.out.println(key + "\t" + value);
//                }
//                list = new ArrayList();
//                System.out.println("");
//            }
//
//        } while (!input.equals("."));
//    }
//
//    private static LinkedHashMap sortByValues(HashMap map) {
//        List list = new LinkedList(map.entrySet());
//        // Defined Custom Comparator here
//        Collections.sort(list, new Comparator() {
//            public int compare(Object o1, Object o2) {
//                return ((Comparable) ((Map.Entry) (o2)).getValue())
//                        .compareTo(((Map.Entry) (o1)).getValue());
//            }
//        });
//
//        // Here I am copying the sorted list in HashMap
//        // using LinkedHashMap to preserve the insertion order
//        LinkedHashMap sortedHashMap = new LinkedHashMap();
//        for (Iterator it = list.iterator(); it.hasNext();) {
//            Map.Entry entry = (Map.Entry) it.next();
//            sortedHashMap.put(entry.getKey(), entry.getValue());
//        }
//        return sortedHashMap;
//    }
//
//    private static ArrayList<String> readFile(String path) {
//        ArrayList<String> list = new ArrayList();
//
//        try {
//            FileInputStream fstream = new FileInputStream(path);
//            DataInputStream in = new DataInputStream(fstream);
//            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//            String strLine;
//
//            while ((strLine = br.readLine()) != null) {
//                list.add(strLine);
//            }
//            in.close();
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }
//        return list;
//    }
//}
