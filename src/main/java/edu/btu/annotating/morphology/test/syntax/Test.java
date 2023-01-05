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
package edu.btu.annotating.morphology.test.syntax;

import edu.btu.annotating.morphology.algo.MorphoAnalysis;
import edu.btu.annotating.morphology.structure.syntax.SyntaxAnalysis;
import edu.btu.annotating.morphology.structure.syntax.Unifier;
import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.setting.Settings;
import edu.btu.annotating.morphology.structure.syntax.structure.Clause;
import edu.btu.annotating.morphology.structure.syntax.structure.base.BaseMaker;
import edu.btu.annotating.morphology.algo.Analyzer;
import edu.btu.annotating.morphology.algo.Morpheme;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Test {

    static int blockLimit, cartesianLimit;

    public static void main(String[] args) throws InterruptedException {
        blockLimit = 10000;
        cartesianLimit = 10000;
        test1();
        //test2();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    public static void test2() throws InterruptedException {
        Analyzer analyzer = new Analyzer(1);
        Scanner scan = new Scanner(System.in, "UTF-8");
        Scanner scan2 = new Scanner(System.in, "UTF-8");
        Scanner scanSubject = new Scanner(System.in, "UTF-8");
        String input;
        mainLoop:
        do {
            System.out.print("sentence: ");
            input = scan.nextLine();
            System.out.println("");

            if (input.isEmpty()) {
                break;
            }

            System.out.print("subject category [1]=not exist [2]=exist: ");
            int sub = scanSubject.nextInt();
            System.out.println("");

            String[] token = input.split(" ");
            MorphoAnalysis[] path = new MorphoAnalysis[token.length];
            int tc = 0;
            for (String t : token) {
                System.out.println("token: " + t);
                MorphoAnalysis[] x = analyzer.getAnalysis(t);
                if (x.length == 1) {
                    path[tc] = x[0];
                    System.out.println("{" + x[0].getTotalTag() + "}");
                } else {
                    for (int i = 0; i < x.length; i++) {
                        System.out.println((i + 1) + ": " + x[i].toStringShort());
                    }
                    System.out.println("enter correct analysis:");
                    int c = scan2.nextInt();
                    if (c == 0) {
                        continue mainLoop;
                    }
                    path[tc] = x[c - 1];
                }
                tc++;
            }
            System.out.println("");
            int i = 0;
            System.out.println(input);
            for (MorphoAnalysis ma : path) {
                Morpheme[] mArr = ma.getMorpheme();
                for (Morpheme m : mArr) {
                    System.out.println(m.getType() + "\t" + m.getState() + "\t" + m.getTag() + "\t" + m.getSubcat() + "\t" + m.getLexical() + "\t" + m.getSurface() + "\t" + (++i) + "\t" + "_\t_\t_");
                }
            }
            System.out.println("");
            /*SyntaxAnalysis sa = new SyntaxAnalysis();
            long start = System.currentTimeMillis();
            sa.processSingle(path, blockLimit, 0);
            long elapsedTimeMillis = System.currentTimeMillis() - start;

            TreeMap<String, ArrayList<Function>> map = new TreeMap();
            Settings.printCode = 1;
            int count = 0;
            for (Function f : sa.getTreeList()) {
                ArrayList<Function> list = map.get(f.toString());
                if (list == null) {
                    list = new ArrayList();
                }
                list.add(f);
                if ((sub == 1 && ((Clause) f).getSubject() == null) || (sub == 2 && ((Clause) f).getSubject() != null)) {
                    map.put(f.toString(), list);
                    count++;
                }
            }

            for (ArrayList<Function> list : new ArrayList<>(map.values())) {
                //System.out.println(f + "\t" +((Clause)f).getPathNo());
                for (Function f : list) {
                    Settings.printCode = 1;
                    System.out.println(f); //chunk list

//                    int pathNo = ((Clause)f).getPathNo();
//                    path = mam.getMap()[pathNo];
//                    String temp = "";
//                    for (int i = 0; i < path.length; i++) {
//                        temp += BaseMaker.make(path[i].getStem()).getClass().getSimpleName() + ":" + path[i].getStem().getLexical() + ", ";
//                    }
//                    System.out.println(temp); //stem list
                    Settings.printCode = 0;
                    System.out.println(f); //syntax tree
                    System.out.println("***************************************************************");
                }
            }
            System.out.println("");
//            System.out.println("number of analysis path:\t" + mam.getCartesianProduct());

            ////
            SyntaxAnalysis sa2 = Unifier.unify(analyzer, input, 1, blockLimit, cartesianLimit);
            ////
            System.out.println("number of syntax tree..:\t" + sa2.getNumTree() *//*sa.getNumTree()*//*);
            System.out.println("reduced number of syntax tree..:\t" + count *//*sa.getNumTree()*//*);
            System.out.println("elapsed time...........:\t" + elapsedTimeMillis);
            System.out.println("");*/
        } while (!input.equals(""));
    }
    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    public static void test1() throws InterruptedException {
        Analyzer analyzer = new Analyzer(1);
        Scanner scan = new Scanner(System.in, "UTF-8");
        String input;
        do {
            System.out.print("sentence: ");
            input = scan.nextLine();
            System.out.println("");

            long start = System.currentTimeMillis();
            SyntaxAnalysis sa = Unifier.unify(analyzer, input, 1, blockLimit, cartesianLimit);
            long elapsedTimeMillis = System.currentTimeMillis() - start;

            TreeMap<String, ArrayList<Function>> map = new TreeMap();
            Settings.printCode = 1;
            for (Function f : sa.getTreeList()) {
                ArrayList<Function> list = map.get(f.toString());
                if (list == null) {
                    list = new ArrayList();
                }
                list.add(f);
                map.put(f.toString(), list);
            }
            Settings.printCode = 0;

            for (ArrayList<Function> list : new ArrayList<>(map.values())) {
                //System.out.println(f + "\t" +((Clause)f).getPathNo());
                for (Function f : list) {
                    Settings.printCode = 1;
                    System.out.println(f); //chunk list

                    if (f instanceof Clause) {
                        MorphoAnalysis[] path = ((Clause) f).getPath();
                        String temp = "";
                        for (int i = 0; i < path.length; i++) {
                            temp += BaseMaker.make(path[i].getStem()).getClass().getSimpleName() + ":" + path[i].getStem().getLexical() + ", ";
                        }
                        System.out.println(temp); //stem list
                    }

                    Settings.printCode = 0;
                    System.out.println(f); //syntax tree
                    System.out.println("***************************************************************");
                }
            }
            System.out.println("");
            System.out.println("number of morphoanalysis map:\t" + sa.getNumMam());
            System.out.println("number of analysis path.....:\t" + sa.getTotalCartesianProduct());
            System.out.println("number of syntax tree.......:\t" + sa.getNumTree());
            System.out.println("elapsed time................:\t" + elapsedTimeMillis);
            System.out.println("");
        } while (!input.equals(""));
    }
}
