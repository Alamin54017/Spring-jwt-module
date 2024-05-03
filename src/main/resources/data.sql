INSERT INTO authority_table
VALUES(1, 'ROLE_ADMIN');

INSERT INTO authority_table
VALUES(2, 'ROLE_USER');

INSERT INTO user_table (user_id, first_name, last_name, username, password)
VALUES(101, 'Alamin', 'Hossain', 'alamin4577', '$2a$12$TdPnR28DC/pP5dRhAr.WkerLvOHmugwiNrg5M/ABaY/bJV5lIKQXS');
-- Here the password is BCrypt Encrypted. The Password is "iamadmin"


INSERT INTO user_table (user_id, first_name, last_name, username, password)
VALUES(102, 'Sanha', 'Shumi', 'sanha001', '$2a$12$4CXHZRs93K9G0ZKES2bnGOa98UxIQo/NFgRdlbo0N6w2y.NiqTcU6');
-- Here the password is BCrypt Encrypted. The Password is "iamuser"

INSERT INTO user_authority (user_id, authority_id)
VALUES(101, 1);

INSERT INTO user_authority (user_id, authority_id)
VALUES(101, 2);

INSERT INTO user_authority (user_id, authority_id)
VALUES(102, 2);