package net.dimjasevic.karlo.fer.evidentor.assets_service.rest.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.RoomPresence;
import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.UserPresence;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.EntityNotFoundMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingFloorPresenceResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingUserPresenceResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.BuildingMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.mapper.v1.BuildingFloorPresenceResponseMapper;
import net.dimjasevic.karlo.fer.evidentor.assets_service.mapper.v1.BuildingResponseMapper;
import net.dimjasevic.karlo.fer.evidentor.assets_service.mapper.v1.BuildingUserPresenceResponseMapper;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.BuildingService;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.view.RoomPresenceService;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.view.UserPresenceService;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/buildings")
public class BuildingController {

    private final BuildingService buildingService;
    private final RoomPresenceService roomPresenceService;
    private final UserPresenceService userPresenceService;

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
    public ResponseEntity<ContentMetaResponse<BuildingFloorPresenceResponse, BuildingMetaResponse>> getBuildingFloorPresence(
            @PathVariable("buildingId") Long buildingId,
            @PathVariable("floorId") Long floorId
    ) {
        Building building;
        try {
            building = buildingService.getBuildingFloor(buildingId, floorId);
        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ContentMetaResponse<>(null, new EntityNotFoundMetaResponse(e.getMessage()))
//            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<RoomPresence> roomsPresence = roomPresenceService.findAll();
        Integer totalNumberOfFloors = buildingService.getNumberOfFloors(buildingId);
        ContentMetaResponse<BuildingFloorPresenceResponse, BuildingMetaResponse> response;
        response = BuildingFloorPresenceResponseMapper.map(
                building,
                building.getFloors().iterator().next(),
                roomsPresence,
                totalNumberOfFloors
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{buildingId}/floors/{floorId}/users/{userId}")
    public ResponseEntity<?> getBuildingFloorUserPresence(
            @PathVariable("buildingId") Long buildingId,
            @PathVariable("floorId") Long floorId,
            @PathVariable("userId") Long userId
    ) {
        Building building;
        try {
            building = buildingService.getBuildingFloor(buildingId, floorId);
        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ContentMetaResponse<>(null, new EntityNotFoundMetaResponse(e.getMessage()))
//            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<UserPresence> userPresences = userPresenceService.findByScanDate(
                userId, LocalDate.now(ZoneId.of("UTC"))
        );
        Integer totalNumberOfFloors = buildingService.getNumberOfFloors(buildingId);
        ContentMetaResponse<BuildingUserPresenceResponse, BuildingMetaResponse> response;
        response = BuildingUserPresenceResponseMapper.map(
                building,
                building.getFloors().iterator().next(),
                userPresences,
                totalNumberOfFloors
        );

        return ResponseEntity.ok(response);
    }
}
