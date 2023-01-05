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

/**
 *
 * @author Ozkan Aslan euzkan@gmail.com
 */
public class Channel {
    private String voice;
    private String polarity;
    private String compound;
    private String tense;
    private String pastType;
    private String person;
    private String plurality;
    private String possessive;
    private String case_;
    private String copulaTense;
    private String copulaPastType;
    private String copulaPerson;

    public Channel() {
        // default values
        voice = "ACT";
        polarity = "POS";
        compound = "NULL";
        tense = "IMP";
        pastType = "NULL";
        person = "PER2S";
        plurality = "SNG";
        possessive = "NULL";
        case_ = "NOM";
        copulaTense = "NULL";
        copulaPastType = "NULL";
        copulaPerson = "NULL";
    }

    protected void setVoice(String voice) {
        this.voice = voice;
    }

    protected void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    protected void setCompound(String compound) {
        this.compound = compound;
    }

    protected void setTense(String tense) {
        this.tense = tense;
    }

    protected void setPastType(String pastType) {
        this.pastType = pastType;
    }

    protected void setPerson(String person) {
        this.person = person;
    }

    protected void setPlurality(String plurality) {
        this.plurality = plurality;
    }

    protected void setPossessive(String possessive) {
        this.possessive = possessive;
    }

    protected void setCase_(String case_) {
        this.case_ = case_;
    }

    protected void setCopulaTense(String copulaTense) {
        this.copulaTense = copulaTense;
    }

    protected void setCopulaPastType(String copulaPastType) {
        this.copulaPastType = copulaPastType;
    }

    protected void setCopulaPerson(String copulaPerson) {
        this.copulaPerson = copulaPerson;
    }

    public String getVoice() {
        return voice;
    }

    public String getPolarity() {
        return polarity;
    }

    public String getCompound() {
        return compound;
    }

    public String getTense() {
        return tense;
    }

    public String getPastType() {
        return pastType;
    }

    public String getPerson() {
        return person;
    }

    public String getPlurality() {
        return plurality;
    }

    public String getPossessive() {
        return possessive;
    }

    public String getCase_() {
        return case_;
    }

    public String getCopulaTense() {
        return copulaTense;
    }

    public String getCopulaPastType() {
        return copulaPastType;
    }

    public String getCopulaPerson() {
        return copulaPerson;
    }

    @Override
    public String toString() {
        return "voice=" + voice + ", polarity=" + polarity + ", compound=" + compound + ", tense=" + tense + ", pastType=" + pastType + ", person=" + person + ", plurality=" + plurality + ", possessive=" + possessive + ", case_=" + case_ + ", copulaTense=" + copulaTense + ", copulaPastType=" + copulaPastType + ", copulaPerson=" + copulaPerson;
    }

    public String toShortString() {
        String temp = "";
        if (!voice.equals("NULL")) temp += "voice=" + voice + "|";
        if (!polarity.equals("NULL")) temp += "polarity=" + polarity + "|";
        if (!compound.equals("NULL")) temp += "compound=" + compound + "|";
        if (!tense.equals("NULL")) temp += "tense=" + tense + "|";
        if (!pastType.equals("NULL")) temp += "pastType=" + pastType + "|";
        if (!person.equals("NULL")) temp += "person=" + person + "|";
        if (!plurality.equals("NULL")) temp += "plurality=" + plurality + "|";
        if (!possessive.equals("NULL")) temp += "possessive=" + possessive + "|";
        if (!case_.equals("NULL")) temp += "case=" + case_ + "|";
        if (!copulaTense.equals("NULL")) temp += "copulaTense=" + copulaTense + "|";
        if (!copulaPastType.equals("NULL")) temp += "copulaPastType=" + copulaPastType + "|";
        if (!copulaPerson.equals("NULL")) temp += "copulaPerson=" + copulaPerson + "|";
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }
}
