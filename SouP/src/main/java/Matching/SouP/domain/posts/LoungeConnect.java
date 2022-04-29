package Matching.SouP.domain.posts;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.user.User;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Getter
@SequenceGenerator(name = "Lounge_SEQ_GEN",sequenceName = "Lounge_SEQ") //초기 값 1, 재할당 50마다
public class LoungeConnect extends BaseTimeEntity {  //다대다 연결 위한 테이블.

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Lounge_SEQ")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="lounge_id")
    private Lounge lounge;


    public static LoungeConnect createConnect(Lounge lounge,User user){  //생성메서드
        LoungeConnect connect = new LoungeConnect();
        connect.setLounge(lounge);
        connect.setUser(user);
        return connect;
    }


    public void setLounge(Lounge lounge){
        this.lounge = lounge;
    }
    public void setUser(User user){
        this.user=user;
    }


}