package org.ufv.dis.Front;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

@Service
public class CovidService implements Serializable {
    public ArrayList<Data> leeCovidMenor() throws URISyntaxException,IOException,InterruptedException{
        APIMenor api=new APIMenor();
        String resultsAPI=api.getCovidMenor();
        Gson gson=new Gson();
        Covid19Menor covid19Menor=gson.fromJson(resultsAPI,new TypeToken<Covid19Menor>(){}.getType());
        ArrayList<Data> datos_general=covid19Menor.getData();
        return datos_general;
    }

    public ArrayList<DataMayor> leeCovidMayor() throws URISyntaxException,IOException,InterruptedException{
        APIMayor api=new APIMayor();
        String resultsAPI=api.getCovidMayor();
        Gson gson=new Gson();
        Covid19Mayor covid19Mayor=gson.fromJson(resultsAPI,new TypeToken<Covid19Mayor>(){}.getType());
        ArrayList<DataMayor> datos_mayores= covid19Mayor.getData();
        return datos_mayores;
    }
    public void enviaCovidMenor(List<Data> datos) throws URISyntaxException, IOException, InterruptedException {
        APIMenor api = new APIMenor();
        ArrayList<Data> datosPost = (ArrayList<Data>) datos;
        Covid19Menor covidNuevo = new Covid19Menor(datosPost);
        Gson gson = new Gson();
        String datosString = gson.toJson(covidNuevo);
        api.setCovidMenor(datosString);
    }
    public void enviaCovidMayor(List<DataMayor> datos) throws URISyntaxException, IOException, InterruptedException {
        APIMayor api = new APIMayor();
        ArrayList<DataMayor> datosPost = (ArrayList<DataMayor>) datos;
        Covid19Mayor covidNuevo = new Covid19Mayor(datosPost);
        Gson gson = new Gson();
        String datosString = gson.toJson(covidNuevo);
        api.setCovidMayor(datosString);
    }
}
