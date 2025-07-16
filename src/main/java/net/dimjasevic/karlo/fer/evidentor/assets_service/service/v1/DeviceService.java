package net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.DeviceRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import net.dimjasevic.karlo.fer.evidentor.domain.telemetry.Telemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeviceService {

    private final DeviceRepository repository;

    public Page<Device> findAllOnlyAlive(Pageable pageable) {
        return repository.findAllOnlyAlive(pageable);
    }

    public Telemetry findLatestTelemetry(Long deviceId) {
        return repository.findLatestTelemetry(deviceId).orElse(null);
    }

    public boolean deleteDevice(Long deviceId) {
        Device device = repository.findById(deviceId).orElse(null);
        if (device != null) {
            device.setDeleted(true);
            repository.save(device);
        }
        return device != null;
    }
}
