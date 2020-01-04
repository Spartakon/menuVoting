DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM menus;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
('User', 'user@yandex.ru', '{noop}password'),    /*100000*/
('Admin', 'admin@gmail.com', '{noop}admin'),     /*100001*/
('User2', 'user2@gmail.com', '{noop}password2');  /*100002*/

INSERT INTO user_roles (role, user_id) VALUES
('ROLE_USER', 100000),
('ROLE_ADMIN', 100001),
('ROLE_USER', 100001),
('ROLE_USER', 100002);

INSERT INTO restaurants (name) VALUES
('Ресторан 1'),                         /*100003*/
('Ресторан 2'),                         /*100004*/
('Ресторан 3');                         /*100005*/

INSERT INTO menus (date, restaurant_id) VALUES
(current_date, 100003),                            /*100006*/
(current_date, 100004),                            /*100007*/
(current_date + INTERVAL '1' DAY, 100003),   /*100008*/
(current_date - INTERVAL '1' DAY, 100003),   /*100009*/
(current_date - INTERVAL '1' DAY, 100004);   /*100010*/

INSERT INTO dishes (NAME, PRICE, MENU_ID) VALUES
('Суп', 100, 100006),        /*100011*/
('Чай', 50, 100007),         /*100012*/
('Мясо', 150, 100006),       /*100013*/
('Булка', 100, 100007),      /*100014*/
('Пюре', 80, 100008);        /*100015*/

INSERT INTO votes (DATE, USER_ID, RESTAURANT_ID) VALUES
(current_date, 100000, 100003),                         /*100016*/
(current_date, 100001, 100004),                         /*100017*/
(current_date - INTERVAL '1' DAY , 100000, 100003),     /*100018*/
(current_date - INTERVAL '1' DAY , 100002, 100004);     /*100019*/
