package Matching.SouP.crawler.okky;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OkkyService {
    private static String urlOkky ="https://okky.kr/articles/gathering";  //https://okky.kr/articles/gathering?offset=24단위로
    private final OkkyRepository okkyRepository;


    public void getOkkyPostData() throws IOException, InterruptedException {
        System.out.println("오키 크롤링 시작.");
        init();
        int start = recentPost();
        int Page = startPage(start);
        while(Page>=0){
            Document doc = Jsoup.connect(urlOkky + "?offset=" + 24 * Page).get();  // 페이지 선택.
            for (int i = 24; i >0; i--) {  //오래된 글부터 크롤링  그럼 반드시 최신글은 DB에서 가장 밑에꺼임.
                Elements element = doc.select("#list-article > div.panel.panel-default.gathering-panel > ul > li:nth-child(" + i + ")");
                Elements title = element.select("div.list-title-wrapper.clearfix");
                Elements information = element.select("div.list-group-item-author.clearfix");
                String num= title.get(0).select("span").text().substring(1);
                if(Integer.parseInt(num)<start)
                    continue;   //이미 불러온 글이면 저장 X
                String link = "https://okky.kr/article/"+num;
                Document realPost = Jsoup.connect(link).get();
                String content = realPost.select("#content-body > article").text();
                String talk = realPost.select("#content-body > article").select("a").attr("href");
                String views = realPost.select("#article > div.panel.panel-default.clearfix.fa- > div.panel-heading.clearfix > div.content-identity.pull-right > div:nth-child(2)").text();
                int size = realPost.select("#content-body > div").select("a").size();
                StringBuilder stack= new StringBuilder();
                for (int j = 3; j <= size+1; j++) {
                    stack.append(realPost.select("#content-body > div > a:nth-child(" + j + ")").text()).append(" ");
                }
                Thread.sleep(1000);
                String postName = title.select("h5 a").text();
                if(postName.contains("마감") || postName.contains("모집완료")) continue; //제목에 [마감]이 들어가있으면 마감인듯
                String userName = information.select("div > div > a").attr("title");
                String date = information.select("div > div > div.date-created > span").text();

                Okky okky = new Okky(num,postName,content,userName,date,stack.toString(),views,link,talk);
                okkyRepository.save(okky);
                System.out.println(okky);
            }
            Page--;
        }
    }

    private void init() { //임시 기준점 -> 이 번호 이후의 글을 긁어온다.
        Okky temp = new Okky("1194000","임시 기준점","","","","","","","");
        okkyRepository.save(temp);
    }


    private int startPage(int start) throws IOException {
        int page=1;  //page가 1이면 okky에선 2페이지이다..
        /**
         * 디비에서 저장된 가장 최근 글이 1페이지에 있나 여부 판단. 만약 글 리젠이 많아서 2페이지 중반부터 크롤링 해야되면? 3페이지 첫글이 start보다 작아야 됌.
         * !!다음 페이지의 맨 첫 번째 글이, 가장 최근에 디비에 저장된 글의 번호보다 크면 다음 페이지로 넘어가야됌
         */
        System.out.println("start =  "+start);
        while(true){
            Document doc = Jsoup.connect(urlOkky + "?offset=" + 24 * page).get();
            String sNum = doc.select("#list-article > div.panel.panel-default.gathering-panel > ul > li:nth-child(" + 1 + ") > div.list-title-wrapper.clearfix").get(0).select("span").text().substring(1);
            int num = Integer.parseInt(sNum);
            System.out.println(page+1+"페이지의 첫 글 번호 : "+num+"  ,    디비에 가장 최근 저장 된 글 번호 : "+start);
            if(num<start){
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

}
