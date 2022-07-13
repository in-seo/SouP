package Matching.SouP.service;

import Matching.SouP.domain.post.Post;
import Matching.SouP.domain.post.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final PostsRepository postsRepository;

    public List<ShowForm> showFav(User user){
        List<ProjectConnect> projectConnectList = user.getProjectConnectList();
        List<ShowForm> list = new ArrayList<>();
        for (int i = projectConnectList.size()-1; i >= 0; i--) {
            ProjectConnect pc = projectConnectList.get(i);
            Post post = postsRepository.findById(pc.getPost().getId()).get();
            ShowForm showForm = new ShowForm(post.getId(),post.getPostName(),post.getContent(),post.getUserName(),post.getDate(),post.getLink(),post.getStack(),post.getViews(),post.getTalk(),post.getSource(),post.getFav());
            if(post.getSource()== Source.SOUP)
                showForm.setContent(post.getProsemirror());
            showForm.setIsfav(true);  // 내 스크랩 글이니 항상 true
            list.add(showForm);
        }
        return list;
    }
}
