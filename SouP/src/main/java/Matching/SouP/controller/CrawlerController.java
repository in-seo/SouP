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
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@RestController
public class CrawlerController {
    private final OkkyService okkyService;
    private final InflearnService inflearnService;
    private final HolaService holaService;
    private final CampickService campickService;
    private final PostService postService;


//    @GetMapping("/crawl")
//    @Scheduled(fixedDelay = 3600000, initialDelay = 10000) //실행 후 10초 뒤에시작, 1시간마다 실행.
    public void crawlList() throws InterruptedException, IOException {
        log.info("현 시각: {} , 크롤링 시작.", LocalDateTime.now());
        okkyService.getOkkyPostData();
        inflearnService.getInflearnPostData();
//        holaService.getHolaPostData();  //잠깐보류  오래걸려서.
//        campickService.getCampickPostData();
        log.info("크롤링 종료");
    }

    @GetMapping("/front-projects")
    public JSONObject front() {
        List<Okky> okky = okkyService.findAllDesc();
        List<Inflearn> inflearn = inflearnService.findAllDesc();
        List<Hola> hola = holaService.findAllDesc();
        List<Campick> campick = campickService.findAllDesc();

        JSONObject obj=new JSONObject();
        obj.put("OKKY",okky);
        obj.put("INFLEARN",inflearn);
        obj.put("HOLA",hola);
        obj.put("CAMPICK",campick);

        return obj;
    }

    @GetMapping("/projects")
    public Page<Post> projects(Pageable pageable) {
        return postService.findAllDesc(pageable);
    }

    @GetMapping("/projects/recent")
    public Post recentPost(){
        return postService.findRecentPost();
    }

    @GetMapping("/projects/hot")
    public List<Post> hotPost() {return postService.findAllNDaysBefore(3);}

}