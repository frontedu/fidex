CREATE TABLE public.usuario
(
    codigo bigserial NOT NULL,
    nome text,
    email text,
    nome_usuario text,
    senha text,
    data_nascimento date,
    ativo boolean,
    PRIMARY KEY (codigo)
);

CREATE TABLE public.papel
(
    codigo bigserial NOT NULL,
    nome text,
    PRIMARY KEY (codigo)
);

CREATE TABLE public.usuario_papel
(
    codigo_usuario bigint NOT NULL,
    codigo_papel bigint NOT NULL
);

ALTER TABLE public.usuario_papel
    ADD FOREIGN KEY (codigo_usuario)
    REFERENCES public.usuario (codigo)
    NOT VALID;


ALTER TABLE public.usuario_papel
    ADD FOREIGN KEY (codigo_papel)
    REFERENCES public.papel (codigo)
    NOT VALID;
END;
