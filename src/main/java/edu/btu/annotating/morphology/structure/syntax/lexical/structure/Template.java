package edu.btu.annotating.morphology.structure.syntax.lexical.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.SubFunction;
import edu.btu.annotating.morphology.structure.syntax.structure.Structure;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Template implements SubFunction {

    private ArrayList<Cell> list;
    private String[] senseIDs;
    private boolean mandatory;
    private boolean available;
    private boolean processed;

    public Template(ArrayList<Cell> list, String[] senseIDs) {
        this.list = list;
        this.senseIDs = senseIDs;
//        processMandatory(senseIDs);
        this.available = true;
        this.processed = false;
    }

    public Template(ArrayList<Cell> list, String[] senseIDs, boolean mandatory, boolean available, boolean processed) {
        this.list = list;
        this.senseIDs = senseIDs;
        this.mandatory = mandatory;
        this.available = available;
        this.processed = processed;
    }

//    private void processMandatory(String[] senseIDs) {
//        String symbol = "#";
//        int count = 0;
//        for (String sid : senseIDs) {
//            if (sid.startsWith(symbol)) {
//                count++;
//            }
//        }
//        if (senseIDs.length == count) {
//            mandatory = true;
//        } else {
//            mandatory = false;
//        }
//    }

    public ArrayList<Cell> getList() {
        return list;
    }

    public void setList(ArrayList<Cell> list) {
        this.list = list;
    }

    public String[] getSenseIDs() {
        return senseIDs;
    }

    public void setSenseIDs(String[] senseIDs) {
        this.senseIDs = senseIDs;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isFitted(Cell cell) {
        int count = 0;
        for (Cell c : list) {
            if (!c.isAvailable()) {
                count++;
            }
        }
        if (cell.isAvailable() && count == list.size() - 1) {
            return true;
        }
        return false;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public static void updateAvailability(Template template) {
        ArrayList<Cell> list = template.getList();
        for (Cell cell : list) {
            if (cell.isAvailable()) {
                return;
            }
        }
        template.setAvailable(false);
    }

    @Override
    public Function duplicate() {
        ArrayList<Cell> copyList = null;
        if (list != null) {
            copyList = new ArrayList();
            for (Cell c : list) {
                if (c == null) {
                    copyList.add(null);
                } else {
                    copyList.add((Cell) c.duplicate());
                }
            }
        }
        return new Template(copyList, this.senseIDs, this.mandatory, this.available, this.processed);
    }

    @Override
    public Structure getStructure() {
        return null;
    }

    @Override
    public String toString() {
        String result = "";
        for (Cell cell : list) {
            result += cell;
        }
        return result + ":" + Arrays.toString(senseIDs);
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
