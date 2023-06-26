package kg.kubatbekov.university_cms.model.length;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lengths")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelationLength {
    @Id
    @Column(name = "length_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "length_subjects_professors")
    private int lengthSubjectsProfessorsRelation;

    @Column(name = "length_subjects_groups")
    private int lengthSubjectsGroupsRelation;

    public RelationLength(int lengthSubjectsProfessorsRelation, int lengthSubjectsGroupsRelation) {
        this.lengthSubjectsProfessorsRelation = lengthSubjectsProfessorsRelation;
        this.lengthSubjectsGroupsRelation = lengthSubjectsGroupsRelation;
    }
}
