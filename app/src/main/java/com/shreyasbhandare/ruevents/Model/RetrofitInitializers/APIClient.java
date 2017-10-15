package com.shreyasbhandare.ruevents.Model.RetrofitInitializers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static String baseURL = "https://graph.facebook.com/";
    public static Retrofit retrofit;

    public static Retrofit getAPIClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
