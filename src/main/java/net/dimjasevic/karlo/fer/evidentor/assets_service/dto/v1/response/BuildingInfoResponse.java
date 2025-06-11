package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response;

public record BuildingInfoResponse(
        Long id,
        String name,
        String image,
        Integer numberOfFloors
) {
}
