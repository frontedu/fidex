CREATE TABLE public.purchase
(
    id serial NOT NULL,
    date date NOT NULL,
    client_id integer,
    createdBy text NOT NULL,
    price numeric(19,2) NOT NULL,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (id)
);