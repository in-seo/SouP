package Matching.SouP.crawler;

import Matching.SouP.domain.post.Source;
import Matching.SouP.dto.project.ShowForm;

import java.util.*;
import static java.util.Arrays.*;

public abstract class CrawlerService {

    public abstract List<ShowForm> findAllDesc(Source source);

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

        public static void add(String tag, List<String> displayNames) {
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
        StackParser.add("react",  asList("react", "리액트"));
        StackParser.add("nodejs",  asList("node.js", "node", "노드"));
        StackParser.add("python",  asList("python","파이썬"));
        StackParser.add("java",  asList("java","자바"));
        StackParser.add("ui_ux",  asList("ui", "ux", "디자인", "디자이너"));
        StackParser.add("spring",  asList("spring", "스프링"));
        StackParser.add("typescript",  asList("typescript", "타입스크립트"));
        StackParser.add("javascript",  asList("javascript", "자바스크립트"));
        StackParser.add("kotlin",  asList("kotlin","코틀린"));
        StackParser.add("git",  asList("git", " 깃"));
        StackParser.add("project",  asList("프로젝트"));
        StackParser.add("swift",  asList("swift", "스위프트"));
        StackParser.add("dart",  asList("dart", "flutter", "플러터"));
        StackParser.add("planning",  asList("기획"));
        StackParser.add("blockchain", asList("블록체인", "가상화폐", "암호화폐", "nft", "디파이", "defi"));
        StackParser.add("svelte",  asList("svelte", "스벨트"));
        StackParser.add("study",  asList("스터디", "모각코"));
        StackParser.add("go",  asList("go"));
        StackParser.add("angular",  asList("angular", "앵귤러"));
        StackParser.add("vue",  asList("vue","뷰"));
        StackParser.add("django",  asList("django", "장고"));
        StackParser.add("nestjs",  asList("nestjs"));
        StackParser.add("express",  asList("express", "익스프레스"));
        StackParser.add("graphql",  asList("graphql", "그래프ql"));
        StackParser.add("sql",  asList("sql"));
        StackParser.add("mongodb",  asList("mongodb", "몽고"));
        StackParser.add("firebase",  asList("firebase", "파이어베이스"));
        StackParser.add("react_native",  asList("react native", " rn", "네이티브"));
        StackParser.add("aws",  asList("aws"));
        StackParser.add("docker",  asList("docker", "도커"));
        StackParser.add("kubernetes",  asList("kubernetes", "쿠버네티스"));
        StackParser.add("c_cpp",  asList("c++", "c언어"));
        StackParser.add("csharp",  asList("c#"));
        StackParser.add("service",  asList("서비스","플랫폼"));
        StackParser.add("code_interview",  asList("코테", "코딩테스트"));
        StackParser.add("mobile",  asList("모바일", "앱", "애플리케이션"));
        StackParser.add("ios",  asList("ios"));
        StackParser.add("android",  asList("안드로이드", "android"));
        StackParser.add("ai_ml",  asList("AI", "딥러닝", "머신러닝", "인공지능"));
        StackParser.add("game",  asList("게임"));
        StackParser.add("frontend",  asList("프론트엔드","프론트"));
        StackParser.add("backend",  asList("백엔드"));
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
