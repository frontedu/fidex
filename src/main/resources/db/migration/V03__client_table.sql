CREATE TABLE public.client
(
    id serial NOT NULL,
    name text NOT NULL,
    cpf text NOT NULL,
    createdBy text NOT NULL,
    phone text NOT NULL,
    points integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (id)
);