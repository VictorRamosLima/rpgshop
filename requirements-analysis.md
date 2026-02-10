# Analise de Aderencia de Requisitos

Data da analise: 2026-02-10
Fonte de requisitos: `rpg_requirements.json` (arquivo encontrado no projeto)
Escopo avaliado: codigo em `src/main/java` e telas em `src/main/resources/templates`

## Criterio de classificacao

- `Contemplado`: implementacao existe e fluxo principal esta disponivel.
- `Parcial`: implementacao incompleta, sem integracao fim-a-fim, ou cobrindo apenas parte do requisito.
- `Nao contemplado`: nao ha implementacao efetiva do requisito.
- `Errado`: existe implementacao, mas com comportamento incorreto frente ao requisito.

## Resumo geral

| Tipo | Total | Contemplado | Parcial | Nao contemplado | Errado |
|---|---:|---:|---:|---:|---:|
| Funcionais (FR) | 33 | 14 | 15 | 0 | 4 |
| Nao funcionais (NFR) | 11 | 4 | 2 | 5 | 0 |
| Regras de negocio (BR) | 36 | 16 | 12 | 5 | 3 |
| **Total** | **80** | **34** | **29** | **10** | **7** |

## Requisitos com status `Errado`

| ID | Motivo principal | Evidencia |
|---|---|---|
| RF0033 | Checkout quebra em cenarios comuns com cupom opcional nulo por mapeamento sem null-safety | `src/main/java/rpgshop/infraestructure/mapper/order/OrderPaymentMapper.java:37`, `src/main/java/rpgshop/infraestructure/mapper/coupon/CouponMapper.java:35` |
| RF0039 | Botao de entrega usa status inexistente `DISPATCHED`, impedindo fluxo pela UI | `src/main/resources/templates/order/detail.html:110`, `src/main/java/rpgshop/domain/entity/order/constant/OrderStatus.java:3` |
| RF0040 | Solicitacao de troca tende a falhar com cupom nulo no mapeamento | `src/main/java/rpgshop/infraestructure/mapper/exchange/ExchangeRequestMapper.java:51`, `src/main/java/rpgshop/infraestructure/mapper/coupon/CouponMapper.java:35` |
| RF0055 | Telas de analise por produto/categoria usam propriedade inexistente `item.order()` | `src/main/resources/templates/analysis/by-product.html:53`, `src/main/resources/templates/analysis/by-category.html:45`, `src/main/java/rpgshop/domain/entity/order/OrderItem.java:10` |
| RN0016 | Inativacao automatica nao registra motivo/categoria FORA DE MERCADO; faz apenas update bruto | `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsUseCase.java:35`, `src/main/java/rpgshop/infraestructure/persistence/repository/product/ProductRepository.java:70` |
| RN0041 | Geracao de pedido de troca fica comprometida pelo erro estrutural de RF0040 | `src/main/java/rpgshop/application/usecase/exchange/RequestExchangeUseCase.java:68`, `src/main/java/rpgshop/infraestructure/mapper/exchange/ExchangeRequestMapper.java:51` |
| RNF0045 | Liberacao de expirados usa `item.id()` como `cartId` no delete (parametro errado) | `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java:25`, `src/main/java/rpgshop/infraestructure/persistence/repository/cart/CartItemRepository.java:39` |

## Mapeamento detalhado

### 1) Geral

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0011 | Nao contemplado | Nao ha metrica/SLO automatizado para garantir <= 1s. | Ausencia de instrumentacao especifica no projeto |
| RNF0012 | Parcial | Estrutura de log existe, mas nao ha integracao para registrar toda escrita automaticamente. | `src/main/java/rpgshop/application/usecase/log/CreateTransactionLogUseCase.java:15`, `src/main/java/rpgshop/infraestructure/persistence/repository/log/TransactionLogRepository.java:15`, `src/main/java/rpgshop/application/usecase/log/CreateTransactionLogUseCase.java:24` |

### 2) Cadastro de Itens de RPG

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0011 | Parcial | Caso de uso de cadastro existe, mas nao ha rota/tela de criacao no controller de produto. | `src/main/java/rpgshop/application/usecase/product/CreateProductUseCase.java:26`, `src/main/java/rpgshop/presentation/controller/product/ProductController.java:51` |
| RF0012 | Parcial | Caso de uso de inativacao existe, mas nao ha endpoint exposto para inativar produto. | `src/main/java/rpgshop/application/usecase/product/DeactivateProductUseCase.java:13`, `src/main/java/rpgshop/presentation/controller/product/ProductController.java:79` |
| RF0013 | Parcial | Logica existe, mas nao ha disparo automatico (scheduler/job/endpoint operacional). | `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsUseCase.java:16`, `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsUseCase.java:24` |
| RF0014 | Parcial | Caso de uso de alteracao existe, sem endpoint/tela de edicao no fluxo atual. | `src/main/java/rpgshop/application/usecase/product/UpdateProductUseCase.java:25`, `src/main/java/rpgshop/presentation/controller/product/ProductController.java:68` |
| RF0015 | Parcial | Backend aceita filtros ricos, mas UI expoe somente subconjunto (nome/status/faixa de preco). | `src/main/java/rpgshop/infraestructure/persistence/repository/product/ProductRepository.java:28`, `src/main/java/rpgshop/presentation/controller/product/ProductController.java:60` |
| RF0016 | Contemplado | Reativacao implementada com endpoint e validacoes. | `src/main/java/rpgshop/presentation/controller/product/ProductController.java:79`, `src/main/java/rpgshop/application/usecase/product/ActivateProductUseCase.java:22` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0021 | Contemplado | Produto possui identificador unico (UUID). | `src/main/java/rpgshop/infraestructure/persistence/entity/product/ProductJpaEntity.java:51` |
| RNF0013 | Nao contemplado | Nao existe script de carga inicial de dominios (data seed). | Recursos mostram apenas `application.properties` e `schema.sql` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0011 | Parcial | Nome/tipo/categoria/grupo/identificador/peso sao validados; dimensoes nao sao obrigatorias. | `src/main/java/rpgshop/application/usecase/product/CreateProductUseCase.java:86` |
| RN0012 | Contemplado | Relacao muitos-para-muitos entre produto e categorias. | `src/main/java/rpgshop/infraestructure/persistence/entity/product/ProductJpaEntity.java:80` |
| RN0013 | Contemplado | Preco de venda calculado pela margem do grupo de precificacao. | `src/main/java/rpgshop/application/usecase/product/CreateProductUseCase.java:60`, `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:92` |
| RN0014 | Contemplado | Preco abaixo da margem exige `managerAuthorized`. | `src/main/java/rpgshop/application/usecase/product/UpdateProductUseCase.java:107` |
| RN0015 | Parcial | Validacao de motivo/categoria existe no caso de uso, mas sem endpoint de inativacao no fluxo atual. | `src/main/java/rpgshop/application/usecase/product/DeactivateProductUseCase.java:24`, `src/main/java/rpgshop/presentation/controller/product/ProductController.java:79` |
| RN0016 | Errado | Inativacao automatica nao classifica via historico de status como `OUT_OF_MARKET`; so desliga flag. | `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsUseCase.java:35`, `src/main/java/rpgshop/infraestructure/persistence/repository/product/ProductRepository.java:70` |
| RN0017 | Contemplado | Reativacao exige motivo e categoria. | `src/main/java/rpgshop/application/usecase/product/ActivateProductUseCase.java:23` |

### 3) Cadastro de Clientes

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0021 | Contemplado | Cadastro de cliente implementado. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java:94` |
| RF0022 | Contemplado | Alteracao de dados cadastrais implementada. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java:138` |
| RF0023 | Contemplado | Inativacao de cliente implementada. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java:157` |
| RF0024 | Contemplado | Consulta com filtros combinaveis (nome/cpf/email/genero/status). | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java:70`, `src/main/java/rpgshop/infraestructure/persistence/repository/customer/CustomerRepository.java:24` |
| RF0025 | Contemplado | Consulta de transacoes via pedidos do cliente. | `src/main/java/rpgshop/presentation/controller/order/OrderController.java:72`, `src/main/resources/templates/customer/detail.html:15` |
| RF0026 | Contemplado | Multiplos enderecos com label curta. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java:180`, `src/main/java/rpgshop/application/usecase/customer/CreateAddressUseCase.java:32` |
| RF0027 | Contemplado | Multiplos cartoes com preferencial. | `src/main/java/rpgshop/application/usecase/customer/CreateCreditCardUseCase.java:48` |
| RF0028 | Contemplado | Alteracao isolada de senha implementada. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java:163`, `src/main/java/rpgshop/application/usecase/customer/ChangePasswordUseCase.java:24` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0031 | Contemplado | Regra de senha forte no backend (regex) e validacao no frontend. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:20`, `src/main/resources/templates/customer/create.html:105` |
| RNF0032 | Contemplado | Confirmacao de senha validada no backend e frontend. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:109`, `src/main/java/rpgshop/application/usecase/customer/ChangePasswordUseCase.java:35` |
| RNF0033 | Nao contemplado | Senha e armazenada e atualizada em texto puro. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:46`, `src/main/java/rpgshop/infraestructure/persistence/repository/customer/CustomerRepository.java:48` |
| RNF0034 | Parcial | Ha endpoint para adicionar endereco, mas nao para editar endereco existente isoladamente. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java:180` |
| RNF0035 | Contemplado | Codigo unico de cliente e gerado no cadastro. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:48`, `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:123` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0021 | Nao contemplado | Cadastro inicial nao exige endereco de cobranca. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:50` |
| RN0022 | Nao contemplado | Cadastro inicial nao exige endereco de entrega. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:50` |
| RN0023 | Contemplado | Campos obrigatorios de endereco sao validados. | `src/main/java/rpgshop/application/usecase/customer/CreateAddressUseCase.java:48` |
| RN0024 | Contemplado | Campos obrigatorios de cartao sao validados. | `src/main/java/rpgshop/application/usecase/customer/CreateCreditCardUseCase.java:62` |
| RN0025 | Contemplado | Bandeira deve existir no sistema (lookup por id). | `src/main/java/rpgshop/application/usecase/customer/CreateCreditCardUseCase.java:41` |
| RN0026 | Parcial | Dados obrigatorios do cliente sao validados, exceto endereco residencial obrigatorio no cadastro inicial. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:73`, `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:50` |
| RN0027 | Parcial | Campo ranking existe, mas nao ha algoritmo de evolucao por perfil de compra. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java:47`, `src/main/java/rpgshop/domain/entity/customer/Customer.java:21` |
| RN0028 | Parcial | Baixa de estoque ocorre apos aprovacao, mas sem integracao de operadora para aprovacao/reprovação real. | `src/main/java/rpgshop/application/usecase/order/ApproveOrderUseCase.java:36`, `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:74` |

### 4) Gerenciar Vendas Eletronicas

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0031 | Contemplado | Adicionar/alterar/remover/visualizar carrinho implementado. | `src/main/java/rpgshop/presentation/controller/cart/CartController.java:46` |
| RF0032 | Contemplado | Alteracao de quantidade implementada. | `src/main/java/rpgshop/application/usecase/cart/UpdateCartItemQuantityUseCase.java:35` |
| RF0033 | Errado | Checkout com cupom opcional tende a falhar por mapeamento sem null-safety. | `src/main/java/rpgshop/infraestructure/mapper/order/OrderPaymentMapper.java:37`, `src/main/java/rpgshop/infraestructure/mapper/coupon/CouponMapper.java:35`, `src/main/java/rpgshop/presentation/controller/order/OrderController.java:107` |
| RF0034 | Parcial | Frete considera peso, mas nao endereco. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:144` |
| RF0035 | Parcial | So aceita endereco ja existente por UUID; nao cadastra novo no checkout. | `src/main/resources/templates/order/checkout.html:20`, `src/main/java/rpgshop/presentation/controller/order/OrderController.java:104` |
| RF0036 | Parcial | So ha um cartao + um cupom no fluxo de tela; sem cadastro de novo pagamento no checkout. | `src/main/java/rpgshop/presentation/controller/order/OrderController.java:111`, `src/main/resources/templates/order/checkout.html:31` |
| RF0037 | Parcial | Pedido inicia em `PROCESSING`, mas fluxo de compra sofre os problemas de RF0033. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:104` |
| RF0038 | Parcial | Use case de despacho existe, mas UI de aprovacao tem divergencia de status (`PENDING` vs `PROCESSING`). | `src/main/java/rpgshop/application/usecase/order/DispatchOrderUseCase.java:34`, `src/main/resources/templates/order/detail.html:101` |
| RF0039 | Errado | Confirmacao de entrega na UI depende de status inexistente `DISPATCHED`. | `src/main/resources/templates/order/detail.html:110`, `src/main/java/rpgshop/domain/entity/order/constant/OrderStatus.java:3` |
| RF0040 | Errado | Solicitacao de troca comprometida por mapeamento de cupom nulo. | `src/main/java/rpgshop/application/usecase/exchange/RequestExchangeUseCase.java:68`, `src/main/java/rpgshop/infraestructure/mapper/exchange/ExchangeRequestMapper.java:51` |
| RF0041 | Parcial | Autorizacao existe no caso de uso, mas depende da criacao correta da troca (RF0040). | `src/main/java/rpgshop/application/usecase/exchange/AuthorizeExchangeUseCase.java:33` |
| RF0042 | Parcial | Consulta por status existe, mas listagem sem filtro usa `findByStatus(null)` e pode retornar vazio. | `src/main/java/rpgshop/presentation/controller/exchange/ExchangeController.java:58`, `src/main/java/rpgshop/infraestructure/persistence/repository/exchange/ExchangeRequestRepository.java:22` |
| RF0043 | Parcial | Recebimento implementado, mas depende de fluxo de troca anterior. | `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java:44` |
| RF0044 | Parcial | Cupom de troca e gerado, mas fluxo completo depende de RF0040/0043 e cupom nao e associado ao request. | `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java:89`, `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java:59` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0042 | Nao contemplado | Nao ha tela/lista de itens removidos por expiracao nem bloqueio explicito de compra por esse motivo. | `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java:20`, `src/main/resources/templates/cart/view.html:18` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0031 | Contemplado | Valida indisponibilidade e quantidade maxima ao adicionar carrinho. | `src/main/java/rpgshop/application/usecase/cart/AddCartItemUseCase.java:58` |
| RN0032 | Contemplado | Revalida estoque no fechamento do pedido. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:121` |
| RN0033 | Contemplado | Limite de um cupom promocional por pedido implementado. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:179` |
| RN0034 | Parcial | Modelo suporta multiplos pagamentos, mas nao valida minimo de R$ 10 por cartao. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:40`, `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:169` |
| RN0035 | Nao contemplado | Nao ha priorizacao explicita de cupons nem regra de cartao < R$10 nessa combinacao. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:169` |
| RN0036 | Nao contemplado | Nao ha geracao de cupom de troco quando pagamento excede total do pedido. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:219` |
| RN0037 | Parcial | Valida cupons, mas sem aprovacao de operadora de cartao. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java:185` |
| RN0038 | Parcial | Ha transicao manual para aprovado/reprovado, sem decisao automatica por operadora. | `src/main/java/rpgshop/application/usecase/order/ApproveOrderUseCase.java:27`, `src/main/java/rpgshop/application/usecase/order/RejectOrderUseCase.java:25` |
| RN0039 | Parcial | Regra de transporte existe, mas fluxo de UI de status esta inconsistente. | `src/main/java/rpgshop/application/usecase/order/DispatchOrderUseCase.java:34`, `src/main/resources/templates/order/detail.html:101` |
| RN0040 | Parcial | Regra de entregue existe, mas gatilho de UI usa status errado. | `src/main/java/rpgshop/application/usecase/order/DeliverOrderUseCase.java:34`, `src/main/resources/templates/order/detail.html:110` |
| RN0041 | Errado | Pedido de troca deveria ser gerado, mas criacao do request esta comprometida por erro de mapeamento. | `src/main/java/rpgshop/application/usecase/exchange/RequestExchangeUseCase.java:68`, `src/main/java/rpgshop/infraestructure/mapper/exchange/ExchangeRequestMapper.java:51` |
| RN0042 | Parcial | Atualizacao para trocado existe no receive, mas depende de fluxo de troca previamente funcional. | `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java:65` |
| RN0043 | Contemplado | Apenas pedidos entregues podem solicitar troca. | `src/main/java/rpgshop/application/usecase/exchange/RequestExchangeUseCase.java:49` |
| RN0044 | Parcial | Bloqueio existe, mas timeout e fixo (nao parametrizado). | `src/main/java/rpgshop/application/usecase/cart/AddCartItemUseCase.java:26`, `src/main/java/rpgshop/application/usecase/cart/UpdateCartItemQuantityUseCase.java:21` |
| RNF0045 | Errado | Liberacao remove com parametro de carrinho incorreto e sem acionamento automatico. | `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java:25`, `src/main/java/rpgshop/infraestructure/persistence/repository/cart/CartItemRepository.java:39` |
| RNF0046 | Nao contemplado | Nao existe mecanismo de notificacao na autorizacao de troca. | `src/main/java/rpgshop/application/usecase/exchange/AuthorizeExchangeUseCase.java:33` |

### 5) Controle de Estoque

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0051 | Contemplado | Entrada de estoque implementada. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:38`, `src/main/java/rpgshop/presentation/controller/stock/StockController.java:54` |
| RF0052 | Contemplado | Valor de venda recalculado com margem do grupo. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:92` |
| RF0053 | Contemplado | Baixa de estoque ocorre na aprovacao do pedido. | `src/main/java/rpgshop/application/usecase/order/ApproveOrderUseCase.java:36` |
| RF0054 | Parcial | Existe use case dedicado, mas nao exposto nem integrado ao fluxo de troca (que so incrementa estoque). | `src/main/java/rpgshop/application/usecase/stock/CreateStockReentryUseCase.java:20`, `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java:76` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0051 | Contemplado | Validacao dos dados obrigatorios de entrada de estoque. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:63` |
| RN005x | Contemplado | Usa maior custo historico para recalcular preco de venda. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:95` |
| RN0061 | Contemplado | Nao permite quantidade <= 0. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:67` |
| RN0062 | Contemplado | Nao permite custo nulo ou <= 0. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:70` |
| RNF0064 | Contemplado | Data de entrada obrigatoria. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java:76` |

### 6) Analise

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0055 | Errado | Casos de uso existem, mas telas principais de comparacao usam campo inexistente `item.order()` e queries com literais improprios em aspas duplas. | `src/main/resources/templates/analysis/by-product.html:53`, `src/main/resources/templates/analysis/by-category.html:45`, `src/main/java/rpgshop/infraestructure/persistence/repository/order/OrderItemRepository.java:31` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0043 | Nao contemplado | Nao ha grafico de linhas, apenas tabelas textuais. | `src/main/resources/templates/analysis/sales.html:30`, `src/main/resources/templates/analysis/by-product.html:39` |

## Observacoes tecnicas adicionais

- Ha varios `@Query` com comparacao de enum/status usando aspas duplas (ex.: `"REJECTED"`, `"DELIVERED"`, `"EXCHANGE"`, `"PROMOTIONAL"`), o que e arriscado/incorreto em JPQL e pode quebrar em runtime.
  - `src/main/java/rpgshop/infraestructure/persistence/repository/order/OrderRepository.java:36`
  - `src/main/java/rpgshop/infraestructure/persistence/repository/order/OrderItemRepository.java:31`
  - `src/main/java/rpgshop/infraestructure/persistence/repository/coupon/CouponRepository.java:50`
  - `src/main/java/rpgshop/infraestructure/persistence/repository/order/OrderPaymentRepository.java:28`
- Casos de uso desenhados para automacao nao estao conectados a nenhum agendador/chamador no projeto:
  - `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsUseCase.java:16`
  - `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java:12`
  - `src/main/java/rpgshop/application/usecase/stock/CreateStockReentryUseCase.java:20`
- Execucao de testes (`.\gradlew.bat test`) falhou por dependencia de Docker/Testcontainers indisponivel no ambiente atual; nao foi uma validacao funcional completa de runtime.
