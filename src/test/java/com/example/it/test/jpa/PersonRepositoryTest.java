package com.example.it.test.jpa;

import com.example.it.test.jpa.dataaccess.entity.PersonEntity;
import com.example.it.test.jpa.dataaccess.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

//************************************************************************************************************
//        WE MUST NOT!!!!!!!!!!!! USE THE AUTO-INCREMENTED ID TO MAKE ASSERTIONS BUT A REAL UNIQUE KEY LIKE EMAIL
//        HERE IT IS JUST FOR THE PURPOSE OF A TUTORIAL
//************************************************************************************************************


@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Person Repository integration test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    private Validator validator;

    private static final Long ID = 1L;
    private static final String USERNAME_IORI = "iori";
    private static final String USER_EMAIL_IORI = "iori@gmail.com";
    private static final String USER_PASSWORD_IORI = "iori";

    private static final String USERNAME_HERVE = "herve";
    private static final String USER_EMAIL_HERVE = "herve@gmail.com";
    private static final String USER_PASSWORD_HERVE = "herve";

    private static final String USER_EMAIL_NOT_EXISTING = "notexitingemail@gmail.com";
    private static final String USERNAME_NOT_EXISTING = "username not existing";
    private static final String SIZE_LESS_THAN_4_CHARS = "noo";
    private static final String SIZE_MORE_THAN_64_CHARS = "toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo long";

    private static final String SHOULD_BE_EMPTY = " should be empty";
    private static final String SHOULD_BE_EQUAL_TO = " should be equal to ";
    private static final String SHOULD_NOT_BE_NULL = " should not be null";
    private static final String SHOULD_BE_NULL = " should be null";

    //************************************************************************************************************
    //    THE JPA BUILT-IN METHOD (CRUD METHODS + OTHERS) MUST NEVER BE TESTED, HERE ITS JUST FOR TUTORIAL PURPOSE
    //************************************************************************************************************

    @BeforeEach
    public void setUp(){

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName(value = "save a valid Entity")
    @Test
    public void givenValidEntitySaveShouldBeOk() {

        log.info("*************************Inside saveValidEntity test method****************************");
        PersonEntity person = personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        assertAll(
                () -> assertNotNull(person, "person object must not be null"),
                () -> assertEquals(person.getId(), ID, person.getId() + SHOULD_BE_EQUAL_TO + ID),
                () -> assertEquals(person.getUsername(), USERNAME_IORI, person.getUsername() + SHOULD_BE_EQUAL_TO + USERNAME_IORI),
                () -> assertEquals(person.getPassword(), USER_PASSWORD_IORI, person.getPassword() + SHOULD_BE_EQUAL_TO + USER_PASSWORD_IORI),
                () -> assertEquals(person.getEmail(), USER_EMAIL_IORI, person.getEmail() + SHOULD_BE_EQUAL_TO + USER_EMAIL_IORI)
        );
    }

    @DisplayName(value = "given with a username with less than 4 characters that entity should throw an exception")
    @Test
    public void givenUsernameWithLessThan4CharactersThatEntityShouldThrowAnException() {

        log.info("*************************Inside givenUsernameWithLessThan4CharactersThatEntityShouldThrowAnException test method****************************");
        Set<ConstraintViolation<PersonEntity>> constraintViolations = validator.validate(new PersonEntity(SIZE_LESS_THAN_4_CHARS, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        System.out.println(constraintViolations);
        assertEquals(1, constraintViolations.size(), "username min size should at least 4");
    }

    @DisplayName(value = "given with a username with less than 4 characters that entity should throw an exception")
    @Test
    public void givenUsernameWithMoreThan64CharactersThatEntityShouldThrowAnException() {

        log.info("*************************Inside givenUsernameWithMoreThan64CharactersThatEntityShouldThrowAnException test method****************************");
        Set<ConstraintViolation<PersonEntity>> constraintViolations = validator.validate(new PersonEntity(SIZE_MORE_THAN_64_CHARS, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        System.out.println(constraintViolations);
        assertEquals(1, constraintViolations.size(), "username max size should be 64");
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
        personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        personRepository.save(new PersonEntity(USERNAME_HERVE, USER_PASSWORD_HERVE, USER_EMAIL_HERVE));
        List<PersonEntity> persons = personRepository.findAll();
        assertEquals(2, persons.size());
    }

    @DisplayName(value = "delete should remove a entity if it exits")
    @Test
    public void givenExistingEntityDeleteShouldBeRemoveFromDatabase() {

        log.info("*************************Inside givenExistingEntityDeleteShouldBeRemoveFromDatabase test method****************************");
        personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        personRepository.save(new PersonEntity(USERNAME_HERVE, USER_PASSWORD_HERVE, USER_EMAIL_HERVE));
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
        personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        PersonEntity person = personRepository.findByUsername(USERNAME_IORI);
        assertAll(
                () -> assertNotNull(person, "person " + SHOULD_NOT_BE_NULL),
                () -> assertEquals(person.getUsername(), USERNAME_IORI),
                () -> assertEquals(person.getEmail(), USER_EMAIL_IORI),
                () -> assertEquals(person.getPassword(), USER_PASSWORD_IORI)
        );
    }

    @DisplayName(value = "findByUsername should return null for non existing entity")
    @Test
    public void givenNonExistingEntityWhenFindByUsernameThenShouldReturnNull(){
        log.info("*************************Inside givenNonExistingEntityFindByUsernameShouldReturnNull test method****************************");
        PersonEntity person = personRepository.findByUsername(USERNAME_IORI);
        assertNull(person, "person " + SHOULD_BE_NULL);
    }

    @DisplayName(value = "findByEmail should return an entity if it exists")
    @Test
    public void givenExistingEntityWhenFindByEmailThenShouldReturnNotNull(){
        log.info("*************************Inside givenExistingEntityWhenFindByEmailThenShouldReturnNotNull test method****************************");
        personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        PersonEntity person = personRepository.findByEmail(USER_EMAIL_IORI);
        assertAll(
                () -> assertNotNull(person, "person " + SHOULD_NOT_BE_NULL),
                () -> assertEquals(person.getUsername(), USERNAME_IORI),
                () -> assertEquals(person.getEmail(), USER_EMAIL_IORI),
                () -> assertEquals(person.getPassword(), USER_PASSWORD_IORI)
        );
    }

    @DisplayName(value = "findByEmail should null for non existing entity")
    @Test
    public void givenNonExistingEntityWhenFindByEmailThenShouldReturnNull(){
        log.info("*************************Inside givenNonExistingEntityWhenFindByEmailThenShouldReturnNull test method****************************");
        PersonEntity person = personRepository.findByEmail(USER_EMAIL_IORI);
        assertNull(person, "person " + SHOULD_BE_NULL);
    }

    @DisplayName(value = "findByUsernameOrEmail should return two entities if username match one entity and email match another entity")
    @Test
    public void givenTwoExistingEntitiesWithEachParameterMatchingAnEntityWhenFindByUsernameOrEmailThenShouldReturnTwoEntities(){
        log.info("*************************Inside givenTwoExistingEntitiesWithEachParameterMatchingAnEntityWhenFindByUsernameOrEmailThenShouldReturnTwoEntities test method****************************");
        personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        personRepository.save(new PersonEntity(USERNAME_HERVE, USER_PASSWORD_HERVE, USER_EMAIL_HERVE));

        List<PersonEntity> persons = personRepository.findByUsernameOrEmail(USERNAME_IORI, USER_EMAIL_HERVE);
        assertAll(
                () -> assertEquals(2, persons.size())
        );
    }

    @DisplayName(value = "findByUsernameOrEmail should return one entity if username or email match one entity")
    @Test
    public void givenOneExistingEntityWithOneParameterMatchingAnEntityWhenFindByUsernameOrEmailThenShouldReturnOneEntity(){
        log.info("************************* inside givenOneExistingEntityWithOneParameterMatchingAnEntityWhenFindByUsernameOrEmailThenShouldReturnOneEntity test method****************************");
        personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        personRepository.save(new PersonEntity(USERNAME_HERVE, USER_PASSWORD_HERVE, USER_EMAIL_HERVE));

        List<PersonEntity> persons = personRepository.findByUsernameOrEmail(USERNAME_IORI, USER_EMAIL_NOT_EXISTING);
        assertAll(
                () -> assertEquals(1, persons.size())
        );
    }

    @DisplayName(value = "findByUsernameOrEmail should empty list if username nor email match any entity")
    @Test
    public void giveNonExistingEntityWithParametersNotMatchingAnEntityWhenFindByUsernameOrEmailThenShouldEmptyList(){
        log.info("*************************Inside giveNonExistingEntityWithParametersNotMatchingAnEntityWhenFindByUsernameOrEmailThenShouldEmptyList test method****************************");
        personRepository.save(new PersonEntity(USERNAME_IORI, USER_PASSWORD_IORI, USER_EMAIL_IORI));
        personRepository.save(new PersonEntity(USERNAME_HERVE, USER_PASSWORD_HERVE, USER_EMAIL_HERVE));

        List<PersonEntity> persons = personRepository.findByUsernameOrEmail(USERNAME_NOT_EXISTING, USER_EMAIL_NOT_EXISTING);
        assertAll(
                () -> assertEquals(0, persons.size())
        );
    }
}

