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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;

    @Autowired
    public SubjectController(SubjectService subjectService, ProfessorService professorService) {
        this.subjectService = subjectService;
        this.professorService = professorService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping("/list")
    public String getSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        return "subjects";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/delete")
    public String deleteSubject(@RequestParam int subjectId) {
        subjectService.deleteById(subjectId);
        return "redirect:/subject/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/add")
    public String addSubject(@ModelAttribute("subject") Subject subject) {
        return "subjectAdd";
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
        return "subjectUpdate";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/update")
    public String updateSubject(@RequestParam int subjectId, @ModelAttribute("subject") Subject subject) {
        subjectService.update(subject);
        return "redirect:/subject/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/setTeacher")
    public String setTeacherForm(@RequestParam int subjectId, Model model) {
        List<Professor> professors = professorService.findAll();
        model.addAttribute("professors", professors);
        model.addAttribute("subjectId", subjectId);
        return "subjectTeacher";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/setTeacher")
    public String setTeacher(@RequestParam int subjectId,@RequestBody Professor professor) {
        System.out.println("subjectId: " + subjectId);
        System.out.println("professor: " + professor);
        System.out.println("professor: " + professor.getProfessorName());
        return "redirect:/subject/list";
    }
}
