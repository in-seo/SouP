package Matching.SouP.controller;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class tempController {
    private final PostService postService;

    @GetMapping("/crawling")
    public String projects(Model model) {
        List<Post> temp = postService.findAllByDesc();
        model.addAttribute("posts",temp);
        return "crawlList";
    }
}
