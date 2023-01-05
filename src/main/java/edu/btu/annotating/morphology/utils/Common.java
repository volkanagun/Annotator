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
package edu.btu.annotating.morphology.utils;

import java.io.*;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Common {
    // adapted
    public static int countLinesNew(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i = 0; i < 1024; ) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                //System.out.println(readChars);
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        } finally {
            is.close();
        }
    }

    public static int countLinesNew(File folder) throws IOException {
        if (!folder.isDirectory()) {
            return 0;
        }

        int c = 0;
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            c += countLinesNew(file.toString());
        }
        return c;
    }

    public static void printNS(int n, String s) {
        for (int i = 0; i < n; i++) {
            System.out.print(s);
        }
        System.out.print("\n");
    }

    public static boolean printPlus(long cc, long quanta, int p) {
        int x = (int) (cc / quanta);
        if (x >= p + 1) {
            System.out.print("+");
            return true;
        }
        return false;
    }

    public static void printPlus(long cc, long quanta) {
        if (cc / quanta <= 100 && cc % quanta == 0) {
            System.out.print("+");
        }
    }
}