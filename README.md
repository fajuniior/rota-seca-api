# Rota Seca API — NovaCore (2ESPF)

API RESTful em Java/Spring Boot da **Rota Seca**, solução da Global Solution 2026.2 (CitizenTech) para quem mora em áreas de Guarulhos que alagam com frequência: alertas de chuva por bairro, ocorrências de alagamento confirmadas por vizinhos, pontos de apoio (espera coberta e abrigos), caminhos salvos e contatos de confiança.

> Protótipo acadêmico com dados simulados. Não substitui as orientações da Defesa Civil (199).

## Equipe

| Nome | RM |
|---|---|
| Arthur Albertini | 565954 |
| Fabio Pereira Rogério Júnior | 564005 |
| Gabriel dos Santos Cardoso | 562103 |
| Lucas Oliveira Santos | 563617 |
| Matheus Tonsa Martini | 564454 |

## Tecnologias

| Ferramenta | Versão |
|---|---|
| Java (JDK) | 21 |
| Spring Boot (spring-boot-starter-webmvc) | 4.1.1 |
| Driver Oracle JDBC (ojdbc17) | gerenciado pelo Spring Boot |
| Banco de dados | Oracle Database (servidor FIAP) |
| Build | Maven Wrapper (`mvnw`) |
| Deploy | Docker + Render |
| IDE usada | IntelliJ IDEA |

## Arquitetura (padrão visto em aula)

```
br.com.fiap.rotaseca
├── entities     classes de domínio (uma por tabela)
├── dao          acesso ao banco com JDBC puro (PreparedStatement)
├── bo           regras de negócio
├── controller   endpoints REST (@RestController) + CorsConfig
├── conexoes     ConexaoFactory (Oracle FIAP)
├── excecoes     exceções próprias + tratador global (@RestControllerAdvice)
└── dto          objetos de entrada/saída que não são tabelas
```

Fluxo de uma requisição: `Controller → BO (regras) → DAO (SQL) → Oracle`. Os erros sobem como exceções e o `TratadorExcecoes` devolve o status HTTP correto com uma mensagem em português.

## Como executar localmente

1. Clone o repositório e abra a pasta no IntelliJ.
2. Rode os scripts `01_DDL_RotaSeca.sql` e `02_DML_RotaSeca.sql` (entrega de Database Design) no seu usuário do Oracle FIAP.
3. Configure as variáveis de ambiente (no IntelliJ: *Run > Edit Configurations > Environment variables*):

   | Variável | Valor |
   |---|---|
   | `DB_USER` | seu usuário Oracle (ex.: `rm564005`) |
   | `DB_PASSWORD` | sua senha Oracle |
   | `DB_URL` | opcional; padrão `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl` |

   A senha **não fica no código** porque o repositório é público.
4. Execute `RotaSecaApplication` ou, no terminal: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).
5. Acesse `http://localhost:8080/status` — deve responder `{"api":"OK","banco":"OK"}`.

Para testar todos os endpoints, importe `docs/RotaSeca.postman_collection.json` no Postman e troque a variável `baseUrl` pelo endereço local ou do Render.

## Deploy no Render

1. *New > Web Service* > conectar este repositório do GitHub.
2. Runtime: **Docker** (o `Dockerfile` já está na raiz). Plano: Free.
3. Em *Environment*, criar `DB_USER` e `DB_PASSWORD`.
4. Após o deploy, abrir `https://<nome-do-servico>.onrender.com/status`.

No plano gratuito o serviço "dorme" depois de 15 minutos sem uso; a primeira requisição depois disso demora cerca de 1 minuto.

## Endpoints

Todas as tabelas têm CRUD completo (`GET` lista, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`):
`/bairros`, `/usuarios`, `/nos-via`, `/trechos-via`, `/ocorrencias`, `/confirmacoes`, `/leituras-chuva`, `/alertas`, `/pontos-apoio`, `/caminhos`, `/contatos`, `/notificacoes`.
No `POST`, o ID pode ser omitido: a API gera o próximo.

Endpoints de negócio:

| Método | Caminho | O que faz | História |
|---|---|---|---|
| GET | `/bairros/{id}/alerta-atual` | Alerta vigente do bairro | US01 |
| GET | `/bairros/{id}/alertas` | Histórico de alertas do bairro | US01 |
| POST | `/alertas` | Alerta do modelo (nível pela probabilidade) ou emitido por agente | US12 |
| GET | `/bairros/{id}/leituras-chuva` | Leituras de chuva do bairro | US17 |
| GET | `/usuarios/{id}/avisos` | Ocorrências ativas nos caminhos salvos do usuário | US02 |
| GET | `/usuarios/{id}/caminhos` | Caminhos salvos | US03 |
| GET | `/usuarios/{id}/contatos` | Contatos de confiança | US08 |
| GET/POST/PUT/DELETE | `/usuarios/{id}/bairros-acompanhados[/{idBairro}]` | Acompanhar bairro de familiar | US23 |
| POST | `/ocorrencias` | Morador reporta ocorrência com um toque (foto opcional) | US05 |
| GET | `/ocorrencias?ativas=true` | Só ocorrências não descartadas e não expiradas | US04 |
| POST | `/ocorrencias/{id}/confirmacoes` | Vizinho confirma a ocorrência | US06 |
| GET | `/ocorrencias/{id}/confirmacoes` | Confirmações da ocorrência | US06 |
| PUT | `/ocorrencias/{id}/validacao` | Agente confirma ou descarta `{"idAgente":5,"stValidacao":"CONFIRMADA"}` | US11 |
| GET | `/pontos-apoio?idBairro=1&aberto=S` | Pontos de apoio com filtros | US07 |
| PUT | `/pontos-apoio/{id}/status?aberto=S&idAgente=5` | Agente abre ou fecha o ponto | US13 |
| GET/POST/PUT/DELETE | `/caminhos/{id}/trechos[/{nrOrdem}]` | Trechos do caminho, em ordem | US03 |
| GET | `/` e `/status` | Informações da API e teste do banco | — |

## Regras de negócio implementadas

- **Ocorrência**: a API define a data de registro, o status `PENDENTE` e a validade de 3 horas (informação velha deixa de gerar aviso); a foto é opcional. Depois de validada pela Defesa Civil, não pode mais ser editada.
- **Confirmação**: o autor não confirma a própria ocorrência; não dá para confirmar ocorrência descartada ou expirada; cada morador confirma uma vez (`409`).
- **Validação**: só usuário com perfil `AGENTE` confirma/descarta ocorrência e abre/fecha ponto de apoio.
- **Alerta**: quando vem do modelo de Data Science sem nível, o nível sai da probabilidade de chuva intensa (`< 0,40` informativo, `< 0,75` atenção, senão grave). Alerta de origem `AGENTE` exige um agente válido.
- **Notificação**: exatamente uma origem (alerta **ou** ocorrência) e um destinatário (usuário **ou** contato).
- Horário sempre de Brasília (`America/Sao_Paulo`), mesmo com o servidor do Render em UTC.

## Tratamento de exceções

| Situação | Status |
|---|---|
| Registro não encontrado | 404 |
| Regra de negócio, campo obrigatório, valor fora do domínio, JSON inválido, ID não numérico | 400 |
| ID referenciado não existe (FK) | 400 |
| Registro duplicado (UNIQUE) ou exclusão de registro com dependentes | 409 |
| Banco fora do ar ou variáveis não configuradas | 503 |

Formato do erro:

```json
{"dataHora":"2026-11-09T12:10:04","status":404,"erro":"Not Found","mensagem":"Bairro 99 não encontrado","caminho":"/bairros/99"}
```

## Testes

`docs/resultado_testes.txt` traz a execução de 72 cenários (CRUD de todas as tabelas, regras de negócio e erros), todos aprovados.

Os dados de exemplo do banco são de 16/02/2026, então as ocorrências antigas já estão expiradas: para testar confirmação e avisos, crie antes uma ocorrência nova com `POST /ocorrencias`.
