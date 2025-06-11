package net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "rooms_current_presence_agg_by_day")
@Immutable
public class RoomPresence {
    @Id
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "scan_time")
    LocalDate scanTime;
    Long count;
}
