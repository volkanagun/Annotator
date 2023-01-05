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

import java.util.Scanner;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Test2NaiveStemmer {
    public static void main(String[] args) {
        NaiveStemmer.init("G:\\My Drive\\_sync\\_code_archive\\java\\idea\\tubitak-3501\\_io\\GiTuCo.filt2.uniq.shuf.stemF.format_F.frq");

        Scanner scan = new Scanner(System.in);
        String input = "";
        do {
            System.out.print("Enter the word: ");
            input = scan.nextLine();
            System.out.println(NaiveStemmer.getStem(input, true));
            System.out.println(NaiveStemmer.morphoDisam(input)[0] + "\t" + NaiveStemmer.morphoDisam(input)[1]);
        } while(!input.isEmpty());
    }
}