# Relatório de Validação de Requisitos - RPG Shop

## Resumo Executivo

Este documento apresenta a análise de conformidade do sistema com os requisitos funcionais (RF), não funcionais (RNF) e regras de negócio (RN) especificados no arquivo `rpg_requirements.json`.

---

## 🔴 Requisitos/Regras NÃO CONTEMPLADOS

### Requisitos Funcionais

| ID | Nome | Status | Observação |
|----|------|--------|------------|
| **RF0035** | Selecionar endereço de entrega | ❌ Parcial | O sistema permite selecionar endereço existente, mas **NÃO permite cadastrar novo endereço durante o checkout** e opcionalmente adicioná-lo ao perfil. O checkout atual (`checkout.html`) apenas recebe um UUID de endereço existente. |
| **RF0036** | Selecionar forma de pagamento | ❌ Parcial | O sistema permite selecionar cartão existente e usar cupons, mas **NÃO permite cadastrar novo cartão durante o checkout**. |

### Requisitos Não Funcionais

| ID | Nome | Status | Observação |
|----|------|--------|------------|
| **RNF0011** | Tempo de resposta para consultas | ❌ Não verificável | Não há implementação de monitoramento/métricas para garantir tempo de resposta ≤ 1 segundo. |
| **RNF0013** | Cadastro de domínios | ❌ Não implementado | **NÃO existe script de implantação (data.sql ou similar)** inserindo registros de tabelas de domínio (grupo de precificação, fabricante/fornecedor, tipos de item, bandeiras de cartão etc.). O `schema.sql` contém apenas criação de índice. |
| **RNF0033** | Senha criptografada | ❌ Não implementado | A senha é armazenada **sem criptografia**. Os use cases `CreateCustomerUseCase` e `ChangePasswordUseCase` salvam a senha em texto puro. Deveria usar BCrypt ou similar. |
| **RNF0042** | Apresentar itens retirados do carrinho | ❌ Não implementado | O sistema remove itens expirados (`ReleaseExpiredCartItemsUseCase`), mas **NÃO lista produtos removidos por expiração** para o usuário e não desabilita a compra informando que devem ser adicionados novamente. |
| **RNF0043** | Gráfico de linhas | ❌ Não implementado | A análise de vendas existe, mas **NÃO apresenta gráfico de linhas**. As páginas de análise (`sales.html`, `by-product.html`, `by-category.html`) mostram apenas tabelas. |

### Regras de Negócio

| ID | Nome | Status | Observação |
|----|------|--------|------------|
| **RN0021** | Cadastro de endereço de cobrança obrigatório | ❌ Não implementado | O cadastro de cliente **NÃO exige** ao menos um endereço de cobrança. O `CreateCustomerUseCase` cria cliente sem endereços e permite adicionar depois. |
| **RN0022** | Cadastro de endereço de entrega obrigatório | ❌ Não implementado | O cadastro de cliente **NÃO exige** ao menos um endereço de entrega. |
| **RN0026** | Dados obrigatórios do cliente | ❌ Parcial | O requisito exige "endereço residencial" como dado obrigatório no cadastro do cliente, mas o sistema permite criar cliente sem nenhum endereço. |
| **RN0027** | Ranking de cliente | ❌ Não implementado | O campo `ranking` existe no `Customer`, mas **NÃO há cálculo baseado no perfil de compra**. O ranking é sempre iniciado com `BigDecimal.ZERO` e nunca é atualizado. |
| **RN0028** | Validar retorno da operadora de cartão | ❌ Não implementado | **NÃO há integração com operadora de cartão**. O pedido vai direto para `PROCESSING` e pode ser aprovado manualmente. Não há validação real de pagamento. |
| **RN0034** | Uso de diversos cartões de crédito | ❌ Parcial | O modelo suporta múltiplos pagamentos, mas **NÃO valida valor mínimo de R$ 10,00 por cartão** (constante `MIN_CARD_AMOUNT` existe mas não é usada para validação). |
| **RN0035** | Uso de cupons junto a cartão de crédito | ❌ Parcial | O sistema permite combinação, mas **NÃO prioriza o valor máximo dos cupons** antes de usar cartão. A lógica de priorização não está implementada. |
| **RN0036** | Gerar cupom de troco | ❌ Não implementado | **NÃO há geração de cupom de troco** quando cupons superarem o valor da compra. O sistema simplesmente valida se o pagamento cobre o total, mas não gera cupom com excedente. |
| **RN0044** | Bloqueio de produtos | ❌ Parcial | O bloqueio está implementado, mas **NÃO há um prazo parametrizado** (está fixo em 30 minutos hardcoded: `BLOCK_DURATION_MINUTES = 30`). |
| **RNF0045** | Retirar item do carrinho | ❌ Parcial | Ao desbloquear, o sistema remove apenas o item específico, mas a regra menciona "remover **todos itens do mesmo produto**" do carrinho (interpretação: pode haver itens não bloqueados do mesmo produto). |
| **RNF0046** | Gerar notificação de autorização de troca | ❌ Não implementado | **NÃO há sistema de notificação** para informar o cliente quando uma troca é autorizada. O `AuthorizeExchangeUseCase` apenas atualiza o status. |

---

## 🟡 Requisitos/Regras PARCIALMENTE CONTEMPLADOS

### Requisitos Funcionais

| ID | Nome | Status | Observação |
|----|------|--------|------------|
| **RF0034** | Calcular frete | ⚠️ Parcial | O frete é calculado apenas com base no **peso dos itens** (`FREIGHT_PER_KG = 2.50`), mas **NÃO considera o endereço do cliente** como especifica o requisito. |

### Regras de Negócio

| ID | Nome | Status | Observação |
|----|------|--------|------------|
| **RN0037** | Validar forma de pagamento | ⚠️ Parcial | Valida cupons (expiração, uso único), mas **NÃO há validação de aprovação da operadora de cartão**. |
| **RN0038** | Alterar status da compra conforme aprovação | ⚠️ Parcial | A lógica existe, mas está vinculada a ações manuais do admin, não a validação automática de operadora. |

---

## 🟢 Requisitos/Regras CONTEMPLADOS

### Cadastro de Itens de RPG
- ✅ RF0011 - Cadastrar item (CreateProductUseCase)
- ✅ RF0012 - Inativar item (DeactivateProductUseCase)
- ✅ RF0013 - Inativar item automaticamente (AutoDeactivateProductsUseCase com categoria OUT_OF_MARKET)
- ✅ RF0014 - Alterar cadastro de item (UpdateProductUseCase)
- ✅ RF0015 - Consulta de itens (ProductFilter com múltiplos campos)
- ✅ RF0016 - Ativar item (ActivateProductUseCase)
- ✅ RNF0021 - Código único (UUID gerado automaticamente)
- ✅ RN0011 - Dados obrigatórios (nome, tipo, categorias, especificações, grupo de precificação, identificador)
- ✅ RN0012 - Associação com categorias (relação muitos-para-muitos)
- ✅ RN0013 - Definir valor de venda (cálculo baseado na margem do grupo de precificação)
- ✅ RN0014 - Validar margem de lucro (UpdateProductUseCase verifica `managerAuthorized`)
- ✅ RN0015 - Motivo de inativação (DeactivateProductCommand exige reason e category)
- ✅ RN0016 - Inativação automática categorizada como FORA DE MERCADO (StatusChangeCategory.OUT_OF_MARKET)
- ✅ RN0017 - Motivo de ativação (ActivateProductCommand exige reason e category)

### Cadastro de Clientes
- ✅ RF0021 - Cadastrar cliente (CreateCustomerUseCase)
- ✅ RF0022 - Alterar cliente (UpdateCustomerUseCase)
- ✅ RF0023 - Inativar cadastro de cliente (DeactivateCustomerUseCase)
- ✅ RF0024 - Consulta de clientes (CustomerFilter com filtros combináveis)
- ✅ RF0025 - Consulta de transações (listByCustomer em OrderController)
- ✅ RF0026 - Cadastro de endereços de entrega (CreateAddressUseCase com label identificador)
- ✅ RF0027 - Cadastro de cartões de crédito (CreateCreditCardUseCase com isPreferred)
- ✅ RF0028 - Alteração apenas de senha (ChangePasswordUseCase)
- ✅ RNF0031 - Senha forte (regex validando 8+ chars, maiúsculas, minúsculas, especiais)
- ✅ RNF0032 - Confirmação de senha (CreateCustomerCommand e ChangePasswordCommand)
- ✅ RNF0034 - Alteração apenas de endereços (endpoints separados para endereços)
- ✅ RNF0035 - Código de cliente (customerCode gerado automaticamente)
- ✅ RN0023 - Composição do registro de endereços (Address contém todos os campos obrigatórios)
- ✅ RN0024 - Composição do registro de cartões (CreditCard contém número, nome, bandeira, código segurança)
- ✅ RN0025 - Bandeiras permitidas (validação via CardBrandGateway)

### Gerenciar Vendas Eletrônicas
- ✅ RF0031 - Gerenciar carrinho de compra (AddCartItemUseCase, UpdateCartItemQuantityUseCase, RemoveCartItemUseCase, ViewCartUseCase)
- ✅ RF0032 - Definir quantidade de itens (UpdateCartItemQuantityUseCase)
- ✅ RF0033 - Realizar compra (CreateOrderUseCase)
- ✅ RF0037 - Finalizar compra (Status inicial PROCESSING)
- ✅ RF0038 - Despachar produtos para entrega (DispatchOrderUseCase → IN_TRANSIT)
- ✅ RF0039 - Produtos entregues (DeliverOrderUseCase → DELIVERED)
- ✅ RF0040 - Solicitar troca (RequestExchangeUseCase)
- ✅ RF0041 - Autorizar trocas (AuthorizeExchangeUseCase → AUTHORIZED)
- ✅ RF0042 - Visualização de trocas (ExchangeController lista por status)
- ✅ RF0043 - Confirmar recebimento de itens para troca (ReceiveExchangeItemsUseCase com returnToStock)
- ✅ RF0044 - Gerar cupom de troca após recebimento (generateExchangeCoupon em ReceiveExchangeItemsUseCase)
- ✅ RN0031 - Validar estoque para adição de itens no carrinho (AddCartItemUseCase valida availableStock)
- ✅ RN0032 - Validar estoque para compra (validateStockAvailability em CreateOrderUseCase)
- ✅ RN0033 - Uso de cupom promocional (máximo um por compra - hasPromotionalCoupon)
- ✅ RN0039 - Alterar status da compra para transporte (DispatchOrderUseCase)
- ✅ RN0040 - Alterar status da compra após entrega (DeliverOrderUseCase)
- ✅ RN0041 - Gerar pedido de troca (RequestExchangeUseCase → status IN_EXCHANGE)
- ✅ RN0042 - Alterar status do pedido após recebimento de troca (ReceiveExchangeItemsUseCase → EXCHANGED)
- ✅ RN0043 - Validação para solicitar troca (apenas pedidos DELIVERED)

### Controle de Estoque
- ✅ RF0051 - Realizar entrada em estoque (CreateStockEntryUseCase)
- ✅ RF0052 - Calcular valor de venda (updateProductStockAndPrice em CreateStockEntryUseCase)
- ✅ RF0053 - Dar baixa em estoque (ApproveOrderUseCase reduz stockQuantity)
- ✅ RF0054 - Realizar reentrada em estoque (CreateStockReentryUseCase / returnItemToStock em troca)
- ✅ RN0051 - Validar dados de estoque (CreateStockEntryCommand contém produto, quantidade, custo, fornecedor, data)
- ✅ RN005x - Definir valor com diferentes custos (findMaxCostValueByProductId usa maior custo)
- ✅ RN0061 - Quantidade de itens (validação quantity > 0)
- ✅ RN0062 - Valor de custo (campo obrigatório em CreateStockEntryCommand)
- ✅ RNF0064 - Data de entrada (campo obrigatório)

### Análise
- ✅ RF0055 - Analisar histórico de vendas (SalesAnalysisUseCase com filtros por produto, categoria, período)

### Geral
- ✅ RNF0012 - Log de transação (TransactionLog com entityName, entityId, operation, responsibleUser, timestamp, previousData, newData)

---

## 📋 Resumo de Conformidade

| Categoria | Total | Contemplados | Parciais | Não Contemplados |
|-----------|-------|--------------|----------|------------------|
| Requisitos Funcionais | 25 | 22 | 3 | 0 |
| Requisitos Não Funcionais | 13 | 7 | 0 | 6 |
| Regras de Negócio | 33 | 22 | 3 | 8 |
| **TOTAL** | **71** | **51 (72%)** | **6 (8%)** | **14 (20%)** |

---

## 🎯 Priorização de Correções

### Alta Prioridade (Segurança/Compliance)
1. **RNF0033** - Implementar criptografia de senha (BCrypt)
2. **RN0028** - Implementar validação de operadora de cartão (mesmo que simulada)
3. **RN0021/RN0022/RN0026** - Exigir endereços obrigatórios no cadastro de cliente

### Média Prioridade (Funcionalidade Core)
1. **RF0035/RF0036** - Permitir cadastro de endereço/cartão durante checkout
2. **RN0036** - Implementar cupom de troco quando cupons excedem valor da compra
3. **RNF0046** - Implementar sistema de notificações para trocas autorizadas
4. **RN0027** - Implementar cálculo de ranking do cliente
5. **RNF0042** - Mostrar itens removidos por expiração do carrinho

### Baixa Prioridade (Nice to Have)
1. **RNF0043** - Adicionar gráfico de linhas na análise de vendas
2. **RNF0013** - Criar script data.sql com dados iniciais de domínio
3. **RF0034** - Incluir localização no cálculo de frete
4. **RN0044** - Parametrizar tempo de bloqueio de carrinho
5. **RNF0011** - Implementar monitoramento de tempo de resposta

