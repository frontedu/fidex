CREATE TABLE public.product
(
    id serial NOT NULL,
    name text NOT NULL,
    price numeric(19,2) NOT NULL,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (id)
);