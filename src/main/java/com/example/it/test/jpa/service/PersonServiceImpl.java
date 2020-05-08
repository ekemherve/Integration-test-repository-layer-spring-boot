package com.example.it.test.jpa.service;

import com.example.it.test.jpa.dataaccess.entity.PersonEntity;
import com.example.it.test.jpa.dataaccess.mapper.PersonMapper;
import com.example.it.test.jpa.dataaccess.repository.PersonRepository;
import com.example.it.test.jpa.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public List<Person> findAll() {
        return personMapper.mapToBOS(personRepository.findAll());
    }

    @Override
    public Person save(Person person) throws Exception {

        List<PersonEntity> persons = personRepository.findByUsernameOrEmail(person.getUsername(), person.getEmail());
        if(!persons.isEmpty()){
            throw new Exception("USERNAME OR EMAIL ALREADY EXITS");
        }

        PersonEntity personToSave = personMapper.mapToEO(person);
        personToSave = personRepository.save(personToSave);
        return personMapper.mapToBO(personToSave);
    }


    @Override
    public void delete(Long id) {

        Optional<PersonEntity> optional = personRepository.findById(id);
        optional.ifPresent(personRepository::delete);
    }
}
