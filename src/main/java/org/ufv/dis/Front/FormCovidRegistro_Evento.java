package org.ufv.dis.Front;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class FormCovidRegistro_Evento extends ComponentEvent<FormCovid_Registro> {
    private Data data;
    public FormCovidRegistro_Evento(FormCovid_Registro source, Data data) {
        super(source, false);
        this.data=data;
    }
    public Data getData() {
        return data;
    }
}
