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
package edu.btu.annotating.morphology.structure.lang;

import edu.btu.annotating.morphology.structure.LexicalForm;
import edu.btu.annotating.morphology.structure.State;
import edu.btu.annotating.morphology.structure.SurfaceForm;
import edu.btu.annotating.morphology.algo.MorphoTag;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class SuffixReader {
    private final static String SURFACE_FORM_DELIM = "/";

    public ArrayList<SuffixEntry> read(String fileName) {
        ArrayList<SuffixEntry> result = new ArrayList();
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                // Ar	ar/er	fact1	FACT	Factitive
                String[] part = strLine.split("\t");
                result.add(new SuffixEntry(new LexicalForm(part[0]), new SurfaceForm(getItems(part[1])), new State(part[2]), new MorphoTag(part[3]), part[4]));
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error-Reader: " + e.getMessage());
        }
        return result;
    }

    private String[] getItems(String s) {
        return s.split(SURFACE_FORM_DELIM);
    }
}
