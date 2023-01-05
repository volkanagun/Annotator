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
public class Morpheme {
    private String type;
    private String surface;
    private String lexical;
    private MorphoTag tag;
    private String tagDesc;
    private String state;
    private String subcat;
    private String deepAnalysis;
    private String verbType;
    private String realSurface;

    public Morpheme(String type, String surface, String lexical, MorphoTag tag, String tagDesc, String state, String subcat, String deepAnalysis, String verbType, String realSurface) {
        this.type = type;
        this.surface = surface;
        this.lexical = lexical;
        this.tag = tag;
        this.tagDesc = tagDesc;
        this.state = state;
        this.subcat = subcat;
        this.deepAnalysis = deepAnalysis;
        this.verbType = verbType;
        this.realSurface = realSurface;
    }

    public String getRealSurface() {
        return realSurface;
    }

    public String getVerbType() {
        return verbType;
    }

    public String getType() {
        return type;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getLexical() {
        return lexical;
    }

    public void setLexical(String lexical) {
        this.lexical = lexical;
    }

    public MorphoTag getTag() {
        return tag;
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

    public String getDeepAnalysis() {
        return deepAnalysis;
    }

    @Override
    public String toString() {
        return type + ", " + surface + ", " + lexical + ", " + tag + ", " + tagDesc + ", " + state + ", " + subcat + ", " + deepAnalysis + ", " + verbType;
    }
}
