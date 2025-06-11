package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response;

import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.FloorResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common.RoomVisualizationResponse;

public record BuildingFloorPresenceResponse(
        Long id,
        String name,
        FloorResponse<RoomResponse> floor
) {

    public record RoomResponse(
            Long id,
            String name,
            RoomVisualizationResponse roomVisualization,
            Long currentPresence
    ) {
    }
}
