package ru.practicum.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE id=?1)", nativeQuery = true)
    boolean userExists(Long userId);

    List<User> findAllByIdIn(List<Long> ids, Pageable page);
}
