package Matching.SouP.crawler.CamPick;


import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.crawler.Selenium;
import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.dto.project.ShowForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampickService extends CrawlerService {

    private static final String urlCampick = "https://www.campuspick.com/study?category=5";
    private final CampickRepository campickRepository;
    private final ConvertToPost convertToPost;

    public void getCampickPostData() throws InterruptedException {
        Selenium set = new Selenium();
        WebDriver driver = set.getDriver();
        login(driver); //로그인 수행
        driver.get(urlCampick);
        try {
            Long lastPost = campickRepository.findRecent();  //캠픽의 마지막 크롤링 글 번호
            log.info("캠픽 크롤링 시작. {}번 부터",lastPost);
            scroll((JavascriptExecutor) driver); //무한 스크롤
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements element = doc.select("body > div > div.container > div.list");
            int count = element.select(">a").size();
            log.info("스크롤 된 글 개수: {}개",count);
            if(count==0) log.warn("불러올 글이 없습니다!");
            for (int i = count ; i > 0 ; i--){
                Elements eachPost = element.select("a:nth-child("+i+")");
                String link = "https://www.campuspick.com"+eachPost.attr("href");  //링크저장
                int num = Integer.parseInt(link.substring(41));
                if(num<=lastPost){
                    Campick update = campickRepository.findByNum(num);
                    if(update!=null){
                        int views = Integer.parseInt(eachPost.select("p.info > span:nth-child(2)").text());
                        update.updateViews(views);
                    }
                    continue;   //이미 불러온 글이면 조회수만 업데이트 후 저장 X
                }
                String postName = eachPost.select("h2").text();
                Document realPost = click(driver, num); //그 글 클릭 및 되돌아가기
                Elements article = realPost.select("body > div > div > article");
                String userName = article.select("p.profile").text();
                String date = article.select("p.info > span:nth-child(1)").text();
                date = standard(date);
                String people = article.select("div > p:nth-child(6) > span").text();
                String content = article.select("p.text").text();
                content = content +"\n 캠퍼스픽 글은 로그인 없이 볼 수 없음에 원본링크를 첨부합니다. : "+link;  //캠픽 글들은 이렇게 해주자. 로그인없이 볼 수가 없음.

                String talk="";
                talk = parseTalk(content,talk);

                StringBuilder stack = parseStack(postName,content);
                String region = eachPost.select("p.badges > span:nth-child(2)").text();

                int views = Integer.parseInt(eachPost.select("p.info > span:nth-child(2)").text());
                Campick pick = new Campick(num,postName,content,userName,date,link,stack.toString(),views,talk,people,region);
                campickRepository.save(pick);
                convertToPost.campick(pick);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close(); // 브라우저 종료
        }
    }


    private Document click(WebDriver driver, int num) throws InterruptedException {
        driver.navigate().to("https://www.campuspick.com/study/view?id="+ num);
        Thread.sleep(1000);
        Document realPost = Jsoup.parse(driver.getPageSource());
        driver.navigate().back();
        return realPost;
    }

    private void login(WebDriver driver) throws InterruptedException {
        driver.get("https://www.campuspick.com/login");
        driver.findElement(By.cssSelector("#container > div.form > div > input:nth-child(1)")).sendKeys("insi2000");
        driver.findElement(By.cssSelector("#container > div.form > div > input:nth-child(2)")).sendKeys("insi4426!");
        driver.findElement(By.cssSelector("#container > div.form > div > input.submit")).click();
        Thread.sleep(2000);  //로그인 처리 대기
    }
    private String standard(String date) {
        LocalDateTime now = LocalDateTime.now();
        date = now.toLocalDate().toString().substring(0,4)+'-'+date.substring(0,2)+'-'+date.substring(3,5)+'T'+date.substring(6,8)+':'+date.substring(9,11)+':'+now.toLocalTime().toString().substring(6,8);
        date = LocalDateTime.parse(date).toString();
        return date;
    }


    private void scroll(JavascriptExecutor driver) {
//        var stTime = new Date().getTime(); //현재시간
//        while (new Date().getTime() < stTime + 2500) { //5초 동안 무한스크롤 지속
//            Thread.sleep(500); //리소스 초과 방지
            driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
//        }  //글 120개
    }

    @Override
    public List<ShowForm> findAllDesc(Source source) {
        return null;
    }

//    @PostConstruct
//    private void init() {
//        Campick temp = new Campick(219630,"기준점","daf","awegaw","awegaew","kdjafha","124",12,"","dfa","");
//        campickRepository.save(temp);
//    }



}
