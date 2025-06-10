package net.dimjasevic.karlo.fer.evidentor.assets_service.mapper.v1;

import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.UserPresence;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingUserPresenceResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.BuildingMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.FloorResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.RoomVisualizationResponse;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import net.dimjasevic.karlo.fer.evidentor.domain.floors.Floor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildingUserPresenceResponseMapper {

    public static ContentMetaResponse<BuildingUserPresenceResponse, BuildingMetaResponse> map(
            Building building,
            Floor floor,
            List<UserPresence> userPresences,
            Integer totalNumberOfFloors
    ) {
        Map<Long, UserPresence> presenceByRoomMap = userPresences
                .stream()
                .collect(
                        Collectors
                                .toMap(
                                        UserPresence::getRoomId,
                                        u -> u,
                                        (u1, _) -> u1
                                )
                );

        BuildingUserPresenceResponse content = new BuildingUserPresenceResponse(
                building.getId(),
                building.getName(),
                new FloorResponse<>(
                        floor.getId(),
                        floor.getIndex(),
                        floor.getRooms()
                                .stream()
                                .map(room -> new BuildingUserPresenceResponse.RoomResponse(
                                        room.getId(),
                                        room.getName(),
                                        new RoomVisualizationResponse(
                                                room.getRoomVisualization().getStartRow(),
                                                room.getRoomVisualization().getStartCol(),
                                                room.getRoomVisualization().getRowspan(),
                                                room.getRoomVisualization().getColspan()
                                        ),
                                        presenceByRoomMap.containsKey(room.getId()) ?
                                                presenceByRoomMap.get(room.getId()).getIndex() :
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
