package Matching.SouP.controller;



import Matching.SouP.crawler.CamPick.Campick;
import Matching.SouP.crawler.CamPick.CampickService;
import Matching.SouP.crawler.Hola.Hola;
import Matching.SouP.crawler.Hola.HolaService;
import Matching.SouP.crawler.inflearn.Inflearn;
import Matching.SouP.crawler.inflearn.InflearnService;
import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.crawler.okky.OkkyService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

//@org.springframework.web.bind.annotation.RestController
//@RequiredArgsConstructor
//public class RestController {
//    private final OkkyService okkyService;
//    private final InflearnService inflearnService;
//    private final HolaService holaService;
//    private final CampickService campickService;
//
//    @GetMapping("https://www.okky.kr/api/community-posts/study")
//    public List<Okky> okky(Model model) throws IOException, InterruptedException {
//        okkyService.getOkkyPostData();
//        List<Okky> listOkky = okkyService.findAll();
//
//        return listOkky;
//    }
//
//    @GetMapping("https://www.inflearn.com/api/community-posts/study")
//    public List<Inflearn> inflearn(Model model) throws IOException, InterruptedException {
//        inflearnService.getInflearnPostData();
//        List<Inflearn> listInf = inflearnService.findAll();
//
//        return listInf;
//    }
//
//    @GetMapping("https://holaworld.io/api/community-posts/study")
//    public List<Hola> hola(Model model) throws IOException, InterruptedException {
//        System.out.println("api 발동");
//        holaService.getHolaPostData();
//        List<Hola> listHola = holaService.findAll();
//        return listHola;
//    }
//
//    @GetMapping("https://www.campuspick.com/api/community-posts/study")
//    public List<Campick> campick(Model model) throws IOException, InterruptedException {
//        campickService.getCampickPostData();
//        List<Campick> listCampick=  campickService.findAll();
//
//        return listCampick;
//    }
//
//}
