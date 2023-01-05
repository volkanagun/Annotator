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

import java.util.HashMap;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Mapper {
    public static void add(HashMap<String, Double> map, String s, Double increment) {
        Double d = map.get(s);
        if (d == null) {
            d = 0.0;
        }
        map.put(s, d + increment);
    }
    public static void add(HashMap<String, Double> map, String s) {
        Double d = map.get(s);
        if (d == null) {
            d = 0.0;
        }
        map.put(s, d + 1.0);
    }
}
