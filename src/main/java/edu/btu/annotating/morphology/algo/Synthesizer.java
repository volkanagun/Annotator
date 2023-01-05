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
package edu.btu.annotating.morphology.algo;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Synthesizer {

    private final static String SYMBOL = "0+abcçdefgğhıijklmnoöprsştuüvyzACÇDEGĞHIKLNPSŞTUÜYZâîôû$&%-J,;!?.()*|…~X ";
    private final static String SURFACEVOCAL = "aeıioöuüâîôû";
    protected final static String LOWERCASE = "abcçdefgğhıijklmnoöprsştuüvyzâîôû";
    protected final static String UPPERCASE = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZÂÎÔÛ";
    private final static String DEFAULT[] = {
            "00", "+0", "aa", "bb", "cc", "çç", "dd", "ee", "ff", "gg",
            "ğğ", "hh", "ıı", "ii", "jj", "kk", "ll", "mm", "nn", "oo",
            "öö", "pp", "rr", "ss", "şş", "tt", "uu", "üü", "vv", "yy",
            "zz", "Aa", "Cc", "Çç", "Dd", "Ee", "Gk", "Ğg", "Hı", "Iı",
            "Kk", "Ll", "N0", "Pp", "S0", "Şş", "Tt", "U0", "Ü0", "Y0",
            "Z0", "âa", "îi", "ôo", "ûu", "$0", "&0", "%0", "-0", "Js", ",,", ";;", "!!", "??", "..", "((", "))", "**", "|0", "……", "~~", "X0", "  "};
    private final static int MATRIX[][] = { // symbol-category matrix
            {-1, -1, -1, -1},// 0 0
            {-1, -1, -1, -1},// + 1
            {0, -1, 1, 1}, // a 2
            {1, 0, -1, -1}, // b 3
            {1, 0, -1, -1}, // c 4
            {1, 1, -1, -1}, // ç 5
            {1, 0, -1, -1}, // d 6
            {0, -1, 0, 1}, // e 7
            {1, 1, -1, -1}, // f 8
            {1, 0, -1, -1}, // g 9
            {1, 0, -1, -1}, // ğ 10
            {1, 1, -1, -1}, // h 11
            {0, -1, 1, 1}, // ı 12
            {0, -1, 0, 1}, // i 13
            {1, 0, -1, -1}, // j 14
            {1, 1, -1, -1}, // k 15
            {1, 0, -1, -1}, // l 16
            {1, 0, -1, -1}, // m 17
            {1, 0, -1, -1}, // n 18
            {0, -1, 1, 0}, // o 19
            {0, -1, 0, 0}, // ö 20
            {1, 1, -1, -1}, // p 21
            {1, 0, -1, -1}, // r 22
            {1, 1, -1, -1}, // s 23
            {1, 1, -1, -1}, // ş 24
            {1, 1, -1, -1}, // t 25
            {0, -1, 1, 0}, // u 26
            {0, -1, 0, 0}, // ü 27
            {1, 0, -1, -1}, // v 28
            {1, 0, -1, -1}, // y 29
            {1, 0, -1, -1}, // z 30
            {0, -1, -1, -1}, // A 31
            {1, -1, -1, -1}, // C 32
            {1, -1, -1, -1}, // Ç 33
            {1, -1, -1, -1}, // D 34
            {0, -1, -1, -1}, // E 35
            {1, -1, -1, -1}, // G 36
            {1, -1, -1, -1}, // Ğ 37
            {0, -1, -1, -1}, // H 38
            {0, -1, -1, -1}, // I 39
            {1, -1, -1, -1}, // K 40
            {1, -1, -1, -1}, // L 41
            {-1, -1, -1, -1}, // N 42
            {1, -1, -1, -1}, // P 43
            {-1, -1, -1, -1}, // S 44
            {-1, -1, -1, -1}, // Ş 45
            {1, -1, -1, -1}, // T 46
            {0, -1, -1, -1}, // U 47
            {0, -1, -1, -1}, // Ü 48
            {-1, -1, -1, -1}, // Y 49
            {0, -1, -1, -1}, // Z 50
            {0, -1, 0, 1}, // â   51
            {0, -1, 0, 1}, // î   52
            {0, -1, 0, 0}, // ô   53
            {0, -1, 0, 0}, // û   54
            {-1, -1, -1, -1}, // $ 55
            {-1, -1, -1, -1}, // & 56
            {-1, -1, -1, -1}, // % 57
            {-1, -1, -1, -1}, // - 58
            {1, 1, -1, -1}, // J 59
            {-1, -1, -1, -1}, // , 60
            {-1, -1, -1, -1}, // ; 61
            {-1, -1, -1, -1}, // ! 62
            {-1, -1, -1, -1}, // ? 63
            {-1, -1, -1, -1}, // . 64
            {-1, -1, -1, -1}, // ( 65
            {-1, -1, -1, -1}, // ) 66
            {-1, -1, -1, -1}, // * 67
            {-1, -1, -1, -1}, // | 68
            {-1, -1, -1, -1}, // … 69
            {-1, -1, -1, -1}, // ~ 70
            {0, -1, -1, -1}, // X 71
            {-1, -1, -1, -1} // <space> 72
    };
    private int fsm1, fsm2, fsm3, fsm4;
    private String lastMorpheme, previousMorpheme;
    private boolean tempFlag = false;//***

    // constructor
//    public Synthesizer(Synthesizer s) {
//     this.fsm1 = s.fsm1;
//     this.fsm2 = s.fsm2;
//     this.fsm3 = s.fsm3;
//     this.fsm4 = s.fsm4;
//     this.lastMorpheme = s.lastMorpheme;
//     }
    // no-arg constructor
    public Synthesizer() {
        this.fsm1 = 0;
        this.fsm2 = 0;
        this.fsm3 = 0;
        this.fsm4 = 0;
        lastMorpheme = null;
    }

    private Integer getIndexOfSymbol(char c) {
        return SYMBOL.indexOf(c);
    }

    private void fsm(int[] cm) {

        // FSM1
        if (fsm1 == 0 && cm[1] == 1) {
            fsm1 = 1;
        } else if (fsm1 == 1 && (cm[1] == 0 || cm[0] == 0)) {
            fsm1 = 0;
        }

        // FSM2
        if (cm[2] == 1 && cm[3] == 1) {
            fsm2 = 1;
        } else if (cm[2] == 0 && cm[3] == 1) {
            fsm2 = 2;
        } else if (cm[2] == 1 && cm[3] == 0) {
            fsm2 = 3;
        } else if (cm[2] == 0 && cm[3] == 0) {
            fsm2 = 4;
        }

        // FSM3
        if (fsm3 == 0 && cm[2] == 1) {
            fsm3 = 1;
        } else if (fsm3 == 0 && cm[2] == 0) {
            fsm3 = 2;
        } else if (fsm3 == 1 && cm[2] == 0) {
            fsm3 = 2;
        } else if (fsm3 == 2 && cm[2] == 1) {
            fsm3 = 1;
        }

        // FSM4
        if (fsm4 == 0 && cm[0] == 1) {
            fsm4 = 1;
        } else if (fsm4 == 1 && cm[0] == 0) {
            fsm4 = 0;
        }

    }

    private boolean lookNextSymbol(char[] lexical, int s, int category, int value) {

        int i = s;
        while (i < lexical.length) {
            int sym = SYMBOL.indexOf(lexical[i]);

            int m = MATRIX[sym][category];

            if (m != -1) {

                if (m == value) {
                    return true;
                } else {
                    return false;
                }
            }

            i++;
        }

        return false;
    }

    private boolean lookNextMorpheme(char[] lexical, int s, String morpheme) {

        String l = new String(lexical);

        if (lexical.length - s >= morpheme.length()) {
            return l.substring(s, s + morpheme.length()).equals(morpheme);
        } else {
            return false;
        }
    }

    private int countMorpheme(char[] lexical) {
        int c = 0;
        for (int i = 0; i < lexical.length; i++) {
            if (lexical[i] == '+') {
                c++;
            }
        }
        return c;
    }

    public String synthesis(char[] lexical, String verbType, char separator) {
        //countSynth++;
        char surface[] = new char[lexical.length];
        int spec = -1;
        boolean stateDrop = false;
        int indexOfMorpheme = 0;
        lastMorpheme = null;
        tempFlag = false;//***

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < lexical.length; i++) {

            buf.append(lexical[i]);
            if (lexical[i] == '+') {

                if (lastMorpheme != null) {
                    spec = -1;
                }

                previousMorpheme = lastMorpheme;
                lastMorpheme = buf.toString();
                //***
                if (lastMorpheme.equals("mA+")) {
                    tempFlag = true;
                }
                indexOfMorpheme++;
                buf.setLength(0);
            }

            //String currentChar = String.valueOf(lexical.charAt(i));
            int c = SYMBOL.indexOf(lexical[i]);//.charAt(i));

            // capture the special characters
            if (c == 57) {
                spec = 57;
            } else if (c == 0) {
                spec = 0;
            } else if (c == 56) {
                spec = 56;
            } else if (c == 49) {
                spec = 49;
            }
            // end-capture the special characters

            // rules
            // yasaklar
            if (previousMorpheme != null && lastMorpheme != null && previousMorpheme.equals("lAr+") && lastMorpheme.equals("lArI+")) {
                surface[i] = '-';
                fsm(MATRIX[58]);
            } /*else if (previousMorpheme != null && lastMorpheme != null && previousMorpheme.equals("lE+") && lastMorpheme.equals("Zr+")) {surface[i] = '-'; fsm(Start.matrix[58]);}*/ else if (previousMorpheme != null && lastMorpheme != null && previousMorpheme.equals("lAr+") && lastMorpheme.equals("lAr+")) {
                surface[i] = '-';
                fsm(MATRIX[58]);
            } /*else if (indexOfMorpheme == 2 && !verbType.equals("") && verbType.charAt(0) == '0' && lastMorpheme.equals("Üş+")) {
             surface[i] = '-';
             fsm(Start.matrix[58]);
             } else if (indexOfMorpheme == 2 && !verbType.equals("") && verbType.charAt(3) == '0' && lastMorpheme.equals("DHr+")) {
             surface[i] = '-';
             fsm(Start.matrix[58]);
             } else if (indexOfMorpheme == 2 && !verbType.equals("") && verbType.charAt(3) == '1' && lastMorpheme.equals("t+")) {
             surface[i] = '-';
             fsm(Start.matrix[58]);
             } else if (indexOfMorpheme == 2 && !verbType.equals("") && verbType.charAt(4) == '0' && lastMorpheme.equals("Ün+")) {
             surface[i] = '-';
             fsm(Start.matrix[58]);
             }*/ //else if (tempFlag && lastMorpheme.equals("Zr+")) {surface[i] = '-'; fsm(Start.matrix[58]);System.out.println("basarili");} // lE+Zr(ajs) icin engelleme
            // end-yasaklar
            else if (stateDrop == true) {
                int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                surface[i] = '0';
                fsm(MATRIX[sy]);
                stateDrop = false;
            } else if (c >= 2 && c <= 30) {
                int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                surface[i] = DEFAULT[c].charAt(1);
                fsm(MATRIX[sy]);
            } // sapkali isaretler/////////
            else if (c == 51) {
                surface[i] = DEFAULT[c].charAt(1);
                fsm(MATRIX[7]);
            } else if (c == 52) {
                surface[i] = DEFAULT[c].charAt(1);
                fsm(MATRIX[13]);
            } else if (c == 53) {
                surface[i] = DEFAULT[c].charAt(1);
                fsm(MATRIX[20]);
            } else if (c == 54) {
                surface[i] = DEFAULT[c].charAt(1);
                fsm(MATRIX[27]);
            } // end-sapkali isaretler/////
            else if (c == 68) { // istisnai morfolojik ozellik tanimlamaya yarayan | isareti
                surface[i] = '0';
                fsm(MATRIX[0]);
            } else if (c == 57 || c == 58) {
                int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                surface[i] = DEFAULT[c].charAt(1);
                fsm(MATRIX[sy]);
            } else if (c == 1) {
                surface[i] = separator;
                fsm(MATRIX[0]);
            } else if (c == 0) {
                if (lookNextSymbol(lexical, i + 1, 0, 0)) {
                    surface[i] = surface[i - 1];
                    fsm(MATRIX[SYMBOL.indexOf(surface[i - 1])]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } // & isareti
            else if (c == 56) {
                surface[i] = '0';
                fsm(MATRIX[0]);
            } else if (c == 32) {
                if (fsm1 == 1 && fsm4 == 1) {
                    surface[i] = 'ç';
                    fsm(MATRIX[5]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 34) {
                if (fsm1 == 1 && fsm4 == 1) {
                    surface[i] = 't';
                    fsm(MATRIX[25]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 36) {
                if (indexOfMorpheme == 0) {
                    if (lookNextSymbol(lexical, i + 1, 0, 0)) {
                        surface[i] = 'g';
                        fsm(MATRIX[9]);
                    } else {
                        int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                        surface[i] = DEFAULT[c].charAt(1);
                        fsm(MATRIX[sy]);
                    }
                } else if (fsm1 == 1 && fsm4 == 1) {
                    surface[i] = 'k';
                    fsm(MATRIX[15]);
                } else {
                    surface[i] = 'g';
                    fsm(MATRIX[9]);
                }
            } else if (c == 38) {// H
                if (fsm2 == 1) {
                    surface[i] = 'ı';
                    fsm(MATRIX[12]);
                } else if (fsm2 == 2) {
                    surface[i] = 'i';
                    fsm(MATRIX[13]);
                } else if (fsm2 == 3) {
                    surface[i] = 'u';
                    fsm(MATRIX[26]);
                } else if (fsm2 == 4) {
                    surface[i] = 'ü';
                    fsm(MATRIX[27]);
                }
            } else if (c == 31) {
                if (fsm3 == 1) {
                    surface[i] = 'a';
                    fsm(MATRIX[2]);
                } else if (fsm3 == 2) {
                    surface[i] = 'e';
                    fsm(MATRIX[7]);
                }
            } else if (c == 39) {
                if (fsm3 == 1) {
                    surface[i] = 'ı';
                    fsm(MATRIX[12]);
                } else if (fsm3 == 2) {
                    surface[i] = 'i';
                    fsm(MATRIX[13]);
                }
            } else if (c == 47) {// U
                if (fsm4 == 1 && fsm2 == 1) {
                    surface[i] = 'ı';
                    fsm(MATRIX[12]);
                } else if (fsm4 == 1 && fsm2 == 2) {
                    surface[i] = 'i';
                    fsm(MATRIX[13]);
                } else if (fsm4 == 1 && fsm2 == 3) {
                    surface[i] = 'u';
                    fsm(MATRIX[26]);
                } else if (fsm4 == 1 && fsm2 == 4) {
                    surface[i] = 'ü';
                    fsm(MATRIX[27]);
                } else if (fsm4 == 0) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                }
            } else if (c == 50) {// Z: istisnai Zt (oldurgan) eki icin tanimlama "kok-ut", "kork-ut"
                if (lastMorpheme.endsWith("k+")) {
                    if (fsm4 == 1 && fsm2 == 1) {
                        surface[i] = 'ı';
                        fsm(MATRIX[12]);
                    } else if (fsm4 == 1 && fsm2 == 2) {
                        surface[i] = 'i';
                        fsm(MATRIX[13]);
                    } else if (fsm4 == 1 && fsm2 == 3) {
                        surface[i] = 'u';
                        fsm(MATRIX[26]);
                    } else if (fsm4 == 1 && fsm2 == 4) {
                        surface[i] = 'ü';
                        fsm(MATRIX[27]);
                    }
                } else {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                }
            } else if (c == 33) {
                if (lookNextSymbol(lexical, i + 1, 0, 0)) {
                    surface[i] = 'c';
                    fsm(MATRIX[4]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 37) {
                if (lookNextSymbol(lexical, i + 1, 0, 0)) {
                    surface[i] = 'ğ';
                    fsm(MATRIX[10]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 40) {
                if (lookNextSymbol(lexical, i + 1, 0, 0)) {
                    surface[i] = 'ğ';
                    fsm(MATRIX[10]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 43) {
                if (lookNextSymbol(lexical, i + 1, 0, 0)) {
                    surface[i] = 'b';
                    fsm(MATRIX[3]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 46) {
                if (lookNextSymbol(lexical, i + 1, 0, 0)) {
                    surface[i] = 'd';
                    fsm(MATRIX[6]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 35) {
                if (fsm2 == 1 && lookNextMorpheme(lexical, i + 1, "+Uyor")) {
                    surface[i] = 'ı';
                    fsm(MATRIX[12]);
                } else if (fsm2 == 2 && lookNextMorpheme(lexical, i + 1, "+Uyor")) {
                    surface[i] = 'i';
                    fsm(MATRIX[13]);
                } else if (fsm2 == 3 && lookNextMorpheme(lexical, i + 1, "+Uyor")) {
                    surface[i] = 'u';
                    fsm(MATRIX[26]);
                } else if (fsm2 == 4 && lookNextMorpheme(lexical, i + 1, "+Uyor")) {
                    surface[i] = 'ü';
                    fsm(MATRIX[27]);
                } else if (fsm3 == 1) {
                    surface[i] = 'a';
                    fsm(MATRIX[2]);
                } else if (fsm3 == 2) {
                    surface[i] = 'e';
                    fsm(MATRIX[7]);
                } else if (fsm2 == 0 && fsm3 == 0 && (lookNextMorpheme(lexical, i + 1, "+YAmE") || lookNextMorpheme(lexical, i + 1, "+YAcAK") || lookNextMorpheme(lexical, i + 1, "+YAbil") || lookNextMorpheme(lexical, i + 1, "+YA") || lookNextMorpheme(lexical, i + 1, "+Uyor") || lookNextMorpheme(lexical, i + 1, "+YAlI") || lookNextMorpheme(lexical, i + 1, "+YArAk") || lookNextMorpheme(lexical, i + 1, "+YHn"))) {
                    surface[i] = 'i';
                    fsm(MATRIX[13]);
                } //dE, yE
                else if (fsm2 == 0 && fsm3 == 0) {
                    surface[i] = 'e';
                    fsm(MATRIX[7]);
                } //dE, yE
            } else if (c == 44) {
                if (spec == 57) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                }
                if (spec == 0) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                }
                if (spec == 49) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                } else if (fsm4 == 0) {
                    surface[i] = 's';
                    fsm(MATRIX[23]);
                } else if (fsm4 == 1) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                }
            } else if (c == 45) {
                if (fsm4 == 0) {
                    surface[i] = 'ş';
                    fsm(MATRIX[24]);
                } else if (fsm4 == 1) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                }
            } /*else if (c == 48) {
             if (fsm4 == 1 && fsm2 == 1) {
             surface[i] = 'ı';
             fsm(Start.matrix[12]);
             } else if (fsm4 == 1 && fsm2 == 2) {
             surface[i] = 'i';
             fsm(Start.matrix[13]);
             } else if (fsm4 == 1 && fsm2 == 3) {
             surface[i] = 'u';
             fsm(Start.matrix[26]);
             } else if (fsm4 == 1 && fsm2 == 4) {
             surface[i] = 'ü';
             fsm(Start.matrix[27]);
             } else if (fsm4 == 0) {
             surface[i] = '0';
             fsm(Start.matrix[0]);
             }
             }*/ else if (c == 42) {
                if (spec == 49) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                } else if (lastMorpheme.equals("lArI+") || lastMorpheme.equals("SH+") || lastMorpheme.equals("ki+") || lastMorpheme.equals("kendi+") || lastMorpheme.equals("bu+")
                        || lastMorpheme.equals("şu+") || lastMorpheme.equals("o+") || spec == 56) {
                    surface[i] = 'n';
                    fsm(MATRIX[18]);
                } else if (fsm4 == 0 && lookNextSymbol(lexical, i + 2, 0, 1)) {
                    surface[i] = 'n';
                    fsm(MATRIX[18]);
                } else if ((fsm4 == 0 && lookNextSymbol(lexical, i + 1, 0, 0))) {
                    surface[i] = 'y';
                    fsm(MATRIX[29]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 49) {
                if ((buf.toString().endsWith("suY"))) {
                    if (countMorpheme(lexical) <= 1) {
                        surface[i] = '0';
                        fsm(MATRIX[0]);
                    } else if (lookNextMorpheme(lexical, i + 1, "+NH") || lookNextMorpheme(lexical, i + 1, "+NA")
                            || lookNextMorpheme(lexical, i + 1, "+SH") || lookNextMorpheme(lexical, i + 1, "+Un") || lookNextMorpheme(lexical, i + 1, "+YlA")) {
                        surface[i] = 'y';
                        fsm(MATRIX[29]);
                    } else /*if (lookNextMorpheme(lexical, i + 1, "+NDA") || lookNextMorpheme(lexical, i + 1, "+NDAn"))*/ {
                        surface[i] = '0';
                        fsm(MATRIX[0]);
                    }
                } else if (fsm4 == 0) {
                    surface[i] = 'y';
                    fsm(MATRIX[29]);
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } else if (c == 55) {
                if (lookNextSymbol(lexical, i + 3, 0, 0)) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                    stateDrop = true;
                } else {
                    int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                    surface[i] = DEFAULT[c].charAt(1);
                    fsm(MATRIX[sy]);
                }
            } // ----L
            else if (indexOfMorpheme == 1 && c == 41 && !verbType.equals("")) {
                if (verbType.charAt(0) == '0') {
                    surface[i] = '-';
                    fsm(MATRIX[58]);
                } else if (verbType.charAt(0) == '1') {
                    if (isEndsWithVocal(lastMorpheme) || lastMorpheme.endsWith("l+")) {
                        surface[i] = 'n';
                        fsm(MATRIX[18]);
                    } else {
                        surface[i] = 'l';
                        fsm(MATRIX[16]);
                    }
                }
            } else if (indexOfMorpheme > 1 && c == 41) {
                surface[i] = 'l';
                fsm(MATRIX[16]);
            } // end-L
            // ----X
            else if (indexOfMorpheme == 1 && c == 71 && !verbType.equals("")) {
                if (verbType.charAt(1) == '0') {
                    if (fsm3 == 1) {
                        surface[i] = 'a';
                        fsm(MATRIX[2]);
                    } else if (fsm3 == 2) {
                        surface[i] = 'e';
                        fsm(MATRIX[7]);
                    }
                } else if (verbType.charAt(1) == '1') {
                    if (fsm4 == 1 && fsm2 == 1) {
                        surface[i] = 'ı';
                        fsm(MATRIX[12]);
                    } else if (fsm4 == 1 && fsm2 == 2) {
                        surface[i] = 'i';
                        fsm(MATRIX[13]);
                    } else if (fsm4 == 1 && fsm2 == 3) {
                        surface[i] = 'u';
                        fsm(MATRIX[26]);
                    } else if (fsm4 == 1 && fsm2 == 4) {
                        surface[i] = 'ü';
                        fsm(MATRIX[27]);
                    } else if (fsm4 == 0) {
                        surface[i] = '0';
                        fsm(MATRIX[0]);
                    }
                }
            } else if (indexOfMorpheme > 1 && c == 71) {
                if (fsm4 == 1 && fsm2 == 1) {
                    surface[i] = 'ı';
                    fsm(MATRIX[12]);
                } else if (fsm4 == 1 && fsm2 == 2) {
                    surface[i] = 'i';
                    fsm(MATRIX[13]);
                } else if (fsm4 == 1 && fsm2 == 3) {
                    surface[i] = 'u';
                    fsm(MATRIX[26]);
                } else if (fsm4 == 1 && fsm2 == 4) {
                    surface[i] = 'ü';
                    fsm(MATRIX[27]);
                } else if (fsm4 == 0) {
                    surface[i] = '0';
                    fsm(MATRIX[0]);
                }
            } // end-X
            else if (c == 59) {
                if (fsm4 == 0) {
                    surface[i] = '-';
                    fsm(MATRIX[58]);
                } else if (fsm4 == 1) {
                    surface[i] = 's';
                    fsm(MATRIX[23]);
                }
            } else if ((c >= 60 && c <= 67) || c == 72 || c == 70) {
                int sy = SYMBOL.indexOf(DEFAULT[c].charAt(1));
                surface[i] = DEFAULT[c].charAt(1);
                fsm(MATRIX[sy]);
            }
        }

        fsm1 = 0;
        fsm2 = 0;
        fsm3 = 0;
        fsm4 = 0;

        return new String(surface);
    }

    public boolean isEndsWithVocal(String s) {
        String s2 = s;
        if (s.endsWith("+")) {
            s2 = s.substring(0, s.length() - 1);
        }

        if (SURFACEVOCAL.indexOf(s2.charAt(s2.length() - 1)) > -1 || s2.charAt(s2.length() - 1) == 'E') {
            return true;
        }

        return false;
    }
}
