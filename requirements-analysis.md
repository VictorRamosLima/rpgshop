# Analise de Aderencia de Requisitos

Data da analise: 2026-02-10
Fonte de requisitos: `rpg_requirements.json`
Documento revisado: `requirements-analysis.md` (versao anterior)
Escopo avaliado:
- Backend em `src/main/java`
- Telas em `src/main/resources/templates`
- Execucao de testes: `.\gradlew.bat test` (falhou por ausencia de Docker/Testcontainers)

## Criterio de classificacao

- `Contemplado`: implementacao existe e cobre o fluxo principal esperado.
- `Parcial`: requisito implementado em parte, com lacuna funcional ou de comportamento.
- `Nao contemplado`: nao ha implementacao efetiva do requisito.
- `Errado`: existe implementacao, mas com comportamento incorreto no fluxo.

## Resumo geral

| Tipo | Total | Contemplado | Parcial | Nao contemplado | Errado |
|---|---:|---:|---:|---:|---:|
| Funcionais (FR) | 33 | 27 | 4 | 0 | 2 |
| Nao funcionais (NFR) | 11 | 4 | 3 | 4 | 0 |
| Regras de negocio (BR) | 36 | 30 | 2 | 4 | 0 |
| **Total** | **80** | **61** | **9** | **8** | **2** |

## Itens com status Errado

| ID | Motivo | Evidencia principal |
|---|---|---|
| RF0012 | Inativacao funciona no backend, mas o fluxo de tela quebra ao abrir detalhe com historico por uso de propriedade inexistente `sc.changedAt()`. | `src/main/resources/templates/product/detail.html`, `src/main/java/rpgshop/domain/entity/product/StatusChange.java` |
| RF0016 | Reativacao sofre o mesmo problema do RF0012, quebrando o retorno para a pagina de detalhe quando ha historico de status. | `src/main/resources/templates/product/detail.html`, `src/main/java/rpgshop/domain/entity/product/StatusChange.java` |

## Itens com status Parcial

| ID | Lacuna principal | Evidencia principal |
|---|---|---|
| RF0015 | Consulta por status ativo/inativo fica inconsistente: filtro usa coluna `is_active`, mas o mapeador de produto nao sincroniza esse campo com o historico de status. | `src/main/java/rpgshop/infraestructure/persistence/repository/product/ProductRepository.java`, `src/main/java/rpgshop/infraestructure/mapper/product/ProductMapper.java` |
| RF0027 | Regra de cartao preferencial e multiplos cartoes existe no backend, mas a tela de detalhe usa `card.brand()` (inexistente), comprometendo a visualizacao apos cadastro de cartao. | `src/main/java/rpgshop/application/usecase/customer/CreateCreditCardUseCase.java`, `src/main/resources/templates/customer/detail.html`, `src/main/java/rpgshop/domain/entity/customer/CreditCard.java` |
| RF0035 | Checkout aceita endereco novo, mas mesmo quando "nao salvar no perfil" o endereco e persistido para o cliente (inativo), nao sendo um fluxo totalmente separado do perfil. | `src/main/java/rpgshop/presentation/controller/order/OrderController.java`, `src/main/java/rpgshop/infraestructure/persistence/gateway/AddressGatewayJpa.java` |
| RF0037 | Pedido nao fica em `PROCESSING` ao finalizar compra; vai direto para `APPROVED` ou `REJECTED` conforme retorno da operadora. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java`, `src/main/java/rpgshop/domain/entity/order/constant/OrderStatus.java` |
| RNF0012 | Estrutura de log existe, mas nao ha integracao sistematica para registrar automaticamente toda operacao de escrita. | `src/main/java/rpgshop/application/usecase/log/CreateTransactionLogUseCase.java`, `src/main/java/rpgshop/application/usecase/log/QueryTransactionLogsUseCase.java` |
| RNF0034 | Existe inclusao de novos enderecos sem editar outros dados, mas nao ha fluxo dedicado para alterar endereco existente de forma isolada. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java`, `src/main/java/rpgshop/application/usecase/customer/CreateAddressUseCase.java` |
| RNF0042 | Ha caso de uso para liberar itens expirados, mas sem agendamento/acionamento automatico e sem listagem clara de itens removidos para o usuario. | `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java`, `src/main/resources/templates/cart/view.html` |
| RN0035 | Permite cupom + cartao e cartao abaixo de R$10 nessa combinacao, mas nao implementa priorizacao automatica de cupons por maior valor. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RNF0045 | A remocao por desbloqueio existe no caso de uso, mas nao esta conectada a execucao automatica por timeout no fluxo da aplicacao. | `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java` |

## Mapeamento detalhado

### 1) Geral - Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0011 | Nao contemplado | Nao ha monitoramento/SLO para comprovar resposta <= 1 segundo em todas as consultas. | `src/main/java`, `src/main/resources` |
| RNF0012 | Parcial | Entidade/repositorio/log de consulta existem, mas nao ha amarracao transversal com todos os fluxos de escrita. | `src/main/java/rpgshop/application/usecase/log/CreateTransactionLogUseCase.java` |

### 2) Cadastro de Itens de RPG

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0011 | Contemplado | Cadastro de produto com tela, controller e use case implementados. | `src/main/resources/templates/product/create.html`, `src/main/java/rpgshop/presentation/controller/product/ProductController.java`, `src/main/java/rpgshop/application/usecase/product/CreateProductUseCase.java` |
| RF0012 | Errado | Inativacao executa, mas fluxo de retorno para detalhe quebra por propriedade inexistente na tela de historico. | `src/main/resources/templates/product/detail.html`, `src/main/java/rpgshop/domain/entity/product/StatusChange.java` |
| RF0013 | Contemplado | Inativacao automatica existe por caso de uso, scheduler e acao manual. | `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsUseCase.java`, `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsScheduler.java`, `src/main/resources/templates/product/list.html` |
| RF0014 | Contemplado | Alteracao cadastral implementada com formulario e use case dedicado. | `src/main/resources/templates/product/edit.html`, `src/main/java/rpgshop/application/usecase/product/UpdateProductUseCase.java` |
| RF0015 | Parcial | Filtros combinados existem, mas filtro por ativo/inativo fica inconsistente por dessintonia entre coluna `is_active` e historico de status. | `src/main/java/rpgshop/infraestructure/persistence/repository/product/ProductRepository.java`, `src/main/java/rpgshop/infraestructure/mapper/product/ProductMapper.java` |
| RF0016 | Errado | Reativacao sofre o mesmo problema de renderizacao da pagina de detalhe com historico. | `src/main/resources/templates/product/detail.html`, `src/main/java/rpgshop/domain/entity/product/StatusChange.java` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0021 | Contemplado | Item recebe identificador unico e validacao de unicidade de SKU/codigo de barras. | `src/main/java/rpgshop/application/usecase/product/CreateProductUseCase.java`, `src/main/java/rpgshop/infraestructure/persistence/entity/product/ProductJpaEntity.java` |
| RNF0013 | Nao contemplado | Nao existe script de carga inicial de dominios (grupo de precificacao, fornecedor, tipos etc). | `src/main/resources`, `src/main/java` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0011 | Contemplado | Dados obrigatorios do item (incluindo dimensoes/peso e identificador) sao validados no cadastro e alteracao. | `src/main/java/rpgshop/application/usecase/product/CreateProductUseCase.java`, `src/main/java/rpgshop/application/usecase/product/UpdateProductUseCase.java` |
| RN0012 | Contemplado | Produto pode estar em multiplas categorias (relacao N:N). | `src/main/java/rpgshop/infraestructure/persistence/entity/product/ProductJpaEntity.java` |
| RN0013 | Contemplado | Preco de venda e calculado pela margem do grupo de precificacao. | `src/main/java/rpgshop/application/usecase/product/CreateProductUseCase.java`, `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java` |
| RN0014 | Contemplado | Preco abaixo da margem exige autorizacao gerencial. | `src/main/java/rpgshop/application/usecase/product/UpdateProductUseCase.java` |
| RN0015 | Contemplado | Inativacao manual exige motivo e categoria. | `src/main/java/rpgshop/application/usecase/product/DeactivateProductUseCase.java` |
| RN0016 | Contemplado | Inativacao automatica usa categoria `OUT_OF_MARKET`. | `src/main/java/rpgshop/application/usecase/product/AutoDeactivateProductsUseCase.java`, `src/main/java/rpgshop/domain/entity/product/constant/StatusChangeCategory.java` |
| RN0017 | Contemplado | Reativacao exige motivo e categoria. | `src/main/java/rpgshop/application/usecase/product/ActivateProductUseCase.java` |

### 3) Cadastro de Clientes

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0021 | Contemplado | Cadastro de cliente implementado com validacoes. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java`, `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java` |
| RF0022 | Contemplado | Alteracao de dados cadastrais implementada. | `src/main/java/rpgshop/application/usecase/customer/UpdateCustomerUseCase.java` |
| RF0023 | Contemplado | Inativacao de cliente implementada. | `src/main/java/rpgshop/application/usecase/customer/DeactivateCustomerUseCase.java` |
| RF0024 | Contemplado | Consulta por filtros combinaveis implementada. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java`, `src/main/java/rpgshop/infraestructure/persistence/repository/customer/CustomerRepository.java` |
| RF0025 | Contemplado | Transacoes do cliente podem ser consultadas via lista de pedidos do cliente. | `src/main/resources/templates/customer/detail.html`, `src/main/java/rpgshop/presentation/controller/order/OrderController.java` |
| RF0026 | Contemplado | Inclusao de multiplos enderecos de entrega com identificacao (label). | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java`, `src/main/java/rpgshop/application/usecase/customer/CreateAddressUseCase.java` |
| RF0027 | Parcial | Backend suporta cartoes multiplos e preferencial, mas tela de detalhe usa metodo inexistente para bandeira do cartao. | `src/main/java/rpgshop/application/usecase/customer/CreateCreditCardUseCase.java`, `src/main/resources/templates/customer/detail.html` |
| RF0028 | Contemplado | Alteracao isolada de senha implementada. | `src/main/java/rpgshop/application/usecase/customer/ChangePasswordUseCase.java` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0031 | Contemplado | Senha forte validada em backend e frontend. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java`, `src/main/resources/templates/customer/create.html` |
| RNF0032 | Contemplado | Confirmacao de senha em cadastro e troca de senha. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java`, `src/main/java/rpgshop/application/usecase/customer/ChangePasswordUseCase.java` |
| RNF0033 | Nao contemplado | Senha e persistida em texto puro, sem hash/criptografia. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java`, `src/main/java/rpgshop/application/usecase/customer/ChangePasswordUseCase.java` |
| RNF0034 | Parcial | Inclusao de endereco isolada existe, mas nao ha fluxo de alteracao isolada de endereco existente. | `src/main/java/rpgshop/presentation/controller/customer/CustomerController.java` |
| RNF0035 | Contemplado | Codigo unico do cliente e gerado no cadastro. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0021 | Nao contemplado | Nao e exigido endereco de cobranca no cadastro inicial. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java` |
| RN0022 | Nao contemplado | Nao e exigido endereco de entrega no cadastro inicial. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java` |
| RN0023 | Contemplado | Composicao obrigatoria de endereco validada. | `src/main/java/rpgshop/application/usecase/customer/CreateAddressUseCase.java` |
| RN0024 | Contemplado | Composicao obrigatoria de cartao validada. | `src/main/java/rpgshop/application/usecase/customer/CreateCreditCardUseCase.java` |
| RN0025 | Contemplado | Bandeira precisa existir no sistema (lookup por ID). | `src/main/java/rpgshop/application/usecase/customer/CreateCreditCardUseCase.java` |
| RN0026 | Contemplado | Dados obrigatorios do cliente (incluindo endereco residencial) sao exigidos no cadastro. | `src/main/java/rpgshop/application/usecase/customer/CreateCustomerUseCase.java`, `src/main/resources/templates/customer/create.html` |
| RN0027 | Contemplado | Ranking numerico existe e e atualizado com base no comportamento de compra. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java`, `src/main/java/rpgshop/application/usecase/order/ApproveOrderUseCase.java` |
| RN0028 | Contemplado | Validacao de operadora ocorre antes de efeitos de aprovacao; baixa de estoque so para aprovados. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java`, `src/main/java/rpgshop/infraestructure/integration/order/SimulatedCardOperatorGateway.java` |

### 4) Gerenciar Vendas Eletronicas

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0031 | Contemplado | Carrinho permite adicionar, editar, remover e visualizar itens. | `src/main/java/rpgshop/presentation/controller/cart/CartController.java` |
| RF0032 | Contemplado | Quantidade pode ser definida no add e alterada no carrinho. | `src/main/java/rpgshop/application/usecase/cart/AddCartItemUseCase.java`, `src/main/java/rpgshop/application/usecase/cart/UpdateCartItemQuantityUseCase.java` |
| RF0033 | Contemplado | Compra a partir do carrinho implementada no checkout. | `src/main/java/rpgshop/presentation/controller/order/OrderController.java`, `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RF0034 | Contemplado | Frete considera itens (peso) e endereco (estado/pais). | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RF0035 | Parcial | Selecao de endereco existente/novo existe, mas endereco novo sempre e persistido para o cliente (mesmo quando nao deveria entrar no perfil). | `src/main/java/rpgshop/presentation/controller/order/OrderController.java` |
| RF0036 | Contemplado | Checkout permite cartao existente, novo cartao, cupons e combinacoes de pagamento. | `src/main/resources/templates/order/checkout.html`, `src/main/java/rpgshop/presentation/controller/order/OrderController.java` |
| RF0037 | Parcial | Finalizacao nao deixa pedido em `PROCESSING`; status final e imediato (`APPROVED`/`REJECTED`). | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RF0038 | Contemplado | Despacho de pedidos aprovados para `IN_TRANSIT`. | `src/main/java/rpgshop/application/usecase/order/DispatchOrderUseCase.java` |
| RF0039 | Contemplado | Confirmacao de entrega para `DELIVERED`. | `src/main/java/rpgshop/application/usecase/order/DeliverOrderUseCase.java` |
| RF0040 | Contemplado | Solicitacao de troca implementada para item de pedido entregue. | `src/main/java/rpgshop/application/usecase/exchange/RequestExchangeUseCase.java` |
| RF0041 | Contemplado | Autorizacao de troca implementada. | `src/main/java/rpgshop/application/usecase/exchange/AuthorizeExchangeUseCase.java` |
| RF0042 | Contemplado | Visualizacao de trocas por filtro/status implementada. | `src/main/java/rpgshop/presentation/controller/exchange/ExchangeController.java` |
| RF0043 | Contemplado | Recebimento de itens de troca com decisao de retorno a estoque implementado. | `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java` |
| RF0044 | Contemplado | Geracao de cupom de troca apos recebimento implementada. | `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0042 | Parcial | Caso de uso de expiracao existe, mas nao esta automatizado e nao ha apresentacao clara dos itens removidos ao usuario. | `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java`, `src/main/resources/templates/cart/view.html` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0031 | Contemplado | Nao permite adicionar indisponivel e valida quantidade contra estoque disponivel. | `src/main/java/rpgshop/application/usecase/cart/AddCartItemUseCase.java` |
| RN0032 | Contemplado | Revalida estoque no momento da compra. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RN0033 | Contemplado | Limite de um cupom promocional por pedido implementado. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RN0034 | Contemplado | Multiplo cartao suportado; regra de minimo por cartao sem cupom implementada. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java`, `src/main/resources/templates/order/checkout.html` |
| RN0035 | Parcial | Cartao com cupom e aceito, mas nao ha priorizacao automatica de cupons por maior valor. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RN0036 | Nao contemplado | Nao ha geracao de cupom de troco quando total pago por cupons supera o valor do pedido. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RN0037 | Contemplado | Validacao de cupons + aprovacao da operadora antes da conclusao. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java`, `src/main/java/rpgshop/infraestructure/integration/order/SimulatedCardOperatorGateway.java` |
| RN0038 | Contemplado | Status do pedido muda conforme aprovacao/reprovacao da operadora. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java` |
| RN0039 | Contemplado | Pedido despachado muda para `IN_TRANSIT`. | `src/main/java/rpgshop/application/usecase/order/DispatchOrderUseCase.java` |
| RN0040 | Contemplado | Pedido entregue muda para `DELIVERED`. | `src/main/java/rpgshop/application/usecase/order/DeliverOrderUseCase.java` |
| RN0041 | Contemplado | Solicitacao de troca gera fluxo com pedido em `IN_EXCHANGE`. | `src/main/java/rpgshop/application/usecase/exchange/RequestExchangeUseCase.java` |
| RN0042 | Contemplado | Recebimento de troca altera pedido para `EXCHANGED`. | `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java` |
| RN0043 | Contemplado | Troca permitida apenas para pedido entregue. | `src/main/java/rpgshop/application/usecase/exchange/RequestExchangeUseCase.java` |
| RN0044 | Contemplado | Bloqueio de carrinho com timeout parametrizado por propriedade. | `src/main/java/rpgshop/application/config/CartBlockingProperties.java`, `src/main/java/rpgshop/application/usecase/cart/AddCartItemUseCase.java` |
| RNF0045 | Parcial | Regra de retirada existe no caso de uso, mas falta execucao automatica vinculada ao timeout. | `src/main/java/rpgshop/application/usecase/cart/ReleaseExpiredCartItemsUseCase.java` |
| RNF0046 | Nao contemplado | Nao ha mecanismo de notificacao ao cliente quando troca e autorizada. | `src/main/java/rpgshop/application/usecase/exchange/AuthorizeExchangeUseCase.java` |

### 5) Controle de Estoque

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0051 | Contemplado | Entrada de estoque implementada com validacoes. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java`, `src/main/resources/templates/stock/create.html` |
| RF0052 | Contemplado | Calculo de valor de venda por custo + margem implementado. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java` |
| RF0053 | Contemplado | Baixa de estoque em venda aprovada implementada. | `src/main/java/rpgshop/application/usecase/order/CreateOrderUseCase.java`, `src/main/java/rpgshop/application/usecase/order/ApproveOrderUseCase.java` |
| RF0054 | Contemplado | Reentrada apos troca implementada e integrada ao recebimento de troca. | `src/main/java/rpgshop/application/usecase/stock/CreateStockReentryUseCase.java`, `src/main/java/rpgshop/application/usecase/exchange/ReceiveExchangeItemsUseCase.java` |

#### Regras de negocio

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RN0051 | Contemplado | Produto, quantidade, custo, fornecedor e data obrigatorios na entrada. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java` |
| RN005x | Contemplado | Recalculo usa maior custo historico para definir preco. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java`, `src/main/java/rpgshop/infraestructure/persistence/repository/stock/StockEntryRepository.java` |
| RN0061 | Contemplado | Quantidade zero/negativa bloqueada. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java` |
| RN0062 | Contemplado | Valor de custo obrigatorio e maior que zero. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java` |
| RNF0064 | Contemplado | Data de entrada obrigatoria. | `src/main/java/rpgshop/application/usecase/stock/CreateStockEntryUseCase.java` |

### 6) Analise

#### Funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RF0055 | Contemplado | Consulta de historico por periodo, produto e categoria implementada. | `src/main/java/rpgshop/presentation/controller/analysis/AnalysisController.java`, `src/main/java/rpgshop/application/usecase/analysis/SalesAnalysisUseCase.java` |

#### Nao funcionais

| ID | Status | Analise | Evidencia |
|---|---|---|---|
| RNF0043 | Nao contemplado | Nao ha grafico de linhas nas telas de analise; apenas tabelas. | `src/main/resources/templates/analysis/sales.html`, `src/main/resources/templates/analysis/by-product.html`, `src/main/resources/templates/analysis/by-category.html` |

## Observacoes tecnicas adicionais

- Ha dessintonia entre status de produto por historico e coluna `is_active` persistida:
  - `Product.isActive()` depende de `statusChanges`.
  - filtros de repositorio usam `products.is_active`.
  - `ProductMapper.toEntity()` nao grava `isActive`.
  - Evidencias: `src/main/java/rpgshop/domain/entity/product/Product.java`, `src/main/java/rpgshop/infraestructure/mapper/product/ProductMapper.java`, `src/main/java/rpgshop/infraestructure/persistence/repository/product/ProductRepository.java`.
- Erros de template impactam fluxos de produto e cliente:
  - `sc.changedAt()` em historico de status de produto (metodo inexistente).
  - `card.brand()` em detalhe de cliente (metodo inexistente).
- Teste automatizado executado (`.\gradlew.bat test`) falhou por dependencia de Docker/Testcontainers indisponivel no ambiente atual; nao houve validacao funcional completa de runtime.
