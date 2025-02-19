package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.users.model.User;
import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {
    @Query(value = """
                      select * from users
                       where (id in (?1) or (coalesce(?1) = 0))
                       limit ?3 offset ?2 """, nativeQuery = true)
    List<User> getUsers(List<Long> ids, Integer from, Integer size);
}
