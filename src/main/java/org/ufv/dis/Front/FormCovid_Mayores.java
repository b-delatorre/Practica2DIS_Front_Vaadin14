package org.ufv.dis.Front;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class FormCovid_Mayores extends FormLayout {
    private TextField cod =new TextField("codigo_geometria");
    private TextField zona=new TextField("zona_basica_salud");
    private TextField tasa14 =new TextField("Incidencia >60 años 14_dias");
    private TextField casos =new TextField("Casos >60 años 14 dias");

    private Button Aceptar=new Button("Aceptar");
    private Button Cancelar=new Button("Cancelar");
    private MainView myUI;
    private DataMayor dato_Mayor;
    private Binder<DataMayor> binder=new Binder<>(DataMayor.class);

    public FormCovid_Mayores(MainView myUI){
        this.myUI=myUI;
        cod.setReadOnly(true);
        setSizeUndefined();

        Aceptar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Aceptar.addClickShortcut(Key.ENTER);

        Cancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Cancelar.addClickShortcut(Key.ENTER);

        Aceptar.addClickListener(e -> setVisible(false));
        Cancelar.addClickListener(e -> setVisible(false));

        HorizontalLayout botones=new HorizontalLayout(Aceptar,Cancelar);
        cod.setReadOnly(true);
        add(cod,zona, tasa14, casos,botones);

        binder.bindInstanceFields(this);
    }
    public void Tabla_A_FormMayores(DataMayor dataMayor) { //Si el usuario hace click en alguna de las filas entonces se muestra y se copian los datos del que esta seleccionado en el formulario
        this.dato_Mayor = dataMayor;
        binder.setBean(dataMayor);
        setVisible(true);
    }
    public DataMayor get_Dato_General(){
        return dato_Mayor;
    }

    public Button getAceptar() {
        return Aceptar;
    }
}
