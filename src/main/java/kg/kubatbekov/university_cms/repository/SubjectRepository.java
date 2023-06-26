package kg.kubatbekov.university_cms.repository;

import kg.kubatbekov.university_cms.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Modifying
    @Query(value = "DELETE FROM Subject s WHERE s.subjectId = :id")
    int deleteById(@Param("id") int id);
}
