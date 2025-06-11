package net.dimjasevic.karlo.fer.evidentor.assets_service.dao.v1;

import net.dimjasevic.karlo.fer.evidentor.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository(value = "ServiceUserRepositoryV1")
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(
            value = "SELECT u.* FROM users u WHERE u.deleted IS FALSE",
            countQuery = "SELECT COUNT(*) FROM users u WHERE u.deleted IS FALSE",
            nativeQuery = true
    )
    Page<User> findAllOnlyAlive(Pageable pageable);
}
