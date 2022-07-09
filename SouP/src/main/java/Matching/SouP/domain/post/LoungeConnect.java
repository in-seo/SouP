package Matching.SouP.domain.post;

import Matching.SouP.domain.BaseTimeEntity;
import Matching.SouP.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class LoungeConnect extends BaseTimeEntity {  //다대다 연결 위한 테이블.

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
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
        user.getLoungeConnectList().add(this);
        this.user=user;
    }


}