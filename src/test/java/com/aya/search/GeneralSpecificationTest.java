package com.aya.search;

import com.aya.search.entity.Community;
import com.aya.search.entity.Student;
import com.aya.search.model.DataManipulationModel;
import com.aya.search.model.Operation;
import com.aya.search.repository.CommunityRepository;
import com.aya.search.repository.StudentRepository;
import com.aya.search.specification.GeneralSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;

import static com.aya.search.model.FilterCriteria.Condition.condition;
import static com.aya.search.model.FilterGroup.Filter.and;
import static com.aya.search.model.FilterGroup.Filter.not;
import static com.aya.search.model.FilterGroup.Filter.or;
import static com.aya.search.model.SortDataModel.Sort.asc;
import static com.aya.search.model.SortDataModel.Sort.desc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class GeneralSpecificationTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CommunityRepository communityRepository;

    @Test
    @DisplayName("Equal Long Value")
    public void test1() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("id", Operation.EQUAL, 1L));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 1);
    }

    @Test
    @DisplayName("Equal Long Value Inner Variable")
    public void test2() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("community.id", Operation.EQUAL, 5L));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 4);
    }

    @Test
    @DisplayName("Is Null")
    public void test3() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("community", Operation.IS_NULL));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 3);
    }

    @Test
    @DisplayName("In Long Values")
    public void test4() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("students.id", Operation.IN, 1, 2));
        GeneralSpecification<Community> communityGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Community> page = communityRepository.findAll(communityGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    @DisplayName("In Without Values")
    public void test5() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("id", Operation.IN));
        GeneralSpecification<Community> communityGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Community> page = communityRepository.findAll(communityGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 0);
    }

    @Test
    @DisplayName("Like")
    public void test6() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("firstName", Operation.LIKE, "%Aya%"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 1);
    }

    @Test
    @DisplayName("In String Value")
    public void test7() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpaLetter", Operation.IN, "A", "A-", "A+"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 13);
    }

    @Test
    @DisplayName("Not In String Value")
    public void test8() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpaLetter", Operation.NOT_IN, "A", "A-", "A+"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 7);
    }

    @Test
    @DisplayName("Is True")
    public void test9() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("isFullTime", Operation.IS_TRUE));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 18);
    }

    @Test
    @DisplayName("Is False")
    public void test10() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("isFullTime", Operation.IS_FALSE));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    @DisplayName("Equal Double")
    public void test13() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.EQUAL, 3.2));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    @DisplayName("Greater Than")
    public void test11() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.GREATER_THAN, 3.2));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 15);
    }

    @Test
    @DisplayName("Greater Than Or Equal")
    public void test12() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.GREATER_THAN_EQUAL, 3.2));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 17);
    }

    @Test
    @DisplayName("Less Than")
    public void test14() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.LESS_THAN, 3.2));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 3);
    }

    @Test
    @DisplayName("Less Than Or Equal")
    public void test15() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.LESS_THAN_EQUAL, 3.2));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 5);
    }

    @Test
    @DisplayName("Equal LocalDate")
    public void test16() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("dateOfBirth", Operation.EQUAL, LocalDate.of(1997, 5, 7)));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 1);
    }

    @Test
    @DisplayName("Greater Than LocalDate")
    public void test17() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("dateOfBirth", Operation.GREATER_THAN, LocalDate.of(1997, 5, 7)));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 19);
    }

    @Test
    @DisplayName("Is Empty String")
    public void test18() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("address", Operation.IS_EMPTY_STRING));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 7);
    }

    @Test
    @DisplayName("Is Not Empty String")
    public void test19() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("address", Operation.IS_NOT_EMPTY_STRING));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 13);
    }

    @Test
    @DisplayName("Between Double")
    public void test20() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.BETWEEN, 3.2, 3.9));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 17);
    }

    @Test
    @DisplayName("Between Date")
    public void test21() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("enrollmentDate", Operation.BETWEEN, LocalDate.of(2022, 2, 15),
                LocalDate.of(2022, 7, 1)));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 5);
    }

    @Test
    @DisplayName("Like String Inner Variable")
    public void test22() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("students.lastName", Operation.LIKE, "Ri%"));
        GeneralSpecification<Community> communityGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Community> page = communityRepository.findAll(communityGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    @DisplayName("Like String Inner Variable")
    public void test23() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("students.lastName", Operation.LIKE, "Ri%"));
        GeneralSpecification<Community> communityGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Community> page = communityRepository.findAll(communityGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    @DisplayName("Invalid Field Value 1")
    public void test24() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("id", Operation.EQUAL, "a"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "The value a is not compatible with type class java.lang.Long");
    }

    @Test
    @DisplayName("Invalid Field Value 2")
    public void test25() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("enrollmentDate", Operation.EQUAL, "05-05-2022"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "The value 05-05-2022 is not compatible with type class java.time.LocalDate");
    }

    @Test
    @DisplayName("Invalid Field Value 3")
    public void test26() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("id", Operation.EQUAL, 3.7));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "The value 3.7 is not compatible with type class java.lang.Long");
    }

    @Test
    @DisplayName("Invalid Number of Field Value 1")
    public void test27() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.IS_NULL, 3.7));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "The number of field values [3.7] are not compatible with operation IS_NULL");
    }

    @Test
    @DisplayName("Invalid Number of Field Value 2")
    public void test28() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.IS_NOT_NULL, 3.7));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "The number of field values [3.7] are not compatible with operation IS_NOT_NULL");
    }

    @Test
    @DisplayName("Invalid Number of Field Value 3")
    public void test29() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.IS_EMPTY_STRING, 3.7));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "The number of field values [3.7] are not compatible with operation IS_EMPTY_STRING");
    }

    @Test
    @DisplayName("Invalid Number of Field Value 4")
    public void test30() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("gpa", Operation.IS_NOT_EMPTY_STRING, 3.7));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "The number of field values [3.7] are not compatible with operation IS_NOT_EMPTY_STRING");
    }

    @Test
    @DisplayName("Invalid Field Name 1")
    public void test31() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("GPA", Operation.GREATER_THAN, 3.7));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "Could not resolve attribute GPA of class com.aya.search.entity.Student.");
    }

    @Test
    @DisplayName("Invalid Field Name 2")
    public void test32() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("community.Teacher", Operation.LIKE, "%Ayah%"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);

        Exception exception = assertThrows(Exception.class, () -> {
            studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        });
        assertEquals(exception.getMessage(), "Could not resolve attribute Teacher of class com.aya.search.entity.Community.");
    }

    @Test
    @DisplayName("AND Condition 1")
    public void test33() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                and(
                        condition("community.className", Operation.LIKE, "%9%"),
                        condition("gpa", Operation.GREATER_THAN, 3.5)
                ));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 5);
    }

    @Test
    @DisplayName("AND Condition 2")
    public void test34() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                and(
                        condition("community.className", Operation.LIKE, "%9%"),
                        condition("gpa", Operation.GREATER_THAN, 3.5),
                        condition("additionalInfo", Operation.IS_NOT_NULL)
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 3);
    }

    @Test
    @DisplayName("OR Condition 1")
    public void test35() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                or(
                        condition("gpaLetter", Operation.EQUAL, "A"),
                        condition("gpaLetter", Operation.EQUAL, "A-"),
                        condition("gpaLetter", Operation.EQUAL, "A+")
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 13);
    }

    @Test
    @DisplayName("OR Condition 2")
    public void test36() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                or(
                        condition("community.className", Operation.EQUAL, "9th B"),
                        condition("community.className", Operation.EQUAL, "9th A")
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 8);
    }

    @Test
    @DisplayName("AND and OR Condition 1")
    public void test37() {
        // (C1 OR C2) AND C3
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                and(
                        or(
                                condition("community.className", Operation.EQUAL, "9th B"),
                                condition("community.className", Operation.EQUAL, "9th A")
                        ),
                        condition("gpaLetter", Operation.IN, "A", "A-")
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 6);
    }

    @Test
    @DisplayName("AND and OR Condition 2")
    public void test38() {
        // C1 AND C2 OR C3 ----> (C1 AND C2) OR C3
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                or(
                        and(
                                condition("gpaLetter", Operation.IN, "A", "A-"),
                                condition("community.className", Operation.EQUAL, "9th B")
                        ),
                        condition("community.className", Operation.EQUAL, "9th A")
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 7);
    }

    @Test
    @DisplayName("AND and OR Condition 3")
    public void test39() {
        // (C1 OR C2 OR C3) AND C4
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                and(
                        condition("gpaLetter", Operation.IN, "A", "A-"),
                        or(
                                condition("community.className", Operation.EQUAL, "7th C"),
                                condition("community.className", Operation.EQUAL, "9th B"),
                                condition("community.className", Operation.EQUAL, "9th A")
                        )
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 10);
    }

    @Test
    @DisplayName("AND and OR Condition 4")
    public void test40() {
        // ((C1 OR C2 OR C3) AND C4) OR C5 OR C6 WE DOES NOT SUPPORT MULTI OR AND AND
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                or(
                        condition("gpaLetter", Operation.EQUAL, "A+"),
                        condition("gpaLetter", Operation.EQUAL, "A"),
                        and(
                                condition("gpaLetter", Operation.EQUAL, "A-"),
                                condition("community.className", Operation.EQUAL, "7th C")
                        ),
                        condition("community.className", Operation.EQUAL, "9th B"),
                        condition("community.className", Operation.EQUAL, "9th A")
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getTotalElements(), 12);
    }

    @Test
    @DisplayName("Sort ASC")
    public void test41() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setSortModel(asc("gpa"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getContent().get(0).getGpa(), 3.0);
    }

    @Test
    @DisplayName("Sort DESC")
    public void test42() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setSortModel(desc("gpa"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getContent().get(0).getGpa(), 3.9);
    }

    @Test
    @DisplayName("Multi Sort DESC and ASC 1")
    public void test43() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setSortModel(desc("gpa"), asc("firstName"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getContent().get(0).getGpa(), 3.9);
        assertEquals(students.getContent().get(0).getFirstName(), "Alice");
    }

    @Test
    @DisplayName("Multi Sort DESC and ASC 2")
    public void test44() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setSortModel(asc("gpa"), desc("firstName"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> students = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(students.getContent().get(0).getGpa(), 3.0);
        assertEquals(students.getContent().get(0).getFirstName(), "David");
    }

    @Test
    @DisplayName("AND and OR Condition 5")
    public void test45() throws JsonProcessingException {
        // (C1 AND C2) OR (C3 AND C4))
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                or(
                        and(
                                condition("id", Operation.IN, 1, 2, 3, 4),
                                condition("firstName", Operation.LIKE, "%a%")
                        ),
                        and(
                                condition("id", Operation.IN, 11, 12, 13, 14),
                                condition("firstName", Operation.LIKE, "%s%")
                        )
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 3);
    }

    @Test
    @DisplayName("AND and OR Condition 6")
    public void test46() throws JsonProcessingException {
        // ((C1 OR C2) AND (C3 OR C4))
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                and(
                        or(
                                condition("id", Operation.IN, 1, 2, 3, 4),
                                condition("firstName", Operation.LIKE, "%a%")
                        ),
                        or(
                                condition("id", Operation.IN, 11, 12, 13, 14),
                                condition("firstName", Operation.LIKE, "%s%")
                        )
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 5);
    }

    @Test
    @DisplayName("AND and OR Condition 7")
    public void test47() throws JsonProcessingException {
        // (C1 OR C2 OR (C3 AND C4))
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                or(
                        condition("id", Operation.IN, 1, 2, 3, 4),
                        condition("firstName", Operation.LIKE, "%a%"),
                        and(
                                condition("id", Operation.IN, 11, 12, 13, 14),
                                condition("firstName", Operation.LIKE, "%s%")
                        )
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 19);
    }

    @Test
    @DisplayName("AND and OR Condition 8")
    public void test48() throws JsonProcessingException {
        // (C1 AND C2 AND (C3 OR C4))
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setSortModel(asc("community.id"),
                desc("firstName"));
        dataManipulationModel.setCriteria(
                and(
                        condition("gpa", Operation.GREATER_THAN, 3.5),
                        condition("community.className", Operation.LIKE, "%9%"),
                        or(
                                condition("additionalInfo", Operation.IS_NULL),
                                condition("additionalInfo", Operation.IS_EMPTY_STRING)
                        )
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    @DisplayName("Not Like")
    public void test49() {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(condition("firstName", Operation.NOT_LIKE, "%Aya%"));
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 19);
    }

    @Test
    @DisplayName("NOT Condition 1")
    public void test50() throws JsonProcessingException {
        DataManipulationModel dataManipulationModel = new DataManipulationModel();
        dataManipulationModel.setCriteria(
                and(
                        not(
                                or(
                                        condition("id", Operation.IN, 1, 2, 3, 4),
                                        condition("firstName", Operation.LIKE, "%a%")
                                )
                        ),
                        not(
                                or(
                                        condition("id", Operation.IN, 11, 12, 13, 14),
                                        condition("firstName", Operation.LIKE, "%s%")
                                )
                        )
                )
        );
        GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
        Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
        assertEquals(page.getTotalElements(), 1);
    }
}