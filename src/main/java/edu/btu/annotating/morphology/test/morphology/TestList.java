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
package edu.btu.annotating.morphology.test.morphology;

import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class TestList {
    private static ArrayList<String> sentence;
    private static int sortMode = 1;

    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer(sortMode);
        readSentence("_data\\big1.txt");

        ArrayList<String> problematicToken = new ArrayList();
        int countToken = 0, countAnalysedToken = 0, countSentence = 0;
        long start = System.currentTimeMillis();
        for (String sent : sentence) {
            String[] token = sent.split(" ");
            for (String t : token) {
                MorphoAnalysis[] analysis = null;
                try {
                    analysis = analyzer.getAnalysis(t);
                } catch (Exception e) {
                    //
                }
                if (analysis.length == 0) {
                    problematicToken.add(t);
                }
                if (analysis != null && analysis.length > 0) {
                    countAnalysedToken++;
                }
                countToken++;
            }
            System.out.println(++countSentence);
        }
        long elapsedTimeMillis = System.currentTimeMillis() - start;

        try {
            FileWriter fstream = new FileWriter("_data\\morphology_test_big1_problematic_tokens");
            BufferedWriter out = new BufferedWriter(fstream);

            for (String s : problematicToken) {
                out.write(s + "\n");
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("#token:\t\t" + countToken);
        System.out.println("#analysed token:\t\t" + countAnalysedToken);
        System.out.println("#sentence:\t\t" + countSentence);
        System.out.println("elapsed time (ms):\t\t" + elapsedTimeMillis);
    }

    private static void readSentence(String path) {
        sentence = new ArrayList();

        try {
            FileInputStream fstream = new FileInputStream(path);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                sentence.add(strLine);
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}
