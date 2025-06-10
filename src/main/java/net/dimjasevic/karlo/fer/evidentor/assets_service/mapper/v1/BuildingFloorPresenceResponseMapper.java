package net.dimjasevic.karlo.fer.evidentor.assets_service.mapper.v1;

import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.RoomPresence;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingFloorPresenceResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.BuildingMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.FloorResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.RoomVisualizationResponse;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import net.dimjasevic.karlo.fer.evidentor.domain.floors.Floor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildingFloorPresenceResponseMapper {

    public static ContentMetaResponse<BuildingFloorPresenceResponse, BuildingMetaResponse> map(
            Building building,
            Floor floor,
            List<RoomPresence> roomsPresence,
            Integer totalNumberOfFloors
    ) {
        Map<Long, RoomPresence> roomsPresenceMap = roomsPresence
                .stream()
                .collect(
                        Collectors
                                .toMap(
                                        RoomPresence::getRoomId,
                                        r -> r
                                )
                );

        BuildingFloorPresenceResponse content = new BuildingFloorPresenceResponse(
                building.getId(),
                building.getName(),
                new FloorResponse<>(
                        floor.getId(),
                        floor.getIndex(),
                        floor.getRooms()
                                .stream()
                                .map(room -> new BuildingFloorPresenceResponse.RoomResponse(
                                        room.getId(),
                                        room.getName(),
                                        new RoomVisualizationResponse(
                                                room.getRoomVisualization().getStartRow(),
                                                room.getRoomVisualization().getStartCol(),
                                                room.getRoomVisualization().getRowspan(),
                                                room.getRoomVisualization().getColspan()
                                        ),
                                        roomsPresenceMap.containsKey(room.getId()) ?
                                                roomsPresenceMap.get(room.getId()).getCount() :
                                                0
                                )).toList()
                )
        );

        BuildingMetaResponse meta = new BuildingMetaResponse(
                floor.getIndex(),
                floor.getIndex() < totalNumberOfFloors - 1,
                floor.getIndex() > 0,
                totalNumberOfFloors
        );

        return new ContentMetaResponse<>(content, meta);
    }
}
