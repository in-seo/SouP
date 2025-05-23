package Matching.SouP.crawler.okky;

import Matching.SouP.common.SlackNotifier;
import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.crawler.Selenium;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OkkyService {
    private static final String urlOkky ="https://okky.kr/community/gathering";
    private final OkkyRepository okkyRepository;
    private final ConvertToPost convertToPost;

    public void getOkkyPostData() {
        Selenium set = new Selenium();
        WebDriver driver = set.getDriver();
        driver.get(urlOkky);
        boolean flag = false;
        try{
            int start = recentPost();
            log.info("OKKY 크롤링 시작. {}번부터",start);
            int Page = startPage(driver,start);
            while(Page>=1){
                driver.get(urlOkky + "?page=" + Page);
                String html = driver.getPageSource();
                Document doc = Jsoup.parse(html);
                Elements elements = doc.select("#__next > main > div > div:nth-child(2) > div > div:nth-child(5) > ul > li[class^=\"py\"]\n");
                for (int i = elements.size() - 1; i >= 0; i--) {
                    Element element = elements.get(i);
                    Elements title = element.select("div > div.my-2 > div > a");
                    // 여기서 각 element에 대한 처리를 진행
                    String postName = title.text();
                    String num;
                    try {
                        String href = title.attr("href");
                        num = href.substring(10, href.lastIndexOf('?'));
                    } catch (StringIndexOutOfBoundsException e) {
                        i--;
                        continue;
                    }
                    if(Integer.parseInt(num)<=start)
                        continue;

                    String link = "https://okky.kr/articles/"+num;
                    Document realPost = click(driver, link);
                    String content = realPost.select("#__next > main > div > div:nth-child(2) > div > div:nth-child(2) > div:nth-child(2) > div:nth-child(2) > div > div > div").text();
                    StringBuilder stack = CrawlerService.parseStack(postName,content);
                    String talk = "";
                    talk = CrawlerService.parseTalk(content,talk);
                    if(content.length()>200)
                        content = content.substring(0, 199);
                    if(talk.length()>200)
                        talk = talk.substring(0, 199);

                    String userName = element.select("div > div:nth-child(1) > a:nth-child(2)").text();
                    String date = LocalDateTime.now().toString();
                    Okky okky = new Okky(num,postName,content,userName,date,link,stack.toString(),talk);
                    okkyRepository.save(okky);
                    convertToPost.okky(okky);
                    flag = true;
                }
                Page--;
            }
            if(!flag)
                log.warn("불러올 글이 없습니다!");
            else
                log.info("오키 크롤링 성공");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close(); // 브라우저 종료
        }
    }

    private Document click(WebDriver driver, String link) throws InterruptedException {
        driver.navigate().to(link);
        Thread.sleep(1000);
        Document realPost = Jsoup.parse(driver.getPageSource());
        driver.navigate().back();
        return realPost;
    }


    private int startPage(WebDriver driver, int start) throws StringIndexOutOfBoundsException {
        int page=2;
        /**
         * 디비에서 저장된 가장 최근 글이 1페이지에 있나 여부 판단. 만약 글 리젠이 많아서 2페이지 중반부터 크롤링 해야되면? 3페이지 첫글이 start보다 작아야 됨.
         * !!다음 페이지의 맨 첫 번째 글이, 가장 최근에 디비에 저장된 글의 번호보다 크면 다음 페이지로 넘어가야됌
         */
        int cnt = 1;
        while(true){
            if (page > 5 || cnt > 6) {
                SlackNotifier slackNotifier = new SlackNotifier();
                slackNotifier.sendMessageToSlack("시작 페이지를 찾지 못했습니다.");
                throw new IllegalStateException("오키 파싱 에러");
            }
            driver.get(urlOkky + "?page=" + page);
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            int num = Integer.MAX_VALUE;
            try {
                Elements elements = doc.select("#__next > main > div > div:nth-child(2) > div > div:nth-child(5) > ul > li[class^=\"py\"]\n");
                String href = elements.get(0).select("div > div.my-2 > div > a").attr("href"); // 각 페이지 첫 글의 번호를 통해 페이지를 선택하자.
                String sNum = href.substring(10, href.lastIndexOf('?'));
                num = Integer.parseInt(sNum);
            }catch (StringIndexOutOfBoundsException | NullPointerException e){
                cnt++;
                log.info("StringIndexOutOfBoundsException");
                SlackNotifier slackNotifier = new SlackNotifier();
                slackNotifier.sendMessageToSlack(e.getMessage());
                continue;
            }
            if(num<start){
                log.info("{}페이지부터 시작",page-1);
                return page-1;
            }
            page++;

        }
    }

    public int recentPost(){
        return okkyRepository.findRecent().intValue();
    }
}
