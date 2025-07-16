package net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1;

import jakarta.validation.constraints.NotNull;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import net.dimjasevic.karlo.fer.evidentor.domain.telemetry.Telemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository(value = "ServiceDeviceRepositoryV1")
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query(
            value = "SELECT d.* FROM devices d WHERE d.deleted IS FALSE",
            countQuery = "SELECT COUNT(*) FROM devices d WHERE d.deleted IS FALSE",
            nativeQuery = true
    )
    Page<Device> findAllOnlyAlive(Pageable pageable);

    // TODO: Integrate this in findAllOnlyAlive
    @Query(
            value = "SELECT t.* FROM telemetry t WHERE t.device_id = :id ORDER BY t.scan_time DESC LIMIT 1;",
            nativeQuery = true
    )
    Optional<Telemetry> findLatestTelemetry(@NotNull @Param("id") Long deviceId);

    boolean existsBySerialNumber(String serialNumber);
}
