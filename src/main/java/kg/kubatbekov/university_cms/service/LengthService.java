package kg.kubatbekov.university_cms.service;

import kg.kubatbekov.university_cms.model.length.RelationLength;
import kg.kubatbekov.university_cms.repository.LengthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LengthService {
    private final LengthRepository lengthRepository;

    @Autowired
    public LengthService(LengthRepository lengthRepository) {
        this.lengthRepository = lengthRepository;
    }

    public RelationLength findSingleValue() {
        if(!lengthRepository.findAll().isEmpty()){
            return lengthRepository.findAll().get(0);
        }else {
            return new RelationLength(0,0);
        }
    }

    public void save(RelationLength relationLength) {
        lengthRepository.save(relationLength);
    }

    public void update(RelationLength relationLength) {
        RelationLength length = findSingleValue();
        length.setLengthSubjectsGroupsRelation(relationLength.getLengthSubjectsGroupsRelation());
        length.setLengthSubjectsProfessorsRelation(relationLength.getLengthSubjectsProfessorsRelation());

        lengthRepository.save(length);
    }
}
