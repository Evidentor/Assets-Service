package net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.Room;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RoomService {

    private final RoomRepository repository;

    public List<Room> getRooms() {
        List<Room> rooms = repository.findAll();
        return rooms.stream().filter(room -> !room.getDeleted()).toList();
    }
}
