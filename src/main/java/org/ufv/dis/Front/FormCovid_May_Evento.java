package org.ufv.dis.Front;

import com.vaadin.flow.component.ComponentEvent;

public class FormCovid_May_Evento extends ComponentEvent<FormCovid_Mayores> {
    private DataMayor data;
    public FormCovid_May_Evento(FormCovid_Mayores source, DataMayor data) {
        super(source, false);
        this.data=data;
    }
    public DataMayor getData() {
        return data;
    }
}
