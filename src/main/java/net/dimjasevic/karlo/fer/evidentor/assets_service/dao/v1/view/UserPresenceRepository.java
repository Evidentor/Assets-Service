package net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.view;

import jakarta.validation.constraints.NotNull;
import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.UserPresence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserPresenceRepository extends Repository<UserPresence, LocalDateTime> {

    @Query(
            value = "SELECT up.* " +
                    "FROM users_presence_agg_by_day up " +
                    "WHERE up.user_id = :id AND up.scan_time::DATE = :date",
            nativeQuery = true
    )
    List<UserPresence> findByScanDate(
            @NotNull @Param("id") Long userId,
            @NotNull @Param("date") LocalDate scanDate
    );
}
