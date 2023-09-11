-- タスクのデータ
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("safih23r2d", 'demo', 'ダミーテキスト。ダミーテキスト。ダミーテ', 'ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ', true, "2100-05-20", now(), null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("494hjf2fff", 'demo', 'ダミーテキスト。ダミ', 'ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ', false, "2100-06-04", now(), null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("ianru29ckd", 'test', 'ダミーテキスト。ダミ', 'ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ', true, "2100-10-22", now(), null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("10u9fhiche", 'test', 'ダミーテキスト。ダミ', 'ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミーテキスト。ダミ', false, "2100-12-01", now(), null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("a2df8a93m1", "admin", "adminタイトル", "admin詳細", false, "2130-02-22", now(), null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("jhf92japdq", 'demo', 'Test Title', 'Test Description.', true, "9999-12-31", '2000-01-01 00:00:00', null);

--ユーザのデータ
INSERT INTo user(user_id, username, password, creation_time, update_time, telephone) VALUES ("demo", "demo", "$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO", '2023-07-15 09:23:10', null, "09009876543");
INSERT INTo user(user_id, username, password, creation_time, update_time, telephone) VALUES ("test", "test", "$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO", '2022-10-20 15:31:00', null, "09010293847");
INSERT INTo user(user_id, username, password, creation_time, update_time, telephone) VALUES ("admin", "admin", "$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO",'2022-11-17 21:12:05', '2023-05-02 07:30:40', "08099991111");
INSERT INTo user(user_id, username, password, creation_time, update_time, telephone) VALUES ("mob", "mob", "$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO", '2020-02-10 12:34:56', '2023-05-20 00:00:00', "08010203040");