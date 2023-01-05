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

import java.io.*;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class TestNaiveStemmer {

    public static void main(String[] args) {
        NaiveStemmer.init("GiTuCo.filt2.uniq.shuf.stemF.format_F.frq");

        String inputFileName = "GiTuCo_sample1m";
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFileName));
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputFileName + ".stem"), "UTF-8"));
            String sentence;
            while ((sentence = br.readLine()) != null) {
                out.write(NaiveStemmer.getStems(sentence, true) + "\n");
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Exception is caught: " + e);
        }
    }
}
