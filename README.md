# Projeto Agente

API REST para gerenciamento de pessoas construída com Spring Boot 3.3 e Java 21. O serviço expõe operações CRUD no padrão `/api/v1/pessoas`, com validação de entrada, mapeamento DTO e camada de serviço isolada.

## Visão Geral
- **Stack principal:** Spring Boot, Spring Web, Spring Data JPA, Bean Validation, MySQL (runtime) e H2 (testes).
- **Principais recursos:** cadastro completo de pessoas, enumeração de gênero, listagem completa dos registros, tratamento de erros consistente via `ApiExceptionHandler`, mapeamento DTO com separação de domínios.
- **Motivações:** servir como base para agentes ou serviços que precisam consumir informações de pessoas com contratos estáveis e documentação clara.

## Requisitos
- Java 21+ configurado em `JAVA_HOME`.
- Maven 3.9+ (ou use o Maven Wrapper incluso).
- MySQL 8+ acessível (padrão `localhost:3306`) ou container via Docker Compose.

## Inicialização Rápida
```bash
git clone https://github.com/<seu-usuario>/projeto-agente.git
cd projeto-agente
./mvnw spring-boot:run
```
Ao finalizar o build, a API estará disponível em `http://localhost:8080/api/v1/pessoas`.

### Via Docker Compose
```bash
docker compose up --build -d
```
O serviço sobe com MySQL provisionado automaticamente (usuario `projeto`, senha `projeto`). Para encerrar e limpar volumes:
```bash
docker compose down -v
```

## Configuração
Os valores padrão residem em `src/main/resources/application.yml`. Para sobrepor em produção, defina variáveis de ambiente ou use um arquivo `application-<profile>.yml`.

Variáveis mais comuns:
- `SPRING_DATASOURCE_URL` — string JDBC alvo; padrão `jdbc:mysql://localhost:3306/projeto_agente`.
- `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`.
- `SERVER_PORT` — altera a porta HTTP (default 8080).

O profile `test` usa H2 em memória (`application-test.yml`) e é carregado automaticamente durante a execução da suíte de testes.

## Estrutura do Projeto
```
src/main/java/com/projeto/agente
├── ProjetoAgenteApplication.java        # entrypoint Spring Boot
├── api/                                 # controllers, DTOs, mappers e handlers de erro
├── domain/                              # modelos de domínio e enums
├── repository/                          # interfaces Spring Data JPA
└── service/                             # regras de negócio e exceções
src/main/resources/                      # arquivos YAML, mapeamentos e templates
src/test/java/                           # testes unitários e de integração (MockMvc)
src/test/resources/                      # configurações de teste (H2)
```

## Rotas
| Método | Endpoint                 | Descrição                              |
|--------|--------------------------|----------------------------------------|
| POST   | `/api/v1/pessoas`        | Cria uma nova pessoa e retorna o DTO   |
| GET    | `/api/v1/pessoas`        | Lista todas as pessoas cadastradas     |
| GET    | `/api/v1/pessoas/{id}`   | Busca uma pessoa específica por ID     |
| PUT    | `/api/v1/pessoas/{id}`   | Atualiza dados completos da pessoa     |
| DELETE | `/api/v1/pessoas/{id}`   | Remove a pessoa e retorna `204 No Content` |

### Exemplo de Payload
```json
{
  "nome": "Maria Silva",
  "email": "maria.silva@example.com",
  "dataNascimento": "1990-05-12",
  "genero": "FEMININO"
}
```
Campos inválidos geram respostas estruturadas em `ApiError`, contendo mensagem, timestamp e lista de `FieldErrorDetail`.

## Testes & Qualidade
- Rode toda a suíte com:
  ```bash
  ./mvnw test
  ```
- Os testes utilizam JUnit 5, Spring Boot Test, MockMvc e banco H2 em memória.
- Para investigar logs de execução, habilite `DEBUG` via `-Dlogging.level.com.projeto.agente=DEBUG`.

## Fluxo de Desenvolvimento
1. Crie uma branch feature a partir de `main`.
2. Rode `./mvnw clean verify` para garantir build e testes antes do PR.
3. Utilize Conventional Commits (`feat:`, `fix:`, `chore:` etc.) e descreva mudanças no PR, listando comandos executados, recursos afetados e issues relacionadas.

## Próximos Passos
- Extensões podem incluir paginação, filtros dinâmicos e autenticação.
- Consulte `AGENTS.md` para diretrizes adicionais de contribuição e organização do código.
