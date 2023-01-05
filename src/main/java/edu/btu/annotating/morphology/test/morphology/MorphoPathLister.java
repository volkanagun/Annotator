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

import edu.btu.annotating.morphology.structure.syntax.MorphoAnalysisMap;
import edu.btu.annotating.morphology.structure.syntax.Unifier;
import edu.btu.annotating.morphology.utils.FileReader;
import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.algo.Morpheme;
import edu.btu.annotating.morphology.algo.MorphoAnalysis;

import java.util.ArrayList;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class MorphoPathLister {
    static int blockLimit, cartesianLimit;

    public static void main(String[] args) throws InterruptedException {
        blockLimit = 10000;
        cartesianLimit = 10000;
        Analyzer analyzer = new Analyzer(1);
        ArrayList<String> sentenceList = FileReader.read("_data\\corpus\\huge_\\huge_20_64_5000_numTree[1,x]_sentList.txt");
        for (String sentence : sentenceList) {
            ArrayList<MorphoAnalysisMap> mapList;
            try {
                mapList = Unifier.unify2(analyzer, sentence, cartesianLimit);
            } catch (Exception e) {
                continue;
            }
            for (MorphoAnalysisMap map : mapList) {
                MorphoAnalysis[][] pathArray = map.getMap();
                if (pathArray != null) {
                    for (MorphoAnalysis[] path : pathArray) {
                        int i = 0;
                        for (MorphoAnalysis ma : path) {
                            Morpheme[] mArr = ma.getMorpheme();
                            for (Morpheme m : mArr) {
                                System.out.println(m.getType() + "\t" + m.getState() + "\t" + m.getTag() + "\t" + m.getSubcat() + "\t" + m.getLexical() + "\t" + m.getSurface() + "\t" + (++i) + "\t" + "_\t_\t_");
                            }
                        }
                        System.out.println("");
                    }
                }
            }
        }
    }
}
