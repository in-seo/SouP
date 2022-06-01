package Matching.SouP.crawler;

import java.util.*;
import static java.util.Arrays.*;


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
        StackParser.add("react", 1, asList("react", "리액트"));
        StackParser.add("nodejs", 1, asList("node.js", "node", "노드"));
        StackParser.add("python", 1, asList("python","파이썬"));
        StackParser.add("java", 1, asList("java","자바"));
        StackParser.add("ui_ux", 1, asList("ui", "ux", "디자인", "디자이너"));
        StackParser.add("spring", 1, asList("spring", "스프링"));
        StackParser.add("typescript", 1, asList("typescript", "타입스크립트"));
        StackParser.add("javascript", 1, asList("javascript", "자바스크립트"));
        StackParser.add("kotlin", 1, asList("kotlin","코틀린"));
        StackParser.add("git", 1, asList("git", " 깃"));
        StackParser.add("project", 1, asList("프로젝트"));
        StackParser.add("swift", 1, asList("swift", "스위프트"));
        StackParser.add("dart", 1, asList("dart", "flutter", "플러터"));
        StackParser.add("planning", 1, asList("기획"));
        StackParser.add("blockchain", 1, asList("블록체인", "가상화폐", "암호화폐", "nft", "디파이", "defi"));
        StackParser.add("svelte", 1, asList("svelte", "스벨트"));
        StackParser.add("study", 1, asList("스터디", "모각코"));
        StackParser.add("go", 1, asList("go"));
        StackParser.add("angular", 1, asList("angular", "앵귤러"));
        StackParser.add("vue", 1, asList("vue","뷰"));
        StackParser.add("django", 1, asList("django", "장고"));
        StackParser.add("nestjs", 1, asList("nestjs"));
        StackParser.add("express", 1, asList("express", "익스프레스"));
        StackParser.add("graphql", 1, asList("graphql", "그래프ql"));
        StackParser.add("sql", 1, asList("sql"));
        StackParser.add("mongodb", 1, asList("mongodb", "몽고"));
        StackParser.add("firebase", 1, asList("firebase", "파이어베이스"));
        StackParser.add("react_native", 1, asList("react native", " rn", "네이티브"));
        StackParser.add("aws", 1, asList("aws"));
        StackParser.add("docker", 1, asList("docker", "도커"));
        StackParser.add("kubernetes", 1, asList("kubernetes", "쿠버네티스"));
        StackParser.add("c_cpp", 1, asList("c++", "c언어"));
        StackParser.add("csharp", 1, asList("c#"));
        StackParser.add("service", 1, asList("서비스","플랫폼"));
        StackParser.add("code_interview", 1, asList("코테", "코딩테스트"));
        StackParser.add("mobile", 1, asList("모바일", "앱", "애플리케이션"));
        StackParser.add("ios", 1, asList("ios"));
        StackParser.add("android", 1, asList("안드로이드", "android"));
        StackParser.add("ai_ml", 1, asList("AI", "딥러닝", "머신러닝", "인공지능"));
        StackParser.add("game", 1, asList("게임"));
        StackParser.add("frontend", 1, asList("프론트엔드","프론트"));
        StackParser.add("backend", 1, asList("백엔드"));
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
