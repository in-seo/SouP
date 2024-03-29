package Matching.SouP.service;

import Matching.SouP.common.error.PostNotFoundException;
import Matching.SouP.domain.post.Post;
import Matching.SouP.domain.post.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.DetailForm;
import Matching.SouP.dto.project.MainAPIForm;
import Matching.SouP.dto.project.ProjectsAPIForm;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.PostsRepository;
import Matching.SouP.repository.ProjectConnectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final ProjectConnectRepository projectConnectRepository;
    private final PostsRepository postsRepository;

    public PageImpl<ShowForm> getListForUser(User user, List<String> stacks, Pageable pageable){
        Page<Post> projectList = filter(stacks, pageable);
        List<ShowForm> showList = new ArrayList<>();
        for (Post post : projectList.getContent()) {
            ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
            if(post.getSource()==Source.SOUP)
                showForm.customContent(post.getProsemirror());

            if(user.getProjectConnectList().size()!=0) {
                List<ProjectConnect> favs = projectConnectRepository.findByPostId(post.getId());
                if(favs.stream().anyMatch(fav -> fav.getPost().getId().equals(post.getId())))
                    showForm.setIsfav(true);
            }
            showList.add(showForm);
        }
        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
    }

    public PageImpl<ShowForm> getListForGuest(List<String> stacks, Pageable pageable){
        Page<Post> projectList;
        if(stacks.size()==0)
            projectList = postsRepository.findAllDesc(pageable);
        else
            projectList = filter(stacks, pageable);  //파라미터 입력받았을 경우
        List<ShowForm> showList = new ArrayList<>();

        for (Post post : projectList.getContent()) {
            ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
            if(post.getSource()==Source.SOUP)
                showForm.customContent(post.getProsemirror());
            showList.add(showForm);
        }
        return new PageImpl<>(showList, pageable, projectList.getTotalElements());
    }

    @Transactional
    public DetailForm showProject(Long id, User user) throws ParseException {
        Post post = postsRepository.findById(id).orElseThrow(PostNotFoundException::new);

        if(post.getSource()==Source.INFLEARN || post.getSource()==Source.SOUP)
            post.addViews();

        DetailForm form = getDetailForm(post);
        for (ProjectConnect connect : user.getProjectConnectList()) {
            if(connect.getPost().getId().equals(id))
                form.setIsfav(true);
        }

        return form;
    }

    @Transactional
    public DetailForm showProject(Long id) throws ParseException {
        Post post = postsRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if(post.getSource()==Source.INFLEARN || post.getSource()==Source.SOUP)
            post.addViews();
        return getDetailForm(post);
    }

    public List<MainAPIForm> findRecentPost(){
        List<Post> projectList = postsRepository.findTop3ByOrderByDateDesc();
        List<MainAPIForm> recentPost = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Post post = projectList.get(i);
            MainAPIForm mainAPIForm = new MainAPIForm(post.getPostName(), post.getContent(), post.getId());
            if(post.getSource()==Source.SOUP)
                mainAPIForm.setContent(post.getProsemirror());
            recentPost.add(mainAPIForm);
        }
        return recentPost;
    }


    public List<MainAPIForm> findHotPost(long n){
        List<Post> projectList = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(3).toString().substring(0,18));
        List<MainAPIForm> hotList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Post post = projectList.get(i);
            MainAPIForm form = new MainAPIForm(post.getPostName(),post.getContent(),post.getId());
            hotList.add(form);
        }
        return hotList;
    }

    public List<ProjectsAPIForm> findHotPost(){
        List<Post> projectList = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(3).toString().substring(0,18));
        List<ProjectsAPIForm> hotList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Post post = projectList.get(i);
            ProjectsAPIForm form = new ProjectsAPIForm(post.getPostName(),post.getUserName(),post.getId());
            hotList.add(form);
        }
        return hotList;
    }

    public List<ProjectsAPIForm> findRandomPost(long n){
        List<Post> projectList = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(3).toString().substring(0,18));
        List<ProjectsAPIForm> randomList = new ArrayList<>();
        if(projectList.size()<3){
            log.error("글이 3개 미만이기에 추천 불가능.");
        }
        else{
            Random ran = new Random();
            int[] arr = new int[3];
            for (int i = 0; i < n; i++) {
                int num = ran.nextInt(projectList.size());
                arr[i]=num;
                boolean duplicate = false;
                for (int j = 0; j < i; j++) {
                    if(arr[i]==arr[j]){
                        i--;
                        duplicate = true;
                    }
                }
                if(!duplicate){
                    Post post = projectList.get(num);
                    ProjectsAPIForm form = new ProjectsAPIForm(post.getPostName(),post.getUserName(),post.getId());
                    randomList.add(form);
                }
            }
        }
        return randomList;
    }

    public List<ProjectsAPIForm> findSuggestPost(Long id){
        Post Post = postsRepository.findById(id).orElseThrow();
        List<Post> projectList;
        List<ProjectsAPIForm> suggestList = new ArrayList<>();
        String[] stacks = Post.getStack().split(",|\\s+");
        if (stacks.length ==1)
            projectList = postsRepository.find1RecommendNDaysBefore(LocalDateTime.now().minusDays(7).toString().substring(0, 18), stacks[0]);
        else if (stacks.length == 2)
            projectList = postsRepository.find2RecommendNDaysBefore(LocalDateTime.now().minusDays(7).toString().substring(0, 18), stacks[0], stacks[1]);
        else if (stacks.length == 3)
            projectList = postsRepository.find3RecommendNDaysBefore(LocalDateTime.now().minusDays(7).toString().substring(0, 18), stacks[0], stacks[1], stacks[2]);
        else
            projectList = postsRepository.findAllNDaysBefore(LocalDateTime.now().minusDays(7).toString().substring(0, 18));

        for (int i = 1; i <= projectList.size(); i++) {
            if(i>=7)
                break;
            Post post = projectList.get(i);
            ProjectsAPIForm form = new ProjectsAPIForm(post.getPostName(), post.getUserName(), post.getId());
            suggestList.add(form);
        }
        return suggestList;
    }


    public List<ShowForm> findAllDesc() {
        List<Post> soupList = postsRepository.findTop8BySourceOrderByDateDesc(Source.SOUP);
        return soupList.stream()
                .map(soup -> new ShowForm(soup.getId(), soup.getPostName(), soup.getProsemirror(), soup.getUserName(), soup.getDate(), soup.getLink(), soup.getStack(), soup.getViews(), soup.getTalk(), Source.SOUP, 0))
                .collect(Collectors.toList());
    }

    private Page<Post> filter(List<String> stacks, Pageable pageable) {
        if (stacks.size() == 1)
            return postsRepository.findBy1StacksDesc(pageable, stacks.get(0));
        else if (stacks.size() == 2)
            return postsRepository.findBy2StacksDesc(pageable, stacks.get(0), stacks.get(1));
        else if (stacks.size() == 3)
            return postsRepository.findBy3StacksDesc(pageable, stacks.get(0), stacks.get(1), stacks.get(2));
        else
            return postsRepository.findAllDesc(pageable);
    }

    private DetailForm getDetailForm(Post post) throws ParseException {
        DetailForm form;
        if(post.getSource()== Source.SOUP){
            JSONParser parser = new JSONParser();
            JSONObject parse = (JSONObject) parser.parse(post.getContent());
            form = new DetailForm(post.getId(), post.getPostName(), parse.toString(), post.getUserName(), post.getDate(), post.getLink(), post.getStack(), post.getViews(), post.getTalk(), post.getSource(), post.getFav(),post.getUser().getId());
            form.setType("prosemirror");
        }
        else{
            form = new DetailForm(post.getId(), post.getPostName(), post.getContent(), post.getUserName(), post.getDate(), post.getLink(), post.getStack(), post.getViews(), post.getTalk(), post.getSource(), post.getFav(),null);
            form.setType("string");
        }
        return form;
    }
}
