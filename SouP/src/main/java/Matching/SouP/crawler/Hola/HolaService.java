package Matching.SouP.crawler.Hola;

import Matching.SouP.crawler.CamPick.Campick;
import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.crawler.Selenium;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolaService extends CrawlerService {
    private static String urlHola = "https://holaworld.io";
    private final HolaRepository holaRepository;
    private final ConvertToPost convertToPost;


    public void getHolaPostData(){
        Selenium set = new Selenium();
        WebDriver driver = set.getDriver();
        driver.get(urlHola);
        try {
            init();
            Long postCount = holaRepository.findRecent();  //저장되어있는 Hola 사이트 글의 개수  홀라는 따로 해야된다 --> 첫글부터 긁어올거라
//            scroll((JavascriptExecutor) driver);  //전체스크롤
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements element = doc.select("#root > div.main_appWrapper__3scwQ > div.main_app__2_XZu > main > ul");
            int count = element.select(">li").size();
            log.info("훌라 크롤링 시작. {}개 예정",count-postCount);
            if(count>postCount){
                for (Long i = count-postCount; i >0; i--) {
                    scroll((JavascriptExecutor) driver);
                    Elements eachPost = element.select("li:nth-child(" + i + ")");
                    driver.findElement(By.cssSelector("#root > div.main_appWrapper__3scwQ > div.main_app__2_XZu > main > ul > li:nth-child(" + i + ")")).click();
                    Thread.sleep(500);
                    Document realPost = Jsoup.parse(driver.getPageSource());
                    String link = driver.getCurrentUrl();
                    driver.navigate().back();
                    String content = realPost.select("#root > div.studyContent_wrapper__VVyNH > div > div").text();
                    String talk = realPost.select("#root > div.studyContent_wrapper__VVyNH > div > div").select("a").attr("href");
                    if(talk.isEmpty()){talk = parseTalk(content,talk);}
                    if(content.length()>200) {
                        content = content.substring(0, 199);
                    }
                    String userName = realPost.select("#root > div.studyContent_wrapper__VVyNH > section.studyContent_postHeader__2Qu_y > div.studyContent_userAndDate__1iYDv > div.studyContent_user__1XYmH > div").text();
                    String date = realPost.select("#root > div.studyContent_wrapper__VVyNH > section.studyContent_postHeader__2Qu_y > div.studyContent_userAndDate__1iYDv > div.studyContent_registeredDate__3lybC").text();
                    date=standard(date);
                    String postName = eachPost.select("h1").text();
                    StringBuilder stack= new StringBuilder();
                    int length= eachPost.select("li").size();
                    for (int j = 1; j <= length; j++) {
                        stack.append(eachPost.select(" ul > li:nth-child(" + j + ")").text());
                        stack.append(" ");
                    }
                    String views = eachPost.select(" section > div:nth-child(2) > p").text();
                    Hola hola = new Hola(postName,content,userName,date,link,stack.toString(),views,talk);
                    holaRepository.save(hola);
                    convertToPost.hola(hola);
                }
            }
            else
                log.warn("불러올 글이 없습니다!");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close(); // 브라우저 종료
        }
    }

    private String standard(String date) {
        date = date+'T'+ LocalDateTime.now().toLocalTime().toString().substring(0,8);
        date = LocalDateTime.parse(date).toString();
        return date;
    }

    private void scroll(JavascriptExecutor driver) throws InterruptedException {
        var stTime = new Date().getTime(); //현재시간
        while (new Date().getTime() < stTime + 1000) { //5초 동안 무한스크롤 지속
            Thread.sleep(300); //리소스 초과 방지
            driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }
    }

    private void init() {
        Hola temp1 = new Hola("클론프로젝트 하실분 !","dd","dd","dd","dd","","null","111");
        Hola temp2 = new Hola("사이드프로젝트 그룹원을 모집합니다)","dd","dd","dd","111","dd","dd","ddd");
        holaRepository.save(temp1);
        holaRepository.save(temp2);
    }
    public List<Hola> findAll(){
        return holaRepository.findAll();
    }
    public List<Hola> findAllDesc() { return holaRepository.findAllDesc();}
}
