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
public class Leaf {
    private Morpheme[] morpheme;
    private String lexical;
    private String totalTag;
    private String state;

    public Leaf(Morpheme[] morpheme) {
        this.morpheme = morpheme;
        processLexical();
        processTotalTag();
        processState();
    }

    public final void processLexical() {
        if (morpheme.length == 0) {
            lexical = "";
            return;
        }
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getLexical() + "+";
        }
        lexical = temp.substring(0, temp.length() - 1);
    }

    public final void processTotalTag() {
        if (morpheme.length == 0) {
            totalTag = "_";
            return;
        }
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getTag() + "+";
        }
        totalTag = temp.substring(0, temp.length() - 1);

    }

    public final void processState() {
        if (morpheme.length == 0) {
            state = "";
            return;
        }
        String temp = "";
        for (Morpheme m : morpheme) {
            temp += m.getState() + "+";
        }
        state = temp.substring(0, temp.length() - 1);
    }

    public Morpheme[] getMorpheme() {
        return morpheme;
    }

    public String getLexical() {
        return lexical;
    }

    public String getTotalTag() {
        return totalTag;
    }

    public String getState() {
        return state;
    }
}
