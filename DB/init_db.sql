CREATE TABLE public.users (
    id serial4 NOT NULL,
    username varchar(255) NULL,
    "password" varchar(255) NULL,
    fio varchar(255) NULL,
    state varchar(255) NULL,
    "role" varchar(255) NULL,
    invitation_code varchar(255) NULL,
    kpi int4 not NULL default 0,
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
     review varchar(255) NULL,
     CONSTRAINT requests_pkey PRIMARY KEY (id),
     CONSTRAINT requests_creator_user_id_fkey FOREIGN KEY (creator_user_id) REFERENCES public.users(id)
);


CREATE TABLE public.notifications (
     id serial4 NOT NULL,
     title varchar(255) NULL,
     body text NULL,
     is_send boolean NULL default false,
     owner_id int4 NULL,
     CONSTRAINT notifications_pkey PRIMARY KEY (id),
     CONSTRAINT notifications_owner_id_fkey FOREIGN KEY (owner_id) REFERENCES public.users(id)
);

INSERT INTO users
(username,"password",fio,state,"role",invitation_code,kpi,inviter_user_id,created_at) VALUES
('vampire','test',NULL,NULL,'supremeVampire','123',0,NULL),
('familiar','test',NULL,NULL,'familiar','321',900,NULL);

