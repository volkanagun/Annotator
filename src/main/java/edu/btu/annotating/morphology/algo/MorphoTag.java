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

import edu.btu.annotating.morphology.structure.Schema;

import java.util.Objects;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class MorphoTag implements Schema {

    private final String[] element;
    private final int numElement;

    public MorphoTag(String[] element) {
        this.element = element;
        this.numElement = element.length;
    }

    public MorphoTag(String s) {
        String[] t = null;
        if (s.contains("+")) {
            t = s.split("[+]");
        } else {
            t = new String[1];
            t[0] = s;
        }
        this.element = t;
        this.numElement = element.length;
    }

    public String[] getElement() {
        return element;
    }

    public int getNumElement() {
        return numElement;
    }

    @Override
    public String toString() {
        String temp = "";
        for (String t : element) {
            temp += t + "+";
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.element);
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
        final MorphoTag other = (MorphoTag) obj;
        if (!Objects.equals(this.element, other.element)) {
            return false;
        }
        return true;
    }

    @Override
    public Object duplicate() {
        return new MorphoTag(element);
    }
}
