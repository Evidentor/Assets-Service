package net.dimjasevic.karlo.fer.evidentor.assets_service.rest.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.PageableMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.DeviceInfoResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.DeviceService;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/v1/devices")
public class DeviceController {

    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<ContentMetaResponse<List<DeviceInfoResponse>, PageableMetaResponse>> findAll(Pageable pageable) {
        Page<Device> devicesPage = deviceService.findAllOnlyAlive(pageable);

        List<DeviceInfoResponse> content = devicesPage.getContent()
                .stream()
                .map(device ->
                        new DeviceInfoResponse(
                                device.getId(),
                                device.getRoom() == null ? null : device.getRoom().getId(),
                                device.getInstallationDate(),
                                device.getSerialNumber())
                )
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
}
