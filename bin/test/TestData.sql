-- タスクのデータ
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("jhf92japdq", 'testUser', 'true task title', 'true task description.', true, "9999-12-31", "1000-01-01T01:01:01", null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("494hjf2fff", 'testUser', 'false task title', 'false task description.', false, "2100-06-04", "2000-02-02T02:02:02", null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("o20jn20fao", 'trueUser', 'true task title', 'true task description.', true, "9999-12-31", "1000-01-01T01:01:01", null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("0ih2bdjjc0", 'trueUser', 'true task title', 'true task description.', true, "2100-06-04", "2000-02-02T02:02:02", null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("0jan2ihxcb", 'falseUser', 'false task title', 'false task description.', false, "9999-12-31", "1000-01-01T01:01:01", null);
INSERT INTO task(task_id, user_id, title, description, completed_flg, deadline, creation_time, update_time) VALUES ("kkah28tdbd", 'falseUser', 'false task title', 'false task description.', false, "2100-06-04", "2000-02-02T02:02:02", null);

--ユーザのデータ
INSERT INTO user(user_id, username, password, creation_time, update_time, telephone) VALUES ("testUser", "testUser", "$2a$10$kV2OZZQhoe14T89ydNr0MOAg5qgP9Oh8fxVpa6eJcmPuReLvYoqyO", '2000-01-01T00:00:00', null, "09009876543");