package com.example.it.test.jpa;

import com.example.it.test.jpa.dataaccess.entity.PersonEntity;
import com.example.it.test.jpa.dataaccess.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@Slf4j
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private static final Long ID = 1L;
    private static final String USERNAME = "iori";
    private static final String USER_EMAIL = "iori@gmail.com";
    private static final String USER_PASSWORD = "iori";

    private static final String SHOULD_BE_EMPTY = " should be empty";
    private static final String SHOULD_BE_EQUAL_TO = " should be equal to ";

    @Test
    public void givenValidEntitySaveShouldBeOk() {

        log.info("*************************Inside saveValidEntity test method****************************");
        PersonEntity person = personRepository.save(new PersonEntity("iori", "iori", "iori@gmail.com"));
        assertAll(
                () -> assertNotNull(person, "person object must not be null"),
                () -> assertTrue(person.getId().equals(ID), person.getId() + SHOULD_BE_EQUAL_TO + ID),
                () -> assertTrue(
                        person.getUsername().equals(USERNAME),
                        person.getUsername() + SHOULD_BE_EQUAL_TO + USERNAME),
                () -> assertTrue(
                        person.getPassword().equals(USER_PASSWORD),
                        person.getPassword() + SHOULD_BE_EQUAL_TO + USER_PASSWORD),
                () -> assertTrue(
                        person.getEmail().equals(USER_EMAIL),
                        person.getEmail() + SHOULD_BE_EQUAL_TO + USER_EMAIL)
        );
    }

    @Test
    public void givenEmptyDatabaseFindAllShouldReturnAnEmptyList() {

        log.info("*************************Inside givenEmptyDatabaseFindAllShouldReturnAnEmptyList test method****************************");
        List<PersonEntity> persons = personRepository.findAll();
        assertEquals(0, persons.size(), "Table " + SHOULD_BE_EMPTY);
    }

    @Test
    public void givenNonEmptyDatabaseFindAllShouldReturnAllPersistedEntities() {

        log.info("*************************Inside givenNonEmptyDatabaseFindAllShouldReturnAllPersistedEntities test method****************************");
        personRepository.save(new PersonEntity("iori", "iori", "iori@gmail.com"));
        personRepository.save(new PersonEntity("kyo", "kyo", "kyo@gmail.com"));
        List<PersonEntity> persons = personRepository.findAll();
        assertEquals(2, persons.size());
    }

    @Test
    public void givenExistingEntityDeleteShouldBeRemoveFromDatabase() {

        log.info("*************************Inside givenExistingEntityDeleteShouldBeRemoveFromDatabase test method****************************");
        personRepository.save(new PersonEntity("iori", "iori", "iori@gmail.com"));
        personRepository.save(new PersonEntity("kyo", "kyo", "kyo@gmail.com"));
        List<PersonEntity> persons = personRepository.findAll();
        assertEquals(2, persons.size());
        personRepository.delete(personRepository.getOne(ID));
        assertEquals(1, personRepository.findAll().size());
        personRepository.delete(personRepository.getOne(2*ID));
        assertEquals(0, personRepository.findAll().size());
    }
}

