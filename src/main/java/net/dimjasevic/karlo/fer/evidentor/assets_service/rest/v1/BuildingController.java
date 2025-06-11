package net.dimjasevic.karlo.fer.evidentor.assets_service.rest.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.RoomPresence;
import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.UserPresence;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.PageableMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingFloorPresenceResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingInfoResponse;
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
import net.dimjasevic.karlo.fer.evidentor.domain.floors.Floor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/v1/buildings")
public class BuildingController {

    private final BuildingService buildingService;
    private final RoomPresenceService roomPresenceService;
    private final UserPresenceService userPresenceService;

    @GetMapping
    public ResponseEntity<ContentMetaResponse<List<BuildingInfoResponse>, PageableMetaResponse>> findAll(Pageable pageable) {
        Page<Building> buildingsPage = buildingService.findAllOnlyAlive(pageable);

        List<BuildingInfoResponse> content = new ArrayList<>();
        // TODO: Move this to one single query
        for (Building building : buildingsPage) {
            Integer numberOfFloors = buildingService.getNumberOfFloors(building.getId());
            content.add(new BuildingInfoResponse(
                    building.getId(),
                    building.getName(),
                    building.getImage(),
                    numberOfFloors
            ));
        }

        // TODO: Mapper
        PageableMetaResponse meta = new PageableMetaResponse(
                buildingsPage.hasPrevious(),
                buildingsPage.hasNext(),
                buildingsPage.getNumber(),
                buildingsPage.getTotalElements(),
                buildingsPage.getTotalPages()
        );
        ContentMetaResponse<List<BuildingInfoResponse>, PageableMetaResponse> response = new ContentMetaResponse<>(
                content, meta
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{buildingId}/floors/{floorId}")
    public ResponseEntity<ContentMetaResponse<BuildingResponse, BuildingMetaResponse>> getBuildingFloor(
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
            return ResponseEntity.notFound().build();
        }

        Integer totalNumberOfFloors = buildingService.getNumberOfFloors(buildingId);
        Floor floor = building.getFloors().iterator().next();
        ContentMetaResponse<BuildingResponse, BuildingMetaResponse> response;
        response = BuildingResponseMapper.map(
                building,
                floor,
                totalNumberOfFloors,
                buildingService.getPreviousFloorId(buildingId, floor.getIndex()),
                buildingService.getNextFloorId(buildingId, floor.getIndex())
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
        Floor floor = building.getFloors().iterator().next();
        ContentMetaResponse<BuildingFloorPresenceResponse, BuildingMetaResponse> response;
        response = BuildingFloorPresenceResponseMapper.map(
                building,
                floor,
                roomsPresence,
                totalNumberOfFloors,
                buildingService.getPreviousFloorId(buildingId, floor.getIndex()),
                buildingService.getNextFloorId(buildingId, floor.getIndex())
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
        Floor floor = building.getFloors().iterator().next();
        ContentMetaResponse<BuildingUserPresenceResponse, BuildingMetaResponse> response;
        response = BuildingUserPresenceResponseMapper.map(
                building,
                floor,
                userPresences,
                totalNumberOfFloors,
                buildingService.getPreviousFloorId(buildingId, floor.getIndex()),
                buildingService.getNextFloorId(buildingId, floor.getIndex())
        );

        return ResponseEntity.ok(response);
    }
}
