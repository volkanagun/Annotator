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
package edu.btu.annotating.morphology.structure;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class VerbCat implements Schema {
    private final String content;
    private boolean passiveType;
    private boolean aoristType;

    public VerbCat(String content) {
        this.content = content;
        if (!content.equals("_")) {
            if (content.charAt(0) == 1) {
                this.passiveType = true;
            }
            if (content.charAt(1) == 1) {
                this.aoristType = true;
            }
        }
    }

    public VerbCat(String content, boolean passiveType, boolean aoristType) {
        this.content = content;
        this.passiveType = passiveType;
        this.aoristType = aoristType;
    }

    public String getContent() {
        return content;
    }

    public boolean isPassiveType() {
        return passiveType;
    }

    public boolean isAoristType() {
        return aoristType;
    }

    @Override
    public String toString() {
        return "VerbCat{" + "passiveType=" + passiveType + ", aoristType=" + aoristType + '}';
    }

    @Override
    public Object duplicate() {
        return new VerbCat(content, passiveType, aoristType);
    }
}
