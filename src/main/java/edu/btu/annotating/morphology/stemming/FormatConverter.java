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

import edu.btu.annotating.morphology.utils.Common;

import java.io.*;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class FormatConverter {

    private static boolean modeWordForm, modeStem, modeStemSurface, modeMorphemeSequence, modeTagSequence, modeMorphologicalCase;
    private static String itemsToRemove = "", itemsToLabel = "";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: stemming.FormatConverter <fileNameToRead> <code>");
            System.exit(1);
        }

        String fileNameToRead = args[0];
        String code = args[1];
        String fileNameToWrite = fileNameToRead + ".format_" + code;
        processCode(code);

        // result file checking
        File tempFile = new File(fileNameToWrite);
        if (tempFile.exists()) {
            System.out.println("\nWarning: The result file \"" + fileNameToWrite + "\" is exist!");
            System.exit(1);
        }
        //

        System.out.println();

        long ln = new File(fileNameToRead).length();
        long quanta = ln / 100;
        if (quanta == 0) quanta = 1;
        Common.printNS(100, "|");
        long cc = 0;
        int p = 0;
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNameToWrite), "UTF-8"));
            BufferedReader br = new BufferedReader(new FileReader(fileNameToRead));
            String line;
            while ((line = br.readLine()) != null) {

                String result = process(line.split("  "));
                if (!result.isEmpty()) {
                    out.write(result + "\n");
                }

                cc += line.length() + 1;
                if (Common.printPlus(cc, quanta, p)) {
                    p++;
                }

            }
            br.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
        System.out.println();
    }

    private static void processCode(String code) {
        String[] arr = code.split("_");

        boolean foundL = false;
        for (String s : arr) {
            if (s.equals("W")) modeWordForm = true;
            else if (s.equals("S")) modeStem = true;
            else if (s.equals("R")) modeStemSurface = true;
            else if (s.equals("M")) modeMorphemeSequence = true;
            else if (s.equals("T")) modeTagSequence = true;
            else if (s.equals("C")) modeMorphologicalCase = true;
            else if (s.startsWith("L")) {
                foundL = true;
                itemsToLabel = s.substring(1);
            } else if (s.startsWith("R")) {
                itemsToRemove = s.substring(1);
            }
        }

        if (itemsToLabel.isEmpty() && foundL) itemsToLabel = "!";
    }

    private static String process(String[] arr) {
        // gerçekleşirse gerçekleş V gerçekleş/Xr/YsA V+PROC/AOR+PER3S/V'+COND
        String result = "";
        for (String s : arr) {
            String[] parts = s.split(" ");

            String wForm = parts[0];
            String stemSurface = parts[1];
            String stem = parts[2];
            String label = getPOSlabel(parts[3]);
            String mCase = getMCaseCode(parts[5]);
            String morphemeList = parts[4];
            String tagList = parts[5];

            if (itemsToRemove.contains(label)) continue;
            if (modeMorphologicalCase && ("pan".contains(label))) label = mCase;
            if (itemsToLabel.equals("!") || itemsToLabel.contains(label)) stem += ":" + label;

            String temp = "";
            if (modeWordForm) temp += wForm + "\t";
            if (modeStem) temp += stem + "\t";
            if (modeStemSurface) temp += stemSurface + "\t";
            if (modeMorphemeSequence) temp += morphemeList + "\t";
            if (modeTagSequence) temp += tagList + "\t";
            temp = temp.substring(0, temp.length() -1);
            result += temp + " ";
        }
        return result;
    }

    private static String getMCase(String tagSeq) {
        String[] tagArr = tagSeq.split("/");
        for (int i = tagArr.length - 1; i >= 0; i--) {
            if (tagArr[i].equals("ACC") || tagArr[i].equals("DAT") || tagArr[i].equals("ABL") || tagArr[i].equals("ILE") ||
                    tagArr[i].equals("INS") || tagArr[i].equals("LOC") || tagArr[i].equals("GEN") || tagArr[i].equals("EQU")) {
                return tagArr[i];
            }
        }
        return "NOM";
    }

    private static String getPOSlabel(String POS) {
        switch (POS) {
            case "-":
                return "u";
            case "V":
                return "v";
            case "N":
                return "n";
            case "Adj":
                return "a";
            case "Adv":
                return "d";
            case "Pro":
                return "p";
            case "Cnj":
                return "c";
            case "Pp":
                return "s";
            default:
                return "o";
        }
    }

    private static String getMCaseCode(String tagSeq) {
        String mCase = getMCase(tagSeq);
        switch (mCase) {
            case "NOM":
                return "0";
            case "ACC":
                return "1";
            case "DAT":
                return "2";
            case "ABL":
                return "3";
            case "ILE":
            case "INS":
                return "4";
            case "LOC":
                return "5";
            case "GEN":
                return "6";
            case "EQU":
                return "7";
            default:
                return "?";
        }
    }
}