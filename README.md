# Using GeneralSpecification in Spring Data JPA for Dynamic Queries

## Introduction

In application development, dynamic queries based on various parameters are often necessary. Spring Data JPA offers a
solution for creating such dynamic queries through Specifications. This document focuses on utilizing
the `GeneralSpecification` class to generate dynamic queries efficiently with Spring Data JPA.

## Prerequisites

Before proceeding, ensure you have:

- Basic knowledge of Java and Spring Boot.
- Understanding of Spring Data JPA and Specifications.

## GeneralSpecification Overview

`GeneralSpecification` is a generic class that implements the Specification interface provided by Spring Data JPA. It
facilitates dynamic query generation based on search criteria provided through a `DataManipulationModel`.

### DataManipulationModel: Overview

The `DataManipulationModel` encapsulates filters and sorting criteria.

```java
public class DataManipulationModel {

    /**
     * list of SortDataModel, The sorting will be performed sequentially based on the order of the 
     * models in the list. The first added model will be executed first, 
     * followed by the next, and so on.
     */
    private List<SortDataModel> sortDataModels;

    /**
     * Can be FilterCriteria or FilterGroup.
     **/
    private Filter criteria;
}
```

- **criteria**: Holds search criteria, which can be either a `FilterCriteria` or a `FilterGroup`.

    - **FilterCriteria**: Represents a single filter condition. It can be instantiated using
      the `condition(fieldName, operation, fieldValue)` method.
```java
package com.aya.search.model;

import lombok.Getter;

/**
 * search data model.
 *
 * @author Ayah Refai
 * @since 03/04/2024
 */
@Getter
public final class FilterCriteria implements Filter {

    /**
     * should match the instance variables of your JPA entity.
     * If you need to filter based on a field inside a related object,
     * you can reference it using dot notation
     **/
    private final String fieldName;

    /**
     * The operation to apply (e.g., EQUALS, GREATER_THAN) {@link Operation}
     */
    private final Operation operation;

    /**
     * The value(s) to compare the field against.
     */
    private final Object[] fieldValue;
    	
}
```

  - **FilterGroup**: Represents a group of filter conditions combined with logical operators (AND/OR). It can be
    instantiated using the `and(FilterCriteria|FilterGroup)` or `or(FilterCriteria|FilterGroup)` method.

   ```java
   package com.aya.search.model;

import lombok.Getter;
import java.util.List;

/**
 * Filter Group.
 *
 * @author Ayah Refai
 * @since 05/24/2024
 */
@Getter
public final class FilterGroup implements Filter {

    /**
     * This defines the logical operation that combines the filters within the group.
     * It can be either "AND" or "OR" {@link Condition}
     */
    private final Condition condition;

    /**
     * This is a list of filters that can either be individual FilterCriteria or other FilterGroup objects.
     * This means you can nest groups of filters to create complex filtering logic.
     * <p>can be list of {@link FilterCriteria} or {@link FilterGroup}.</p>
     */
    private final List<com.aya.search.model.Filter> conditions;
}
   ```

- **SortDataModel**: Represents sorting criteria with a field name and sort order. It can be instantiated using
  the `asc(fieldName)` or `desc(fieldName)` methods.

```java
package com.aya.search.model;

import lombok.Getter;

/**
 * sort data model.
 *
 * @author Ayah Refai
 * @since 03/04/2024
 */
@Getter
public final class SortDataModel {

    /**
     * should match the instance variables of your JPA entity.
     * If you need to sorted based on a field inside a related object,
     * you can reference it using dot notation
     **/
    private final String sortField;

    /**
     * The sort order to apply {@link SortOrder};
     */
    private final SortOrder sortOrder;
}
```

### Usage

#### Adding Dependencies

Ensure that you include the necessary dependency in your project's Maven `pom.xml`.

```xml

<dependencies>
    <!-- Other dependencies -->
    <dependency>
        <groupId>com.aya.specification</groupId>
        <artifactId>aya-search-lib</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

#### Creating DataManipulationModel

Instantiate a `DataManipulationModel` object encapsulating your search and sort criteria.

```java
public static void main(String... args) {
    DataManipulationModel dataManipulationModel = new DataManipulationModel();
    dataManipulationModel.setSortModel(asc("gpa"), desc("firstName"));
    dataManipulationModel.setCriteria(
                    and(
                            condition("id", Operation.IN, 1, 2, 3, 4),
                            condition("firstName", Operation.LIKE, "%a%"),
                            or(
                                    condition("id", Operation.IN, 11, 12, 13, 14),
                                    condition("firstName", Operation.LIKE, "%s%")
                            )
                    )
            );
}
```

#### Creating GeneralSpecification

Instantiate a `GeneralSpecification` object with the `DataManipulationModel`.

```java
GeneralSpecification<Student> studentGeneralSpecification = new GeneralSpecification<>(dataManipulationModel);
```

#### Using GeneralSpecification with Spring Data JPA Repository

Pass the `GeneralSpecification` object to the `findAll` method of your Spring Data JPA repository along with
a `Pageable` object for pagination.

```java
Page<Student> page = studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
```

*Note: Ensure that your `studentRepository` implements `JpaSpecificationExecutor`.*

## Example

Here's an example demonstrating the usage of `GeneralSpecification` in a service method to retrieve paginated results
based on dynamic search and sort criteria.

### Student.java anf Community.java
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double gpa;

    @Column(name = "is_full_time")
    private boolean isFullTime;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "additional_info")
    private String additionalInfo;

    private String address;

    @Column(unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "gpa_letter")
    private String gpaLetter;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "teacher")
    private String teacher;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private List<Student> students;
}
```

### StudentRepository.java
```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

}
```

### StudentService.java
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aya.search.entity.Student;
import com.aya.search.model.DataManipulationModel;
import com.aya.search.model.Operation;
import com.aya.search.repository.StudentRepository;
import com.aya.search.specification.GeneralSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static com.aya.search.model.FilterCriteria.Condition.condition;
import static com.aya.search.model.FilterGroup.Filter.and;
import static com.aya.search.model.FilterGroup.Filter.or;
import static com.aya.search.model.SortDataModel.Sort.asc;
import static com.aya.search.model.SortDataModel.Sort.desc;

@Service
public class StudentService {

    @Autowired
    public StudentRepository studentRepository;

    public Page<Student> getStudents() {
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
        return studentRepository.findAll(studentGeneralSpecification, PageRequest.of(0, 10));
    }
}
```

### query.sql
```sql
    select s1_0.id,
           s1_0.additional_info,
           s1_0.address,
           s1_0.community_id,
           s1_0.date_of_birth,
           s1_0.email,
           s1_0.enrollment_date,
           s1_0.first_name,
           s1_0.gpa,
           s1_0.gpa_letter,
           s1_0.is_full_time,
           s1_0.last_name,
           s1_0.phone_number
    from student s1_0
    join community c1_0 on c1_0.id = s1_0.community_id
    where s1_0.gpa > 3.5
      and c1_0.class_name like '%9%' escape ''
        and (
            s1_0.additional_info is null
                or s1_0.additional_info = ''
            )
    order by s1_0.community_id,
             s1_0.first_name desc
    offset 0 rows fetch
        first 10 rows only
```
### result.json
```json
[
  {
    "id": 11,
    "gpa": 3.7,
    "community": {
      "id": 1,
      "className": "9th A",
      "teacher": "Mr. Smith",
      "students": null
    },
    "dateOfBirth": [
      2002,
      2,
      12
    ],
    "enrollmentDate": [
      2022,
      9,
      1
    ],
    "additionalInfo": null,
    "address": "",
    "email": "olivia.rodriguez@example.com",
    "firstName": "Olivia",
    "gpaLetter": "A-",
    "lastName": "Rodriguez",
    "phoneNumber": "9876543210",
    "fullTime": true
  },
  {
    "id": 17,
    "gpa": 3.9,
    "community": {
      "id": 3,
      "className": "9th B",
      "teacher": "Ms. Brown",
      "students": null
    },
    "dateOfBirth": [
      2000,
      7,
      30
    ],
    "enrollmentDate": [
      2023,
      4,
      20
    ],
    "additionalInfo": null,
    "address": "369 Maple St, City",
    "email": "charlotte.rivera@example.com",
    "firstName": "Charlotte",
    "gpaLetter": "A",
    "lastName": "Rivera",
    "phoneNumber": "9870123456",
    "fullTime": true
  }
]
```
This revised documentation provides a clear understanding of how to use `GeneralSpecification` for dynamic queries in
Spring Data JPA.