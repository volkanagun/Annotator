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
package edu.btu.annotating.morphology.stemming;

import edu.btu.annotating.morphology.structure.lang.LowerCase;
import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;
import edu.btu.annotating.morphology.algo.Synthesizer;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class NaiveStemmer {
    private static Analyzer analyzer;
    private static Synthesizer synthesizer;
    protected static HashMap<String, Integer> weights = new HashMap<>();
    protected static List<String> apostropheList;

    public static void init(String freqsFileName) {
        getFreqs(freqsFileName);
        analyzer = new Analyzer(1);
        synthesizer = new Synthesizer();

        apostropheList = new ArrayList<>() {{
            add("\u0027");
            add("\u0060");
            add("\u00B4");
            add("\u2018");
            add("\u2019");
            add("\u0091");
            add("\u0092");
        }};
    }

    private static void getFreqs(String freqsFileName) {
        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(freqsFileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    weights.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (Exception e) {
            System.out.println("Exception is caught: " + e);
        }
    }

    public static Object[] morphoDisam(String token) {
        token = LowerCase.getLowercase(token);
        MorphoAnalysis[] analyses = analyzer.getAnalysis(token);
        if (analyses.length == 0) {
            return null;
        } else {

            //////
            int c = 0, f = 0, fi = 0;
            for (MorphoAnalysis ma : analyses) {
                Integer w = weights.get(ma.getStem().getSurface());
                if (w != null && w > f) {
                    f = w;
                    fi = c;
                }
                c++;
            }
            //////

            return new Object[]{analyses, fi};
        }
    }

    public static String getPOS(String token) {
        token = LowerCase.getLowercase(token);
        String[] arr = processApostrophe(token);
        if (!arr[0].equals(arr[1])) {
            return "?";
        }
        MorphoAnalysis[] analyses = analyzer.getAnalysis(token);
        if (analyses.length == 0) {
            return null;
        }
        int c = 0, f = 0, fi = 0;
        for (MorphoAnalysis ma : analyses) {
            Integer w = weights.get(ma.getStem().getSurface());
            if (w != null && w > f) {
                f = w;
                fi = c;
            }
            c++;
        }
        return analyses[fi].getPos().getMajor();
    }

    public static String getStem(String token, boolean surfaceFormMode) {
        token = LowerCase.getLowercase(token);
        String[] arr = processApostrophe(token);
        if (!arr[0].equals(arr[1])) {
            return arr[1];
        }
        MorphoAnalysis[] analyses = analyzer.getAnalysis(token);
        if (analyses.length == 0) {
            return null;
        }
        int c = 0, f = 0, fi = 0;
        for (MorphoAnalysis ma : analyses) {
            Integer w = weights.get(ma.getStem().getSurface());
            if (w != null && w > f) {
                f = w;
                fi = c;
            }
            c++;
        }
        if (surfaceFormMode) {
            return clear(synthesizer.synthesis(analyses[fi].getStem().getLexical().toCharArray(), "?",'\t'), '\t');
        } else {
            return analyses[fi].getStem().getLexical();
        }
    }

    public static String getStems(String sentence, boolean surfaceFormMode) {
        String[] tokens = sentence.split(" ");
        String temp = "";
        for (String token : tokens) {
            token = LowerCase.getLowercase(token);
            String[] arr = processApostrophe(token);
            if (!arr[0].equals(arr[1])) {
                temp += arr[1] + " ";
                continue;
            }
            MorphoAnalysis[] analyses = analyzer.getAnalysis(token);
            if (analyses.length == 0) {
                temp += token + " ";
            } else {

                //////
                int c = 0, f = 0, fi = 0;
                for (MorphoAnalysis ma : analyses) {
                    Integer w = weights.get(ma.getStem().getSurface());
                    if (w != null && w > f) {
                        f = w;
                        fi = c;
                    }
                    c++;
                }
                //////

                //temp += analysis.getStem().getLexical() + " ";
                if (surfaceFormMode) {
                    temp += clear(synthesizer.synthesis(analyses[fi].getStem().getLexical().toCharArray(), "?",'\t'), '\t') + " ";
                } else {
                    temp += analyses[fi].getStem().getLexical() + " ";
                }
            }
        }
        return temp.substring(0, temp.length() - 1);
    }

    private static String[] processApostrophe(String s) {
        if (s.length() < 3) return new String[]{s, s};
        String center = s.substring(1, s.length() - 1);
        String reduced = "";
        int count = 0, index = -1;
        for (int i = 0; i < center.length(); i++) {
            if (apostropheList.contains(center.substring(i, i + 1))) {
                count++;
                index = i;
            } else {
                reduced += center.charAt(i);
            }
        }
        if (count != 1) return new String[]{s, s};
        String reduced_ = s.charAt(0) + reduced + s.charAt(s.length() - 1);
        String trimmed = s.substring(0, index + 1);
        return new String[]{reduced_, trimmed};
    }

    private static String clear(String s, char separator) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) != '0' && s.charAt(i) != separator)
                buf.append(s.charAt(i));
        return buf.toString();
    }
}