package Matching.SouP.service;

import Matching.SouP.common.SoupResponse;
import Matching.SouP.common.error.PostNotFoundException;
import Matching.SouP.common.error.UserNotFoundException;
import Matching.SouP.domain.post.Post;
import Matching.SouP.domain.post.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.user.User;
import Matching.SouP.dto.project.ShowForm;
import Matching.SouP.repository.PostsRepository;
import Matching.SouP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public User getUserWithPostFav(String email) {
        return userRepository.findByEmailFetchPC(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User getUserWithPostList(String email) {
        return userRepository.findByEmailFetchPL(email)
                .orElseThrow(UserNotFoundException::new);
    }


    @Transactional(readOnly = true)
    public JSONObject makeUserDto(String email) {
        Optional<User> optionalUser = getOptionalUser(email);
        if (optionalUser.isPresent())
            return addDetails(optionalUser.get());

        return SoupResponse.fail();
    }


    public JSONObject changeNickName(String email, String nickName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        user.changeName(nickName);
        return SoupResponse.success();
    }


    private Optional<User> getOptionalUser(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }


    private JSONObject addDetails(User user) {
        JSONObject obj = SoupResponse.success();
        obj.put("user_id", user.getId());
        obj.put("email", user.getEmail());
        obj.put("userName", user.getNickName());
        obj.put("origin", user.getOrigin());
        return obj;
    }

    public List<ShowForm> showFav(User user) {
        List<ProjectConnect> projectConnectList = user.getProjectConnectList();
        List<ShowForm> list = new ArrayList<>();

        for (int i = projectConnectList.size() - 1; i >= 0; i--) {
            ProjectConnect pc = projectConnectList.get(i);
            Post post = postsRepository.findById(pc.getPost().getId()).orElseThrow(PostNotFoundException::new);
            ShowForm showForm = new ShowForm(post.getId(), post.getPostName(), post.getContent(), post.getUserName(), post.getDate(), post.getLink(), post.getStack(), post.getViews(), post.getTalk(), post.getSource(), post.getFav());
            if (post.getSource() == Source.SOUP)
                showForm.customContent(post.getProsemirror());
            showForm.setIsfav(true);  // 내 스크랩 글이니 항상 true
            list.add(showForm);
        }
        return list;
    }
}
