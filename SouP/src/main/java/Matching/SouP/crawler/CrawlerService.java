package Matching.SouP.crawler;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.dto.project.ShowForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

public class CrawlerService {
    public String parseTalk(String content, String talk){
        StringTokenizer st = new StringTokenizer(content); //공백으로 나눔
        while(st.hasMoreTokens()){
            String str=st.nextToken();
            if(str.contains("https")){
                if(str.contains("open")){
                    talk = str;
                    return talk;  //오픈 카톡주소면 바로 반환
                }
                else
                    talk = str;
            }
        }
        return talk;
    }
    public StringBuilder parseStack(String postName,String content, StringBuilder stack){  //핵심로직!!
        List<String> stackList = new ArrayList<>(
                Arrays.asList("파이썬","python","자바","java",
                        "노드", "node", "리액트","react",
                        "자바스크립트","js", "스프링","spring",
                        "코틀린","kotlin","프론트","front",
                        "백엔드","backend", "앱","app","웹", "web",
                        "뷰","vue", "스위프트","swift","플러터","flutter",
                        "코딩","게임","서비스","기획","블록체인",
                        "nft","코테","백준",
                        "ai","docker","ios","android",
                        "aws","c++","html","css",
                        "ux","ui","스터디","프로젝트"));
        int cnt=0;  // cnt<=3
        postName = postName.toLowerCase();
        content = content.toLowerCase();
        for (int i = 0; i < stackList.size(); i++) {
            String keyword = stackList.get(i);
            if(content.contains(keyword) || postName.contains(keyword)){
                cnt++;
                if(i%2==0 && i<28)
                    i++; //만약 한글인경우 영어로 치환하기위함.
                stack.append(keyword).append(" ");
            }
            if(cnt>=3)
                return stack;
        }
        return stack;
    }


}
