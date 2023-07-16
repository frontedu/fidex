CREATE TABLE public.prize
(
    id serial NOT NULL,
    date date NOT NULL,
    client_id integer,
    product_id integer,
    createdBy text NOT NULL,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (id)
);