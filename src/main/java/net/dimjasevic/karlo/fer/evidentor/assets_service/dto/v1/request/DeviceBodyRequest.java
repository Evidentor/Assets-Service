package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.request;

import java.time.LocalDate;

public record DeviceBodyRequest(
        String serialNumber,
        Long roomId,
        LocalDate installationDate
) {
}
