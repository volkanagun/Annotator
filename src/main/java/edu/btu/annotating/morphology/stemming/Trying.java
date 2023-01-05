package edu.btu.annotating.morphology.stemming;

import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Trying {
    private static HashMap<String, double[]> mapWordVector1, mapWordVector2;
    private static int wv_vectorSize, window;
    private static ArrayList<String> data = new ArrayList<>();
    protected static final String LOWERCASE = "abcçdefgğhıijklmnoöprsştuüvyzâîôû";
    protected static final String UPPERCASE = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZÂÎÔÛ";

    public static void main(String[] args) {
        String vecFile1 = "C:\\Users\\ozkan\\Desktop\\vector\\GiTuCo.filt2.uniq.shuf.samp61m_w5_v100_mc10_i5_sg.w2v.vec";
        String vecFile2 = "C:\\Users\\ozkan\\Desktop\\vector\\GiTuCo.filt2.uniq.shuf.stemF.format_F.samp65m_w5_v100_mc10_i5_sg.w2v.vec";
        String corpusFile = "C:\\Users\\ozkan\\Desktop\\vector\\GiTuCo_sample1m";
        wv_vectorSize = 100;
        window = 2;
        Analyzer analyzer = new Analyzer(1);

        mapWordVector1 = readWordVectors(new File(vecFile1));
        mapWordVector2 = readWordVectors(new File(vecFile2));
        readData(corpusFile);

        HashMap<String, ArrayList<Double>> map1 = new HashMap<>();
        HashMap<String, ArrayList<Double>> map2 = new HashMap<>();
        int line = 0;
        for (String sentence : data) {
            System.out.println(++line);
            String[] tokens = sentence.split(" ");
            for (int i = 0; i < tokens.length; i++) {
                String t1 = getLowercase(tokens[i]);
                double sum = 0.0;
                int c = 0;
                for (int j = i - window; j <= i + window; j++) {
                    if (i == j || j < 0 || j >= tokens.length) continue;
                    String t2 = getLowercase(tokens[j]);
                    double sim = getCosineSimilarity(mapWordVector1.get(t1), mapWordVector1.get(t2));
                    sum += sim;
                    c++;
                }
                ArrayList<Double> temp = map1.get(t1);
                if (temp == null) temp = new ArrayList<>();
                temp.add((double) sum / c);
                map1.put(t1, temp);

            }
        }

        for (String sentence : data) {
            String[] tokens = sentence.split(" ");
            for (int i = 0; i < tokens.length; i++) {
                String t1 = getLowercase(tokens[i]);
                MorphoAnalysis[] analyses = analyzer.getAnalysis(t1);
            }
        }

        System.out.println();
    }

    private static HashMap<String, double[]> readWordVectors(File file) {
        HashMap<String, double[]> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int c = 0;
            loop:
            while ((line = br.readLine()) != null) {
                String[] part = line.split("\t");
                if (c == 0) wv_vectorSize = part.length - 1;
                String word = part[0];
                double[] vec = new double[wv_vectorSize];
                for (int i = 1; i < part.length; i++) {
                    try {
                        vec[i - 1] = Double.valueOf(part[i]);
                    } catch (Exception e) {
                        continue loop;
                    }
                }
                map.put(word, vec);
                c++;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
        return map;
    }

    private static void readData(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (Exception e) {
            System.out.println("Exception is caught: " + e);
        }
    }

    public static double getCosineSimilarity(double[] v1, double[] v2) {
        if (v1 == null || v2 == null || v1.length != v2.length || v1.length == 0) return 0.0;
        double sum1 = 0.0, sum2 = 0.0, sum3 = 0.0;
        int vectorSize = v1.length;
        for (int i = 0; i < vectorSize; i++) {
            sum1 += v1[i] * v2[i];
            sum2 += v1[i] * v1[i];
            sum3 += v2[i] * v2[i];
        }
        return sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
    }

    private static String getLowercase(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            int posL = LOWERCASE.indexOf(c);
            int posU = UPPERCASE.indexOf(c);
            if (posL > -1) {
                result += c;
            } else if (posU > -1) {
                result += LOWERCASE.charAt(posU);
            } else {
                result += c;
            }
        }
        return result;
    }
}