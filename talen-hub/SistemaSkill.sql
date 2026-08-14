CREATE SEQUENCE categorias_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE skills_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE usuarios_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE usuarios_skills_seq
    START WITH 1
    INCREMENT BY 1;


CREATE TABLE categorias (
                            id BIGINT NOT NULL DEFAULT nextval('categorias_seq'),
                            nome VARCHAR(100) NOT NULL,

                            CONSTRAINT pk_categoria
                                PRIMARY KEY (id),

                            CONSTRAINT uk_categoria_nome
                                UNIQUE (nome)
);


CREATE TABLE usuarios (
                          id BIGINT NOT NULL DEFAULT nextval('usuarios_seq'),
                          nome VARCHAR(150) NOT NULL,
                          email VARCHAR(150) NOT NULL,
                          senha VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL,
                          ativo BOOLEAN NOT NULL,
                          data_criacao TIMESTAMP NOT NULL,

                          CONSTRAINT pk_usuario
                              PRIMARY KEY (id),

                          CONSTRAINT uk_usuario_email
                              UNIQUE (email),

                          CONSTRAINT ck_usuario_role
                              CHECK (role IN ('USER', 'ADMIN'))
);


CREATE TABLE skills (
                        id BIGINT NOT NULL DEFAULT nextval('skills_seq'),
                        nome VARCHAR(100) NOT NULL,
                        descricao VARCHAR(500) NOT NULL,
                        imagem_url VARCHAR(500),
                        categoria_id BIGINT NOT NULL,

                        CONSTRAINT pk_skill
                            PRIMARY KEY (id),

                        CONSTRAINT uk_skill_nome_categoria
                            UNIQUE (nome, categoria_id),

                        CONSTRAINT fk_skill_categoria
                            FOREIGN KEY (categoria_id)
                                REFERENCES categorias(id)
);


CREATE TABLE usuarios_skills (
                                 id BIGINT NOT NULL DEFAULT nextval('usuarios_skills_seq'),
                                 usuario_id BIGINT NOT NULL,
                                 skill_id BIGINT NOT NULL,
                                 level VARCHAR(20) NOT NULL,

                                 CONSTRAINT pk_usuario_skill
                                     PRIMARY KEY (id),

                                 CONSTRAINT fk_usuario_skill_usuario
                                     FOREIGN KEY (usuario_id)
                                         REFERENCES usuarios(id),

                                 CONSTRAINT fk_usuario_skill_skill
                                     FOREIGN KEY (skill_id)
                                         REFERENCES skills(id),

                                 CONSTRAINT uk_usuario_skill
                                     UNIQUE (usuario_id, skill_id),

                                 CONSTRAINT ck_usuario_skill_level
                                     CHECK (level IN (
                                                      'BASICO',
                                                      'INTERMEDIARIO',
                                                      'AVANCADO'
                                         ))
);