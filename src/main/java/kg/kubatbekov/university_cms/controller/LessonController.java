package kg.kubatbekov.university_cms.controller;

import kg.kubatbekov.university_cms.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/list")
    public String getLessons(Model model) {
        model.addAttribute("lessons", lessonService.findAll());
        return "lessons";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/delete")
    public String deleteLesson(@RequestParam int lessonId) {
        lessonService.deleteById(lessonId);
        return "redirect:/lesson/list";
    }
}
