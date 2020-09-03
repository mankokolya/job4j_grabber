CREATE database job_grabber;
\c job_grabber

CREATE schema jobs
    create table rabbit
    (
        id bigserial not null primary key,
        created date not null
    );