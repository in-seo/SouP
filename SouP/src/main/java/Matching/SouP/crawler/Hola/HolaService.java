package Matching.SouP.crawler.Hola;

import Matching.SouP.crawler.PostAdaptor;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.crawler.Selenium;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolaService extends CrawlerService {
    private static String urlHola = "https://holaworld.io";
    private final HolaRepository holaRepository;
    private final PostAdaptor postAdaptor;
    private final int beginIndex = 27;

    public void getHolaPostData(){
        Selenium set = new Selenium();
        WebDriver driver = set.getDriver();
        driver.get(urlHola);
        boolean flag = false;
        try {
            String standard = recentPost();
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements element = doc.select("#root > main > ul");
            log.info("훌라 크롤링 시작, 가장 최신글번호 = {}", standard);
            Thread.sleep(500);
            int count = element.select(">a").size();
            log.warn("글 갯수 = {} ",count);
            for (int i = count; i > 0; i--) {
                if(i==count){
                    driver.findElement(By.cssSelector("#root > main > ul > a:nth-child(1)")).click();
                    String first = driver.getCurrentUrl().substring(beginIndex);
                    if(first.compareTo(standard) <= 0) {
                        log.warn("사이트 내 가장 최신글 번호 = {}, 따라서 불러올 글이 없습니다!",first);
                        return;
                    }
                    else
                        driver.navigate().back();
                }
                int aSelector = i*2 - 1; // 홀수번만 사용 예정
                Elements eachPost = element.select("a:nth-child(" + aSelector + ")");
                driver.get(urlHola + eachPost.attr("href"));
                Thread.sleep(500);
                Document realPost = Jsoup.parse(driver.getPageSource());
                String link = driver.getCurrentUrl();
                String num = link.substring(beginIndex);
                if(num.compareTo(standard)<=0){
                    driver.navigate().back();
                    continue;   //이미 불러온 글이면 패스
                }
                driver.navigate().back();
                String content = realPost.select("#root > div.studyContent_wrapper__VVyNH > div.studyContent_postContentWrapper__187Zh > div").text();
                String talk = realPost.select("#root > div.studyContent_wrapper__VVyNH > div.studyContent_postContentWrapper__187Zh > div").select("a").attr("href");
                if(talk.isEmpty()){talk = parseTalk(content,talk);}
                if(talk.length()>200)
                    talk = talk.substring(0,199);
                if(content.length()>200)
                    content = content.substring(0, 199);
                String userName = realPost.select("#root > div.studyContent_wrapper__VVyNH > section.studyContent_postHeader__2Qu_y > div.studyContent_userAndDate__1iYDv > div.studyContent_user__1XYmH > div").text();
                String date = realPost.select("#root > div.studyContent_wrapper__VVyNH > section.studyContent_postHeader__2Qu_y > div.studyContent_userAndDate__1iYDv > div.studyContent_registeredDate__3lybC").text();
                date=standard(date);
                String postName = eachPost.select("h1").text();
                StringBuilder stack = parseStack(postName,content);
                int views = Integer.parseInt(eachPost.select(" section > div.studyItem_viewsAndComment__1Bxpj > div:nth-child(1) > p").text());
                Hola hola = new Hola(num,postName,content,userName,date,link,stack.toString(),views,talk);
                holaRepository.save(hola);
                postAdaptor.saveHola(hola);
                flag = true;
            }
            if(!flag)
                log.warn("불러올 글이 없습니다!");
            else
                log.info("홀라 크롤링 성공");
        }catch (StringIndexOutOfBoundsException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.close(); // 브라우저 종료
        }
    }

    private String standard(String date) {
        date = date.substring(0,4)+'-'+date.substring(5,7)+'-'+date.substring(8,10)+'T'+ LocalDateTime.now().toLocalTime().toString().substring(0,8);
        date = LocalDateTime.parse(date).toString();
        return date;
    }

//    private void scroll(JavascriptExecutor driver) throws InterruptedException {
//        var stTime = new Date().getTime(); //현재시간
//        while (new Date().getTime() < stTime + 500) { //5초 동안 무한스크롤 지속
//            Thread.sleep(300); //리소스 초과 방지
//            driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
//        }
//    }

    public String recentPost(){
        return holaRepository.findRecent();
    }

}
