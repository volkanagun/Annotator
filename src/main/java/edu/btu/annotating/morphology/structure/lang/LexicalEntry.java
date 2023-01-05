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

import edu.btu.annotating.morphology.algo.MorphoTag;
import edu.btu.annotating.morphology.structure.*;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class LexicalEntry implements Schema {
    private LexicalForm lForm;
    private SurfaceForm sForm;
    private MorphoTag tag;
    private State state;
    private String subcat;
    private VerbCat vCat;
    private DeepAnalysis deepA;

    public LexicalEntry(LexicalForm lForm, SurfaceForm sForm, MorphoTag tag, State state, String subcat, VerbCat vCat, DeepAnalysis deepA) {
        this.lForm = lForm;
        this.sForm = sForm;
        this.tag = tag;
        this.state = state;
        this.subcat = subcat;
        this.vCat = vCat;
        this.deepA = deepA;
    }

    public LexicalForm getlForm() {
        return lForm;
    }

    public SurfaceForm getsForm() {
        return sForm;
    }

    public MorphoTag getTag() {
        return tag;
    }

    public State getState() {
        return state;
    }

    public String getSubcat() {
        return subcat;
    }

    public VerbCat getvCat() {
        return vCat;
    }

    public DeepAnalysis getDeepA() {
        return deepA;
    }

    @Override
    public String toString() {
        return "LexicalEntry{" + "lForm=" + lForm + ", sForm=" + sForm + ", tag=" + tag + ", state=" + state + ", subcat=" + subcat + ", vCat=" + vCat + ", deepA=" + deepA + '}';
    }

    @Override
    public Object duplicate() {
        return new LexicalEntry((LexicalForm) lForm.duplicate(), (SurfaceForm) sForm.duplicate(), (MorphoTag) tag.duplicate(), (State) state.duplicate(), subcat, (VerbCat) vCat.duplicate(), (DeepAnalysis) deepA.duplicate());
    }
}
