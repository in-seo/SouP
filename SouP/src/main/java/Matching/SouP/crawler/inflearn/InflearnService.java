package Matching.SouP.crawler.inflearn;

import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.CrawlerService;
import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.dto.project.ShowForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InflearnService extends CrawlerService {
    private static final String urlInf ="https://www.inflearn.com/community/studies"; //https://www.inflearn.com/community/studies?page=2
    private final InflearnRepository inflearnRepository;
    private final ConvertToPost convertToPost;


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
                String num = link.substring(9);
                link = "https://www.inflearn.com"+link;
                if(Integer.parseInt(num)<=start){
                    continue;   //이미 불러온 글이면 조회수만 업데이트 후 저장 X
                }
                Document realPost = Jsoup.connect(link).get();
                String date = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__header > div.header__sub-title > span").text().substring(2);
                date = standard(date); //표준시간 변환
                String content = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__content > div.content__body.markdown-body > div").text();
                if(content.length()<3)
                    content=realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__content > div.content__body.markdown-body").text();
                    content+=realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__content > div.content__body.markdown-body > div > ul").text();

//                Elements tags = element.select("a > div > div.question__info > div.question__tags");
//                int tagCount = tags.select("button").size();
//                for (int j = 1; j <= tagCount; j++) {
//                    stack.append(tags.select("button:nth-child(" + j + ")").select("span.ac-tag__name").text()).append(" ");
//                    if(j==3)
//                        break;
//                }
                StringBuilder stack = parseStack(postName,content);

                String talk = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__content > div.content__body.markdown-body").select("a").attr("href");
                        if(talk.isEmpty()){talk = parseTalk(content,talk);}
                if(content.length()>200) {
                    content = content.substring(0, 199);
                }

                String userName = realPost.select("#main > section.community-post-detail__section.community-post-detail__post > div.section__content > div > div.community-post-info__header > div.header__sub-title > h6").text();
                        String pass = title.select("span").text();

                if(pass.equals("모집완료 ")){
                    continue;  //모집완료이면 패스
                }
                if(postName.contains("마감") || postName.contains("모집완료")) continue; //제목에 [마감]이 들어가있으면 마감
                Inflearn post = new Inflearn(num,postName,content,userName,date,link, stack.toString(),talk);
                inflearnRepository.save(post);
                convertToPost.inflearn(post);
            }
            Page--;
        }
    }
//    @PostConstruct
//    private void init() { //임시 기준점 -> 이 번호 이후의 글을 긁어온다.
//        Inflearn temp = new Inflearn("548298","기준점","","","","","","");
//        inflearnRepository.save(temp);
//    }

    private String standard(String date) {
        date = date.substring(0,4)+'-'+date.substring(5,7)+'-'+date.substring(8,10)+'T'+ LocalDateTime.now().toLocalTime().toString().substring(0,8);
        date = LocalDateTime.parse(date).toString();
        return date;
    }

    private int startPage(int start) throws IOException {
        int page=1;
        /**
         * 디비에서 저장된 가장 최근 글이 1페이지에 있나 여부 판단. 만약 글 리젠이 많아서 2페이지 중반부터 크롤링 해야되면? 3페이지 첫글이 start보다 작아야 됌.
         * !!다음 페이지의 맨 첫 번째 글이, 가장 최근에 디비에 저장된 글의 번호보다 크면 다음 페이지로 넘어가야됌
         */
        while(true){
            Document doc = Jsoup.connect(urlInf + "?page="+page).get();
            String sNum = doc.select("#main > section.community-body > div.community-body__content > div.question-list-container > ul > li:nth-child(1) > a").attr("href").substring(9);
            int num = Integer.parseInt(sNum);
            if(num<start){
                log.info("{}페이지부터 시작",page-1);
                return page-1;
            }
            page++;
        }
    }
    public int recentPost(){
        return inflearnRepository.findRecent().intValue();
    }


    @Override
    public List<ShowForm> findAllDesc(Source source) {
        return null;
    }
}
