package kg.kubatbekov.university_cms.repository;

import kg.kubatbekov.university_cms.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserByUsername(String username);
}
