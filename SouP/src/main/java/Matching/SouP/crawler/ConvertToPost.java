package Matching.SouP.crawler;

import Matching.SouP.crawler.CamPick.Campick;
import Matching.SouP.crawler.Hola.Hola;
import Matching.SouP.crawler.inflearn.Inflearn;
import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvertToPost {
    private final PostsRepository postsRepository;

    public void okky(Okky okky){
        Post post = new Post(okky.getId(),okky.getPostName(),okky.getContent(),okky.getUserName(),okky.getDate(),okky.getLink(),okky.getStack(),okky.getViews(),okky.getTalk(), Source.OKKY);
        postsRepository.save(post);
    }
    public void inflearn(Inflearn inflearn){
        Post post = new Post(inflearn.getId(), inflearn.getPostName(), inflearn.getContent(), inflearn.getUserName(),inflearn.getDate(),inflearn.getLink(),inflearn.getStack(),inflearn.getViews(),inflearn.getTalk(),Source.INFLEARN);
        postsRepository.save(post);
    }
    public void hola(Hola hola){
        Post post = new Post(hola.getId(),hola.getPostName(),hola.getContent(),hola.getUserName(),hola.getDate(),hola.getLink(),hola.getStack(),hola.getViews(),hola.getTalk(),Source.HOLA);
        postsRepository.save(post);
    }
    public void campick(Campick campick){
        Post post = new Post(campick.getId(), campick.getPostName(), campick.getContent(), campick.getUserName(),campick.getDate(), campick.getLink(), campick.getStack(), campick.getViews(),campick.getTalk(),Source.CAMPICK);
        postsRepository.save(post);
    }

    public void soup(Post post){
        postsRepository.save(post);
    }
}
