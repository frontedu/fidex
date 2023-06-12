CREATE TABLE public.client
(
    id serial NOT NULL,
    name text NOT NULL,
    cpf text NOT NULL,
    phone_number text NOT NULL,
    poits integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (id)
);