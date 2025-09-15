# Teste Técnico — Backend Java (Jr forte → Pleno)
Sem decoreba. Aqui você modela, constrói e integra **duas aplicações** que conversam via **API REST**, usando **PostgreSQL** e migrações.

## 👀 O que você vai construir
Dois serviços:
- **inventory-service** — produtos e estoque
- **order-service** — pedidos (reserva/devolução de estoque)

## ✅ Entregáveis (ordem de leitura)
1. **Plano de ação** (máx. 1 página): objetivo, decisões-chave, riscos/mitigações, ordem de implementação (MVP → incrementos).
2. **Modelagem**: diagrama ER + **DDL** (PK, FK, UNIQUE, índices).
3. **Contratos de API**: rotas, payloads e status (OpenAPI/Swagger ou Markdown).
4. **Código**: Java 17 + Spring Boot 3.2.x (ou superior), camadas, validações, integração REST (timeout + retry/backoff).
5. **Testes mínimos**: 1 repo, 1 controller (ok + 422) e 1 integração simulando a outra API.
6. **README do projeto**: como subir, variáveis, exemplos `curl`/Postman e o que ficou “para depois”.

## 🧩 Regras do domínio (resumo)
### inventory-service
**Product**: `id (UUID)`, `sku (unique)`, `name`, `price (numeric)`, `stock (int)`, `created_at`

Endpoints:
- `POST /products`
- `GET /products?search=&page=&size=`
- `GET /products/{id}`
- `PATCH /products/{id}/stock`
- `GET /health`

### order-service
**Order**: `id (UUID)`, `status (CREATED|PAID|CANCELLED)`, `total`, `created_at`  
**OrderItem**: `order_id`, `product_id`, `qty`, `unit_price`

Fluxo `POST /orders`:
1) valida itens; 2) consulta produto no **inventory** e **reserva estoque**;  
3) se faltar estoque em qualquer item → **422** com detalhes e **nenhuma reserva pendurada**;  
4) persiste pedido + itens **em transação**; `total = Σ(qty * unit_price)` com **preço atual**.

Endpoints:
- `POST /orders`
- `GET /orders/{id}`
- `GET /orders?status=&page=&size=`
- `POST /orders/{id}/cancel` (devolve estoque)

## 🧪 Casos de teste (aceite) — use depois de subir tudo
```bash
# Criar produtos
curl -s -X POST localhost:8081/products \
 -H 'Content-Type: application/json' \
 -d '{"sku":"ABC-001","name":"Teclado","price":199.90,"stock":5}'

curl -s -X POST localhost:8081/products \
 -H 'Content-Type: application/json' \
 -d '{"sku":"ABC-002","name":"Mouse","price":99.90,"stock":1}'

# Listar com paginação/filtro
curl -s 'localhost:8081/products?search=tec&page=0&size=10'

# Criar pedido ok (2 itens)
curl -s -X POST localhost:8082/orders \
 -H 'Content-Type: application/json' \
 -d '{
  "items":[
    {"productId":"<UUID-TECLADO>","qty":2},
    {"productId":"<UUID-MOUSE>","qty":1}
  ]}'

# Criar pedido com estoque insuficiente (espera 422 e detalhes)
curl -i -X POST localhost:8082/orders \
 -H 'Content-Type: application/json' \
 -d '{
  "items":[{"productId":"<UUID-MOUSE>","qty":3}]
 }'

# Cancelar pedido (devolve estoque)
curl -i -X POST localhost:8082/orders/<ORDER_ID>/cancel

# (Bônus) Idempotência de criação
curl -i -X POST localhost:8082/orders \
 -H 'Content-Type: application/json' \
 -H 'Idempotency-Key: 123abc' \
 -d '{ "items":[{"productId":"<UUID-TECLADO>","qty":1}] }'
curl -i -X POST localhost:8082/orders \
 -H 'Content-Type: application/json' \
 -H 'Idempotency-Key: 123abc' \
 -d '{ "items":[{"productId":"<UUID-TECLADO>","qty":1}] }'
```

Resposta 422 sugerida:
```json
{
  "title": "Insufficient stock",
  "details": [
    {"productId":"<UUID>","requested":3,"available":1}
  ]
}
```

## 🧮 Critérios (100 pts)
- **Modelagem de dados** (25): DDL limpa, FKs/UNIQUE, índices úteis.
- **Contratos & REST** (20): verbos/status, paginação, erros claros.
- **Integração service→service** (20): timeout + retry/backoff, sem reserva “fantasma”.
- **Qualidade** (15): testes úteis, camadas, validações.
- **Operabilidade** (10): sobe fácil, migrações automáticas, README executável.
- **Bônus Pleno** (10): Idempotency-Key, observabilidade (logs/metrics/trace), circuit breaker, auth simples entre serviços.

---

# Ambiente local (Docker)
Este repositório traz **PostgreSQL + pgAdmin** prontos. Seus serviços irão rodar nas portas **8081** (inventory) e **8082** (order).

## 1) Pré-requisitos
- Docker / Docker Compose
- Java 17, Maven/Gradle
- (Opcional) Postman

## 2) Subir banco e pgAdmin
```bash
docker compose up -d
# aguarde os healthchecks ficarem "healthy"
```

- Postgres: `localhost:5432` (user `admin`, pass `admin`)
- pgAdmin: `http://localhost:5050` (login `admin@local`, senha `admin`)

O compose cria **duas bases** automaticamente: `inventory_db` e `order_db`.

## 3) Configurar seus serviços
Use as variáveis a seguir (ou copie `.env.example` para `.env`):

**inventory-service**
```
DB_URL=jdbc:postgresql://localhost:5432/inventory_db
DB_USER=admin
DB_PASS=admin
```

**order-service**
```
DB_URL=jdbc:postgresql://localhost:5432/order_db
DB_USER=admin
DB_PASS=admin
```

Portas sugeridas (override no `application.yml`):
- inventory-service → `8081`
- order-service → `8082`

## 4) Migrações
- Use **Flyway** ou **Liquibase**. Ao subir o app, as migrações devem criar as tabelas/índices.
- A pasta `initdb` só cria os **bancos**; o **schema é sua responsabilidade** (faz parte da avaliação).

## 5) Estrutura sugerida do repo
```
.
├── inventory-service/        # app 1
├── order-service/            # app 2
├── docker-compose.yml        # postgres + pgadmin
├── initdb/
│   └── 01-create-databases.sql
├── .env.example
└── README.md                 # este arquivo
```

## 6) FAQ rápido
- **Posso usar libs?** Pode. Explique no README.
- **Precisa autenticação?** Não é obrigatório. Vale bônus se bem-feita (token simples/JWT).
- **Não finalizei tudo.** Prefira um **MVP rodando + README honesto** ao invés de metade feita em cada lugar.

Boa sorte e bom código. Sem mistério, sem pegadinha.
