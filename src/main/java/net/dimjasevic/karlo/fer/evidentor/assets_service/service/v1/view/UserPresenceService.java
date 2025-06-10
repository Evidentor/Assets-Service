package net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.view;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.view.UserPresenceRepository;
import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.UserPresence;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class UserPresenceService {

    private UserPresenceRepository repository;

    public List<UserPresence> findByScanDate(Long userId, LocalDate scanDate) {
        return repository.findByScanDate(userId, scanDate);
    }
}
