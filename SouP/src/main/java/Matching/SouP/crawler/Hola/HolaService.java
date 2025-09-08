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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolaService extends CrawlerService {
    private static final String urlHola = "https://holaworld.io";
    private final HolaRepository holaRepository;
    private final PostAdaptor postAdaptor;
    private final int beginIndex = 27;

    public void getHolaPostData() {
        Selenium set = new Selenium();
        WebDriver driver = set.getDriver();
        driver.get(urlHola);
        boolean flag = false;

        try {
            String standard = recentPost();
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements element = doc.select("#root > main > div > ul");

            log.info("훌라 크롤링 시작, 가장 최신글번호 = {}", standard);
            Thread.sleep(500);

            int count = element.select(">a").size();
            log.warn("글 갯수 = {}", count);

            boolean hasAdvertise = false;

            for (int i = count; i > 0; i--) {
                if (i == count) {
                    try {
                        driver.findElement(By.cssSelector("#root > main > div > ul > a:nth-child(1)")).click();
                        String first = driver.getCurrentUrl();
                        if (first.length() > beginIndex) {
                            first = first.substring(beginIndex);
                        }
                        if (first.compareTo(standard) <= 0) {
                            log.warn("사이트 내 가장 최신글 번호 = {}, 따라서 불러올 글이 없습니다!", first);
                            return;
                        } else {
                            driver.navigate().back();
                        }
                    } catch (NoSuchElementException e) {
                        hasAdvertise = true;
                    }
                }

                int childNum = i * 2 - 1; // 홀수번만 사용 예정
                if (hasAdvertise) childNum = i * 2;

                Elements eachPost = element.select("a:nth-child(" + childNum + ")");
                if (eachPost.isEmpty()) continue;

                driver.get(urlHola + eachPost.attr("href"));
                Thread.sleep(1000);

                Document realPost = Jsoup.parse(driver.getPageSource());
                String link = driver.getCurrentUrl();
                String num = link.length() > beginIndex ? link.substring(beginIndex) : link;

                if (num.compareTo(standard) <= 0) {
                    driver.navigate().back();
                    continue;
                }
                driver.navigate().back();

                String content = realPost.select("#root > main > div > div > div").text();
                String talk = realPost.select("#root > main > div > div > div a").attr("href");
                if (talk.isEmpty()) talk = parseTalk(content, talk);
                if (talk.length() > 200) talk = talk.substring(0, 199);
                if (content.length() > 200) content = content.substring(0, 199);

                String userInfo = realPost.select("#root > main > div > section > div[class^=\"_userAndDate\"]").text();
                String[] infoParts = userInfo.split("\\s+");
                String userName = infoParts.length > 0 ? infoParts[0] : "unknown";
                String date = infoParts.length > 1 ? infoParts[1] : LocalDateTime.now().toString();
                date = standard(date);

                String postName = eachPost.select("h1").text();
                StringBuilder stack = parseStack(postName, content); // 안전하게 빈 StringBuilder 가능

                int views = 0;
                try {
                    String viewsText = eachPost.select(" section > div[class^=\"_views\"] > div:nth-child(1) > p").text();
                    views = Integer.parseInt(viewsText);
                } catch (NumberFormatException e) {
                    log.warn("뷰 수 파싱 실패, 기본값 0으로 설정");
                }

                Hola hola = new Hola(num, postName, content, userName, date, link, stack.toString(), views, talk);
                holaRepository.save(hola);
                postAdaptor.saveHola(hola);
                flag = true;
            }

            if (!flag) log.warn("불러올 글이 없습니다!");
            else log.info("홀라 크롤링 성공");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("크롤링 도중 인터럽트 발생", e);
        } finally {
            driver.close(); // 브라우저 종료
        }
    }

    private String standard(String date) {
        try {
            date = date.substring(0, 4) + '-' + date.substring(5, 7) + '-' + date.substring(8, 10) +
                    'T' + LocalDateTime.now().toLocalTime().toString().substring(0, 8);
            return LocalDateTime.parse(date).toString();
        } catch (Exception e) {
            log.warn("날짜 포맷 변환 실패, 현재 시간 사용");
            return LocalDateTime.now().toString();
        }
    }

    public String recentPost() {
        return holaRepository.findRecent();
    }

}
