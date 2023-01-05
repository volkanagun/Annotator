package edu.btu.annotating.morphology.structure.syntax.structure;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public abstract class Structure {
    private boolean inflectionLocked; // genel amacli cekimleme kilidi
    private boolean possessiveLocked;
    private boolean incomplete;

    public boolean isIncomplete() {
        return incomplete;
    }

    public void setIncomplete(boolean incomplete) {
        this.incomplete = incomplete;
    }

    public boolean isInflectionLocked() {
        return inflectionLocked;
    }

    public void setInflectionLocked(boolean inflectionLocked) {
        this.inflectionLocked = inflectionLocked;
    }

    public boolean isPossessiveLocked() {
        return possessiveLocked;
    }

    public void setPossessiveLocked(boolean possessiveLocked) {
        this.possessiveLocked = possessiveLocked;
    }
}
