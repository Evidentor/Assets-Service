package net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.DeviceRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.Room;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.RoomRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.telemetry.Telemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class DeviceService {

    private final RoomRepository roomRepository;
    private final DeviceRepository repository;

    public Page<Device> findAllOnlyAlive(Pageable pageable) {
        return repository.findAllOnlyAlive(pageable);
    }

    public Telemetry findLatestTelemetry(Long deviceId) {
        return repository.findLatestTelemetry(deviceId).orElse(null);
    }

    public Device create(String serialNumber, Long roomId, LocalDate installationDate) {
        if (serialNumber == null || serialNumber.isEmpty()) {
            throw new IllegalArgumentException("Serial number cannot be empty");
        }

        if (repository.existsBySerialNumber(serialNumber)) {
            throw new IllegalArgumentException("Device with serial number already exists");
        }

        Room room = null;
        if (roomId != null) {
            room = roomRepository.findById(roomId).orElse(null);
        }
        LocalDateTime installationDateTime = installationDate == null ? LocalDateTime.now() : installationDate.atStartOfDay();

        Device device = new Device(room, serialNumber, installationDateTime, false);
        repository.save(device);
        return device;
    }

    public Device update(Long deviceId, String serialNumber, Long roomId, LocalDate installationDate) {
        Device device = repository.findById(deviceId).orElse(null);
        if (device == null) {
            throw new IllegalArgumentException("Device does not exist");
        }

        if (serialNumber != null && !serialNumber.isEmpty()) {
            if (repository.existsBySerialNumber(serialNumber)) {
                throw new IllegalArgumentException("Device with serial number already exists");
            }
            device.setSerialNumber(serialNumber);
        }

        Room room;
        if (roomId != null) {
            room = roomRepository.findById(roomId).orElse(null);
            if (room != null) {
                device.setRoom(room);
            }
        }

        if (installationDate != null) {
            device.setInstallationDate(installationDate.atStartOfDay());
        }

        repository.save(device);
        return device;
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
