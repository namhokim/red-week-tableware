package com.github.namhokim.packager.endpoint;

import com.github.namhokim.packager.external.Dish;
import com.github.namhokim.packager.service.TablewareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PackageDishEndpoint {

    private final TablewareService tablewareService;

    public PackageDishEndpoint(TablewareService tablewareService) {
        this.tablewareService = tablewareService;
    }

    @GetMapping("/package/{size}")
    List<Dish> packaging(@PathVariable Long size) {
        return tablewareService.getDishes(size);
    }

}
