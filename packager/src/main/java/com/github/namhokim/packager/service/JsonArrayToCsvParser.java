package com.github.namhokim.packager.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JsonArrayToCsvParser {

    private final ObjectMapper objectMapper;

    public JsonArrayToCsvParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void convertJsonArrayToCsv(Reader reader, Writer writer) throws IOException {
        try (final JsonParser parser = objectMapper.createParser(reader)) {
            final JsonObjectKeeper keeper = new JsonObjectKeeper();

            JsonToken token;
            while ((token = parser.nextToken()) != null) {
                if (token == JsonToken.START_OBJECT) {
                    keeper.startNewObject();
                }
                if (token == JsonToken.FIELD_NAME) {
                    keeper.setFieldName(parser.getText());
                }
                if (token == JsonToken.VALUE_STRING) {
                    keeper.setFieldValue(parser.getText());
                }
                if (token == JsonToken.END_OBJECT) {
                    final String csvLines = keeper.convertToCsvLines();
                    writer.write(csvLines);
                }
            }

        }
    }

}
