package net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1;

import jakarta.validation.constraints.NotNull;
import net.dimjasevic.karlo.fer.evidentor.domain.buildings.Building;
import net.dimjasevic.karlo.fer.evidentor.domain.telemetry.Telemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "ServiceBuildingRepositoryV1")
public interface BuildingRepository extends JpaRepository<Building, Long> {

    @Query("SELECT b FROM Building b WHERE b.id = :id")
    Optional<Building> findByIdWithoutJoins(@NotNull @Param("id") Long id);

    @Query(
            value = "SELECT b.* FROM buildings b WHERE b.deleted IS FALSE",
            countQuery = "SELECT COUNT(*) FROM buildings b WHERE b.deleted IS FALSE",
            nativeQuery = true
    )
    Page<Building> findAllOnlyAlive(Pageable pageable);

    @Query(
            value = "SELECT b FROM Building b " +
                    "LEFT JOIN FETCH b.floors f " +
                    "LEFT JOIN FETCH f.rooms r " +
                    "LEFT JOIN FETCH RoomVisualization rv ON r.id = rv.roomId " +
                    "WHERE b.id = :buildingId AND f.id = :floorId"
    )
    Optional<Building> findByIdAndFloorIdWithRoomVisualizations(
            @NotNull @Param("buildingId") Long buildingId,
            @NotNull @Param("floorId") Long floorId
    );

    @Query(
            value = "SELECT COUNT(f.id) FROM Building b LEFT JOIN b.floors f WHERE b.id = :id"
    )
    Integer getNumberOfFloors(@NotNull @Param("id") Long id);

    @Query(
            value = "SELECT f.id FROM Building b LEFT JOIN b.floors f WHERE b.id = :id AND f.index = :index - 1"
    )
    Optional<Long> getPreviousFloorId(@NotNull @Param("id") Long buildingId, @NotNull @Param("index") Integer floorIndex);

    @Query(
            value = "SELECT f.id FROM Building b LEFT JOIN b.floors f WHERE b.id = :id AND f.index = :index + 1"
    )
    Optional<Long> getNextFloorId(@NotNull @Param("id") Long buildingId, @NotNull @Param("index") Integer floorIndex);

    // TODO: Move this somewhere else (it is telemetry)
    @Query(
            value = "SELECT t FROM Telemetry t " +
                    "JOIN FETCH t.room r " +
                    "JOIN FETCH r.floor f " +
                    "JOIN FETCH f.building b " +
                    "WHERE b.id = :id AND f.id = :floorId " +
                    "ORDER BY t.id.scanTime DESC " +
                    "LIMIT :limit"
    )
    List<Telemetry> findMostRecentTelemetries(
            @NotNull @Param("id") Long buildingId,
            @NotNull @Param("floorId") Long floorId,
            @NotNull @Param("limit") Integer limit
    );
}