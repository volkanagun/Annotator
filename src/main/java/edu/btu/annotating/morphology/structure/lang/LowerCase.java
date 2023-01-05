package edu.btu.annotating.morphology.structure.lang;

public class LowerCase {

    protected static final String LOWERCASE = "abcçdefgğhıijklmnoöprsştuüvyzâîôû";
    protected static final String UPPERCASE = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZÂÎÔÛ";

    public static String getLowercase(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            int posL = LOWERCASE.indexOf(c);
            int posU = UPPERCASE.indexOf(c);
            if (posL > -1) {
                result += c;
            } else if (posU > -1) {
                result += LOWERCASE.charAt(posU);
            } else {
                result += c;
            }
        }
        return result;
    }

}