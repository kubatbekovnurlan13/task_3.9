package kg.kubatbekov.university_cms.controller;

import kg.kubatbekov.university_cms.model.Group;
import kg.kubatbekov.university_cms.model.Professor;
import kg.kubatbekov.university_cms.model.Subject;
import kg.kubatbekov.university_cms.service.GroupService;
import kg.kubatbekov.university_cms.service.ProfessorService;
import kg.kubatbekov.university_cms.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final GroupService groupService;

    @Autowired
    public SubjectController(SubjectService subjectService, ProfessorService professorService, GroupService groupService) {
        this.subjectService = subjectService;
        this.professorService = professorService;
        this.groupService = groupService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping("/list")
    public String getSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        return "subject/subjects";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/delete")
    public String deleteSubject(@RequestParam int subjectId) {
        System.out.println("delete subject");
        subjectService.deleteById(subjectId);
        return "redirect:/subject/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/add")
    public String addSubject(@ModelAttribute("subject") Subject subject) {
        return "subject/subjectAdd";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/save")
    public String saveSubject(@ModelAttribute("subject") Subject subject) {
        subjectService.save(subject);
        return "redirect:/subject/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/updateForm")
    public String updateSubjectForm(@RequestParam int subjectId, Model model) {
        Subject subject = subjectService.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject id " + subjectId));
        model.addAttribute("subject", subject);
        return "subject/subjectUpdate";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/update")
    public String updateSubject(@RequestParam int subjectId, @ModelAttribute("subject") Subject subject) {
        subjectService.update(subject);
        return "redirect:/subject/list";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/assignTeacher")
    public String assignTeacherForm(@RequestParam int subjectId, Model model) {
        Subject subject = subjectService.findById(subjectId).get();
        List<Professor> professors = professorService.findAll()
                .stream()
                .filter(prof -> !prof.getSubjects().contains(subject))
                .toList();

        model.addAttribute("professors", professors);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("professor", new Professor());
        return "subject/subjectTeacher";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/assignTeacher")
    public String assignTeacher(@RequestParam int subjectId, Integer professorId) {
        Subject subject = subjectService.findById(subjectId).get();
        Professor professor = professorService.findById(professorId).get();

        professor.getSubjects().add(subject);
        subject.getProfessors().add(professor);

        professorService.save(professor);
        subjectService.save(subject);

        return "redirect:/subject/list";
    }


    @Secured({"ROLE_ADMIN"})
    @GetMapping("/assignGroup")
    public String assignGroupForm(@RequestParam int subjectId, Model model) {
        Subject subject = subjectService.findById(subjectId).get();
        List<Group> groups = groupService.findAll()
                .stream()
                .filter(group -> !group.getSubjects().contains(subject))
                .toList();

        model.addAttribute("groups", groups);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("group", new Group());
        return "subject/subjectGroup";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/assignGroup")
    public String assignGroup(@RequestParam int subjectId, Integer groupId) {
        Subject subject = subjectService.findById(subjectId).get();
        Group group = groupService.findById(groupId).get();

        group.getSubjects().add(subject);
        subject.getGroups().add(group);

        groupService.save(group);
        subjectService.save(subject);

        return "redirect:/subject/list";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/reassignTeacher")
    public String reassignTeacherForm(@RequestParam int subjectId, Model model) {
        Subject subject = subjectService.findById(subjectId).get();

        model.addAttribute("professors", subject.getProfessors());
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("professor", new Professor());
        return "subject/subjectTeacherReassign";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/reassignTeacher")
    public String reassignTeacher(@RequestParam int subjectId, Integer professorId) {
        Subject subject = subjectService.findById(subjectId).get();
        Professor professor = professorService.findById(professorId).get();

        professor.getSubjects().remove(subject);
        subject.getProfessors().remove(professor);

        professorService.save(professor);
        subjectService.save(subject);

        return "redirect:/subject/list";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/reassignGroup")
    public String  reassignGroupForm(@RequestParam int subjectId, Model model) {
        Subject subject = subjectService.findById(subjectId).get();

        model.addAttribute("groups", subject.getGroups());
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("group", new Group());
        return "subject/subjectGroupReassign";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/reassignGroup")
    public String reassignGroup(@RequestParam int subjectId, Integer groupId) {
        Subject subject = subjectService.findById(subjectId).get();
        Group group = groupService.findById(groupId).get();

        group.getSubjects().remove(subject);
        subject.getGroups().remove(group);

        groupService.save(group);
        subjectService.save(subject);

        return "redirect:/subject/list";
    }
}
