insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn)
values
    (1, 'test1@gmail.com', 'password', 'test1', 21, 1, true, 'key', true)
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn)
values
    (2, 'test2@gmail.com', 'password', 'test2', 24, 1, true, 'key', true)
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn)
values
    (3, 'test3@gmail.com', 'password', 'test3', 27, 1, true, 'key', true)
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn)
values
    (4, 'test4@gmail.com', 'password', 'test4', 31, 1, true, 'key', true)
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn)
values
    (5, 'test5@gmail.com', 'password', 'test5', 35, 1, true, 'key', true)
on duplicate key update member_id = values(member_id);