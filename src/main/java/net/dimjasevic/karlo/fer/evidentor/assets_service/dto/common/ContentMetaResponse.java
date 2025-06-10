package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common;

public record ContentMetaResponse<C, M>(
        C content,
        M meta
) {
}
