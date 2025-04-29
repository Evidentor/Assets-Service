package net.dimjasevic.karlo.fer.evidentor.assets_service.api.v1.buildings;

import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.BuildingRepository;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

    private final BuildingRepository repository;

    public BuildingService(BuildingRepository repository) {
        this.repository = repository;
    }

    public Building getById(Long buildingId) {
        return repository.findById(buildingId).orElseThrow(() -> new RuntimeException(
                String.format("Building with id [%d] not found", buildingId)
        ));
    }
}
