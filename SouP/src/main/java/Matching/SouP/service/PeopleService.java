package Matching.SouP.service;


import Matching.SouP.domain.People;
import Matching.SouP.repository.PeopleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Transactional
    public void join(String username,int age) {
        People people = new People();
        people.setUsername(username);
        people.setAge(age);
        peopleRepository.save(people);
        System.out.println("save complete");
    }

    public People findById(long id){
        return peopleRepository.findById(id);
    }
}
