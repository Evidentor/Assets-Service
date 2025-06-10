package net.dimjasevic.karlo.fer.evidentor.assets_service.rest.v1;

import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.EntityNotFoundMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.BuildingMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.mapper.v1.BuildingResponseMapper;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.BuildingService;
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

    @GetMapping("/{buildingId}/floors/{floorId}")
    public ResponseEntity<ContentMetaResponse<?, ?>> getBuildingFloor(
            @PathVariable("buildingId") Long buildingId,
            @PathVariable("floorId") Long floorId
    ) {
        Building building;
        try {
            building = buildingService.getBuildingFloor(buildingId, floorId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ContentMetaResponse<>(null, new EntityNotFoundMetaResponse(e.getMessage()))
            );
        }

        Integer totalNumberOfFloors = buildingService.getNumberOfFloors(buildingId);
        ContentMetaResponse<BuildingResponse, BuildingMetaResponse> response;
        response = BuildingResponseMapper.map(
                building,
                building.getFloors().iterator().next(),
                totalNumberOfFloors
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{buildingId}/floors/{floorId}/users")
    public ResponseEntity<?> getBuildingFloorPresence(
            @PathVariable("buildingId") Long buildingId,
            @PathVariable("floorId") Long floorId
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{buildingId}/floors/{floorId}/users/{userId}")
    public ResponseEntity<?> getBuildingFloorUserPresence(
            @PathVariable("buildingId") Long buildingId,
            @PathVariable("floorId") Long floorId,
            @PathVariable("userId") Long userId
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
