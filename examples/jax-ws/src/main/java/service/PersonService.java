package service;

import domain.Person;

import javax.jws.WebService;

@WebService
public class PersonService {

    public Person getPerson() {
        return new Person("Kevin", 26);
    }
}
