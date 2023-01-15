package org.ufv.dis.Front;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
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
    private VerticalLayout results_Mayores=new VerticalLayout();
    private VerticalLayout Content_tab_Mayor=new VerticalLayout();

    private Data lastItem;

    public MainView(@Autowired CovidService service) throws URISyntaxException, IOException, InterruptedException {
        //HorizontalLayout inputs=new HorizontalLayout();
        VerticalLayout results_General=new VerticalLayout();
        VerticalLayout results_Mayores=new VerticalLayout();
          //Creamos el vertical layout que contendrá la pestaña de datos generales
        VerticalLayout Content_tab_Mayor=new VerticalLayout();      //Creamos el vertical layout que contendrá la pestaña de datos mayores

        //Tab tab_General = new Tab("Tasa acumulada poblacion general");
        //Tab tab_Mayores = new Tab("Tasa acumulada poblacion mayores de 65");

        /*ComboBox<String> comboBox=new ComboBox<>("Seleccione el tipo de info....");
        comboBox.setAllowCustomValue(false);
        comboBox.setItems("Tasa acumulada poblacion general", "Tasa acumulada poblacion mayores de 65");
        comboBox.setHelperText("Seleccione el tipo de dato");
        inputs.add(comboBox);*/

        Grid<Data> grid_General= new Grid<>(Data.class,false);
        grid_General.addColumn(Data::getCasos14).setHeader("Casos 14 dias");
        grid_General.addColumn(Data::getCasos).setHeader("Casos");
        grid_General.addColumn(Data::getCod).setHeader("Codigo");
        grid_General.addColumn(Data::getFecha).setHeader("Fecha");
        grid_General.addColumn(Data::getTasa14).setHeader("Tasa 14 dias");
        grid_General.addColumn(Data::getTasaTotal).setHeader("Tasa Total");
        grid_General.addColumn(Data::getZona).setHeader("Zona");


        grid_Mayor.addColumn(DataMayor::getCasos).setHeader("Casos");
        grid_Mayor.addColumn(DataMayor::getCod).setHeader("Codigo");
        grid_Mayor.addColumn(DataMayor::getFecha).setHeader("Fecha");
        grid_Mayor.addColumn(DataMayor::getTasa14).setHeader("Tasa 14 dias");
        grid_Mayor.addColumn(DataMayor::getZona).setHeader("Zona");

        formulario_nuevoRegistro=new FormCovid_Registro(this);
        Button nuevoRegistro_Gen = new Button("Nuevo registro", click -> formulario_nuevoRegistro.setVisible(true));
        Button nuevoRegistro_May=new Button("Nuevo registro",click -> formulario_mayores.setVisible(true));

        // Creamos las pestañas
        //Tab tab_General = new Tab("Tasa acumulada poblacion general");
        //Tab tab_Mayores = new Tab("Tasa acumulada poblacion mayores de 65");
        HorizontalLayout tabBar = new HorizontalLayout();
        Button Boton_General = new Button("Tasa acumulada poblacion general");
        tabBar.add(Boton_General);
        Button Boton_mayores = new Button("Tasa acumulada poblacion mayores de 65");
        tabBar.add(Boton_mayores);

        VerticalLayout firstTabLayout = new VerticalLayout();
        VerticalLayout secondTabLayout = new VerticalLayout();





        //Tabs Pestanas = new Tabs (); //Creamos el TabSheet Pestanas que contendra las dos Pestañas con las dos secciones

        results_General.removeAll(); //Por si quedaba algo residual restablecemos la variable results
        grid_General.setItems(service.leeCovidMenor()); //Obtenemos los elementos del grid de la llamada de la api
        results_General.add(grid_General);
        Content_Tab_General.add(nuevoRegistro_Gen, results_General,formulario_general); //Y los añadimos a la pestaña de datos generales
        //tab_General.add(Content_Tab_General);

        //Pestanas.add("Tasa acumulada poblacion general",Content_Tab_General);
        //Repetimos lo anterior pero para los datos de personas mayores
        results_Mayores.removeAll();
        grid_Mayor.setItems(service.leeCovidMayor());
        results_Mayores.add(grid_Mayor);
        Content_tab_Mayor.add(nuevoRegistro_May,results_Mayores,formulario_mayores);
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
       // tab_Mayores.add(Content_tab_Mayor);
        //Pestanas.add("Tasa acumulada poblacion mayores de 65",Content_tab_Mayor);

        //Pestanas.setSizeFull();

        //add(Pestanas);
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
            }
        });
        //grid_General.getDataProvider().refreshItem(formulario_general.get_Dato_General());
        //grid_General.getDataProvider().refreshAll();

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

            }
        });

    }

    public void UpdateGrid(Data UpdatedItem){

        grid_General.getDataProvider().refreshItem(UpdatedItem);
        grid_General.getDataProvider().refreshAll();
    }
    public void UpdateGridMayores(DataMayor UpdatedItem){

        grid_Mayor.getDataProvider().refreshItem(formulario_mayores.get_Dato_General());
        grid_Mayor.getDataProvider().refreshAll();
    }

    //public Data getLastItem() {
       // List<Data> allItems = grid_General.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
        //lastItem = allItems.stream().sorted((i1,i2) -> i2.getCod().compareTo(i1.getCod())).findFirst().orElse(null);
      //  return lastItem;

    //}


    /*public void onSaveEvent(Data updatedItem) {


        //Content_Tab_General.setSizeFull();
        grid_General.getDataProvider().refreshItem(formulario_general.get_Dato_General());
        grid_General.setItems(updatedItem);

        grid_General.getDataProvider().refreshAll();
        results_General.setVisible(false);
        results_General.setVisible(true);
        results_General.remove(grid_General);
        results_General.add(grid_General);
        Content_Tab_General.setVisible(false);
        Content_Tab_General.setVisible(true);
        Content_Tab_General.remove(results_General);
        Content_Tab_General.add(results_General);
        mainView.setVisible(false);
        mainView.setVisible(true);
        mainView.remove(Content_Tab_General);
        mainView.add(Content_Tab_General);
        mainView.setSizeFull();
        Content_Tab_General.setSizeFull();


        //grid_Mayor.getDataProvider().refreshItem(formulario_general.ge());
    }*/
}
