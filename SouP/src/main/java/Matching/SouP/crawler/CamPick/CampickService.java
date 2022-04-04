package Matching.SouP.crawler.CamPick;


import Matching.SouP.crawler.Selenium;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampickService {

    private static String urlCampick = "https://www.campuspick.com/study?category=5";
    private final CampickRepository campickRepository;

    public void getCampickPostData() throws InterruptedException {
        System.out.println("캠픽 크롤링 시작.");
        Selenium set = new Selenium();
        WebDriver driver = set.getDriver();
        login(driver); //로그인 수행
        driver.get(urlCampick);
        try {
            init();
            Long lastPost = campickRepository.findRecent();  //캠픽의 마지막 크롤링 글 번호
            System.out.println(lastPost);
//            scroll((JavascriptExecutor) driver); //무한 스크롤
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements element = doc.select("body > div > div.container > div.list");
            int count = element.select(">a").size();
            System.out.println("가져올 글 개수 : "+count);
            for (int i = count ; i > 0 ; i--){
                Elements eachPost = element.select("a:nth-child("+i+")");
                String link = eachPost.attr("href");  //링크저장
                int num = Integer.parseInt(link.substring(15));
                if(num<=lastPost){
                    continue;   //마지막 글 번호보다 커야 그 이후 글이다.
                }
                Document realPost = click(driver, num); //그 글 클릭 및 되돌아가기
                Elements article = realPost.select("body > div > div > article");
                String userName = article.select("p.profile").text();
                String date = article.select("p.info > span:nth-child(1)").text();
                String people = article.select("div > p:nth-child(6) > span").text();
                String content = article.select("p.text").text();
                String region = eachPost.select("p.badges > span:nth-child(2)").text();
                String postName = eachPost.select("h2").text();
                String views = eachPost.select("p.info > span:nth-child(2)").text();
                Campick pick = new Campick(num,postName,content,userName,date,views,link,people,region);
                campickRepository.save(pick);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close(); // 브라우저 종료
        }
    }

    private Document click(WebDriver driver, int num) throws InterruptedException {
        driver.navigate().to("https://www.campuspick.com/study/view?id="+ num);
        Thread.sleep(3000);
        Document realPost = Jsoup.parse(driver.getPageSource());
        driver.navigate().back();
        return realPost;
    }

    private void login(WebDriver driver) throws InterruptedException {
        driver.get("https://www.campuspick.com/login");
        driver.findElement(By.cssSelector("#container > div.form > div > input:nth-child(1)")).sendKeys("insi2000");
        driver.findElement(By.cssSelector("#container > div.form > div > input:nth-child(2)")).sendKeys("insi4426!");
        driver.findElement(By.cssSelector("#container > div.form > div > input.submit")).click();
        Thread.sleep(3000);  //로그인 처리 대기
    }

    private void scroll(JavascriptExecutor driver) throws InterruptedException {
        var stTime = new Date().getTime(); //현재시간
        while (new Date().getTime() < stTime + 2500) { //5초 동안 무한스크롤 지속
            Thread.sleep(500); //리소스 초과 방지
            driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }  //글 120개
    }

    private void init() {
        Campick temp1 = new Campick(198900,"agawega","daf","awegaw","awegaew","kdjafha","124","https://afawef","dfa");
        campickRepository.save(temp1);
    }
    public List<Campick> findAll(){
        return campickRepository.findAll();
    }
    public int recentPost(){
        Long recent = campickRepository.findRecent();
        Optional<Campick> recentPost = campickRepository.findById(recent);
        return recentPost.get().getNum();  //캠픽의 글 번호
    }

}
