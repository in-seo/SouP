package Matching.SouP.crawler.inflearn;

import Matching.SouP.crawler.PostAdaptor;
import Matching.SouP.crawler.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InflearnService {
    private static final String urlInf ="https://www.inflearn.com/community/studies";
    private final InflearnRepository inflearnRepository;
    private final PostAdaptor postAdaptor;


    public void getInflearnPostData() throws IOException {
        int start = recentPost();
        log.info("인프런 크롤링 시작. {}번부터",start);
        int Page = startPage(start);
        while(Page>0){
            Document doc = Jsoup.connect(urlInf + "?page=" + Page).get();  // 페이지 선택.
            for (int i = 20; i >0; i--) {  //오래된 글부터 크롤링  그럼 반드시 최신글은 DB에서 가장 밑에꺼임.
                Elements element = doc.select("#main > section.community-body > div.community-body__content > div.question-list-container > ul > li:nth-child("+i+")");
                Elements title = element.select("a > div > div.question__info > div.question__title");
                String postName = title.select("h3").text();
                String link = element.select("a").attr("href");
                String num = link.substring(9).split("/")[0];
                link = "https://www.inflearn.com"+link;
                if(Integer.parseInt(num) <= start){
                    continue;   //이미 불러온 글이면 조회수만 업데이트 후 저장 X
                }
                Document realPost = Jsoup.connect(link).get();
                String date = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__header > div.header__sub-title > div > div > span.sub-title__created-at > span.sub-title__value").text();

                date = standard(date); //표준시간 변환
                String content = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__content > div.content__body.markdown-body").text();

                StringBuilder stack = CrawlerService.parseStack(postName,content);

                String talk = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__content > div.content__body.markdown-body").select("a").attr("href");
                if(talk.isEmpty())
                    talk = CrawlerService.parseTalk(content,talk);

                if(talk.length()>200)
                    talk = talk.substring(0,199);

                if(content.length()>200)
                    content = content.substring(0, 199);


                String userName = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__header > div.header__sub-title > div > h6 > a").text();
                String pass = title.select("span").text();

                if(pass.equals("모집완료") || postName.contains("마감") || postName.contains("모집완료"))
                    continue;

                Inflearn post = new Inflearn(num,postName,content,userName,date,link, stack.toString(),talk);
                inflearnRepository.save(post);
                postAdaptor.saveInflearn(post);
            }
            Page--;
        }
    }
    private String standard(String date) {
        date = "20"+date.substring(0,2)+"-"+date.substring(3,5)+"-"+date.substring(6,8)+"T"+date.substring(9)+":00";
        date = LocalDateTime.parse(date).toString();
        return date;
    }

    private int startPage(int start) throws IOException {
        int page=1;
        /**
         * 디비에서 저장된 가장 최근 글이 1페이지에 있나 여부 판단. 만약 글 리젠이 많아서 2페이지 중반부터 크롤링 해야되면? 3페이지 첫글이 start보다 작아야 됨.
         * !!다음 페이지의 맨 첫 번째 글이, 가장 최근에 디비에 저장된 글의 번호보다 크면 다음 페이지로 넘어가야됨
         */
        while(true){
            Document doc = Jsoup.connect(urlInf + "?page="+page).get();
            String sNum = doc.select("#main > section.community-body > div.community-body__content > div.question-list-container > ul > li:nth-child(1) > a").attr("href").substring(9).split("/")[0];
            int num = Integer.parseInt(sNum);
            if(num<start){
                log.info("{}페이지부터 시작",page-1);
                return page-1;
            }
            page++;
        }
    }
    public int recentPost() {
        return inflearnRepository.findRecent().intValue();
    }

}
