package com.github.namhokim.packager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonObjectKeeper {
    private final Map<String, String> map = new LinkedHashMap<>();  // 넣은 순서 보장을 위해 LinkedHashMap 사용
    private final CsvMapper csvMapper = new CsvMapper();
    boolean isFirstRow = true;
    private String fieldName = null;
    private CsvSchema csvSchemaWithoutHeader;

    public void startNewObject() {
        this.map.clear();
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldValue(String fieldValue) {
        this.map.put(this.fieldName, fieldValue);
    }

    public String convertToCsvLines() throws JsonProcessingException {
        return csvMapper.writerFor(Map.class)
                .with(getSchema())
                .writeValueAsString(map);
    }

    /**
     * 첫 행일 경우만 헤더가 필요한 스키마를 반환한다.
     */
    private CsvSchema getSchema() {
        if (this.isFirstRow) {
            this.isFirstRow = false;

            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
            map.keySet().forEach(csvSchemaBuilder::addColumn);
            csvSchemaWithoutHeader = csvSchemaBuilder.build().withoutHeader();

            return csvSchemaBuilder.build().withHeader();
        }

        return csvSchemaWithoutHeader;
    }

}
