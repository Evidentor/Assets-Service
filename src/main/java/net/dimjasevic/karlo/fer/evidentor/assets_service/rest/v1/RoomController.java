package net.dimjasevic.karlo.fer.evidentor.assets_service.rest.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.*;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.RoomService;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/rooms")
public class RoomController {

    private final RoomService service;

    // TODO: Move this to rooms controller
    @GetMapping
    public ResponseEntity<ContentMetaResponse<List<RoomDropdownResponse>, Object>> getRoomsDropdownMenu() {
        List<Room> rooms = service.getRooms();
        ContentMetaResponse<List<RoomDropdownResponse>, Object> response = new ContentMetaResponse<>(
                rooms.stream().map(
                        room -> new RoomDropdownResponse(
                                room.getId(),
                                room.getName()
                        )
                ).toList(),
                null
        );
        return ResponseEntity.ok(response);
    }
}
