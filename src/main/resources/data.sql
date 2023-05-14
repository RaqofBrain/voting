INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (name, address, phone_number)
VALUES ('Додо пицца', 'Большой пр. П.С. 33', '88005553535'),
       ('Вкусно и точка', 'Каменоостровский пр. 39', '88909890'),
       ('Теремок', 'Большой пр. П.С. 64', '9090808');

INSERT INTO dish (name, price, restaurant_id)
VALUES ('Пицца Карбонара', 469, 1),
       ('Пицца Додо', 519, 1),
       ('2 Додстера', 319, 1),
       ('Чизбургер', 59, 2),
       ('Биг Спешиал Ростбиф', 289, 2),
       ('Биг хит + Чизбургер', 209, 2),
       ('Блин Илья Муромец', 402, 3),
       ('Борщ с рубленым мясом', 227, 3);

INSERT INTO menu (menu_date, restaurant_id)
VALUES (CURRENT_TIMESTAMP, 1),
       (DATEADD('DAY', -1, CURRENT_TIMESTAMP), 1),
       (DATEADD('DAY', -2, CURRENT_TIMESTAMP), 1),
       (CURRENT_TIMESTAMP, 2),
       (CURRENT_TIMESTAMP, 3);

INSERT INTO dish_menu (menu_id, dish_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (4, 5),
       (4, 6),
       (5, 7),
       (5, 8);

INSERT INTO vote (vote_date, vote_time, user_id, restaurant_id)
VALUES (CURRENT_TIMESTAMP, '00:00', 1, 1),
       (DATEADD('DAY', -1, CURRENT_TIMESTAMP), '10:00', 1, 3),
       (CURRENT_TIMESTAMP, '15:00', 2, 2),
       (DATEADD('DAY', -1, CURRENT_TIMESTAMP), '12:00', 2, 3);

