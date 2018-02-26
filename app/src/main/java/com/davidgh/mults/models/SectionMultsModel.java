package com.davidgh.mults.models;

import java.util.ArrayList;

/**
 * Created by davidgh on 2/16/18.
 */

public class SectionMultsModel {
    private String header;
    private ArrayList<SingleMultModel> allMultsInSection;

    public SectionMultsModel() {
    }

    public SectionMultsModel(String header, ArrayList<SingleMultModel> allMultsInSection) {
        this.header = header;
        this.allMultsInSection = allMultsInSection;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<SingleMultModel> getAllMultsInSection() {
        return allMultsInSection;
    }

    public void setAllMultsInSection(ArrayList<SingleMultModel> allMultsInSection) {
        this.allMultsInSection = allMultsInSection;
    }
}
