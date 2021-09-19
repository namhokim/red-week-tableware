package com.github.namhokim.packager.endpoint;

import com.github.namhokim.packager.service.TablewareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class PackageDishEndpoint {

    private final TablewareService tablewareService;

    public PackageDishEndpoint(TablewareService tablewareService) {
        this.tablewareService = tablewareService;
    }

    @GetMapping("/package/{size}")
    public void packaging(@PathVariable Long size, HttpServletResponse response) throws IOException, InterruptedException {
        response.setHeader("Content-Type", "text/csv");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        tablewareService.getDishes(size, response.getWriter()).await();
    }

}
