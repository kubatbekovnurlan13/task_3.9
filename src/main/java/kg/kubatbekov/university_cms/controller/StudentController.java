package kg.kubatbekov.university_cms.controller;

import kg.kubatbekov.university_cms.model.Group;
import kg.kubatbekov.university_cms.model.Student;
import kg.kubatbekov.university_cms.model.Subject;
import kg.kubatbekov.university_cms.service.GroupService;
import kg.kubatbekov.university_cms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping("/list")
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "student/students";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/delete")
    public String deleteStudent(@RequestParam int studentId) {
        studentService.deleteById(studentId);
        return "redirect:/student/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("group",new Group());
        return "student/studentAdd";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.save(student);
        return "redirect:/student/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/updateForm")
    public String updateStudentForm(@RequestParam int studentId, Model model) {
        Student student = studentService.findById(studentId).get();
        model.addAttribute("student", student);
        model.addAttribute("groups", groupService.findAll());
        return "student/studentUpdate";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/update")
    public String updateStudent(@RequestParam int studentId, @ModelAttribute("student") Student student) {
        studentService.update(student);
        return "redirect:/student/list";
    }
}
