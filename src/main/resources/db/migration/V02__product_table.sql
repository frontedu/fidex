CREATE TABLE public.product
(
    id serial NOT NULL,
    name text NOT NULL,
    quantity integer NOT NULL,
    price numeric(19,2) NOT NULL,
    createdBy text NOT NULL,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (id)
);