CREATE DATABASE company_worker;

\c company_worker

CREATE TABLE company
(
    id bigserial NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id bigserial NOT NULL,
    name character varying,
    company_id integer REFERENCES company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

INSERT INTO company VALUES (default, 'Coca Cola');
INSERT INTO company VALUES (default, 'Nike');
INSERT INTO company VALUES (default, 'Adidas');
INSERT INTO company VALUES (default, 'Apple');
INSERT INTO company VALUES (default, 'Microsoft');
INSERT INTO company VALUES (default, 'IBM');
INSERT INTO company VALUES (default, 'Bank Of America');

INSERT INTO person (name, company_id) values ('Nick', 4);
INSERT INTO person (name, company_id) values ('Ira', 1);
INSERT INTO person (name, company_id) values ('Bill', 5);
INSERT INTO person (name, company_id) values ('John', 2);
INSERT INTO person (name, company_id) values ('Anna', 3);
INSERT INTO person (name, company_id) values ('Lena', 6);
INSERT INTO person (name, company_id) values ('Marry', 7);
INSERT INTO person (name, company_id) values ('Tim', 7);
INSERT INTO person (name, company_id) values ('Kaleb', 7);


select p.name as person, c.name as company from person p left join company c
    on c.id = p.company_id where p.company_id <> 5;

select c.name, count(p.company_id) from company c left join person p
    on c.id = p.company_id group by c.name order by count(p.company_id) DESC LIMIT 1;