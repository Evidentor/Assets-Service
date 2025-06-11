package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common;

public record PageableMetaResponse(
        Boolean hasPrevious,
        Boolean hasNext,
        Long totalElements,
        Integer totalPages
) {
}
