Autor: Guilherme Trujilo Higashi

📌 Descrição do Projeto

Este é um microsserviço RESTful para gerenciamento de pedidos. Ele permite criar, listar e atualizar pedidos, suportando paginação, filtragem e eventos Kafka para mensageria. O sistema também inclui logging estruturado e monitoramento via DataDog.

🚀 Tecnologias Utilizadas

Java

Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Kafka, Spring Actuator)

PostgreSQL 

Kafka 

DataDog 

Docker e Docker Compose

JUnit 5 e Mockito 

🛠️ Configuração e Execução

📌 1. Clonar o repositório

git clone https://github.com/seu-repositorio/order-microservice.git
cd order-microservice

📌 2. Configurar variáveis de ambiente

Crie um arquivo .env na raiz do projeto:

POSTGRES_USER=admin
POSTGRES_PASSWORD=admin
POSTGRES_DB=orders_db
KAFKA_BROKER=localhost:9092
DATADOG_API_KEY=sua_chave_api

📌 3. Subir os containers Docker

Certifique-se de que o Docker e Docker Compose estão instalados e rode:

docker-compose up -d

Isso iniciará PostgreSQL, Kafka e DataDog.

📌 4. Rodar a aplicação

Se estiver rodando localmente, use:

mvn spring-boot:run

Ou Aperte o botão Run no MicroserviceApplication 

Ou construa o JAR e execute:

mvn clean package
java -jar target/order-microservice.jar

A API estará disponível em: http://localhost:8081  (Poderá ser alterado no application.properties caso necessite)

📡 Endpoints da API

📌 Criar um Pedido

POST /orders

Corpo JSON:

{
  "customerName": "Guilherme Trujilo",
  "product": "Laptop",
  "quantity": 2
}

📌 Listar Pedidos (com paginação e filtro)

GET /orders?customerName=Guilherme&status=PENDING&page=0&size=10

📌 Buscar Pedido por ID

GET /orders/{id}

📌 Atualizar Status do Pedido

PATCH /orders/{id}/status?newStatus=CONFIRMED

📌 Health Check
GET /actuator/health

✅ Testes

Rodar testes unitários e de integração:

mvn test
