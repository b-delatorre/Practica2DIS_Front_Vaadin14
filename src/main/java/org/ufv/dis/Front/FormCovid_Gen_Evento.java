package org.ufv.dis.Front;

import com.vaadin.flow.component.ComponentEvent;

public class FormCovid_Gen_Evento extends ComponentEvent<FormCovid_General> {
    private Data data;
    public FormCovid_Gen_Evento(FormCovid_General source, Data data) {
        super(source, false);
        this.data=data;
    }
    public Data getData() {
        return data;
    }
}

