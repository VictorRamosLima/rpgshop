# Analise de Aderencia de Requisitos

**Data da analise:** 2026-02-15
**Fonte de requisitos:** `rpg_requirements.json`
**Escopo avaliado:** Backend (`src/main/java`), Templates (`src/main/resources/templates`), Configuracoes (`application.properties`)

---

## Criterio de classificacao

| Classificacao | Definicao |
|---|---|
| **Contemplado** | Implementacao existe e cobre o fluxo principal esperado pelo requisito. |
| **Parcial** | Requisito implementado em parte, com lacuna funcional ou de comportamento. |
| **Nao contemplado** | Nao ha implementacao efetiva do requisito. |
| **Errado** | Existe implementacao, mas com comportamento incorreto no fluxo. |

---

## Resumo geral

| Tipo | Total | Contemplado | Parcial | Nao contemplado | Errado |
|---|---:|---:|---:|---:|---:|
| Funcionais (RF) | 33 | 33 | 0 | 0 | 0 |
| Nao funcionais (RNF) | 11 | 4 | 3 | 4 | 0 |
| Regras de negocio (RN) | 36 | 36 | 0 | 0 | 0 |
| **Total** | **80** | **73** | **3** | **4** | **0** |

**Todos os 73 requisitos contemplados foram verificados no codigo e estao corretos. Abaixo estao detalhados apenas os 7 requisitos com pendencias.**

---

## Requisitos parcialmente implementados (3)

### RNF0012 — Log de transacao

**Requisito:** "Para toda operacao de escrita (Insercao ou Alteracao) deve ser registrado data, hora, usuario responsavel, alem de manter os dados alterados."

**O que existe:**
- A entidade `TransactionLog` esta definida no dominio com campos `entityName`, `entityId`, `operation`, `responsibleUser`, `previousData`, `newData` e `timestamp`.
- O caso de uso `CreateTransactionLogUseCase` esta implementado e funcional, validando campos obrigatorios.
- O caso de uso `QueryTransactionLogsUseCase` permite consultar os logs.
- A tela `log/list.html` exibe o historico de logs.

**O que falta:**
- `CreateTransactionLogUseCase` nao e injetado em nenhum outro caso de uso da aplicacao. Ele so e referenciado no proprio arquivo de definicao e no seu teste unitario.
- Nenhuma operacao de escrita (criacao de produto, cadastro de cliente, entrada de estoque, criacao de pedido, etc.) chama automaticamente o log de transacao.
- Para atender o requisito, seria necessario integrar o log de forma transversal (via AOP, interceptors ou chamada direta) em todos os casos de uso de escrita.

**Evidencias:**
- `src/main/java/rpgshop/application/usecase/log/CreateTransactionLogUseCase.java` — caso de uso existe
- Busca por `CreateTransactionLogUseCase` no projeto retorna apenas o proprio arquivo e seu teste — nenhuma integracao

---

### RNF0034 — Alteracao apenas de enderecos

**Requisito:** "Enderecos de entrega ou cobranca podem ser alterados/adicionados sem editar outros dados."

**O que existe:**
- `CreateAddressUseCase` permite adicionar novos enderecos de forma isolada, sem alterar outros dados do cliente.
- O controller de clientes (`CustomerController`) expoe endpoint `POST /customers/{id}/address` para adicao de endereco separada do formulario de edicao do cliente.

**O que falta:**
- Nao existe `UpdateAddressUseCase` ou fluxo equivalente para editar um endereco ja existente de forma isolada.
- O requisito diz "alterados/adicionados" — a parte de adicao esta coberta, mas a alteracao de endereco existente nao.
- Nao ha formulario de edicao de endereco individual nos templates.

**Evidencias:**
- `src/main/java/rpgshop/application/usecase/customer/CreateAddressUseCase.java` — adicao existe
- Busca por `UpdateAddressUseCase` no projeto — nao existe

---

### RNF0042 — Apresentar itens retirados do carrinho

**Requisito:** "Listar produtos removidos por expiracao; desabilitar compra; itens devem ser adicionados novamente."

**O que existe:**
- `ReleaseExpiredCartItemsUseCase` remove itens expirados do carrinho (backend funcional).
- `ReleaseExpiredCartItemsScheduler` executa a liberacao automaticamente a cada minuto via `@Scheduled(cron)`, controlado pela propriedade `rpgshop.cart.blocking.auto-release-enabled=true`.
- O template `cart/view.html` exibe badges de "Bloqueado" e "Disponivel" por item.

**O que falta:**
- O template de visualizacao do carrinho nao exibe uma lista dos itens que foram removidos por expiracao.
- Nao ha informacao de quando o bloqueio expira (`expiresAt` nao e mostrado ao usuario).
- Nao ha mecanismo para desabilitar o botao de compra quando existem itens expirados pendentes de remocao.
- O usuario nao recebe feedback visual de que itens foram removidos por timeout — eles simplesmente desaparecem.

**Evidencias:**
- `src/main/resources/templates/cart/view.html:34-35` — exibe apenas badges Bloqueado/Disponivel, sem data de expiracao
- `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java` — remove itens mas nao retorna lista para exibicao ao usuario

---

## Requisitos nao implementados (4)

### RNF0011 — Tempo de resposta para consultas

**Requisito:** "Toda consulta de usuario deve ter resposta em no maximo 1 segundo."

**Por que nao esta implementado:**
- Nao existe nenhum mecanismo de monitoramento de tempo de resposta na aplicacao.
- Nao ha interceptors, filtros, aspectos AOP ou anotacoes `@Timed` para medir latencia de requisicoes.
- Nao ha integracao com ferramentas de observabilidade (Micrometer, Spring Actuator metrics, etc.).
- Sem medicao, nao e possivel comprovar ou garantir o SLO de 1 segundo.

**Para implementar seria necessario:**
- Adicionar interceptor ou filtro que registre tempo de resposta de cada requisicao.
- Opcionalmente integrar com Micrometer/Actuator para exposicao de metricas.
- Definir alertas ou logs para violacoes do limite de 1 segundo.

---

### RNF0013 — Cadastro de dominios (script de implantacao)

**Requisito:** "Deve existir um script de implantacao inserindo registros de tabelas de dominio (grupo de precificacao, fabricante/fornecedor, tipos de item etc.)."

**Por que nao esta implementado:**
- Nao existem scripts Flyway de migracao (`db/migration/*.sql`).
- Nao existe arquivo `data.sql` ou `import.sql` na pasta `resources`.
- Nao ha `CommandLineRunner` ou `ApplicationRunner` para carga inicial de dados.
- O Hibernate esta configurado com `ddl-auto=update`, o que cria as tabelas automaticamente, mas nao insere dados de dominio.
- Tabelas como grupo de precificacao, tipos de produto, fornecedores, categorias e bandeiras de cartao precisam de registros iniciais para a aplicacao funcionar, mas nao ha script para isso.

**Para implementar seria necessario:**
- Criar migrations Flyway com `INSERT INTO` para cada tabela de dominio.
- Ou criar um `CommandLineRunner` que verifique e insira dados iniciais na inicializacao.

---

### RNF0033 — Senha criptografada

**Requisito:** "A senha deve ser criptografada."

**Por que nao esta implementado:**
- A senha e armazenada em texto puro no banco de dados.
- `CreateCustomerUseCase` salva a senha diretamente sem nenhuma transformacao: o campo `password` do `Customer` recebe o valor do `CreateCustomerCommand.password()` sem hash.
- `ChangePasswordUseCase` atualiza a senha via `customerGateway.updatePassword(command.customerId(), command.newPassword())` — o parametro e nomeado `encodedPassword` na interface do gateway, mas o valor nao e codificado em nenhum momento.
- Nao ha nenhuma dependencia de `BCryptPasswordEncoder`, `PasswordEncoder`, `MessageDigest` ou qualquer biblioteca de criptografia no projeto.
- Isso representa uma vulnerabilidade de seguranca: qualquer acesso ao banco de dados expoe todas as senhas dos clientes.

**Para implementar seria necessario:**
- Adicionar dependencia do Spring Security (ou BCrypt standalone).
- Criar um `PasswordEncoder` bean.
- Aplicar hash na senha antes de salvar em `CreateCustomerUseCase` e `ChangePasswordUseCase`.
- Comparar senhas usando o encoder (nao comparacao direta de strings).

**Evidencias:**
- `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java` — salva senha sem hash
- `src/main/java/rpgshop/application/usecase/customer/ChangePasswordUseCase.java:42` — `customerGateway.updatePassword(command.customerId(), command.newPassword())` sem encoding

---

### RNF0043 — Grafico de linhas

**Requisito:** "O sistema deve apresentar o historico de vendas em um grafico de linhas."

**Por que nao esta implementado:**
- Todas as telas de analise (`sales.html`, `by-product.html`, `by-category.html`) exibem dados exclusivamente em tabelas HTML.
- A tela `dashboard.html` contem apenas links de navegacao para as demais paginas de analise.
- Nao ha nenhuma biblioteca de graficos integrada (Chart.js, D3.js, ApexCharts, etc.).
- Nao ha elementos `<canvas>`, `<svg>` ou containers de graficos nos templates.
- O backend (`SalesAnalysisUseCase`) retorna dados tabulares que precisariam ser adaptados para alimentar um grafico temporal.

**Para implementar seria necessario:**
- Adicionar uma biblioteca de graficos JavaScript (ex: Chart.js via CDN).
- Criar um endpoint que retorne dados agregados por periodo (ex: vendas por dia/semana/mes).
- Renderizar o grafico de linhas com eixo X temporal e eixo Y de valor/quantidade.

**Evidencias:**
- `src/main/resources/templates/analysis/sales.html` — apenas `<table>`
- `src/main/resources/templates/analysis/by-product.html` — apenas `<table>`
- `src/main/resources/templates/analysis/by-category.html` — apenas `<table>`
- `src/main/resources/templates/analysis/dashboard.html` — apenas navegacao

---

## Notas sobre a analise anterior (requirements-analysis.md)

A analise anterior (`requirements-analysis.md`, datada de 2026-02-10) reportou 61 contemplados, 9 parciais, 8 nao contemplados e 2 errados. Esta nova analise, apos verificacao direta no codigo fonte, corrige os seguintes pontos:

| ID | Status anterior | Status corrigido | Justificativa da correcao |
|---|---|---|---|
| RF0012 | Errado | Contemplado | O template `product/detail.html:204` usa `sc.createdAt()`, que e o campo correto do record `StatusChange`. Nao ha referencia a `sc.changedAt()`. |
| RF0016 | Errado | Contemplado | Mesmo caso do RF0012 — template correto. |
| RF0015 | Parcial | Contemplado | `ProductMapper.toEntity()` linha 73 sincroniza `isActive` via `domain.isActive()`. O campo JPA `isActive` e atualizado toda vez que o produto e salvo (ativacao/inativacao), mantendo consistencia com o filtro do repositorio. |
| RF0027 | Parcial | Contemplado | O template `customer/detail.html:266` usa `card.cardBrand()`, que e o campo correto do record `CreditCard` (linha 13). Nao ha referencia a `card.brand()`. |
| RF0035 | Parcial | Contemplado | O checkout implementa `saveDetached()` para enderecos que nao devem ir ao perfil (salvos com `customer=null` e `isActive=false`). Enderecos salvos ao perfil usam `save()` com vinculo ao cliente. O fluxo atende "selecionar existente ou cadastrar novo e opcionalmente adiciona-lo ao perfil". |
| RF0037 | Parcial | Contemplado | `CreateOrderUseCase` cria o pedido com `OrderStatus.PROCESSING` (linha 116) e retorna. A aprovacao/rejeicao ocorre separadamente via `ApproveOrderUseCase`/`RejectOrderUseCase`, acionados pelo administrador em endpoints distintos (`POST /{id}/approve`, `POST /{id}/reject`). O pedido permanece em PROCESSING ate acao administrativa. |
| RN0021 | Nao contemplado | Contemplado | `CreateCustomerUseCase` linhas 80 e 83-84 cria endereco de cobranca (`AddressPurpose.BILLING`) e valida sua existencia no cadastro. |
| RN0022 | Nao contemplado | Contemplado | `CreateCustomerUseCase` linhas 81 e 86-87 cria endereco de entrega (`AddressPurpose.DELIVERY`) e valida sua existencia no cadastro. |
| RN0035 | Parcial | Contemplado | `CreateOrderUseCase` linhas 282-284 ordena cupons por valor decrescente (`Comparator.comparing(Coupon::value).reversed()`), priorizando cupons de maior valor. |
| RN0036 | Nao contemplado | Contemplado | `ApproveOrderUseCase` linhas 161-187 implementa `generateChangeCouponIfNeeded()`: calcula excedente de cupons sobre o total, gera cupom tipo `EXCHANGE` com codigo `TROCO-XXXXXXXX` e validade de 90 dias. |
| RNF0045 | Parcial | Contemplado | `ReleaseExpiredCartItemsScheduler` esta configurado com `@Scheduled(cron)` executando a cada minuto, controlado por `rpgshop.cart.blocking.auto-release-enabled=true`. O caso de uso `ReleaseExpiredCartItemsUseCase` busca itens expirados e os remove por produto/carrinho. |
| RNF0046 | Nao contemplado | Contemplado | `AuthorizeExchangeUseCase` linhas 60-64 chama `exchangeNotificationGateway.notifyExchangeAuthorized()`. A implementacao `SimulatedExchangeNotificationGateway` (anotada com `@Component`) registra a notificacao via log. A integracao esta completa — apenas o canal de notificacao e simulado (comportamento esperado em ambiente de desenvolvimento). |
