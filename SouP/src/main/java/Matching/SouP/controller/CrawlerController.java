package Matching.SouP.controller;

import Matching.SouP.controller.exception.ErrorResponse;
import Matching.SouP.crawler.CamPick.CampickService;
import Matching.SouP.crawler.Hola.HolaService;
import Matching.SouP.crawler.inflearn.InflearnService;
import Matching.SouP.crawler.okky.OkkyService;
import Matching.SouP.domain.post.Source;
import Matching.SouP.dto.project.MainAPIForm;
import Matching.SouP.dto.project.ProjectsAPIForm;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.service.PostService;
import Matching.SouP.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CrawlerController {
    private final OkkyService okkyService;
    private final InflearnService inflearnService;
    private final HolaService holaService;
    private final CampickService campickService;
    private final PostService postService;
    private final ProjectService projectService;
    private static LocalDateTime crawlTime;

    @CacheEvict(value = { "front", "featured" }, allEntries = true)
//    @Scheduled(fixedDelay = 3600000, initialDelay = 20000) //실행 후 20초 뒤에시작, 1시간마다 실행.
    public void crawlList() throws InterruptedException, IOException {
        crawlTime = LocalDateTime.now();
        log.info("현 시각: {} , 크롤링 시작.", crawlTime);
//        okkyService.getOkkyPostData();
        inflearnService.getInflearnPostData();
        holaService.getHolaPostData();  //잠깐보류  오래걸려서.
        campickService.getCampickPostData();
        log.info("크롤링 종료");
    }

    @Cacheable(value = "front")
    @Transactional(readOnly = true)
    @GetMapping("/front/projects")
    public JSONObject front() {
        JSONObject obj=new JSONObject();
        List<ShowForm> okky = projectService.findAllDesc(Source.OKKY);
        List<ShowForm> inflearn = projectService.findAllDesc(Source.INFLEARN);
        List<ShowForm> hola = projectService.findAllDesc(Source.HOLA);
        List<ShowForm> campick = projectService.findAllDesc(Source.CAMPICK);
        List<ShowForm> soup = postService.findAllDesc();
        obj.put("OKKY",okky);
        obj.put("INFLEARN",inflearn);
        obj.put("HOLA",hola);
        obj.put("CAMPICK",campick);
        obj.put("SOUP",soup);

        return obj;
    }

    @Cacheable(value = "featured")
    @GetMapping("/front/featured")
    public JSONObject mainFeatured(){
        JSONObject obj = new JSONObject();
        List<MainAPIForm> recentPost = postService.findRecentPost();
        List<MainAPIForm> hotPost = postService.findHotPost(8);
        obj.put("HOT",hotPost);
        obj.put("NEW",recentPost);
        return obj;
    }

    @GetMapping("/projects/featured")
    public JSONObject detailFeatured(){
        JSONObject obj = new JSONObject();
        List<ProjectsAPIForm> randomForm = postService.findRandomPost(3);
        List<ProjectsAPIForm> hotForm = postService.findHotPost();
        obj.put("RECOMMEND",randomForm);
        obj.put("HOT",hotForm);
        return obj;
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleException1() {
        return ErrorResponse.of(HttpStatus.NOT_FOUND, "로직을 실행하기 위한 DB에 저장된 값 개수가 부족함");
    }

}