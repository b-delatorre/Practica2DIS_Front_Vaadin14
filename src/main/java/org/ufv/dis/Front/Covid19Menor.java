package org.ufv.dis.Front;

import java.util.ArrayList;

public class Covid19Menor {
    private ArrayList<Data> data = new ArrayList<>();

    public Covid19Menor(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
