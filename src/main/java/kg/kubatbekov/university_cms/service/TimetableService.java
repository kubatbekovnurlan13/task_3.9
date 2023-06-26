package kg.kubatbekov.university_cms.service;

import kg.kubatbekov.university_cms.generator.GeneticAlgorithm;
import kg.kubatbekov.university_cms.generator.Population;
import kg.kubatbekov.university_cms.generator.Solution;
import kg.kubatbekov.university_cms.generator.Timetable;
import kg.kubatbekov.university_cms.model.*;
import kg.kubatbekov.university_cms.model.length.RelationLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimetableService {
    private final RoomService roomService;
    private final TimeslotService timeslotService;
    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final GroupService groupService;
    private final LessonService lessonService;
    private final LengthService lengthService;

    @Autowired
    public TimetableService(RoomService roomService,
                            TimeslotService timeslotService,
                            ProfessorService professorService,
                            SubjectService subjectService,
                            GroupService groupService,
                            LessonService lessonService,
                            LengthService lengthService) {
        this.roomService = roomService;
        this.timeslotService = timeslotService;
        this.professorService = professorService;
        this.subjectService = subjectService;
        this.groupService = groupService;
        this.lessonService = lessonService;
        this.lengthService = lengthService;
    }

    public void runTimetableService() {
        int SIZE_OF_SUBJECTS_PROFESSORS = subjectService.subjectsProfessorsSize();
        int SIZE_OF_GROUPS_SUBJECTS = groupService.groupsSubjectsSize();
        int DB_SIZE_OF_SUBJECTS_PROFESSORS = lengthService.findSingleValue().getLengthSubjectsProfessorsRelation();
        int DB_SIZE_OF_GROUPS_SUBJECTS = lengthService.findSingleValue().getLengthSubjectsGroupsRelation();

        int LESSONS_SIZE = lessonService.findLessonsSize();

        if (DB_SIZE_OF_SUBJECTS_PROFESSORS == 0
                && DB_SIZE_OF_GROUPS_SUBJECTS == 0
                && LESSONS_SIZE == 0) {
            System.out.println("1");
            List<Lesson> timetable = generateTimetable();
            lessonService.saveAll(timetable);

            RelationLength relationLength = new RelationLength(SIZE_OF_SUBJECTS_PROFESSORS, SIZE_OF_GROUPS_SUBJECTS);
            lengthService.update(relationLength);
        } else if (DB_SIZE_OF_SUBJECTS_PROFESSORS != 0
                && DB_SIZE_OF_GROUPS_SUBJECTS != 0
                && LESSONS_SIZE == 0) {
            System.out.println("2");

            List<Lesson> newTimetable = generateTimetable();
            lessonService.saveAll(newTimetable);
        } else if (SIZE_OF_SUBJECTS_PROFESSORS > DB_SIZE_OF_SUBJECTS_PROFESSORS
                || SIZE_OF_GROUPS_SUBJECTS > DB_SIZE_OF_GROUPS_SUBJECTS) {
            System.out.println("3");

            lessonService.deleteAll();
            List<Lesson> newTimetable1 = generateTimetable();
            lessonService.saveAll(newTimetable1);

            lessonService.deleteAll();
            List<Lesson> newTimetable2 = generateTimetable();
            lessonService.saveAll(newTimetable2);

            RelationLength relationLength = new RelationLength(SIZE_OF_SUBJECTS_PROFESSORS, SIZE_OF_GROUPS_SUBJECTS);
            lengthService.update(relationLength);
        } else {
            RelationLength relationLength = new RelationLength(SIZE_OF_SUBJECTS_PROFESSORS, SIZE_OF_GROUPS_SUBJECTS);
            lengthService.update(relationLength);
        }
    }

    private List<Lesson> generateTimetable() {
        // Get a Timetable object with all the available information.
        Timetable timetable = initializeTimetable();

        // Initialize GA
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.9, 2, 5);

        // Initialize population
        Population population = ga.initializePopulation(timetable);

        // Evaluate population
        ga.calculateFitnessOfSolutionInPopulation(population, timetable);

        // Keep track of current generation
        int generation = 1;

        // Start evolution loop
        while (!ga.isTerminationConditionMet(generation, 1000)
                && !ga.isTerminationConditionMet(population)) {
            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population, timetable);

            // Evaluate population
            ga.calculateFitnessOfSolutionInPopulation(population, timetable);
        }
        Solution bestSolution = population.getSolutionOfSortedPopulation(0);
        timetable.createCourses(bestSolution);

        return timetable.getCourses();
    }

    public Timetable initializeTimetable() {
        Timetable timetable = new Timetable();

        List<Room> rooms = roomService.findAll();
        // Set up rooms
        timetable.setRooms(rooms);

        List<Timeslot> timeslots = timeslotService.findAll();
        // Set up timeslots
        timetable.setTimeslots(timeslots);

        List<Professor> professors = professorService.findAll();
        // Set up professors
        timetable.setProfessors(professors);

        List<Subject> subjects = subjectService.findAll();
        // Set up modules and define the professors that teach them
        timetable.setSubjects(subjects);

        List<Group> groups = groupService.findAll();
        // Set up student groups and the modules they take.
        timetable.setGroups(groups);

        return timetable;
    }
}
