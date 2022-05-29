package Matching.SouP.crawler;

import Matching.SouP.domain.posts.Post;
import Matching.SouP.dto.project.ShowForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static java.util.Map.entry;


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

    static private class StackParser {
        private static final HashMap<String, String> displayNameToTag = new HashMap<>();

        public static void add(String tag, Integer priority, List<String> displayNames) {
            displayNames.forEach(displayName -> {
                displayNameToTag.put(displayName, tag);
            });
        }

        public static List<String> parse(String content) {
            HashSet<String> stackList = new HashSet<>();
            for ( String displayName : displayNameToTag.keySet() ){
                if (content.contains(displayName)) stackList.add(displayNameToTag.get(displayName));
            }
            return new ArrayList<>(stackList);
        }
    }

    static {
        StackParser.add("react", 1, Arrays.asList("react", "리액트"));
        StackParser.add("angular", 1, Arrays.asList("angular", "앵귤러"));
        StackParser.add("vue", 1, Arrays.asList("vue"));
        StackParser.add("svelte", 1, Arrays.asList("svelte", "스벨트"));
        StackParser.add("spring", 1, Arrays.asList("spring", "스프링"));
        StackParser.add("nodejs", 1, Arrays.asList("node.js", "nodejs", "노드"));
        StackParser.add("go", 1, Arrays.asList("go"));
        StackParser.add("django", 1, Arrays.asList("django", "장고"));
        StackParser.add("nestjs", 1, Arrays.asList("nestjs"));
        StackParser.add("express", 1, Arrays.asList("express", "익스프레스"));
        StackParser.add("graphql", 1, Arrays.asList("graphql", "그래프ql"));
        StackParser.add("sql", 1, Arrays.asList("sql"));
        StackParser.add("mongodb", 1, Arrays.asList("mongodb", "몽고"));
        StackParser.add("firebase", 1, Arrays.asList("firebase", "파이어베이스"));
        StackParser.add("react_native", 1, Arrays.asList("react native", " rn", "react 네이티브"));
        StackParser.add("aws", 1, Arrays.asList("aws"));
        StackParser.add("docker", 1, Arrays.asList("docker", "도커"));
        StackParser.add("kubernetes", 1, Arrays.asList("kubernetes", "쿠버네티스"));
        StackParser.add("git", 1, Arrays.asList("git", " 깃"));
        StackParser.add("typescript", 1, Arrays.asList("typescript", "타입스크립트"));
        StackParser.add("javascript", 1, Arrays.asList("javascript", "자바스크립트"));
        StackParser.add("python", 1, Arrays.asList("python"));
        StackParser.add("java", 1, Arrays.asList("java"));
        StackParser.add("kotlin", 1, Arrays.asList("kotlin"));
        StackParser.add("c_cpp", 1, Arrays.asList("c++", "c언어"));
        StackParser.add("csharp", 1, Arrays.asList("c#"));
        StackParser.add("swift", 1, Arrays.asList("swift", "스위프트", "ios"));
        StackParser.add("dart", 1, Arrays.asList("dart", "flutter", "플러터"));
        StackParser.add("study", 1, Arrays.asList("스터디", "모각코"));
        StackParser.add("project", 1, Arrays.asList("프로젝트"));
        StackParser.add("planning", 1, Arrays.asList("기획"));
        StackParser.add("service", 1, Arrays.asList("서비스"));
        StackParser.add("code_interview", 1, Arrays.asList("코테", "코딩테스트"));
        StackParser.add("frontend", 1, Arrays.asList("프론트엔드"));
        StackParser.add("backend", 1, Arrays.asList("백엔드"));
        StackParser.add("mobile", 1, Arrays.asList("모바일", "앱개발", "애플리케이션"));
        StackParser.add("ios", 1, Arrays.asList("ios"));
        StackParser.add("android", 1, Arrays.asList("안드로이드", "android"));
        StackParser.add("ui_ux", 1, Arrays.asList("ui", "ux", "디자인", "디자이너"));
        StackParser.add("ai_ml", 1, Arrays.asList("AI", "딥러닝", "머신러닝", "인공지능"));
        StackParser.add("game", 1, Arrays.asList("게임"));
        StackParser.add("blockchain", 1, Arrays.asList("블록체인", "가상화폐", "암호화폐", "nft", "디파이", "defi"));
    }

    public StringBuilder parseStack(String postName,String content){  //핵심로직!!
        StringBuilder stack = new StringBuilder();
        postName = postName.toLowerCase();
        content = content.toLowerCase();
        List<String> stackList = StackParser.parse(postName + ' ' + content);
        stack.append( String.join(",", stackList) );
        return stack;
    }


}
