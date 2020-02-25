insert into User (id, username, password, name, surname, email) values ('870c2a9ee3014269bcdef660826b4d66', 'user1', 'user1pass', 'name one', 'surname one', 'user1@uca.es');
insert into Course (id, name, description, time, price) values (1, 'course1', 'description1', 15, 150);
insert into Course (id, name, description, time, price) values (2, 'course2', 'description2', 15, 150);
insert into Course (id, name, description, time, price) values (3, 'course3', 'description3', 15, 150);
insert into enrolled_courses (user_id, course_id) values ('870c2a9ee3014269bcdef660826b4d66', 1)
insert into enrolled_courses (user_id, course_id) values ('870c2a9ee3014269bcdef660826b4d66', 2)
insert into enrolled_courses (user_id, course_id) values ('870c2a9ee3014269bcdef660826b4d66', 3)
