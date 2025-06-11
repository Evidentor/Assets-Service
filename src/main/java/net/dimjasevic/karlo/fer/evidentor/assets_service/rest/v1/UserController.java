package net.dimjasevic.karlo.fer.evidentor.assets_service.rest.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.ContentMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.common.PageableMetaResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dto.v1.response.UserInfoResponse;
import net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1.UserService;
import net.dimjasevic.karlo.fer.evidentor.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<ContentMetaResponse<List<UserInfoResponse>, PageableMetaResponse>> findAll(Pageable pageable) {
        Page<User> usersPage = userService.findAllOnlyAlive(pageable);

        List<UserInfoResponse> content = usersPage.getContent()
                .stream()
                .map(user ->
                        new UserInfoResponse(
                                user.getId(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getCardId())
                )
                .collect(Collectors.toList());

        // TODO: Mapper
        PageableMetaResponse meta = new PageableMetaResponse(
                usersPage.hasPrevious(),
                usersPage.hasNext(),
                usersPage.getNumber(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages()
        );
        ContentMetaResponse<List<UserInfoResponse>, PageableMetaResponse> response = new ContentMetaResponse<>(
                content, meta
        );
        return ResponseEntity.ok(response);
    }
}
