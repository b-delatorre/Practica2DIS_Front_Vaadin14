package org.ufv.dis.Front;

import java.util.ArrayList;

public class Covid19Mayor {
    private ArrayList<DataMayor> data;

    public Covid19Mayor(ArrayList<DataMayor> data) {
        this.data = data;
    }

    public ArrayList<DataMayor> getData() {
        return data;
    }

    public void setData(ArrayList<DataMayor> data) {
        this.data = data;
    }
}
