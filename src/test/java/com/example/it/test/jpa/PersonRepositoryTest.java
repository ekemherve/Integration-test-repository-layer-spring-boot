package com.example.it.test.jpa;

import com.example.it.test.jpa.dataaccess.entity.PersonEntity;
import com.example.it.test.jpa.dataaccess.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

//************************************************************************************************************
//        WE MUST NOT!!!!!!!!!!!! USE THE ID TO MAKE ASSERTIONS BUT A REAL UNIQUE KEY LIKE EMAIL
//        HERE IT IS JUST FOR THE PURPOSE OF A TUTORIAL
//************************************************************************************************************


@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Person Repository integration test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private static final Long ID = 1L;
    private static final String USERNAME = "iori";
    private static final String USER_EMAIL = "iori@gmail.com";
    private static final String USER_PASSWORD = "iori";

    private static final String SHOULD_BE_EMPTY = " should be empty";
    private static final String SHOULD_BE_EQUAL_TO = " should be equal to ";
    private static final String SHOULD_NOT_BE_NULL = " should not be null";
    private static final String SHOULD_BE_NULL = " should be null";

    //************************************************************************************************************
    //    THE JPA BUILT-IN METHOD (CRUD METHODS + OTHERS) MUST NEVER BE TESTED, HERE ITS JUST FOR TUTORIAL PURPOSE
    //************************************************************************************************************

    @DisplayName(value = "save a valid Entity")
    @Test
    public void givenValidEntitySaveShouldBeOk() {

        log.info("*************************Inside saveValidEntity test method****************************");
        PersonEntity person = personRepository.save(new PersonEntity(USERNAME, USER_PASSWORD, USER_EMAIL));
        assertAll(
                () -> assertNotNull(person, "person object must not be null"),
                () -> assertEquals(person.getId(), ID, person.getId() + SHOULD_BE_EQUAL_TO + ID),
                () -> assertEquals(person.getUsername(), USERNAME, person.getUsername() + SHOULD_BE_EQUAL_TO + USERNAME),
                () -> assertEquals(person.getPassword(), USER_PASSWORD, person.getPassword() + SHOULD_BE_EQUAL_TO + USER_PASSWORD),
                () -> assertEquals(person.getEmail(), USER_EMAIL, person.getEmail() + SHOULD_BE_EQUAL_TO + USER_EMAIL)
        );
    }

    @DisplayName(value = "findAll should return an empty list when database is empty")
    @Test
    public void givenEmptyDatabaseFindAllShouldReturnAnEmptyList() {

        log.info("*************************Inside givenEmptyDatabaseFindAllShouldReturnAnEmptyList test method****************************");
        List<PersonEntity> persons = personRepository.findAll();
        assertEquals(0, persons.size(), "Table " + SHOULD_BE_EMPTY);
    }

    @DisplayName(value = "findAll should return all entities when database is populated")
    @Test
    public void givenNonEmptyDatabaseFindAllShouldReturnAllPersistedEntities() {

        log.info("*************************Inside givenNonEmptyDatabaseFindAllShouldReturnAllPersistedEntities test method****************************");
        personRepository.save(new PersonEntity(USERNAME, USER_PASSWORD, USER_EMAIL));
        personRepository.save(new PersonEntity("herve", "herve", "herve@gmail.com"));
        List<PersonEntity> persons = personRepository.findAll();
        assertEquals(2, persons.size());
    }

    @DisplayName(value = "delete should remove a entity if it exits")
    @Test
    public void givenExistingEntityDeleteShouldBeRemoveFromDatabase() {

        log.info("*************************Inside givenExistingEntityDeleteShouldBeRemoveFromDatabase test method****************************");
        personRepository.save(new PersonEntity(USERNAME, USER_PASSWORD, USER_EMAIL));
        personRepository.save(new PersonEntity("herve", "herve", "herve@gmail.com"));
        List<PersonEntity> persons = personRepository.findAll();
        assertEquals(2, persons.size());
        personRepository.delete(personRepository.getOne(ID));
        assertEquals(1, personRepository.findAll().size());
        personRepository.delete(personRepository.getOne(2*ID));
        assertEquals(0, personRepository.findAll().size());
    }

    //************************************************************************************************************
    //    WE CAN PUT THE BELOW TESTS IN ANOTHER CLASS AND RUN THEM GIVEN A PROFILE (IE : REPOSITORY-TEST)
    //************************************************************************************************************

    @DisplayName(value = "findByUsername should return an entity if it exists")
    @Test
    public void givenExistingEntityWhenFindByUsernameThenShouldReturnNotNull(){
        log.info("*************************Inside givenExistingEntityDeleteShouldBeRemoveFromDatabase test method****************************");
        personRepository.save(new PersonEntity(USERNAME, USER_PASSWORD, USER_EMAIL));
        PersonEntity person = personRepository.findByUsername(USERNAME);
        assertAll(
                () -> assertNotNull(person, "person " + SHOULD_NOT_BE_NULL),
                () -> assertEquals(person.getUsername(), USERNAME),
                () -> assertEquals(person.getEmail(), USER_EMAIL),
                () -> assertEquals(person.getPassword(), USER_PASSWORD)
        );
    }

    @DisplayName(value = "findByUsername should return null for non existing entity")
    @Test
    public void givenNonExistingEntityWhenFindByUsernameThenShouldReturnNull(){
        log.info("*************************Inside givenNonExistingEntityFindByUsernameShouldReturnNull test method****************************");
        PersonEntity person = personRepository.findByUsername(USERNAME);
        assertNull(person, "person " + SHOULD_BE_NULL);
    }

    @DisplayName(value = "findByEmail should return an entity if it exists")
    @Test
    public void givenExistingEntityWhenFindByEmailThenShouldReturnNotNull(){
        log.info("*************************Inside givenExistingEntityWhenFindByEmailThenShouldReturnNotNull test method****************************");
        personRepository.save(new PersonEntity(USERNAME, USER_PASSWORD, USER_EMAIL));
        PersonEntity person = personRepository.findByEmail(USER_EMAIL);
        assertAll(
                () -> assertNotNull(person, "person " + SHOULD_NOT_BE_NULL),
                () -> assertEquals(person.getUsername(), USERNAME),
                () -> assertEquals(person.getEmail(), USER_EMAIL),
                () -> assertEquals(person.getPassword(), USER_PASSWORD)
        );
    }

    @DisplayName(value = "findByEmail should null for non existing entity")
    @Test
    public void givenNonExistingEntityWhenFindByEmailThenShouldReturnNull(){
        log.info("*************************Inside givenNonExistingEntityWhenFindByEmailThenShouldReturnNull test method****************************");
        PersonEntity person = personRepository.findByEmail(USER_EMAIL);
        assertNull(person, "person " + SHOULD_BE_NULL);
    }

    @DisplayName(value = "findByUsernameOrEmail should return two entities if username match one entity and email match another entity")
    @Test
    public void givenTwoExistingEntitiesWithEachParameterMatchingAnEntityWhenFindByUsernameOrEmailThenShouldReturnTwoEntities(){
        log.info("*************************Inside givenTwoExistingEntitiesWithEachParameterMatchingAnEntityWhenFindByUsernameOrEmailThenShouldReturnTwoEntities test method****************************");
        personRepository.save(new PersonEntity(USERNAME, USER_PASSWORD, USER_EMAIL));
        personRepository.save(new PersonEntity("herve", "herve", "herve@gmail.com"));

        List<PersonEntity> persons = personRepository.findByUsernameOrEmail(USERNAME, "herve@gmail.com");
        assertAll(
                () -> assertEquals(2, persons.size())
        );
    }
}

