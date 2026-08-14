# Talent Hub — Backend


Backend da aplicação **Talent Hub**, desenvolvido para gerenciamento de talentos, usuários e informações profissionais.


A aplicação foi desenvolvida utilizando **Java e Spring Boot**, seguindo uma arquitetura organizada em camadas e utilizando uma API REST para comunicação com o frontend.


## 🚀 Tecnologias


- Java 17
- Spring Boot 4
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- PostgreSQL
- Maven
- Lombok
- Swagger / OpenAPI


## 📋 Funcionalidades


- Cadastro e gerenciamento de usuários
- Autenticação de usuários
- Autorização utilizando JWT
- Gerenciamento de informações profissionais
- Gerenciamento de habilidades
- Persistência de dados utilizando PostgreSQL
- API REST para integração com o frontend
- Documentação da API utilizando Swagger/OpenAPI


## 🏗️ Estrutura do projeto


O projeto está organizado seguindo uma separação por responsabilidades, facilitando a manutenção e evolução da aplicação.


src/
└── main/
    ├── java/
    │   └── br.com.talen_hub/
    │       ├── config/
    │       ├── usuario/
    │       ├── jwt/
    │       └── ...
    │
    └── resources/
        └── application.properties
🔐 Variáveis de ambiente

As informações sensíveis da aplicação não devem ser armazenadas diretamente no código-fonte.

Configure as seguintes variáveis de ambiente:

DB_URL=jdbc:postgresql://localhost:5432/talent_hub
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha


JWT_SECRET=sua_chave_secreta
JWT_EXPIRATION=86400000


SERVER_PORT=8080

Importante: não envie arquivos .env ou credenciais reais para o GitHub.

⚙️ Configuração
1. Clone o repositório
git clone https://github.com/MadowRod/talent-hubBackend.git
2. Entre no diretório
cd talent-hubBackend
3. Configure o PostgreSQL

Crie um banco de dados chamado:

talent_hub

Depois configure as variáveis de ambiente necessárias.

4. Execute o projeto

Com Maven:

mvn spring-boot:run

Ou execute a classe principal:

TalenHubApplication

Por padrão, a aplicação será executada em:

http://localhost:8080
📚 Documentação da API

Após iniciar o backend, a documentação da API pode ser acessada através do Swagger:

http://localhost:8080/swagger-ui.html

A especificação OpenAPI também pode ser acessada em:

http://localhost:8080/v3/api-docs
🗄️ Banco de dados

O projeto utiliza PostgreSQL como banco de dados.

A comunicação com o banco é realizada através de:

Spring Data JPA
Hibernate
PostgreSQL JDBC Driver
🔒 Segurança

A autenticação da aplicação utiliza JWT (JSON Web Token).

Após a autenticação, o token é utilizado para validar o acesso aos endpoints protegidos da API.

As credenciais e chaves utilizadas pela aplicação devem ser configuradas através de variáveis de ambiente.

📦 Build

Para gerar o build da aplicação:

mvn clean package

O arquivo .jar será gerado no diretório:

target/

Para executar o .jar:

java -jar target/talen-hub-*.jar
👨‍💻 Desenvolvedor

Desenvolvido por MadowRod.

⭐ Projeto desenvolvido como parte da entrevista para a Neki.
