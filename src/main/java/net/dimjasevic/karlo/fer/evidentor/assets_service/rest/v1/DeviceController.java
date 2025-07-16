package net.dimjasevic.karlo.fer.evidentor.assets_service.rest.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.PageableMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.request.DeviceBodyRequest;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.DeviceInfoResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.DeviceService;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import net.dimjasevic.karlo.fer.evidentor.domain.telemetry.Telemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/devices")
public class DeviceController {

    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<ContentMetaResponse<List<DeviceInfoResponse>, PageableMetaResponse>> findAll(Pageable pageable) {
        Page<Device> devicesPage = deviceService.findAllOnlyAlive(pageable);

        List<DeviceInfoResponse> content = devicesPage.getContent()
                .stream()
                .map(device -> {
                    Telemetry latestTelemetry = deviceService.findLatestTelemetry(device.getId());
                    LocalDateTime lastMessageTimestamp = latestTelemetry == null ? null : latestTelemetry.getScanTime();
                    return new DeviceInfoResponse(
                            device.getId(),
                            device.getRoom() == null ? null : device.getRoom().getId(),
                            device.getInstallationDate(),
                            device.getSerialNumber(),
                            lastMessageTimestamp
                    );
                })
                .collect(Collectors.toList());

        // TODO: Mapper
        PageableMetaResponse meta = new PageableMetaResponse(
                devicesPage.hasPrevious(),
                devicesPage.hasNext(),
                devicesPage.getNumber(),
                devicesPage.getTotalElements(),
                devicesPage.getTotalPages()
        );
        ContentMetaResponse<List<DeviceInfoResponse>, PageableMetaResponse> response = new ContentMetaResponse<>(
                content, meta
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceInfoResponse> findById(@PathVariable("deviceId") long deviceId) {
        Device device;
        try {
            device = deviceService.getById(deviceId);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DeviceInfoResponse(
                device.getId(),
                device.getRoom() == null ? null : device.getRoom().getId(),
                device.getInstallationDate(),
                device.getSerialNumber(),
                null
        ));
    }

    @PostMapping
    public ResponseEntity<ContentMetaResponse<DeviceInfoResponse, Object>> create(
            @RequestBody DeviceBodyRequest request
    ) {
        Device device;
        try {
            device = deviceService.create(request.serialNumber(), request.roomId(), request.installationDate());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        ContentMetaResponse<DeviceInfoResponse, Object> response = new ContentMetaResponse<>(
                new DeviceInfoResponse(
                        device.getId(),
                        device.getRoom() == null ? null : device.getRoom().getId(),
                        device.getInstallationDate(),
                        device.getSerialNumber(),
                        null
                ), null
        );
        return ResponseEntity
                .created(URI.create(String.format("/api/v1/devices/%d", device.getId())))
                .body(response);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<ContentMetaResponse<DeviceInfoResponse, Object>> update(
            @PathVariable("deviceId") Long deviceId,
            @RequestBody DeviceBodyRequest request
    ) {
        Device device;
        try {
            device = deviceService.update(deviceId, request.serialNumber(), request.roomId(), request.installationDate());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        ContentMetaResponse<DeviceInfoResponse, Object> response = new ContentMetaResponse<>(
                new DeviceInfoResponse(
                        device.getId(),
                        device.getRoom() == null ? null : device.getRoom().getId(),
                        device.getInstallationDate(),
                        device.getSerialNumber(),
                        null
                ), null
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable("deviceId") Long deviceId) {
        boolean deleted = deviceService.deleteDevice(deviceId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
