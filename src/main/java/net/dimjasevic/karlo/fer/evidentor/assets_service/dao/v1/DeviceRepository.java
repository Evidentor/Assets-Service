package net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1;

import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository(value = "ServiceDeviceRepositoryV1")
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query(
            value = "SELECT d.* FROM devices d WHERE d.deleted IS FALSE",
            countQuery = "SELECT COUNT(*) FROM devices d WHERE d.deleted IS FALSE",
            nativeQuery = true
    )
    Page<Device> findAllOnlyAlive(Pageable pageable);
}
