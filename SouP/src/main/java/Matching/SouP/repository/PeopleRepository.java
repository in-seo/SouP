package Matching.SouP.repository;

import Matching.SouP.domain.People;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PeopleRepository {  //이렇게 구현해도 되는데 jpaRepository 인터페이스 써보는거 연습

    @PersistenceContext
    EntityManager em;

    public Long save(People people) {
        em.persist(people);
        return people.getId();
    }
    public People findById(Long id) {
        return em.find(People.class, id);
    }

    public List<People> findByName(String name){
        return em.createQuery("select p from People p where p.username=:name",People.class)
                .setParameter("name",name)
                .getResultList();
    }

    public List<People> findAll(){
        return em.createQuery("select p from People p",People.class)
                .getResultList();
    }
}
