package net.fiv.garagetestbot.controller;

import lombok.RequiredArgsConstructor;
import net.fiv.garagetestbot.model.Branch;
import net.fiv.garagetestbot.model.Feedback;
import net.fiv.garagetestbot.model.enums.UserRole;
import net.fiv.garagetestbot.service.BranchService;
import net.fiv.garagetestbot.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final BranchService branchService;

    @GetMapping
    public String getFeedbacks(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Integer criticality,
            Model model
    ) {
        List<Feedback> feedbacks = feedbackService.filter(branch, role, criticality);
        List<Branch> branches = branchService.getAllBranches();
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("branches", branches);
        model.addAttribute("roles", UserRole.values());
        return "admin/feedbacks";
    }
}
