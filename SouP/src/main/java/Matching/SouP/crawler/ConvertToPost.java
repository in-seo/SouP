package Matching.SouP.crawler;

import Matching.SouP.crawler.CamPick.Campick;
import Matching.SouP.crawler.Hola.Hola;
import Matching.SouP.crawler.inflearn.Inflearn;
import Matching.SouP.crawler.okky.Okky;
import Matching.SouP.domain.posts.Post;
import Matching.SouP.domain.posts.Source;
import Matching.SouP.domain.project.ProjectConnect;
import Matching.SouP.domain.project.ProjectInfo;
import Matching.SouP.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConvertToPost {
    private final PostsRepository postsRepository;

    public void okky(Okky okky){
        String date = okky.getDate();
        date = date.substring(0,10)+"T"+date.substring(11);
        LocalDateTime iptime = LocalDateTime.parse(date);
        okky.makeStandardDate(iptime.toString());
        Post post = new Post(okky.getId(),okky.getPostName(),okky.getContent(),okky.getUserName(),okky.getStack(),okky.getLink(),okky.getDate(),okky.getTalk(), Source.OKKY);
        postsRepository.save(post);
    }
    public void inflearn(Inflearn inflearn){
        String date = inflearn.getDate();
        LocalDateTime now = LocalDateTime.now();
        date = date.substring(0,4)+'-'+date.substring(5,7)+'-'+date.substring(8,10)+'T'+ now.toLocalTime().toString().substring(0,8);
        LocalDateTime iptime = LocalDateTime.parse(date);
        inflearn.makeStandardDate(iptime.toString());
        Post post = new Post(inflearn.getId(), inflearn.getPostName(), inflearn.getContent(), inflearn.getUserName(),inflearn.getStack(),inflearn.getLink(),inflearn.getDate(),inflearn.getTalk(),Source.INFLEARN);
        postsRepository.save(post);
    }
    public void hola(Hola hola){
        String date = hola.getDate();
        LocalDateTime now = LocalDateTime.now();
        date = date+'T'+ now.toLocalTime().toString().substring(0,8);
        LocalDateTime iptime = LocalDateTime.parse(date);
        hola.makeStandardDate(iptime.toString());
        Post post = new Post(hola.getId(),hola.getPostName(),hola.getContent(),hola.getUserName(),hola.getStack(),hola.getLink(),hola.getDate(),hola.getTalk(),Source.HOLA);
        postsRepository.save(post);
    }
    public void campick(Campick campick){
        String date = campick.getDate();
        LocalDateTime now = LocalDateTime.now();
        date = now.toLocalDate().toString().substring(0,4)+'-'+date.substring(0,2)+'-'+date.substring(3,5)+'T'+date.substring(6,8)+':'+date.substring(9,11)+':'+now.toLocalTime().toString().substring(6,8);
        LocalDateTime iptime = LocalDateTime.parse(date);
        campick.makeStandardDate(iptime.toString());
        Post post = new Post(campick.getId(), campick.getPostName(), campick.getContent(), campick.getUserName(), "X",campick.getLink(),campick.getDate(),campick.getTalk(),Source.CAMPICK);
        postsRepository.save(post);
    }
    public void soup(ProjectConnect projectConnect){
        ProjectInfo soup = projectConnect.getProjectInfo();
        Post post = new Post(soup.getId(), soup.getProjectName(),soup.getText(),projectConnect.getUser().getNickName(),soup.getStack(),soup.getLink(),soup.getCreatedDate().toString().substring(0,18),"",Source.SOUP);
        postsRepository.save(post);
    }
}
