package net.dimjasevic.karlo.fer.evidentor.assets_service.api.v1.buildings;

import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/{buildingId}")
    public ResponseEntity<Building> getBuilding(@PathVariable("buildingId") Long buildingId) {
        try {
            Building building = buildingService.getById(buildingId);
            return new ResponseEntity<>(building, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
