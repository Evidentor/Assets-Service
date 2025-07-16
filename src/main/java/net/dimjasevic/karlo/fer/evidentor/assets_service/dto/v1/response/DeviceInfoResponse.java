package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response;

import java.time.LocalDateTime;

public record DeviceInfoResponse(
        Long id,
        Long roomId,
        LocalDateTime installationDate,
        String serialNumber,
        LocalDateTime lastMessageTimestamp
) {
}
