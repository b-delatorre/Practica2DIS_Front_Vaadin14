package org.ufv.dis.Front;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@PageTitle("Datos covid_19")
@Route
public class MainView extends VerticalLayout{
    private  Grid<Data> grid_General= new Grid<>(Data.class,false);
    private Grid<DataMayor> grid_Mayor= new Grid<>(DataMayor.class,false);
    private FormCovid_General formulario_general=new FormCovid_General(this);
    private FormCovid_Mayores formulario_mayores=new FormCovid_Mayores(this);
    private FormCovid_Registro formulario_nuevoRegistro;
    private VerticalLayout Content_Tab_General=new VerticalLayout();
    private VerticalLayout results_General=new VerticalLayout();
    private VerticalLayout mainView=new VerticalLayout();

    private Data lastItem;

    public MainView(@Autowired CovidService service) throws URISyntaxException, IOException, InterruptedException {

        VerticalLayout results_General=new VerticalLayout();
        VerticalLayout results_Mayores=new VerticalLayout();

          //Creamos el vertical layout que contendrá la pestaña de datos generales
        VerticalLayout Content_tab_Mayor=new VerticalLayout();      //Creamos el vertical layout que contendrá la pestaña de datos mayores

        Grid<Data> grid_General= new Grid<>(Data.class,false);
        grid_General.addColumn(Data::getCod).setHeader("Codigo");
        grid_General.addColumn(Data::getCasos14).setHeader("Casos 14 dias");
        grid_General.addColumn(Data::getCasos).setHeader("Casos");
        grid_General.addColumn(Data::getFecha).setHeader("Fecha");
        grid_General.addColumn(Data::getTasa14).setHeader("Tasa 14 dias");
        grid_General.addColumn(Data::getTasaTotal).setHeader("Tasa Total");
        grid_General.addColumn(Data::getZona).setHeader("Zona");

        grid_Mayor.addColumn(DataMayor::getCod).setHeader("Codigo");
        grid_Mayor.addColumn(DataMayor::getCasos).setHeader("Casos");
        grid_Mayor.addColumn(DataMayor::getFecha).setHeader("Fecha");
        grid_Mayor.addColumn(DataMayor::getTasa14).setHeader("Tasa 14 dias");
        grid_Mayor.addColumn(DataMayor::getZona).setHeader("Zona");

        formulario_nuevoRegistro=new FormCovid_Registro(this);
        Button nuevoRegistro_Gen = new Button("Nuevo registro", click -> formulario_nuevoRegistro.setVisible(true));

        // Creamos las pestañas
        HorizontalLayout tabBar = new HorizontalLayout();
        Button Boton_General = new Button("Tasa acumulada poblacion general");
        tabBar.add(Boton_General);
        Button Boton_mayores = new Button("Tasa acumulada poblacion mayores de 65");
        tabBar.add(Boton_mayores);


        results_General.removeAll(); //Por si quedaba algo residual restablecemos la variable results
        grid_General.setItems(service.leeCovidMenor()); //Obtenemos los elementos del grid de la llamada de la api
        results_General.add(grid_General);
        Content_Tab_General.add(nuevoRegistro_Gen, results_General,formulario_general); //Y los añadimos a la pestaña de datos generales

        //Repetimos lo anterior pero para los datos de personas mayores
        results_Mayores.removeAll();
        grid_Mayor.setItems(service.leeCovidMayor());
        results_Mayores.add(grid_Mayor);
        Content_tab_Mayor.add(results_Mayores,formulario_mayores);
        mainView.add(tabBar);
        mainView.add(Content_Tab_General);
        Boton_General.addClickListener(event -> { //Si el usuario presiona el botón para ir a la otra pestaña sustituimos
            mainView.remove(Content_tab_Mayor);
            mainView.add(Content_Tab_General);
            add(mainView);

        });

        Boton_mayores.addClickListener(event -> {
            mainView.remove(Content_Tab_General);
            mainView.add(Content_tab_Mayor);
            add(mainView);
        });
        add(mainView);

        // Formularios de muestra de datos completos de los registros cuando se seleccionan
        formulario_general.setVisible(false);
        grid_General.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                formulario_general.setVisible(false);
            } else {
                formulario_general.TablaAFormGen(event.getValue());
                Content_Tab_General.setSizeFull();
                grid_General.getDataProvider().refreshItem(formulario_general.get_Dato_General());
                List<Data> datosNew = grid_General.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
                try {
                    service.enviaCovidMenor(datosNew);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                formulario_general.getAceptar().addClickListener(event1 -> {
                    grid_General.getDataProvider().refreshItem(formulario_general.get_Dato_General());
                });
            }
        });

        formulario_mayores.setVisible(false);
        grid_Mayor.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                formulario_mayores.setVisible(false);
            } else {
                formulario_mayores.Tabla_A_FormMayores(event.getValue());
                Content_tab_Mayor.setSizeFull();
                grid_Mayor.getDataProvider().refreshItem(formulario_mayores.get_Dato_General());
                List<DataMayor> datosNew2 = grid_Mayor.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
                try {
                    service.enviaCovidMayor(datosNew2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                formulario_mayores.getAceptar().addClickListener(event1 -> {
                    grid_Mayor.getDataProvider().refreshItem(formulario_mayores.get_Dato_General());
                });
            }
        });

        //Con el botón de nuevo registro, se crea un modal para añadir la nueva entrada
        nuevoRegistro_Gen.addClickListener(e -> {
            List<Data> datosNew = grid_General.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
            ArrayList<Data> finalDatosFinal = (ArrayList<Data>) datosNew;
            Dialog modal = new Dialog();
            HorizontalLayout layoutCampo = new HorizontalLayout();
            VerticalLayout botonesVertical = new VerticalLayout();
            HorizontalLayout botonesHori = new HorizontalLayout();
            modal.getElement().setAttribute("aria-label", "Nueva entrada");
            TextField zonaNueva = new TextField("Zona");
            IntegerField tasa14Nueva = new IntegerField("Tasa los primeros 14 días");
            IntegerField tasaTotalNueva = new IntegerField("Tasa total");
            IntegerField casosNuevo = new IntegerField("Casos");
            IntegerField casos14Nuevo = new IntegerField("Casos últimos 14 días");
            DateTimePicker fechaNueva = new DateTimePicker("Fecha");

            Button save = new Button("Añadir");
            save.addClickListener(eve -> {
                int newCode = Integer.parseInt(finalDatosFinal.get(finalDatosFinal.size() - 1).getCod());
                newCode++;
                String fechaNew = String.valueOf(fechaNueva.getValue());
                fechaNew = fechaNew.replaceAll("-", "/");
                fechaNew = fechaNew.replaceAll("T", " ");
                fechaNew = fechaNew.concat(":00");
                Data nuevaData = new Data(String.valueOf(newCode), zonaNueva.getValue(), tasa14Nueva.getValue(), tasaTotalNueva.getValue(), casosNuevo.getValue(), casos14Nuevo.getValue(),fechaNew);
                finalDatosFinal.add(nuevaData);
                grid_General.setItems(finalDatosFinal);
                try {
                    service.enviaCovidMenor(grid_General.getDataProvider().fetch(new Query<>()).collect(Collectors.toList()));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                modal.close();
            });
            Button cancel = new Button("Cancelar", event -> {
                modal.close();
            });
            layoutCampo.add(zonaNueva, tasa14Nueva, tasaTotalNueva, casosNuevo, casos14Nuevo,fechaNueva);
            botonesHori.add(save, cancel);
            botonesVertical.add(botonesHori);
            modal.add(layoutCampo, botonesVertical);
            modal.open();
        });
    }
}
