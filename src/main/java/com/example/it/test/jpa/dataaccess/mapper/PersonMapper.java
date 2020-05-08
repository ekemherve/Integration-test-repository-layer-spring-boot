package com.example.it.test.jpa.dataaccess.mapper;

import com.example.it.test.jpa.dataaccess.entity.PersonEntity;
import com.example.it.test.jpa.model.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person mapToBO(PersonEntity person);
    PersonEntity mapToEO(Person person);
    List<Person> mapToBOS(List<PersonEntity> persons);
    List<PersonEntity> mapToEOS(List<Person> persons);

}
