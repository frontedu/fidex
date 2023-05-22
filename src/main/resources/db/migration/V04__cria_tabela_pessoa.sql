CREATE TABLE public.pessoa
(
    codigo serial NOT NULL,
    nome text,
    cpf text,
    data_nascimento date,
    profissao text,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);