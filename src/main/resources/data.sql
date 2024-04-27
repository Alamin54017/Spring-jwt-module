INSERT INTO authority_table
VALUES(1, 'ROLE_ADMIN');

INSERT INTO authority_table
VALUES(2, 'ROLE_USER');

INSERT INTO user_table (user_id, first_name, last_name, username, password)
VALUES(101, 'Alamin', 'Hossain', 'alamin4577', 'iamadmin');

INSERT INTO user_table (user_id, first_name, last_name, username, password)
VALUES(102, 'Sanha', 'Shumi', 'sanha001', 'iamuser');

INSERT INTO user_authority (user_id, authority_id)
VALUES(101, 1);

INSERT INTO user_authority (user_id, authority_id)
VALUES(101, 2);

INSERT INTO user_authority (user_id, authority_id)
VALUES(102, 2);