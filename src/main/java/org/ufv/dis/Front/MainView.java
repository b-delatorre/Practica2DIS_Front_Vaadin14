package org.ufv.dis.Front;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.tabs.Tab;
//import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URISyntaxException;


@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@PageTitle("Datos covid_19")
@Route
public class MainView extends VerticalLayout{

    public MainView(@Autowired CovidService service) throws URISyntaxException, IOException, InterruptedException {
        //HorizontalLayout inputs=new HorizontalLayout();
        VerticalLayout results_General=new VerticalLayout();
        VerticalLayout results_Mayores=new VerticalLayout();
        VerticalLayout Content_Tab_General=new VerticalLayout();  //Creamos el vertical layout que contendrá la pestaña de datos generales
        VerticalLayout Content_tab_Mayor=new VerticalLayout();      //Creamos el vertical layout que contendrá la pestaña de datos mayores
        //Tab tab_General = new Tab("Tasa acumulada poblacion general");
        //Tab tab_Mayores = new Tab("Tasa acumulada poblacion mayores de 65");


        FormCovid_General formulario_general=new FormCovid_General(this);
        FormCovid_Mayores formulario_mayores=new FormCovid_Mayores(this);
        FormCovid_General formulario_nuevoRegistro=new FormCovid_General(this);

        /*ComboBox<String> comboBox=new ComboBox<>("Seleccione el tipo de info....");
        comboBox.setAllowCustomValue(false);
        comboBox.setItems("Tasa acumulada poblacion general", "Tasa acumulada poblacion mayores de 65");
        comboBox.setHelperText("Seleccione el tipo de dato");
        inputs.add(comboBox);*/

        Grid<Data> grid_General= new Grid<>(Data.class,false);
        grid_General.addColumn(Data::getCasos).setHeader("Casos");
        grid_General.addColumn(Data::getCod).setHeader("Codigo");
        grid_General.addColumn(Data::getFecha).setHeader("Fecha");
        grid_General.addColumn(Data::getTasa14).setHeader("Tasa 14 dias");
        grid_General.addColumn(Data::getTasaTotal).setHeader("Tasa Total");
        grid_General.addColumn(Data::getZona).setHeader("Zona");
        Grid<DataMayor> grid_Mayor= new Grid<>(DataMayor.class,false);
        grid_Mayor.addColumn(DataMayor::getCasos).setHeader("Casos");
        grid_Mayor.addColumn(DataMayor::getCod).setHeader("Codigo");
        grid_Mayor.addColumn(DataMayor::getFecha).setHeader("Fecha");
        grid_Mayor.addColumn(DataMayor::getTasa14).setHeader("Tasa 14 dias");
        grid_Mayor.addColumn(DataMayor::getZona).setHeader("Zona");

        Button nuevoRegistro = new Button("Nuevo registro", click -> formulario_general.setVisible(true));

        // Creamos las pestañas
        Tab tab_General = new Tab("Tasa acumulada poblacion general");
        Tab tab_Mayores = new Tab("Tasa acumulada poblacion mayores de 65");


        Tabs Pestanas = new Tabs (); //Creamos el TabSheet Pestanas que contendra las dos Pestañas con las dos secciones

        results_General.removeAll(); //Por si quedaba algo residual restablecemos la variable results
        grid_General.setItems(service.leeCovidMenor()); //Obtenemos los elementos del grid de la llamada de la api
        results_General.add(grid_General);
        Content_Tab_General.add(nuevoRegistro, results_General,formulario_general); //Y los añadimos a la pestaña de datos generales

        tab_General.add(Content_Tab_General);

        //Pestanas.add("Tasa acumulada poblacion general",Content_Tab_General);
        //Repetimos lo anterior pero para los datos de personas mayores
        results_Mayores.removeAll();
        grid_Mayor.setItems(service.leeCovidMayor());
        results_Mayores.add(grid_Mayor);
        Content_tab_Mayor.add(results_Mayores,formulario_mayores);
        tab_Mayores.add(Content_tab_Mayor);
        //Pestanas.add("Tasa acumulada poblacion mayores de 65",Content_tab_Mayor);

        Pestanas.setSizeFull();

        add(Pestanas);
        formulario_general.setVisible(false);
        grid_General.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                formulario_general.setVisible(false);
            } else {
                formulario_general.TablaAFormGen(event.getValue());
                Content_Tab_General.setSizeFull();
                // Actualizamos el grid
            }
        });
        formulario_mayores.setVisible(false);
        grid_Mayor.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                formulario_mayores.setVisible(false);
            } else {
                formulario_mayores.Tabla_A_FormMayores(event.getValue());
                Content_tab_Mayor.setSizeFull();
            }
        });

    }
}
