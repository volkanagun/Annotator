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

import edu.btu.annotating.morphology.structure.LexicalForm;
import edu.btu.annotating.morphology.structure.Schema;
import edu.btu.annotating.morphology.structure.State;
import edu.btu.annotating.morphology.structure.SurfaceForm;
import edu.btu.annotating.morphology.algo.MorphoTag;

import java.util.Objects;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class SuffixEntry implements Schema {
    private LexicalForm lForm;
    private SurfaceForm sForm;
    private State state;
    private MorphoTag mTag;
    private String description;

    public SuffixEntry(LexicalForm lForm, SurfaceForm sForm, State state, MorphoTag mTag, String description) {
        this.lForm = lForm;
        this.sForm = sForm;
        this.state = state;
        this.mTag = mTag;
        this.description = description;
    }

    public LexicalForm getlForm() {
        return lForm;
    }

    public SurfaceForm getsForm() {
        return sForm;
    }

    public State getState() {
        return state;
    }

    public MorphoTag getMTag() {
        return mTag;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.lForm);
        hash = 83 * hash + Objects.hashCode(this.sForm);
        hash = 83 * hash + Objects.hashCode(this.state);
        hash = 83 * hash + Objects.hashCode(this.mTag);
        hash = 83 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SuffixEntry other = (SuffixEntry) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.lForm, other.lForm)) {
            return false;
        }
        if (!Objects.equals(this.sForm, other.sForm)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.mTag, other.mTag)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SuffixEntry{" + "lForm=" + lForm + ", sForm=" + sForm + ", state=" + state + ", mTag=" + mTag + ", description=" + description + '}';
    }

    @Override
    public Object duplicate() {
        return new SuffixEntry((LexicalForm) lForm.duplicate(), (SurfaceForm) sForm.duplicate(), (State) state.duplicate(), (MorphoTag) mTag.duplicate(), description);
    }
}
