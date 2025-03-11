# Projeto Spring - Sistema de Gerenciamento de Usuários e Cidadãos

## Descrição
Este projeto implementa um sistema de gerenciamento de **Usuários** e **Cidadãos** com funcionalidades de login, bloqueio/desbloqueio, tentativa de falhas e troca de senha. O sistema utiliza Spring Boot e JPA para persistência de dados em um banco de dados relacional. Ele foi projetado para garantir que os usuários sejam autenticados e suas senhas sejam mantidas seguras, incluindo funcionalidades como bloqueio após múltiplas tentativas de login falhadas.

## Funcionalidades

### 1. **Cadastro de Usuários**
   - É possível cadastrar um **usuário** no sistema, associando-o a um **Cidadão**.
   - O sistema verifica se o **Cidadão** existe no banco de dados antes de permitir a criação do usuário. Além disso, não permite o cadastro de mais de um usuário para o mesmo **Cidadão**.

### 2. **Login**
   - O login do usuário é verificado, com validação de senha e bloqueio após 3 tentativas falhas.
   - Se o usuário não fizer login dentro de 30 dias, será solicitado que ele troque sua senha.

### 3. **Bloqueio e Desbloqueio de Usuário**
   - Após 3 tentativas falhas de login, o usuário é bloqueado automaticamente.
   - Apenas administradores (com a chave de acesso correta) podem desbloquear usuários.

### 4. **Troca de Senha**
   - O usuário pode alterar sua senha, sendo necessário informar a senha atual.
   - Se a senha atual estiver incorreta, a troca de senha não é permitida.

### 5. **Listagem e Edição de Usuários**
   - É possível listar todos os usuários registrados no sistema.
   - Usuários podem ser atualizados ou deletados com base no ID.

### 6. **Associação entre Usuário e Cidadão**
   - Cada **usuário** está associado a um **Cidadão** (uma entidade com dados como nome, endereço e bairro).
   - A relação é feita via um relacionamento de **OneToOne** entre as entidades **UsuarioModel** e **CidadaoModel**.

---

## Endpoints da API

### 1. **Listar Usuários**
   - **Método:** `GET`
   - **Endpoint:** `/user`
   - **Descrição:** Lista todos os usuários cadastrados no sistema.
   - **Resposta:** Retorna uma lista de todos os usuários.
   - **Exemplo de `curl`:**
     ```bash
     curl -X GET http://localhost:8080/user
     ```

### 2. **Buscar Usuário por ID**
   - **Método:** `GET`
   - **Endpoint:** `/user/{id}`
   - **Descrição:** Busca um usuário pelo seu ID.
   - **Resposta:** Retorna os dados do usuário se encontrado ou mensagem de erro se não encontrado.
   - **Exemplo de `curl`:**
     ```bash
     curl -X GET http://localhost:8080/user/1
     ```

### 3. **Criar Novo Usuário**
   - **Método:** `POST`
   - **Endpoint:** `/user`
   - **Descrição:** Cria um novo usuário no sistema.
   - **Requisitos:** O usuário deve estar associado a um cidadão existente.
   - **Exemplo de corpo de requisição (JSON):**
     ```json
     {
       "username": "novo_usuario",
       "senha": "senha123",
       "cidadao": {
         "id": 1
       }
     }
     ```
   - **Exemplo de `curl`:**
     ```bash
     curl -X POST http://localhost:8080/user \
     -H "Content-Type: application/json" \
     -d '{"username": "novo_usuario", "senha": "senha123", "cidadao": {"id": 1}}'
     ```

### 4. **Atualizar Usuário**
   - **Método:** `PUT`
   - **Endpoint:** `/user/{id}`
   - **Descrição:** Atualiza os dados de um usuário existente.
   - **Exemplo de corpo de requisição (JSON):**
     ```json
     {
       "username": "usuario_atualizado",
       "senha": "senha456"
     }
     ```
   - **Exemplo de `curl`:**
     ```bash
     curl -X PUT http://localhost:8080/user/1 \
     -H "Content-Type: application/json" \
     -d '{"username": "usuario_atualizado", "senha": "senha456"}'
     ```

### 5. **Deletar Usuário**
   - **Método:** `DELETE`
   - **Endpoint:** `/user/{id}`
   - **Descrição:** Deleta um usuário pelo seu ID.
   - **Exemplo de `curl`:**
     ```bash
     curl -X DELETE http://localhost:8080/user/1
     ```

### 6. **Login**
   - **Método:** `POST`
   - **Endpoint:** `/user/login`
   - **Descrição:** Realiza o login do usuário.
   - **Requisitos:** O usuário deve fornecer o ID e a senha.
   - **Exemplo de corpo de requisição (JSON):**
     ```json
     {
       "id": 1,
       "senha": "senha123"
     }
     ```
   - **Exemplo de `curl`:**
     ```bash
     curl -X POST http://localhost:8080/user/login \
     -H "Content-Type: application/json" \
     -d '{"id": 1, "senha": "senha123"}'
     ```

### 7. **Trocar Senha**
   - **Método:** `PUT`
   - **Endpoint:** `/user/{id}/senha`
   - **Descrição:** Altera a senha de um usuário existente.
   - **Requisitos:** O usuário deve fornecer a senha atual e a nova senha.
   - **Exemplo de corpo de requisição (JSON):**
     ```json
     {
       "senha_atual": "senha123",
       "senha_nova": "nova_senha456"
     }
     ```
   - **Exemplo de `curl`:**
     ```bash
     curl -X PUT http://localhost:8080/user/1/senha \
     -H "Content-Type: application/json" \
     -d '{"senha_atual": "senha123", "senha_nova": "nova_senha456"}'
     ```

### 8. **Desbloquear Usuário**
   - **Método:** `POST`
   - **Endpoint:** `/user/desbloquear/{id}`
   - **Descrição:** Desbloqueia um usuário (somente administradores podem realizar esta ação).
   - **Requisitos:** O administrador deve fornecer uma chave de acesso (`admin`).
   - **Exemplo de corpo de requisição (JSON):**
     ```json
     {
       "chave": "admin"
     }
     ```
   - **Exemplo de `curl`:**
     ```bash
     curl -X POST http://localhost:8080/user/desbloquear/1 \
     -H "Content-Type: application/json" \
     -d '{"chave": "admin"}'
     ```

---



## Tecnologias Utilizadas
- **Spring Boot:** Framework principal para o desenvolvimento da API RESTful.
- **JPA (Jakarta Persistence API):** Utilizado para mapear as entidades Java para o banco de dados relacional.
- **MySQL/PostgreSQL (ou outro DB relacional):** Sistema de gerenciamento de banco de dados relacional para persistência de dados.

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 17 ou superior.
- Maven para gerenciamento de dependências.
- Banco de dados MySQL ou PostgreSQL configurado e rodando.

### Passos
1. Clone o repositório do projeto:
   ```bash
   git clone https://github.com/seu_usuario/seu_repositorio.git
   ```
   
2. Navegue até o diretório do projeto:
   ```bash
   cd seu_repositorio
   ```

3. Compile e rode o projeto:
   ```bash
   mvn spring-boot:run
   ```

4. A API estará disponível em `http://localhost:8080`.

---

## Conclusão
Este sistema permite a gestão de usuários e cidadãos de forma eficiente, com segurança nas operações de login, bloqueio e alteração de senha, além de garantir que a associação entre usuários e cidadãos seja corretamente validada.