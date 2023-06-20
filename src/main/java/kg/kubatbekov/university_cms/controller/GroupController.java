package kg.kubatbekov.university_cms.controller;

import kg.kubatbekov.university_cms.model.Group;
import kg.kubatbekov.university_cms.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping("/list")
    public String getGroups(Model model){
        model.addAttribute("groups",groupService.findAll());
        return "group/groups";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/delete")
    public String deleteGroup(@RequestParam int groupId) {
        groupService.deleteById(groupId);
        return "redirect:/group/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/add")
    public String addGroup(@ModelAttribute("group") Group group) {
        return "group/groupAdd";
    }


    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/save")
    public String saveGroup(@ModelAttribute("group") Group group) {
        groupService.save(group);
        return "redirect:/group/list";
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @GetMapping("/updateForm")
    public String updateGroupForm(@RequestParam int groupId, Model model) {
        Group group = groupService.findById(groupId).get();
        model.addAttribute("group", group);
        return "group/groupUpdate";
    }
    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping("/update")
    public String updateGroup(@RequestParam int groupId, @ModelAttribute("group") Group group) {
        Group oldGroup = groupService.findById(groupId).get();
        oldGroup.setGroupName(group.getGroupName());
        oldGroup.setGrade(group.getGrade());

        groupService.update(oldGroup);

        return "redirect:/group/list";
    }
}
