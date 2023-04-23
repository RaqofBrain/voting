INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME, PHONE_NUMBER, ADDRESS)
VALUES ('Додо Пицца', '+78003020060', 'Большой проспект П.С. 33'),
       ('Red. Steak&Wine', '+78129274664', 'Улица Ленина 9'),
       ('Теремок', '+78123632332', 'Большой проспект П.С. 64');

INSERT INTO MENU (RESTAURANT_ID, MENU_DATE)
VALUES (1, '2023-04-12'),
       (2, '2023-04-12'),
       (3, '2023-04-12'),
       (1, '2023-04-11');

INSERT INTO DISH (NAME, PRICE, CREATED)
VALUES ('Пицца Моцарелла', 3500, CURRENT_TIMESTAMP),
       ('Блин Илья Муромец', 1400, CURRENT_TIMESTAMP),
       ('Бургер с говядиной', 5000, CURRENT_TIMESTAMP),
       ('Пицца Прошуто Фунги', 4000, CURRENT_TIMESTAMP);

INSERT INTO MENU_DISHES (MENU_ID, DISH_ID)
VALUES (1, 1),
       (1, 4),
       (2, 3),
       (3, 2);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, VOTE_DATE, VOTE_TIME)
VALUES (1, 1, '2023-04-11', '10:00:00'),
       (2, 1, '2023-04-11', '11:00:00');