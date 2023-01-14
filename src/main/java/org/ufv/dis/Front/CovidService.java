package org.ufv.dis.Front;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
}
