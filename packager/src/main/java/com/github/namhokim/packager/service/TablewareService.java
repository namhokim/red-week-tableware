package com.github.namhokim.packager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.namhokim.packager.error.ExternalConnectionException;
import com.github.namhokim.packager.error.ExternalNoResponseException;
import com.github.namhokim.packager.external.TablewareApi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
public class TablewareService {
    private final TablewareApi tablewareApi;
    private final ObjectMapper objectMapper;

    public TablewareService(@Value("${external.tableware.base-url}") String baseUrl, ObjectMapper objectMapper) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();

        this.tablewareApi = retrofit.create(TablewareApi.class);
        this.objectMapper = objectMapper;
    }

    public CountDownLatch getDishes(Long size, Writer writer) {
        final CountDownLatch latch = new CountDownLatch(1);

        final Call<ResponseBody> call = tablewareApi.getDishes(size);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    log.info("onResponse: Start");
                    final ResponseBody body = response.body();
                    if (body != null) {
                        JsonArrayToCsvParser parser = new JsonArrayToCsvParser(objectMapper);
                        parser.convertJsonArrayToCsv(body.charStream(), writer);
                    } else {
                        throw new ExternalNoResponseException("Empty dishes was found.");
                    }
                } catch (IOException e) {
                    log.error("Dishes handling Error", e);
                } finally {
                    log.info("onResponse: Done");
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable cause) {
                latch.countDown();
                throw new ExternalConnectionException("Dishes handling Failure", cause);
            }

        });

        return latch;
    }
}
