package com.github.namhokim.packager.external;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TablewareApi {

    @GET("/order/dishes")
    Call<ResponseBody> getDishes(@Query("size") Long size);

}
