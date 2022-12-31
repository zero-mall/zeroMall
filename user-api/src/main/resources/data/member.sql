insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn, subscribed_at)
values
    (1, 'test1@gmail.com', 'password', 'test1', 21, 1, true, 'key', true, '2022-11-30')
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn, subscribed_at)
values
    (2, 'test2@gmail.com', 'password', 'test2', 24, 1, true, 'key', true, '2022-11-30')
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn, subscribed_at)
values
    (3, 'test3@gmail.com', 'password', 'test3', 27, 1, true, 'key', true, '2022-11-30')
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn, subscribed_at)
values
    (4, 'test4@gmail.com', 'password', 'test4', 31, 1, true, 'key', true, '2022-11-30')
on duplicate key update member_id = values(member_id);

insert into member
(member_id, email, password, nickname, age, current_point, email_auth_yn, email_auth_key, subscribe_yn, subscribed_at)
values
    (5, 'test5@gmail.com', 'password', 'test5', 35, 1, true, 'key', true, '2022-11-28')
on duplicate key update member_id = values(member_id);