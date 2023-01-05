package edu.btu.annotating.morphology.structure.syntax.role;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.RFunction;
import edu.btu.annotating.morphology.structure.syntax.structure.Structure;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Role implements RFunction {
    private Function content;
    private int serialNumber;

    public Role(Function content, int serialNumber) {
        this.content = content;
        this.serialNumber = serialNumber;
    }

    public Function getContent() {
        return content;
    }

    public void setContent(Function content) {
        this.content = content;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Function duplicate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Structure getStructure() {
        return null;
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
