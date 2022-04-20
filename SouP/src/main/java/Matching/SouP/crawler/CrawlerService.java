package Matching.SouP.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

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
    public StringBuilder parseStack(String content, StringBuilder stack){  //핵심로직!!
        List<String> stackList = new ArrayList<>(
                Arrays.asList("파이썬","python","자바","java",
                        "노드", "node", "리액트","react",
                        "자바스크립트","js", "스프링","spring",
                        "코틀린","kotlin","프론트","front",
                        "플러터","flutter","백엔드","backend",
                        "swift","aws","c++","html"));
        int cnt=0;  // cnt<=3
        content = content.toLowerCase();
        for (int i = 0; i < stackList.size(); i++) {
            if(content.contains(stackList.get(i))){
                cnt++;
                if(i%2==0 && i<22)
                    i++; //만약 한글인경우 영어로 치환하기위함.
                stack.append(stackList.get(i)).append(" ");
            }
            if(cnt>=3)
                return stack;
        }
        return stack;
    }
}
