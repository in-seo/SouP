package Matching.SouP.crawler.okky;

import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.crawler.Selenium;
import Matching.SouP.domain.post.Source;
import Matching.SouP.dto.project.ShowForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OkkyService extends CrawlerService{
    private static final String urlOkky ="https://okky.kr/community/gathering";
    private final OkkyRepository okkyRepository;
    private final ConvertToPost convertToPost;

    public void getOkkyPostData() throws IOException, InterruptedException {
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
                for (int i = 21; i >0; i--) {  //오래된 글부터 크롤링  그럼 반드시 최신글은 DB에서 가장 밑에꺼임.
                    if(i==6)
                        continue;
                    Elements element = doc.select("#__next > main > div > div:nth-child(2) > div > div:nth-child(5) > div > ul > li.py-4:nth-child(" + i + ")");
                    Elements title = element.select("div > div.my-2 > a");
                    String postName = title.text();
                    String num="";
                    try {
                        num= title.attr("href").substring(10);
                    } catch (StringIndexOutOfBoundsException e) {
                        i--;
                        continue;
                    }
                    if(Integer.parseInt(num)<=start){
                        Okky update = okkyRepository.findByNum(Integer.parseInt(num));
                        if(update!=null)
                            update.updateViews(update.getViews()+10);

                        continue;   //이미 불러온 글이면 조회수만 업데이트 후 저장 X
                    }
                    String link = "https://okky.kr/articles/"+num;
                    Document realPost = click(driver, link);
                    String content = realPost.select("#__next > main > div > div:nth-child(2) > div > div:nth-child(3) > div:nth-child(2) > div:nth-child(3) > div > div > div").text();
                    StringBuilder stack = parseStack(postName,content);
                    String talk = "";
                    talk = parseTalk(content,talk);
                    if(content.length()>200) {
                        content = content.substring(0, 199);
                    }

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

    private String standard(String date) {
        date = date.substring(0,10)+"T"+ date.substring(11);
        date = LocalDateTime.parse(date).toString();
        return date;
    }

    private int startPage(WebDriver driver, int start) /*throws IOException, StringIndexOutOfBoundsException*/ {
        int page=2;  //page가 1이면 okky에선 1페이지이다..
        /**
         * 디비에서 저장된 가장 최근 글이 1페이지에 있나 여부 판단. 만약 글 리젠이 많아서 2페이지 중반부터 크롤링 해야되면? 3페이지 첫글이 start보다 작아야 됌.
         * !!다음 페이지의 맨 첫 번째 글이, 가장 최근에 디비에 저장된 글의 번호보다 크면 다음 페이지로 넘어가야됌
         */
        int cnt = 1;
        while(true){
            driver.get(urlOkky + "?page=" + page);
            String html = driver.getPageSource();
            Document doc = Jsoup.parse(html);
            int num = 3000000;
            try {
                String sNum = doc.select("#__next > main > div > div:nth-child(2) > div > div:nth-child(5) > div > ul > li:nth-child("+cnt+") > div > div.my-2 > a").attr("href").substring(10);//각 페이지 첫 글
                num = Integer.parseInt(sNum);
            }catch (StringIndexOutOfBoundsException e){
                cnt++;
                continue;
            }catch (NullPointerException e){
                cnt++;
                continue;
            }
            if(num<start){
                log.info("{}페이지부터 시작",page-1);
                return page-1;
            }
            cnt=1;
            page++;
        }
    }

    public int recentPost(){
        Long recent = okkyRepository.findRecent();
        return recent.intValue();
    }

    @Override
    public List<ShowForm> findAllDesc(Source source) {
        return null;
    }

//    @PostConstruct
//    private void init() { //임시 기준점 -> 이 번호 이후의 글을 긁어온다.
//        Okky temp = new Okky("1316111","임시 기준점","","","","","","");
//        okkyRepository.save(temp);
//    }
}
