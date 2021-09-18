package com.github.namhokim.packager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.namhokim.packager.error.ExternalConnectionException;
import com.github.namhokim.packager.external.Dish;
import com.github.namhokim.packager.external.TablewareApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

@Service
public class TablewareService {
    private final TablewareApi tablewareApi;

    public TablewareService(
            @Value("${external.tableware.base-url}") String baseUrl,
            ObjectMapper mapper) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        this.tablewareApi = retrofit.create(TablewareApi.class);
    }

    public List<Dish> getDishes(Long size) {
        final Call<List<Dish>> call = tablewareApi.getDishes(size);
        final Response<List<Dish>> response;
        try {
            response = call.execute();
            return response.body();
        } catch (IOException e) {
            throw new ExternalConnectionException("Cannot connect to tableware API", e);
        }
    }
}
