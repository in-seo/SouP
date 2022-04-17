package Matching.SouP.controller;

import Matching.SouP.crawler.CamPick.Campick;
import Matching.SouP.crawler.CamPick.CampickService;
import Matching.SouP.crawler.Hola.Hola;
import Matching.SouP.crawler.Hola.HolaService;
import Matching.SouP.crawler.inflearn.Inflearn;
import Matching.SouP.crawler.inflearn.InflearnService;
import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.crawler.okky.OkkyService;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.service.PostService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

//@Controller
@RequiredArgsConstructor
@RestController
public class CrawlerController {
    private final OkkyService okkyService;
    private final InflearnService inflearnService;
    private final HolaService holaService;
    private final CampickService campickService;
    private final PostService postService;


    @GetMapping("/crawl")
    public String crawlList(Model model) throws IOException, InterruptedException {
        okkyService.getOkkyPostData();
        List<Okky> listOkky = okkyService.findAll();
        model.addAttribute("listOkky",listOkky);

        inflearnService.getInflearnPostData();
        List<Inflearn> listInf = inflearnService.findAll();
        model.addAttribute("listInf",listInf);

        holaService.getHolaPostData();
        List<Hola> listHola = holaService.findAll();
        model.addAttribute("listHola",listHola);

        campickService.getCampickPostData();
        List<Campick> listCampick=  campickService.findAll();
        model.addAttribute("listCampick",listCampick);

        return "crawlList";
    }

    @GetMapping("/front-projects")
    public JSONObject front() {
        List<Okky> okky = okkyService.findAllDesc();
        List<Inflearn> inflearn = inflearnService.findAllDesc();
        List<Hola> hola = holaService.findAllDesc();
        List<Campick> campick = campickService.findAllDesc();

        JSONObject obj=new JSONObject();
        obj.put("okky",okky);
        obj.put("inflearn",inflearn);
        obj.put("hola",hola);
        obj.put("campick",campick);

        return obj;
    }

    @GetMapping("/projects")
    public List<Post> projects(Pageable pageable) {
        List<Post> posts = postService.findAllDesc();
        return posts;
    }

}