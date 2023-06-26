package kg.kubatbekov.university_cms.repository;

import kg.kubatbekov.university_cms.model.length.RelationLength;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LengthRepository extends JpaRepository<RelationLength, Integer> {
}