CREATE TABLE public.users (
    id serial4 NOT NULL,
    username varchar(255) NULL,
    "password" varchar(255) NULL,
    fio varchar(255) NULL,
    state varchar(255) NULL,
    "role" varchar(255) NULL,
    invitation_code varchar(255) NULL,
    kpi int4 NULL,
    inviter_user_id int4 NULL,
    created_at timestamp DEFAULT now() NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.requests (
     id serial4 NOT NULL,
     title varchar(255) NULL,
     "comment" text NULL,
     people text NULL,
     creator_user_id int4 NULL,
     "type" varchar(255) NULL,
     status varchar(255) NULL,
     map_point varchar(255) NULL,
     created_at timestamp DEFAULT now() NULL,
     executor_user_id int4 NULL,
     CONSTRAINT requests_pkey PRIMARY KEY (id),
     CONSTRAINT requests_creator_user_id_fkey FOREIGN KEY (creator_user_id) REFERENCES public.users(id)
);

--CREATE DATABASE inventory;
CREATE TABLE product (
     id SERIAL PRIMARY KEY,
     name VARCHAR(50),
     quantity INTEGER,
     brand VARCHAR(50)
);
INSERT INTO product (name, quantity, brand) VALUES ('A55', '10', 'Samsung');
