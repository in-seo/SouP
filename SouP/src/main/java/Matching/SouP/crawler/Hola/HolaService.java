package Matching.SouP.crawler.Hola;

import Matching.SouP.crawler.CamPick.Campick;
import Matching.SouP.crawler.Selenium;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolaService {
    private static String urlHola = "https://holaworld.io";
    private final HolaRepository holaRepository;
    private WebElement element;

    public void getHolaPostData(){
        System.out.println("홀라 크롤링 시작.");
        Selenium set = new Selenium();
        WebDriver driver = set.getDriver();
        driver.get(urlHola);
        try {
            init();
            Long postCount = holaRepository.findRecent();  //저장되어있는 Hola 사이트 글의 개수  홀라는 따로 해야된다 --> 첫글부터 긁어올거라
//            scroll((JavascriptExecutor) driver);
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements element = doc.select("#root > div.main_appWrapper__3scwQ > div.main_app__2_XZu > main > ul");
            int count = element.select(">li").size();
            if(count>postCount){
                for (Long i = count-postCount; i >0; i--) {
                    scroll((JavascriptExecutor) driver);
                    Elements eachPost = element.select("li:nth-child(" + i + ")");
                    driver.findElement(By.cssSelector("#root > div.main_appWrapper__3scwQ > div.main_app__2_XZu > main > ul > li:nth-child(" + i + ")")).click();
                    Thread.sleep(2000);
                    Document realPost = Jsoup.parse(driver.getPageSource());
                    String link = driver.getCurrentUrl();
                    driver.navigate().back();
                    String content = realPost.select("#root > div.studyContent_wrapper__VVyNH > div > div").text();
                    if(content.length()>200) {
                        content = content.substring(0, 199);
                    }
                    String userName = realPost.select("#root > div.studyContent_wrapper__VVyNH > section.studyContent_postHeader__2Qu_y > div.studyContent_userAndDate__1iYDv > div.studyContent_user__1XYmH > div").text();
                    String date = realPost.select("#root > div.studyContent_wrapper__VVyNH > section.studyContent_postHeader__2Qu_y > div.studyContent_userAndDate__1iYDv > div.studyContent_registeredDate__3lybC").text();
                    String talk = realPost.select("#root > div.studyContent_wrapper__VVyNH > div > div").select("a").attr("href");
                    String postName = eachPost.select("h1").text();
                    StringBuilder stack= new StringBuilder();
                    int length= eachPost.select("li").size();
                    for (int j = 1; j <= length; j++) {
                        stack.append(eachPost.select(" ul > li:nth-child(" + j + ")").text());
                        stack.append(" ");
                    }
                    String views = eachPost.select(" section > div:nth-child(2) > p").text();
                    System.out.println(postName+" "+ stack+" " + views);
                    Hola hola = new Hola(postName,content,userName,date,views,link,talk,stack.toString());
                    holaRepository.save(hola);
                }
            }
            else
                System.out.println("불러올 글이 없습니다.");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close(); // 브라우저 종료
        }
    }


    private void scroll(JavascriptExecutor driver) throws InterruptedException {
        var stTime = new Date().getTime(); //현재시간
        while (new Date().getTime() < stTime + 2000) { //5초 동안 무한스크롤 지속
            Thread.sleep(500); //리소스 초과 방지
            driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }
    }

    private void init() {
        Hola temp1 = new Hola("dd","dd","dd","dd","dd","클론프로젝트 하실분 !","null","111");
        Hola temp2 = new Hola("사이드프로젝트 그룹원을 모집합니다)","dd","dd","dd","111","dd","dd","ddd");
        holaRepository.save(temp1);
        holaRepository.save(temp2);
    }
    public List<Hola> findAll(){
        return holaRepository.findAll();
    }
    public List<Hola> findAllDesc() { return holaRepository.findAllDesc();}
}
