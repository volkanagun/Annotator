package edu.btu.annotating.morphology.structure.syntax.lexical.structure;

import edu.btu.annotating.morphology.structure.syntax.function.Function;
import edu.btu.annotating.morphology.structure.syntax.function.SubFunction;
import edu.btu.annotating.morphology.structure.syntax.role.Complement;
import edu.btu.annotating.morphology.structure.syntax.structure.Structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Subcat implements SubFunction {

    private ArrayList<Template> list;
    private boolean mandatory;
    private boolean ok; // subcat'i olusturan template'lerden en az biri doluysa bu degisken true olur. boylece eylem obeginin en az bir yapiyi tam olarak kabul ettigini dogrulamis oluruz.

    public Subcat(String s) {
        // asagidaki kod satiri deneysel bir eklemedir. isi bitince kaldirilmasi gerekir
        //s = "NOM1|ACC1|DAT1|ABL1|LOC1|ILE1|NOM1ACC1|NOM1DAT1|NOM1ABL1|NOM1LOC1|NOM1ILE1|ACC1DAT1|ACC1ABL1|ACC1LOC1|ACC1ILE1|DAT1ABL1|DAT1LOC1|DAT1ILE1|ABL1LOC1|ABL1ILE1|LOC1ILE1";
        String[] arr;
        s = processNOM(s);
        if (s.contains("|")) {
            arr = s.split("[|]");
        } else {
            arr = new String[]{s};
        }

        ArrayList<Template> list2 = new ArrayList();
        for (String item0 : arr) {
            if (!item0.contains(":")) {
                item0 += ":0";
            }
            String[] arr2 = item0.split(":");
            String item = arr2[0];
            boolean m = item.startsWith("#");
/*            if (item.equals("NULL")) {
                continue;
            }*/
            // ACC2DAT1
            String temp = "";
            ArrayList<Cell> list1 = new ArrayList();
            for (int i = 0; i < item.length(); i++) {

                if (isNumber(item.charAt(i))) {
                    temp += item.charAt(i);
                    if (i < item.length() - 1 && !isNumber(item.charAt(i + 1))) {
                        Cell cell = new Cell(temp);
                        list1.add(cell);
                        temp = "";
                    }
                } else {
                    temp += item.charAt(i);
                }
            }
            if (!temp.isEmpty()) {
                list1.add(new Cell(temp));
            }
            try {
                Template t = new Template(list1, arr2[1].split(","));
                t.setMandatory(m);
                list2.add(t);
            } catch (Exception e) {
                System.out.println("");
            }
        }

        this.list = list2;
        processMandatory();
    }

    private String processNOM(String a) {
        String[] arr = a.split("[|]");
        String result = "";
        for (String ss : arr) {
            String ss2 = clean(ss);
            if (ss2.equals("ACC:")) {
                result += ss + "|NOM:1|";
            } else {
                result += ss + "|";
            }
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    private String clean(String a) {
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            if (!isNumber(a.charAt(i))) {
                result += a.charAt(i);
            }
        }
        return result;
    }

    private void processMandatory() {
        int count = 0;
        for (Template template : list) {
            if (template.isMandatory()) {
                count++;
            }
        }
        if (list.size() == count) {
            mandatory = true;
        } else {
            mandatory = false;
        }
    }

    public Subcat(ArrayList<Template> list, boolean mandatory, boolean ok) {
        this.list = list;
        this.mandatory = mandatory;
        this.ok = ok;
    }

    public Subcat() {
    }

    private static boolean isNumber(char c) {
        if (c == '1' || c == '2' || c == '3' || c == '4') {
            return true;
        }
        return false;
    }

    public ArrayList<Template> getList() {
        return list;
    }

    public void setList(ArrayList<Template> list) {
        this.list = list;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getActiveTemplateLength() {

        int maxTemplateSize = 0;
        for (Template template : this.getList()) {
            if (!template.isAvailable() && template.getList().size() > maxTemplateSize) {
                maxTemplateSize = template.getList().size();
            }
        }

        return maxTemplateSize;
    }

    public String getActiveTemplates() {
        String result = "";

        int maxTemplateSize = 0;
        for (Template template : this.getList()) {
            if (!template.isAvailable() && template.getList().size() > maxTemplateSize) {
                maxTemplateSize = template.getList().size();
            }
        }

        for (Template template : this.getList()) {
            if (!template.isAvailable() && template.getList().size() == maxTemplateSize) {
                result += template + "|";
            }
        }
        if (result.equals("")) {
            return result;
        } else {
            return result.substring(0, result.length()-1);
        }
    }

    public String getUniqueActiveTemplate() {
        String s = getActiveTemplates();

        String s2 = "";
        for (int i = 0; i < s.length(); i++) {
            if (!isNumber(s.charAt(i))) {
                s2 += s.charAt(i);
            }
        }

        String[] ss;
        if (s2.contains("|")) {
            ss = s2.split("[|]");
        } else {
            ss = new String[] {s2};
        }
        HashSet hs = new HashSet();
        for (String st : ss) {
            hs.add(st);
        }

        String result = "";
        Iterator it = hs.iterator();
        while (it.hasNext()) {
            result += (String) it.next() + "|";
        }
        if (!result.equals("")) {
            return result.substring(0, result.length()-1);
        }
        return result;
    }

    public boolean isHalfness() {
        // true: template'lerden hicbiri dolu degilse ve en az biri bos değilse
        for (Template template : list) {
            if (!template.isAvailable()) {
                return false;
            }
        }

        for (Template template : list) {
            if (template.isProcessed()) {
                return true;
            }
        }

        return false;
    }

    public static boolean check(Subcat subcat, String s) {
        boolean flag = false;
        for (Template template : subcat.getList()) {
            if (template.isProcessed()) {
                flag = true;
                for (Cell cell : template.getList()) {
                    if (cell.getS().contains(s) && template.isFitted(cell)) {
                        return true;
                    }
                }
            }
        }
        if (flag) {
            return false;
        }
        for (Template template : subcat.getList()) {
            for (Cell cell : template.getList()) {
                if (cell.getS().contains(s) && template.isAvailable() && cell.isAvailable()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void update(Subcat subcat, ArrayList<Complement> compList, String s) {
        int numComp = compList.size() + 1;
        for (Template template : subcat.getList()) {
            if (template.getList().size() < numComp) {
                template.setAvailable(true);
            }
        }

        for (Template template : subcat.getList()) {
            for (Cell cell : template.getList()) {
                if (cell.getS().contains(s)) {
                    Cell.updateAvailability(cell);
                    template.setProcessed(true);
                    Template.updateAvailability(template);
                }
            }
        }
        processOk(subcat);
    }

    public boolean search(String str) {
        for (Template t : list) {
            for (Cell c : t.getList()) {
                if (c.getS().equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void processOk(Subcat subcat) {
        int count = 0;
        for (Template template : subcat.getList()) {
            if (!template.isAvailable()) {
                count++;
            }
        }
        if (count > 0) {
            subcat.setOk(true);
        } else {
            subcat.setOk(false);
        }
    }

    @Override
    public Function duplicate() {
        ArrayList<Template> copyList = null;
        if (list != null) {
            copyList = new ArrayList();
            for (Template t : list) {
                if (t == null) {
                    copyList.add(null);
                } else {
                    copyList.add((Template) t.duplicate());
                }
            }
        }
        return new Subcat(copyList, this.mandatory, this.ok);
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
