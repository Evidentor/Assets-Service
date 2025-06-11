package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common;

public record RoomVisualizationResponse(
        Short startRow,
        Short startCol,
        Short rowSpan,
        Short colSpan
) {
}
