# 🏦 Trade Arena - API

API desenvolvida para o desafio técnico da **Trade Arena**, com foco em boas práticas de **arquitetura**, **testes** e **operabilidade**.

[![Java](https://img.shields.io/badge/Java-17-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)]()
[![Postgres](https://img.shields.io/badge/PostgreSQL-15-blue)]()
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)]()
---

## * IMPORTANTE *
   PARA QUE SEJA GERADO O CODIGO DE AUTENTICAÇÂO, TEM QUE CHAMAR A ROTA DENTRO DA API DE ORDERS QUE GERA UM TOKEN A PARTIR DO SISTEMA MESMO, SEM FAZER LOGIN.
   E A PARTIR DESTE TOKEN, UTILIZAR EM TODA A API
  - ```bash 
         curl -X GET "http://localhost:8081/auth/generate" \
              -H "Accept: application/json"
      ```
---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3**
- **PostgreSQL** (via Docker)
- **Flyway** (migrações automáticas)
- **Swagger/OpenAPI** (documentação interativa)
- **Docker Compose** (subir ambiente completo)

---

## 📦 Como rodar o projeto

### 1. Clonar repositório
```bash
git clone https://github.com/FelipeFernandes777/Teste_Tecnico_TradeArena.git
cd Teste_Tecnico_TradeArena

```
2. Suba os containers
```bash
   docker compose up --build
```
Isso irá subir:

PostgreSQL na porta 5432

inventory-service em http://localhost:8080/products

order-service em http://localhost:8081

As credenciais e nomes de banco já estão configurados no docker-compose.yml.

---
3. Documentação da API
   -   Inventory Service - http://localhost:8080/swagger-ui/index.html
   -   Order Serivce - http://localhost:8081/swagger-ui/index.html
---


4. Variaveis de ambiente obrigatorias:
   - DATABASE_URL= { URL DE CONEXÃO  (ex: jdbc:postgresql://localhost5432/trade_arena)}
   - DATABASE_USERNAME= { USUARIO DO BANCO DE DADOS (ex: postgres)}
   - DATABASE_PASSWORD= { SENHA DO BANCO DE DADOS (ex: admin)}
   - SECRET_KEY= { CHAVE JWT (ex: e94d3628ad2dac8e6b25261ec6c53d56)}

## 📑 Endpoints Principais
### <b>Inventory Service (8080) </b>

POST /products → cria produto

GET /products?search=&page=&size= → lista produtos (filtro por nome/sku)

GET /products/{id} → detalhe de produto

PATCH /products/{id}/stock → ajusta estoque (positivo ou negativo)

GET /health → checagem de saúde

### <b>Order Service (8081)</b>

POST /orders → cria pedido (reserva estoque via inventory)

GET /orders/{id} → detalhe do pedido

GET /orders?status=&page=&size= → lista pedidos

POST /orders/{id}/cancel → cancela pedido e devolve estoque

---

## 🧪 Exemplos com curl

1. Criar um produto:
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU123",
    "name": "Teclado Gamer",
    "price": 199.90,
    "stock": 10
  }'
```

2. Listar Produtos
```bash
curl "http://localhost:8080/products?page=0&size=5"
```
3. Criar Pedido
```bash
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      { "productId": "UUID_DO_PRODUTO", "qty": 2 },
      { "productId": "UUID_DO_PRODUTO", "qty": 5 }
    ]
  }'
```
Cancelar Pedido
```bash
curl -X POST http://localhost:8081/orders/{id}/cancel

```
---
## ✅ Checklist
- [x] Sobe com **1 comando** (`docker compose up`)
- [x] **Migrações automáticas** (Flyway)
- [x] **Documentação interativa** (Swagger)
- [x] **Testes automatizados**
- [x] **README executável**
---
## 👨‍💻 Autor

Felipe Fernandes de Carvalho

GitHub: [linkedin.com/in/felipe-fernandes-ab7a3622a/](https://github.com/FelipeFernandes777)
Linkedin : [linkedin.com/in/felipe-fernandes](linkedin.com/in/felipe-fernandes-ab7a3622a/)




