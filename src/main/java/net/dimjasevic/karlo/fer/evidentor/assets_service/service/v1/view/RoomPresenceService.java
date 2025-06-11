package net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.view;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.view.RoomPresenceRepository;
import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.RoomPresence;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RoomPresenceService {

    private RoomPresenceRepository repository;

    public List<RoomPresence> findAll() {
        return repository.findAll();
    }
}
