INSERT INTO account
VALUES
    (nextval('account_id_seq'::regclass), 'user1', 'pass1', 'Иван', 'Иванов', 'Иванович'),
    (nextval('account_id_seq'::regclass), 'user2', 'pass1', 'Петр', 'Петров', 'Петрович'),
    (nextval('account_id_seq'::regclass), 'user3', 'pass1', 'Имя', 'Фамилия', 'Отчество');

INSERT INTO carrier
VALUES
    (nextval('carrier_id_seq'::regclass), 'Петров и Ко', '+7 (191) 322-22-33)'),
    (nextval('carrier_id_seq'::regclass), 'Васечкин и Ко', '+7 (191) 223-33-22)');

INSERT INTO point
VALUES
    (nextval('point_id_seq'::regclass), 'Пункт А'),
    (nextval('point_id_seq'::regclass), 'Пункт Б'),
    (nextval('point_id_seq'::regclass), 'Пункт В'),
    (nextval('point_id_seq'::regclass), 'Пункт Г'),
    (nextval('point_id_seq'::regclass), 'Пункт Д'),
    (nextval('point_id_seq'::regclass), 'Пункт Е');

INSERT INTO path
VALUES
    (nextval('path_id_seq'::regclass), 1, 2, 1, '10 Hours'),
    (nextval('path_id_seq'::regclass), 2, 3, 2, '2 Hours'),
    (nextval('path_id_seq'::regclass), 4, 5, 1, '5 Hours'),
    (nextval('path_id_seq'::regclass), 3, 5, 2, '6 Hours'),
    (nextval('path_id_seq'::regclass), 1, 6, 1, '3 Hours'),
    (nextval('path_id_seq'::regclass), 2, 4, 2, '1 Hour');

INSERT INTO ticket (id, path_id, place, price, date_time)
VALUES
    (nextval('ticket_id_seq'::regclass), 1, 1, 1.0, NOW()),
    (nextval('ticket_id_seq'::regclass), 1, 2, 1.0, NOW() + interval '2 hour'),
    (nextval('ticket_id_seq'::regclass), 2, 1, 1.0, NOW() + interval '1 day'),
    (nextval('ticket_id_seq'::regclass), 2, 2, 1.0, NOW() + interval '1 day 2 hour'),
    (nextval('ticket_id_seq'::regclass), 3, 1, 1.0, NOW() + interval '2 day'),
    (nextval('ticket_id_seq'::regclass), 3, 2, 1.0, NOW() + interval '2 day 2 hour'),
    (nextval('ticket_id_seq'::regclass), 4, 1, 1.0, NOW() + interval '3 day'),
    (nextval('ticket_id_seq'::regclass), 4, 2, 1.0, NOW() + interval '3 day 2 hour'),
    (nextval('ticket_id_seq'::regclass), 5, 1, 1.0, NOW() + interval '4 day'),
    (nextval('ticket_id_seq'::regclass), 5, 2, 1.0, NOW() + interval '4 day 2 hour'),
    (nextval('ticket_id_seq'::regclass), 5, 3, 1.0, NOW() + interval '5 day'),
    (nextval('ticket_id_seq'::regclass), 6, 1, 1.0, NOW() + interval '5 day 2 hour'),
    (nextval('ticket_id_seq'::regclass), 6, 2, 1.0, NOW() + interval '6 day'),
    (nextval('ticket_id_seq'::regclass), 6, 3, 1.0, NOW() + interval '6 day 2 hour');