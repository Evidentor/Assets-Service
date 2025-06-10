package net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.view;

import net.dimjasevic.karlo.fer.evidentor.assets_service.domain.view.RoomPresence;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RoomPresenceRepository extends Repository<RoomPresence, Long> {

    List<RoomPresence> findAll();
}

