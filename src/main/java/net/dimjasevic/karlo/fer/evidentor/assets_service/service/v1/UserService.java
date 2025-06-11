package net.dimjasevic.karlo.fer.evidentor.assets_service.service.v1;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1.UserRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public Page<User> findAllOnlyAlive(Pageable pageable) {
        return repository.findAllOnlyAlive(pageable);
    }

}
