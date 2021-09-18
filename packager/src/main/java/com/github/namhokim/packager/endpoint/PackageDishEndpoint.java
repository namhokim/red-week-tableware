package com.github.namhokim.packager.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.namhokim.packager.service.TablewareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@RestController
public class PackageDishEndpoint {

    private final TablewareService tablewareService;
    private final ObjectMapper objectMapper;

    public PackageDishEndpoint(TablewareService tablewareService, ObjectMapper objectMapper) {
        this.tablewareService = tablewareService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/package/{size}")
    public void packaging(@PathVariable Long size, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/csv");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        final Reader reader = tablewareService.getDishes(size);
        final PrintWriter writer = response.getWriter();
        convertJsonToCsv(reader, writer);
    }

    private void convertJsonToCsv(Reader reader, Writer writer) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(reader);
        Iterator<JsonNode> elements = jsonNode.elements();
        if (elements.hasNext()) {
            JsonNode firstObject = elements.next();
            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
            firstObject.fieldNames().forEachRemaining(csvSchemaBuilder::addColumn);
            new CsvMapper().writerFor(JsonNode.class)
                    .with(csvSchemaBuilder.build().withHeader())
                    .writeValue(writer, jsonNode);
        }
    }

}
