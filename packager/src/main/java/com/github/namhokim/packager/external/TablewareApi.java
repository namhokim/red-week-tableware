package com.github.namhokim.packager.external;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface TablewareApi {

    @GET("/order/dishes")
    Call<List<Dish>> getDishes(@Query("size") Long size);

}
