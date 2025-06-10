package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.common;

import java.util.List;

public record FloorResponse<T>(
        Long id,
        Integer index,
        List<T> rooms
) {
}
