//package Matching.SouP.controller;
//
//
//import Matching.SouP.config.auth.LoginUser;
//import Matching.SouP.config.auth.dto.SessionUser;
//import Matching.SouP.dto.NoticeForm;
//import Matching.SouP.service.NoticeService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//public class NoticeController {
//
//    private final NoticeService noticeService;
//
//    @PostMapping("/notice")
//    public String notice(@RequestBody NoticeForm form, @LoginUser SessionUser user) {
//        log.info("notice controller");
//        noticeService.createNotice(form.getPostName(),form.getContent());
//        return "index";
//    }
//}
