package net.dimjasevic.karlo.fer.evidentor.assets_service.api.v1.buildings;

import jakarta.transaction.Transactional;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.BuildingRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.floors.Floor;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class BuildingServiceTest {
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private BuildingController controller;

    private final Map<Long, Set<Room>> floorRooms = new HashMap<>();
    private Building building;

    @BeforeEach
    void setUp() {
        building = new Building("Building", "", false);

        Floor floor1 = new Floor(building, 0, false);
        Floor floor2 = new Floor(building, 1, false);
        building.setFloors(new HashSet<>(List.of(floor1, floor2)));

        Room room1 = new Room(floor1, "Room11", false);
        Room room2 = new Room(floor1, "Room12", false);
        Room room3 = new Room(floor1, "Room13", false);
        Room room4 = new Room(floor2, "Room21", false);
        floor1.setRooms(new HashSet<>(List.of(room1, room2, room3)));
        floor2.setRooms(new HashSet<>(List.of(room4)));

        buildingRepository.save(building);

        floorRooms.put(floor1.getId(), floor1.getRooms());
        floorRooms.put(floor2.getId(), floor2.getRooms());
    }

    @Test
    public void whenOneNonExistingBuildingQueried_thenBadRequest() {
        // WHEN
        ResponseEntity<Building> response = controller.getBuilding(building.getId() + 1);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenOneExistingBuildingQueried_thenReturnBuilding() {
        // WHEN
        ResponseEntity<Building> response = controller.getBuilding(building.getId());
        Building body = response.getBody();

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(building.getId());
        assertThat(body.getFloors()).hasSize(2);
        body.getFloors().forEach(floor -> {
            assertThat(floor).isNotNull();
            assertThat(floor.getRooms().size()).isEqualTo(floorRooms.get(floor.getId()).size());
        });
    }
}
