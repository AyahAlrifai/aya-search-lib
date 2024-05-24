CREATE TABLE community
(
    id         SERIAL PRIMARY KEY,
    class_name VARCHAR(255),
    teacher    VARCHAR(255)
);

CREATE TABLE student
(
    id              SERIAL PRIMARY KEY,
    gpa DOUBLE NOT NULL,
    is_full_time    BOOLEAN      NOT NULL,
    community_id    BIGINT,
    date_of_birth   TIMESTAMP    NOT NULL,
    enrollment_date TIMESTAMP    NOT NULL,
    additional_info VARCHAR(255),
    address         VARCHAR(255) NOT NULL default '',
    email           VARCHAR(255) NOT NULL,
    first_name      VARCHAR(255) NOT NULL,
    gpa_letter      VARCHAR(255),
    last_name       VARCHAR(255) NOT NULL,
    phone_number    VARCHAR(255) NOT NULL,
    CONSTRAINT student_email_unique UNIQUE (email),
    FOREIGN KEY (community_id) REFERENCES community (id)
);

INSERT INTO community (id, class_name, teacher)
VALUES (1, '9th A', 'Mr. Smith'),
       (2, '4th B', 'Dr. Johnson'),
       (3, '9th B', 'Ms. Brown'),
       (4, '6th A', 'Prof. Martinez'),
       (5, '7th C', 'Mrs. Taylor');


INSERT INTO student (first_name, last_name, date_of_birth, email, phone_number, address, enrollment_date, gpa,
                     gpa_letter, additional_info, is_full_time, community_id)
VALUES ('Ayah', 'Rifai', '1997-05-07', 'alrefayayah@gmail.com', '00962778761538', 'Jordan', '2005-05-17', 3.8, 'A',
        'Some additional info', TRUE, 5),
       ('John', 'Doe', '1999-05-15', 'john.doe@example.com', '1234567890', '', '2022-01-01', 3.5, 'A-',
        NULL, TRUE, 1),
       ('Alice', 'Smith', '2000-08-20', 'alice.smith@example.com', '9876543210', '456 Oak St, Town', '2022-02-15', 3.9,
        'A', 'Member of Chess Club', TRUE, 1),
       ('Michael', 'Johnson', '2001-03-10', 'michael.johnson@example.com', '5551234567', '',
        '2021-12-10', 3.2, 'B+', 'Plays basketball', TRUE, 2),
       ('Emily', 'Brown', '1998-11-28', 'emily.brown@example.com', '1239876543', '987 Elm St, County', '2022-03-20',
        3.7, 'A-', 'Volunteers at local shelter', TRUE, 2),
       ('David', 'Wilson', '2002-07-05', 'david.wilson@example.com', '7894561230', '', '2022-04-05',
        3.0, 'B', NULL, TRUE, 3),
       ('Sophia', 'Taylor', '2003-09-12', 'sophia.taylor@example.com', '3216540987', '654 Cedar St, Borough',
        '2022-05-10', 3.6, 'A-', 'Plays violin in orchestra', TRUE, 3),
       ('James', 'Lee', '2001-01-25', 'james.lee@example.com', '4567890123', '', '2022-06-15', 3.8,
        'A', 'Part-time job at cafe', FALSE, 5),
       ('Emma', 'Garcia', '2000-04-30', 'emma.garcia@example.com', '9870123456', '369 Birch St, Town', '2022-07-20',
        3.9, 'A', NULL, TRUE, 5),
       ('William', 'Martinez', '1999-08-15', 'william.martinez@example.com', '1234567890', '123 Elm St, Village',
        '2022-08-25', 3.4, 'B', 'Plays soccer', TRUE, 1),
       ('Olivia', 'Rodriguez', '2002-02-12', 'olivia.rodriguez@example.com', '9876543210', '',
        '2022-09-01', 3.7, 'A-', NULL, TRUE, 1),
       ('Daniel', 'Hernandez', '2003-11-05', 'daniel.hernandez@example.com', '5551234567', '789 Oak St, Village',
        '2022-10-15', 3.2, 'B+', 'Member of Drama Club', TRUE, 2),
       ('Isabella', 'Lopez', '2000-06-20', 'isabella.lopez@example.com', '1239876543', '987 Pine St, County',
        '2022-11-20', 3.9, 'A', 'Prefect in Student Council', TRUE, 3),
       ('Alexander', 'Gonzalez', '2001-09-08', 'alexander.gonzalez@example.com', '7894561230', '321 Elm St, City',
        '2023-01-05', 3.0, 'B', NULL, TRUE, NULL),
       ('Mia', 'Perez', '2003-04-15', 'mia.perez@example.com', '3216540987', '', '2023-02-10', 3.6,
        'A-', 'Plays piano', TRUE, NULL),
       ('Ethan', 'Torres', '2002-01-25', 'ethan.torres@example.com', '4567890123', '147 Oak St, Borough', '2023-03-15',
        3.8, 'A', 'Volunteers at animal shelter', FALSE, NULL),
       ('Charlotte', 'Rivera', '2000-07-30', 'charlotte.rivera@example.com', '9870123456', '369 Maple St, City',
        '2023-04-20', 3.9, 'A', NULL, TRUE, 3),
       ('Noah', 'Collins', '1999-08-18', 'noah.collins@example.com', '1234567890', '', '2023-05-25',
        3.4, 'B', 'Plays guitar', TRUE, 4),
       ('Ava', 'Bell', '2001-12-05', 'ava.bell@example.com', '9876543210', '456 Elm St, Town', '2023-06-30', 3.7, 'A-',
        'Captain of soccer team', TRUE, 5),
       ('Liam', 'Young', '2003-03-20', 'liam.young@example.com', '5551234567', '789 Oak St, County', '2023-07-05', 3.1,
        'B+', 'Prefect in Student Council', TRUE, 2);

