package org.ufv.dis.Front;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class FormCovid_General extends FormLayout {
    private TextField cod = new TextField("codigo_geometria");
    private TextField zona = new TextField("zona_basica_salud");
    private TextField tasa14 = new TextField("Incidencia_14_dias");
    private TextField tasaTotal = new TextField("Incidencia_Total");
    private TextField casos = new TextField("Total_Casos");

    private Button Aceptar = new Button("Aceptar");
    private Button Cancelar = new Button("Cancelar", e -> setVisible(false));
    private MainView myUI;
    private Data dato_General;
    private Binder<Data> binder = new Binder<>(Data.class);

    public FormCovid_General(MainView myUI) {

        this.myUI = myUI;
        setSizeUndefined();
        cod.setReadOnly(true);
        add(cod, zona, tasa14);

        Aceptar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Aceptar.addClickShortcut(Key.ENTER);

        Cancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Cancelar.addClickShortcut(Key.ENTER);

        Aceptar.addClickListener(e -> setVisible(false));
        HorizontalLayout botones = new HorizontalLayout(Aceptar, Cancelar);
        add(cod, zona, tasa14, tasaTotal, casos, casos, botones);

        binder.bindInstanceFields(this);

        Aceptar.addClickListener(e -> GuardarCambios());
        //binder.addStatusChangeListener(e -> GuardarCambios().setEnabled(binder.isValid()));

    }

    public void TablaAFormGen(Data datoGeneral) { //Si el usuario hace click en alguna de las filas entonces se muestra y se copian los datos del que esta seleccionado en el formulario
        this.dato_General = datoGeneral;
        binder.setBean(datoGeneral);
        setVisible(true);
    }
    private void GuardarCambios() {
        try {
            binder.writeBean(dato_General);
            fireEvent(new SaveEvent(this, dato_General));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Data get_Dato_General(){
        return dato_General;
    }

}