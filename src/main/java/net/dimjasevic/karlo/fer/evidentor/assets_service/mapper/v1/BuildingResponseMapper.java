package net.dimjasevic.karlo.fer.evidentor.assets_service.mapper.v1;

import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.BuildingResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.BuildingMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.FloorResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.RoomVisualizationResponse;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import net.dimjasevic.karlo.fer.evidentor.domain.floors.Floor;

public class BuildingResponseMapper {

    public static ContentMetaResponse<BuildingResponse, BuildingMetaResponse> map(
            Building building,
            Floor floor,
            Integer totalNumberOfFloors
    ) {
        BuildingResponse content = new BuildingResponse(
                building.getId(),
                building.getName(),
                new FloorResponse<>(
                        floor.getId(),
                        floor.getIndex(),
                        floor.getRooms()
                                .stream()
                                .map(room -> new BuildingResponse.RoomResponse(
                                        room.getId(),
                                        room.getName(),
                                        new RoomVisualizationResponse(
                                                room.getRoomVisualization().getStartRow(),
                                                room.getRoomVisualization().getStartCol(),
                                                room.getRoomVisualization().getRowspan(),
                                                room.getRoomVisualization().getColspan()
                                        )
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
