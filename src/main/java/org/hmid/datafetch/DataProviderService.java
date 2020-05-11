package org.hmid.datafetch;

import com.google.gson.JsonObject;
import org.hmid.datafetch.model.CountryData;
import org.hmid.datafetch.model.CovidDataModel;
import org.hmid.datafetch.model.GlobalData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.CompletableFuture;

//consommation d api
public class DataProviderService {
    public CovidDataModel getData(String countryName) {
        //La méthode getData() va nous permettre de récupérer les données
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://coronavirus-19-api.herokuapp.com/")// base url du site de l'api
                .addConverterFactory(GsonConverterFactory.create())//creer le convertor gson
                .build();//le contruire

        //je fait appel a mon interface api
        CovidApi covidApi = retrofit.create(CovidApi.class);

        // premier Future
        CompletableFuture<GlobalData> callback1 = new CompletableFuture<>();

        //faisons appel à l’interface pour créer l’objet de connexion
        covidApi.getGlobalData()
                .enqueue(new Callback<GlobalData>() {
                    @Override
                    public void onResponse(Call<GlobalData> call, Response<GlobalData> response) {
                        callback1.complete(response.body());
                    }

                    @Override
                    public void onFailure(Call<GlobalData> call, Throwable t) {
                        callback1.completeExceptionally(t);
                    }
                });
        // Deuxième Future
        CompletableFuture<CountryData> callback2 = new CompletableFuture<>();

        covidApi.getCountryData(countryName)
                .enqueue(new Callback<CountryData>() {
                    @Override
                    public void onResponse(Call<CountryData> call, Response<CountryData> response) {
                        callback2.complete(response.body());
                    }

                    @Override
                    public void onFailure(Call<CountryData> call, Throwable t) {
                        callback2.completeExceptionally(t);
                    }
                });
        // L'obtention des Futures
        GlobalData globalData = callback1.join();
        CountryData countryData = callback2.join();
        // retour du model
        return new CovidDataModel(globalData,countryData);
    }
}
