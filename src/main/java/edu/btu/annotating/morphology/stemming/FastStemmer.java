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
import edu.btu.annotating.morphology.utils.Basics;
import edu.btu.annotating.morphology.utils.Common;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class FastStemmer {

    protected static HashMap<String, Integer> weights;
    protected static Set<String> setToken;
    protected static Map<String, String> mapTokenAnalysis;
    protected static List<String> apostropheList;
    protected static String sortCrit;
    private static final String numbers = "0123456789";

    public static void main(String[] args) throws IOException, InterruptedException {
        long timeStart = System.currentTimeMillis();

        if (args.length != 4) {
            warning();
            System.exit(1);
        }

        String fileNameToRead = args[0];
        sortCrit = args[1];
        int numThread = Integer.parseInt(args[2]);
        String fileNameToWrite = fileNameToRead + ".stem" + sortCrit;
        setToken = Collections.synchronizedSet(new HashSet<>());
        mapTokenAnalysis = Collections.synchronizedMap(new HashMap<>());
        apostropheList = new ArrayList<>() {{
            add("\u0027");
            add("\u0060");
            add("\u00B4");
            add("\u2018");
            add("\u2019");
            add("\u0091"); // new added
            add("\u0092"); // new added
        }};

        ////////////////////////////////////////
        weights = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[3]));
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
        ////////////////////////////////////////

        // result file checking
//        File tempFile = new File(fileNameToWrite);
//        if (tempFile.exists()) {
//            System.out.println("\nWarning: The result file \"" + fileNameToWrite + "\" is exist!");
//            System.exit(1);
//        }
        //

        // sorting criterion checking
        if (!sortCrit.equals("B") && !sortCrit.equals("S") && !sortCrit.equals("F")) {
            warning();
            System.exit(1);
        }

        System.out.println();

        long ln = Basics.countLinesNew(fileNameToRead);
        {
            System.out.println("Indexing...");
            long quanta = ln / 100;
            if (quanta == 0) quanta = 1;
            Common.printNS(100, "|");

            //////////////////////////////////////////////////////////////////////////////////////
            Thread threads[] = new Thread[numThread];
            long qt = ln / numThread;
            long end;
            for (int i = 0; i < numThread; i++) {
                if (i == numThread - 1) end = ln;
                else end = (i + 1) * qt;
                threads[i] = new Thread(new FastStemmer().new NaiveStemmerIndexer(i * qt, end, quanta, fileNameToRead));
                threads[i].start();
            }

            for (int i = 0; i < numThread; i++) {
                threads[i].join();
            }
            //////////////////////////////////////////////////////////////////////////////////////

            System.out.println("\n");
        }

        System.out.println("Vocabulary size: " + setToken.size() + "\n");

        {
            System.out.println("Stemming...");
            int quanta = setToken.size() / 100;
            if (quanta == 0) quanta = 1;
            Common.printNS(100, "|");

            //////////////////////////////////////////////////////////////////////////////////////
            Thread threads[] = new Thread[numThread];
            int qt = setToken.size() / numThread;
            int end;
            for (int i = 0; i < numThread; i++) {
                if (i == numThread - 1) end = setToken.size();
                else end = (i + 1) * qt;
                threads[i] = new Thread(new FastStemmer().new NaiveStemmerProcessor(i * qt, end, quanta, new Analyzer(1), new Synthesizer()));
                threads[i].start();
                threads[i].join();
            }

            for (int i = 0; i < numThread; i++) {
                threads[i].join();
            }
            //////////////////////////////////////////////////////////////////////////////////////

            System.out.println("\n");
        }

        {
            System.out.println("Writing...");
            long quanta = ln / 100;
            if (quanta == 0) quanta = 1;
            Common.printNS(100, "|");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNameToWrite), "UTF-8"));
            //////////////////////////////////////////////////////////////////////////////////////
            Thread threads[] = new Thread[numThread];
            long qt = ln / numThread;
            long end;
            for (int i = 0; i < numThread; i++) {
                if (i == numThread - 1) end = ln;
                else end = (i + 1) * qt;
                threads[i] = new Thread(new FastStemmer().new NaiveStemmerFinalizer(i * qt, end, quanta, fileNameToRead, out, mapTokenAnalysis));
                threads[i].start();
            }

            for (int i = 0; i < numThread; i++) {
                threads[i].join();
            }
            //////////////////////////////////////////////////////////////////////////////////////
            out.close();

            System.out.println("");
        }

        long timeElapsed = System.currentTimeMillis() - timeStart;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed);
        if (minutes > 0) {
            System.out.println("\nElapsed time: " + minutes + " minutes.");
        } else {
            System.out.println("\nElapsed time: " + seconds + " seconds.");
        }
    }

    private static void warning() {
        System.out.println("Usage: stemming.NaiveStemmer <fileNameToRead> <sorting criterion [B]iggest stem / [S]mallest stem / [F]requent stem> <number of threads> <fileNameFreqs>");
    }

    class NaiveStemmerIndexer implements Runnable {

        private long start, end, quanta;
        private String fileNameToRead;

        public NaiveStemmerIndexer(long start, long end, long quanta, String fileNameToRead) {
            this.start = start;
            this.end = end;
            this.quanta = quanta;
            this.fileNameToRead = fileNameToRead;
        }

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileNameToRead));
                String line;
                long cc = 0, i = -1;
                while ((line = br.readLine()) != null) {
                    i++;
                    if (i >= start && i < end) {
                        String[] tokens = line.split(" ");
                        for (String token : tokens) {
                            FastStemmer.setToken.add(token);
                        }

                        cc++;
                        Common.printPlus(cc, quanta);
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception is caught: " + e);
                System.exit(1);
            }
        }
    }

    class NaiveStemmerProcessor implements Runnable {
        private int start, end, quanta;
        private Analyzer analyzer;
        private Synthesizer synthesizer;

        public NaiveStemmerProcessor(int start, int end, int quanta, Analyzer analyzer, Synthesizer synthesizer) {
            this.start = start;
            this.end = end;
            this.quanta = quanta;
            this.analyzer = analyzer;
            this.synthesizer = synthesizer;
        }

        @Override
        public void run() {
            int cc = 0, i = -1;
            Iterator iter = FastStemmer.setToken.iterator();
            while (iter.hasNext()) {
                String token = (String) iter.next();
                i++;
                if (i >= start && i < end) {
                    String processedToken = token;

                    processedToken = processAccents(processedToken);
                    processedToken = LowerCase.getLowercase(processedToken);
                    processedToken = clean(processedToken);

                    String[] arr = processApostrophe(processedToken);
                    processedToken = arr[0];

                    String result = "";

                    if (!arr[0].equals(arr[1])) {
                        result = token + " " + arr[1] + " " + arr[1] + " - - -";
                    } else {
                        MorphoAnalysis[] analysisArr = manageExceptions(analyzer.getAnalysis(processedToken));
                        if (analysisArr.length == 0) {
                            result = token + " " + arr[1] + " " + arr[1] + " - - -";
                        } else {
                            MorphoAnalysis a = null;
                            if (FastStemmer.sortCrit.equals("B")) {
                                // most biggest stem
                                a = analysisArr[0];
                            } else if (FastStemmer.sortCrit.equals("S")) {
                                // most smallest stem
                                a = analysisArr[analysisArr.length - 1];
                            } else if (FastStemmer.sortCrit.equals("F")) {
                                int c = 0, f = 0, fi = 0;
                                for (MorphoAnalysis ma : analysisArr) {
                                    Integer w = weights.get(ma.getStem().getLexical());
                                    if (w != null && w > f) {
                                        f = w;
                                        fi = c;
                                    }
                                    c++;
                                }
                                a = analysisArr[fi];
                            }
                            result = token + " " + clear(synthesizer.synthesis(a.getStem().getLexical().toCharArray(), "?",'\t'), '\t') + " " + a.getStem().getLexical() + " " + a.getStem().getPos().getMajor() + " " + a.getMorphemesLexical() + " " + a.getMorphemesTag();
                        }
                    }
                    FastStemmer.mapTokenAnalysis.put(token, result);

                    cc++;
                    Common.printPlus(cc, quanta);

                    //System.out.println(result);
                }
                if (i == end) {
                    break;
                }
            }
        }

        private String clear(String s, char separator) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < s.length(); i++)
                if (s.charAt(i) != '0' && s.charAt(i) != separator)
                    buf.append(s.charAt(i));
            return buf.toString();
        }

        private String[] processApostrophe(String s) {
            if (s.length() < 3) return new String[]{s, s};
            String center = s.substring(1, s.length() - 1);
            String reduced = "";
            int count = 0, index = -1;
            for (int i = 0; i < center.length(); i++) {
                if (FastStemmer.apostropheList.contains(center.substring(i, i + 1))) {
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

        private MorphoAnalysis[] filter(MorphoAnalysis[] analysisArr) {
            ArrayList<MorphoAnalysis> list = new ArrayList<>();
            for (MorphoAnalysis analysis : analysisArr) {
                if (!analysis.getMorphemesLexical().contains("/lE/") && !analysis.getMorphemesLexical().endsWith("/lE")) {
                    list.add(analysis);
                }
            }
            return list.toArray(new MorphoAnalysis[list.size()]);
        }

        private String processAccents(String s) {
            StringBuilder result = new StringBuilder();
            for (char c : s.toCharArray()) {
                switch (c) {
                    case 'â':
                        result.append('a');
                        break;
                    case 'î':
                        result.append('i');
                        break;
                    case 'ô':
                        result.append('o');
                        break;
                    case 'û':
                        result.append('u');
                        break;
                    default:
                        result.append(c);
                        break;
                }
            }
            return result.toString();
        }

        private String clean(String s) {
            String result = s;
            if (FastStemmer.apostropheList.contains(result.substring(0, 1))) {
                result = result.substring(1);
            }
            if (FastStemmer.apostropheList.contains(result.substring(result.length() - 1))) {
                result = result.substring(0, s.length() - 1);
            }
            return result;
        }

        private MorphoAnalysis[] manageExceptions(MorphoAnalysis[] analysisArr) {
            MorphoAnalysis[] result = new MorphoAnalysis[1];
            for (MorphoAnalysis analysis : analysisArr) {
                if (analysis.search("lAr~PLU")) {
                    result[0] = analysis;
                    break;
                }
                if (analysis.search("de~Cnj")) {
                    result[0] = analysis;
                    break;
                }
                if (analysis.search("ki~REL")) {
                    result[0] = analysis;
                    break;
                }
            }
            if (result[0] == null) {
                return analysisArr;
            }
            return result;
        }
    }

    class NaiveStemmerFinalizer implements Runnable {
        private long start, end, quanta;
        private String fileNameToRead;
        private Writer out;
        private Map<String, String> mapTokenAnalysis;

        public NaiveStemmerFinalizer(long start, long end, long quanta, String fileNameToRead, Writer out, Map<String, String> mapTokenAnalysis) {
            this.start = start;
            this.end = end;
            this.quanta = quanta;
            this.fileNameToRead = fileNameToRead;
            this.out = out;
            this.mapTokenAnalysis = mapTokenAnalysis;
        }

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileNameToRead));
                long cc = 0, i = -1;
                String line;
                while ((line = br.readLine()) != null) {
                    i++;
                    if (i >= start && i < end) {
                        String[] tokenArr = line.split(" ");
                        String temp = "";
                        for (int j = 0; j < tokenArr.length; j++) {
                            temp += mapTokenAnalysis.get(tokenArr[j]) + "  ";
                        }
                        out.write(temp + "\n");

                        cc++;
                        Common.printPlus(cc, quanta);
                    }
                }
                br.close();
            } catch (Exception e) {
                System.out.println("Exception is caught: " + e);
                System.exit(1);
            }
        }
    }
}