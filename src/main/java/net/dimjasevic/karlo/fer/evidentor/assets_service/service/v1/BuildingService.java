package net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.BuildingRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BuildingService {

    private final BuildingRepository repository;

    public Building getById(Long buildingId) {
        return repository.findById(buildingId).orElseThrow(() -> new RuntimeException(
                String.format("Building with id [%d] not found", buildingId)
        ));
    }

    public Page<Building> findAllOnlyAlive(Pageable pageable) {
        return repository.findAllOnlyAlive(pageable);
    }

    public Building getBuildingFloor(Long buildingId, Long floorId) {
        Building building = repository.findByIdAndFloorIdWithRoomVisualizations(buildingId, floorId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Building with id [%s] not found", buildingId)
                ));

        if (building.getFloors().isEmpty()) {
            throw new RuntimeException(
                    String.format("Building [%s] does not have floor with index [%s]", buildingId, floorId)
            );
        }

        if (building.getFloors().size() > 1) {
            throw new RuntimeException(
                    String.format("Building [%s] has multiple floors with index [%s]", buildingId, floorId)
            );
        }

        return building;
    }

    public Integer getNumberOfFloors(Long buildingId) {
        return repository.getNumberOfFloors(buildingId);
    }
}
