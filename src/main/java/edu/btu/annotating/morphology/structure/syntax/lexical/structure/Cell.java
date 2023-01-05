package edu.btu.annotating.morphology.structure.syntax.lexical.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.SubFunction;
import edu.btu.annotating.morphology.structure.syntax.structure.Structure;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Cell implements SubFunction {

    private String s;
    private boolean available;

    public Cell(String s) {
        this.s = s;
        this.available = true;
    }

    public Cell(String s, boolean available) {
        this.s = s;
        this.available = available;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public static void updateAvailability(Cell cell) {
        cell.setAvailable(false);
    }

    @Override
    public Function duplicate() {
        return new Cell(this.s, this.available);
    }

    @Override
    public Structure getStructure() {
        return null;
    }

    @Override
    public String toString() {
        return s;
    }

    @Override
    public Structure getDelegate() {
        return null;
    }

    @Override
    public boolean checkMandatory() {
        return true;
    }
}
