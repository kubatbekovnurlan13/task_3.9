package kg.kubatbekov.university_cms.controller;

import kg.kubatbekov.university_cms.model.Professor;
import kg.kubatbekov.university_cms.model.Subject;
import kg.kubatbekov.university_cms.service.ProfessorService;
import kg.kubatbekov.university_cms.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/professor")
public class ProfessorController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(SubjectService subjectService, ProfessorService professorService) {
        this.subjectService = subjectService;
        this.professorService = professorService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping("/list")
    public String getProfessors(Model model) {
        model.addAttribute("professors", professorService.findAll());
        return "professor/professors";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/delete")
    public String delete(@RequestParam int professorId) {
        professorService.deleteById(professorId);
        return "redirect:/professor/list";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/add")
    public String add(Model model, @ModelAttribute("professor") Professor professor) {
        model.addAttribute("subjects", subjectService.findAll());
        return "professor/professorAdd";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/save")
    public String save(@ModelAttribute("professor") Professor professor) {
        List<Subject> subjects = professor.getSubjects();
        subjects.forEach(subject -> subject.getProfessors().add(professor));

        professorService.save(professor);
        subjectService.saveAll(subjects);
        return "redirect:/professor/list";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/updateForm")
    public String updateForm(@RequestParam int professorId, Model model) {
        Professor professor = professorService.findById(professorId).get();
        model.addAttribute("professor", professor);
        return "professor/professorUpdate";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/update")
    public String update(@RequestParam int professorId, @ModelAttribute("professor") Professor professor) {
        Professor oldProfessor = professorService.findById(professorId).get();
        oldProfessor.setProfessorName(professor.getProfessorName());

        professorService.update(oldProfessor);
        return "redirect:/professor/list";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/assignSubject")
    public String assignSubjectForm(@RequestParam int professorId, Model model) {
        Professor professor = professorService.findById(professorId).get();
        List<Subject> subjects = subjectService.findAll()
                .stream()
                .filter(subject -> !subject.getProfessors().contains(professor))
                .toList();

        model.addAttribute("subjects", subjects);
        model.addAttribute("professorId", professorId);
        model.addAttribute("subject", new Subject());
        return "professor/professorSubject";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/assignSubject")
    public String assignSubject(@RequestParam int professorId, Integer subjectId) {
        Subject subject = subjectService.findById(subjectId).get();
        Professor professor = professorService.findById(professorId).get();

        professor.getSubjects().add(subject);
        subject.getProfessors().add(professor);

        professorService.save(professor);
        subjectService.save(subject);

        return "redirect:/professor/list";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/reassignSubject")
    public String reassignSubjectForm(@RequestParam int professorId, Model model) {
        Professor professor = professorService.findById(professorId).get();
        List<Subject> subjects = subjectService.findAll()
                .stream()
                .filter(subject -> subject.getProfessors().contains(professor))
                .toList();

        model.addAttribute("subjects", subjects);
        model.addAttribute("professorId", professorId);
        model.addAttribute("subject", new Subject());
        return "professor/professorSubjectReassign";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/reassignSubject")
    public String reassignSubject(@RequestParam int professorId, Integer subjectId) {
        Subject subject = subjectService.findById(subjectId).get();
        Professor professor = professorService.findById(professorId).get();

        professor.getSubjects().remove(subject);
        subject.getProfessors().remove(professor);

        professorService.save(professor);
        subjectService.save(subject);

        return "redirect:/professor/list";
    }
}
