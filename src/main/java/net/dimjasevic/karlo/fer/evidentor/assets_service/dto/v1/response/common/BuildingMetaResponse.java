package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common;

public record BuildingMetaResponse(
        Integer currentFloor,
        Boolean hasNextFloor,
        Boolean hasPreviousFloor,
        Integer totalNumberOfFloors,
        Long previousFloorId,
        Long nextFloorId
) {
}
