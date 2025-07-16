package net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response;

import java.time.LocalDateTime;

public record TelemetryResponse (String userName, String roomName, LocalDateTime scanTime) {
}
