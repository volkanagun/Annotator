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

import java.util.Scanner;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class SyntaxTreeSimilarityTry {

    public static void main(String[] args) {
        Scanner scan1 = new Scanner(System.in, "UTF-8");
        Scanner scan2 = new Scanner(System.in, "UTF-8");
        String gold, input;

        System.out.print("gold syntax tree: ");
        gold = scan1.nextLine();
        do {
            System.out.println("");
            System.out.print("other syntax tree: ");
            input = scan2.nextLine();
            System.out.println(distanceLevenshtein(gold, input));
            System.out.println("");
        } while (!input.equals(""));
    }

    public static int distanceLevenshtein(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}
