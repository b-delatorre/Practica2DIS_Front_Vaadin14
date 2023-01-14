package org.ufv.dis.Front;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URISyntaxException;

@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@PageTitle("Datos covid_19")
@Route
public class MainView extends VerticalLayout {
    public MainView(@Autowired CovidService service) throws URISyntaxException, IOException, InterruptedException {

        VerticalLayout contentTabGeneral = new VerticalLayout();
        VerticalLayout contentTabMayores = new VerticalLayout();

        VerticalLayout results_General = new VerticalLayout();
        VerticalLayout results_Mayores = new VerticalLayout();

        Grid<Data> grid_General = new Grid<>(Data.class, false);
        Grid<DataMayor> grid_Mayores = new Grid<>(DataMayor.class, false);

        Tab tabGeneral = new Tab("Tasa acumulada poblacion general");
        Tab tabMayores = new Tab("Tasa acumulada poblacion mayores de 65");

        FormCovid_General formulario_general = new FormCovid_General(this);
        FormCovid_Mayores formulario_mayores = new FormCovid_Mayores(this);

        //Configuramos las dos tablas
        grid_General.addClassName("general-grid");
        grid_General.addColumn(Data::getCod).setHeader("Codigo");
        grid_General.addColumn(Data::getCasos).setHeader("Casos");
        grid_General.addColumn(Data::getFecha).setHeader("Fecha");
        grid_General.addColumn(Data::getTasa14).setHeader("Tasa 14 dias");
        grid_General.addColumn(Data::getTasaTotal).setHeader("Tasa Total");
        grid_General.addColumn(Data::getZona).setHeader("Zona");

        grid_General.getColumns().forEach(col -> col.setAutoWidth(true));


        grid_Mayores.addClassName("mayores-grid");
        grid_Mayores.addColumn(DataMayor::getCasos).setHeader("Casos");
        grid_Mayores.addColumn(DataMayor::getCod).setHeader("Codigo");
        grid_Mayores.addColumn(DataMayor::getFecha).setHeader("Fecha");
        grid_Mayores.addColumn(DataMayor::getTasa14).setHeader("Tasa 14 dias");
        grid_Mayores.addColumn(DataMayor::getZona).setHeader("Zona");

        grid_General.getColumns().forEach(col -> col.setAutoWidth(true));

        // Definimos las dos pestañas
        Tabs pestanas = new Tabs(tabGeneral, tabMayores);
        pestanas.setOrientation(Tabs.Orientation.HORIZONTAL);

        // Configuramos un evento para la seleccion de pestaña
        pestanas.addSelectedChangeListener(event ->
            {
                if (pestanas.equals(tabGeneral)) {
                    // Limpiamos por si hay datos basura
                    results_General.removeAll();
                    // Obtenemos los datos de la llamda a la api
                    try {
                        grid_General.setItems(service.leeCovidMenor());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    results_General.add(grid_General);
                    contentTabGeneral.add(results_General, formulario_general);
                }
                else if (pestanas.equals(tabMayores)) {
                    results_General.removeAll();
                    try {
                        grid_Mayores.setItems(service.leeCovidMayor());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    results_Mayores.add(grid_Mayores);
                    contentTabMayores.add(results_Mayores, formulario_mayores);
                }
            }
        );

        if (pestanas.equals(tabGeneral)) {
            // Limpiamos por si hay datos basura
            results_General.removeAll();
            // Obtenemos los datos de la llamda a la api
            try {
                grid_General.setItems(service.leeCovidMenor());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            results_General.add(grid_General);
            contentTabGeneral.add(results_General, formulario_general);
        }
        else if (pestanas.equals(tabMayores)) {
            results_General.removeAll();
            try {
                grid_Mayores.setItems(service.leeCovidMayor());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            results_Mayores.add(grid_Mayores);
            contentTabMayores.add(results_Mayores, formulario_mayores);
        }


        add(pestanas, contentTabGeneral, contentTabMayores);

        // Configuramos los eventos para seleccion de filas
        formulario_general.setVisible(false);
        grid_General.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                formulario_general.setVisible(false);
            } else {
                formulario_general.TablaAFormGen(event.getValue());
                contentTabGeneral.setSizeFull();
            }
        });
        formulario_mayores.setVisible(false);
        grid_Mayores.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                formulario_mayores.setVisible(false);
            } else {
                formulario_mayores.Tabla_A_FormMayores(event.getValue());
                contentTabMayores.setSizeFull();
            }
        });

    }
}