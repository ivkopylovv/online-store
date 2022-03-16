CREATE TABLE public.user_entity
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    login character varying(255),
    name character varying(255),
    password character varying(255),
    role character varying(255) DEFAULT 'user',
    PRIMARY KEY (id)
);