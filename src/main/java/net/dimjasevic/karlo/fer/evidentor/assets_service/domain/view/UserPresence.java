package net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "users_presence_agg_by_day")
@Immutable
public class UserPresence {
    private Long userId;
    private Long roomId;
    @Id
    private LocalDateTime scanTime;
    private Short index;
}
