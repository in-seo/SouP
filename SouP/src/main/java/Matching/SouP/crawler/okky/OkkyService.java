package Matching.SouP.crawler.okky;

import Matching.SouP.crawler.ConvertToPost;
import Matching.SouP.crawler.inflearn.Inflearn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OkkyService {
    private static String urlOkky ="https://okky.kr/articles/gathering";  //https://okky.kr/articles/gathering?offset=24단위로
    private final OkkyRepository okkyRepository;
    private final ConvertToPost convertToPost;

    public void getOkkyPostData() throws IOException{
        init();
        int start = recentPost();
        log.info("OKKY 크롤링 시작. {}번부터",start);
        int Page = startPage(start);
        while(Page>=0){
            Document doc = Jsoup.connect(urlOkky + "?offset=" + 24 * Page).get();  // 페이지 선택.
            for (int i = 24; i >0; i--) {  //오래된 글부터 크롤링  그럼 반드시 최신글은 DB에서 가장 밑에꺼임.
                Elements element = doc.select("#list-article > div.panel.panel-default.gathering-panel > ul > li:nth-child(" + i + ")");
                Elements title = element.select("div.list-title-wrapper.clearfix");
                Elements information = element.select("div.list-group-item-author.clearfix");
                String postName = title.select("h5 a").text();
                    if(postName.contains("마감") || postName.contains("모집완료")) continue; //제목에 [마감]이 들어가있으면 마감
                String num= title.get(0).select("span").text().substring(1);
                if(Integer.parseInt(num)<start)
                    continue;   //이미 불러온 글이면 저장 X
                String link = "https://okky.kr/article/"+num;
                Document realPost = Jsoup.connect(link).get();
                String content = realPost.select("#content-body > article").text();
                if(content.length()>200) {
                    content = content.substring(0, 199);
                }
                String talk = realPost.select("#content-body > article").select("a").attr("href");
                String views = realPost.select("#article > div.panel.panel-default.clearfix.fa- > div.panel-heading.clearfix > div.content-identity.pull-right > div:nth-child(2)").text();
                int size = realPost.select("#content-body > div").select("a").size();
                StringBuilder stack= new StringBuilder();
                for (int j = 3; j <= size+1; j++) {
                    stack.append(realPost.select("#content-body > div > a:nth-child(" + j + ")").text()).append(" ");
                }
                String userName = information.select("div > div > a").attr("title");
                String date = information.select("div > div > div.date-created > span").text();
                date = standard(date);
                Okky okky = new Okky(num,postName,content,userName,date,link,stack.toString(),views,talk);
                okkyRepository.save(okky);
                convertToPost.okky(okky);
            }
            Page--;
        }
    }

    private String standard(String date) {
        date = date.substring(0,10)+"T"+ date.substring(11);
        date = LocalDateTime.parse(date).toString();
        return date;
    }

    private void init() { //임시 기준점 -> 이 번호 이후의 글을 긁어온다.
        Okky temp = new Okky("1207000","임시 기준점","","","","","","","");
        okkyRepository.save(temp);
    }


    private int startPage(int start) throws IOException {
        int page=1;  //page가 1이면 okky에선 2페이지이다..
        /**
         * 디비에서 저장된 가장 최근 글이 1페이지에 있나 여부 판단. 만약 글 리젠이 많아서 2페이지 중반부터 크롤링 해야되면? 3페이지 첫글이 start보다 작아야 됌.
         * !!다음 페이지의 맨 첫 번째 글이, 가장 최근에 디비에 저장된 글의 번호보다 크면 다음 페이지로 넘어가야됌
         */
        while(true){
            Document doc = Jsoup.connect(urlOkky + "?offset=" + 24 * page).get();
            String sNum = doc.select("#list-article > div.panel.panel-default.gathering-panel > ul > li:nth-child(" + 1 + ") > div.list-title-wrapper.clearfix").get(0).select("span").text().substring(1);//각 페이지 첫 글
            int num = Integer.parseInt(sNum);
            if(num<start){
                log.info("{}페이지부터 시작",page-1);
                return page-1;
            }
            page++;
        }
    }

    public int recentPost(){
        Long recent = okkyRepository.findRecent();
        Optional<Okky> recentPost = okkyRepository.findById(recent);
        return recentPost.get().getNum();
    }

    public List<Okky> findAll(){
        return okkyRepository.findAll();
    }
    public List<Okky> findAllDesc() { return okkyRepository.findAllDesc();}

}
