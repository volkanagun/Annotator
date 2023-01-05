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

import edu.btu.annotating.morphology.structure.POS;

import java.util.ArrayList;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Stem {
    private Morpheme[] morpheme;
    private String surface;
    private String lexical;
    private String totalTag;
    private String tagDesc;
    private String state;
    private String subcat;
    private POS pos;

    public Stem(Morpheme[] morpheme) {
        this.morpheme = morpheme;
        processSurface();
        processLexical();
        processTotalTag();
        processState();
        processSubcat();
        processPOS();
    }

    private void processPOS() {
        // burada stem'in POS'unu belirlemek icin, stem'in morpheme dizisini bir sozcugu olusturan morpheme dizisiymis gibi kullanacagiz
        // boylece POS belirleme islemini burada yani Stem sinifinda tekrar etmemis olacagiz.
        // bunu yapma nedenimiz kelimenin POS'u ile stem'in POS'unun farkli olabilmesi, eger bu iki POS hep ayni olsaydi Stem sinifinda POS belirlemeye gerek kalmazdi

        MorphoAnalysis a = new MorphoAnalysis(morpheme, 1, true);
        pos = a.getPos();
    }

    private void processSurface() {
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getSurface();
        }
        surface = temp;
    }

    private void processLexical() {
        lexical = exceptLastChar(morpheme);
    }

    private void processTotalTag() {
        if (morpheme == null) {
            totalTag = "_";
        } else {
            String temp = "";
            for (Morpheme m : morpheme) {
                temp += m.getTag() + "+";
            }
            totalTag = temp.substring(0, temp.length() - 1);
        }
    }

    private void processState() {
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getState() + "+";
        }
        state = temp.substring(0, temp.length() - 1);
    }

    private void processSubcat() {
        // ozel islem gerektiriyor
        // ornegin, govdedeki edilgenlik ACC'leri gecersiz yapar.
        subcat = "_";
        for (Morpheme m : morpheme) {
            if (!m.getSubcat().equals("_")) {
                subcat = m.getSubcat();
            }
            if (subcat.equals("")) {
                subcat = "_";
            }
            if (m.getState().equals("pass")) {
                subcat = arrange(subcat, "ACC");
                subcat = arrange(subcat, "NOM");
            }
        }
    }

    private String arrange(String sb, String str) {
        if (sb.equals("_")) {
            return "_";
        }
        String[] subcatArr;
        if (sb.contains("|")) {
            subcatArr = sb.split("[|]");
        } else {
            subcatArr = new String[1];
            subcatArr[0] = sb;
        }
        ArrayList<String> list = new ArrayList();
        for (String s : subcatArr) {
            if (s.equals("_")) {
                continue;
            }
            String[] arr = s.split(":");
            String s_ = arr[0];
            String n_ = arr[1];
            if (s_.contains(str)) {
                int x = 0;
                for (int i = s_.indexOf(str) + 3; i < s_.length(); i++) {
                    if (s_.charAt(i) == '1' || s_.charAt(i) == '2' || s_.charAt(i) == '3' || s_.charAt(i) == '4') {
                        x = i;
                    } else {
                        break;
                    }
                }
                String rest = s_.substring(0, s_.indexOf(str)) + s_.substring(x + 1, s_.length());
                if (!rest.isEmpty()) {
                    list.add(rest + ":" + n_);
                }
            } else {
                list.add(s_ + ":" + n_);
            }
        }
        if (list.size() == 0) {
            sb = "_";
        } else {
            String temp = "";
            for (String s : list) {
                temp += s + "|";
            }
            temp = temp.substring(0, temp.length() - 1);
            sb = temp;
        }
        return sb;
    }

    public Morpheme[] getMorpheme() {
        return morpheme;
    }

    public String getSurface() {
        return surface;
    }

    public String getLexical() {
        return lexical;
    }

    public String getTotalTag() {
        return totalTag;
    }

    public String getTagDesc() {
        return tagDesc;
    }

    public String getState() {
        return state;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public void setLexical(String lexical) {
        this.lexical = lexical;
    }

    public POS getPos() {
        return pos;
    }

    public String getMorphemeToString() {
        String lex = "", tag = "";
        ///
        if (!morpheme[0].getState().equals("<inner>") && !morpheme[0].getDeepAnalysis().equals("_")) {
            String ba = morpheme[0].getDeepAnalysis();
            String[] arr = ba.split("~");
            lex = arr[1] + "/";
            tag = arr[2] + "/";
        } else {
            lex = morpheme[0].getLexical() + "/";
            tag = morpheme[0].getTag() + "/";
        }
        ///
        for (int i = 1; i < morpheme.length; i++) {
            Morpheme m = morpheme[i];
            lex += m.getLexical() + "/";
            tag += m.getTag() + "/";
        }
        lex = lex.substring(0, lex.length() - 1);
        tag = tag.substring(0, tag.length() - 1);
        return lex + "~" + tag;
    }

    @Override
    public String toString() {
        return surface + ", " + lexical + ", " + totalTag + ", " + tagDesc + ", " + state + ", " + subcat + ", " + pos;
    }

    private String exceptLastChar(Morpheme[] m) {
        // insan+lHK >> insanlıK
        // bicak+lE >> bicaklE   yalnizca K ve E icin gecerli

        if (m == null) {
            return "_";
        }

        if (m.length == 1) {
            String sur = m[0].getSurface();
            String lex = m[0].getLexical();
            String last = lex.substring(lex.length() - 1, lex.length());
            /*if (!last.equals("E") && !last.equals("K") && !last.equals("T")) {
                last = sur.substring(sur.length() - 1, sur.length());
            }
            return sur.substring(0, sur.length() - 1) + last;*/
            return lex;
        } else {
            String temp = "";
            for (int i = 0; i < m.length - 1; i++) {
                String sur = m[i].getSurface();
                temp += sur;
            }
            String sur = m[m.length - 1].getSurface();
            String lex = m[m.length - 1].getLexical();
            String last = lex.substring(lex.length() - 1, lex.length());
            if (!last.equals("E") && !last.equals("K") && !last.equals("T")) {
                last = sur.substring(sur.length() - 1, sur.length());
            }
            return temp + sur.substring(0, sur.length() - 1) + last;
        }
    }
}
