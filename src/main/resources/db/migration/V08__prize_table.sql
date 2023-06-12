CREATE TABLE public.prize
(
    id serial NOT NULL,
    date date NOT NULL,
    client_id integer,
    product_id integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (id)
);