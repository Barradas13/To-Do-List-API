# 📋 API To-Do List com Java & Spring Boot

Este projeto é um exemplo prático de como criar uma API RESTful para gerenciamento de tarefas (To-Do List) utilizando Java com o framework **Spring Boot**.


## Iniciar o Projeto

### 1. Criar o Projeto com Spring Boot Initializr

Você pode iniciar o projeto usando a extensão do Spring Boot no VS Code ou diretamente pelo site [start.spring.io](https://start.spring.io/).

**Via VS Code:**

- Pressione `Ctrl + Shift + P`
- Pesquise por: `Spring Initializr: Create a Maven Project`
- Configure o projeto com as opções desejadas (grupo, artefato, nome etc.)

### 2. Adicionar Dependências

Selecione as seguintes dependências:

- **Spring Web** (para criação da API REST)
- **Spring Data JPA** (para persistência no banco de dados)
- **Spring Boot DevTools** (para hot reload durante o desenvolvimento)
- **MySQL Driver** (driver para conexão com banco MySQL)
- **Lombok** (para reduzir boilerplate com getters/setters, constructors etc.)
- **Validation** (para validações com anotações, como `@NotNull`, `@Size`, etc.)

---

## Estrutura de Pacotes

Crie os pacotes controller, model e service para estruturar seu projeto com o padrão que você preferir

## Configurando o banco de dados

Em resources.application.properties configure seu banco de dados MySQL com as informações dele por exemplo username, password...