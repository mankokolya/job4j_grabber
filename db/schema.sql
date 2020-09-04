CREATE database job_grabber;
\c job_grabber

CREATE schema jobs
    create table sql_ru_jobs
    (
        id bigserial not null primary key,
        url text not null,
        title varchar(300) not null,
        date date not null
    );