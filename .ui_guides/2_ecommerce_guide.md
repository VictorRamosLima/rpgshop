# GUIA NORMATIVO DE UI/UX — ESPECIALIZAÇÃO EM E-COMMERCE

**Versão:** 1.0
**Stack Tecnológica Alvo:** Spring Boot, Thymeleaf, HTML5 semântico, HTMX, CSS puro
**Pré-requisito:** O Guia 01 (UI/UX Geral) DEVE ser lido e obedecido em conjunção com este documento
**Classificação:** Documento normativo — todas as diretrizes DEVEM ser seguidas na íntegra

---

## SEÇÃO 1 — ANATOMIA DE UM E-COMMERCE: PÁGINAS E HIERARQUIA DE NAVEGAÇÃO

### 1.1 — Mapa de Páginas Obrigatórias

O sistema DEVE conter, no mínimo, as seguintes páginas e seus respectivos templates Thymeleaf:

```
NÍVEL 0 — Raiz
├── Homepage (index)
│
NÍVEL 1 — Catálogo
├── Listagem de Categorias
├── Listagem de Produtos (por categoria, por busca, por filtro)
├── Página de Produto Individual (PDP — Product Detail Page)
│
NÍVEL 2 — Carrinho e Checkout
├── Carrinho de Compras
├── Checkout — Dados Pessoais
├── Checkout — Endereço de Entrega
├── Checkout — Método de Pagamento
├── Checkout — Revisão e Confirmação
├── Página de Confirmação de Pedido (Thank You Page)
│
NÍVEL 3 — Conta do Utilizador
├── Login
├── Registro
├── Minha Conta (Dashboard)
├── Meus Pedidos
├── Detalhes do Pedido
├── Meus Endereços
├── Lista de Desejos (Wishlist)
│
NÍVEL 4 — Informacional
├── Sobre Nós
├── FAQ
├── Política de Privacidade
├── Termos de Uso
├── Contato
├── Página de Erro 404
├── Página de Erro 500
```

### 1.2 — Estrutura de Navegação

```
HEADER (fixo no topo — sticky)
├── Logo (link para homepage)
├── Barra de Busca (search)
├── Navegação Principal (categorias)
├── Ícones de Ação:
│   ├── Wishlist (com contador)
│   ├── Carrinho (com contador e mini-preview)
│   └── Conta/Login
└── Menu Mobile (hamburger — apenas em breakpoints < 1024px)
```

---

## SEÇÃO 2 — HOMEPAGE

### 2.1 — Estrutura e Hierarquia

A homepage DEVE seguir a seguinte estrutura de blocos, nesta ordem exata:

1. **Hero Banner / Destaque Principal** — Imagem de destaque com CTA principal. DEVE ocupar a largura total da viewport. DEVE conter: título (h1), subtítulo, botão de ação primário. Altura recomendada: 60-80vh em desktop, 50vh em mobile.

2. **Categorias em Destaque** — Grid com 3-6 categorias principais. Cada categoria DEVE conter: imagem representativa, nome da categoria, link para a listagem. Usar CSS Grid com `grid-template-columns: repeat(auto-fit, minmax(200px, 1fr))`.

3. **Produtos em Destaque / Novidades** — Grid de produtos com 4-8 itens. Exibir: imagem, nome, preço, badge de novidade se aplicável. Botão "Adicionar ao carrinho" visível no hover ou abaixo do preço.

4. **Banner Secundário / Promoção** — Banner de largura total com oferta ou destaque temático.

5. **Produtos Mais Vendidos** — Grid ou carrossel com os produtos mais populares.

6. **Seção de Confiança** — Ícones com textos curtos: Frete Grátis, Pagamento Seguro, Garantia, Suporte. Usar Flexbox com `justify-content: center` e `gap: var(--space-8)`.

### 2.2 — CSS da Homepage

```css
/* Hero Banner */
.hero {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  padding: var(--space-16) var(--space-6);
  text-align: center;
  overflow: hidden;
}

.hero-background {
  position: absolute;
  inset: 0;
  z-index: -1;
  object-fit: cover;
  width: 100%;
  height: 100%;
  filter: brightness(0.3);
}

.hero-content {
  max-width: 680px;
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.hero-title {
  font-size: var(--font-size-hero);
  font-weight: 700;
  color: var(--text-primary);
  text-wrap: balance;
}

.hero-subtitle {
  font-size: var(--font-size-md);
  color: var(--text-secondary);
}

/* Seção de Categorias */
.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--space-6);
}

.category-card {
  position: relative;
  aspect-ratio: 3/4;
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
}

.category-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--duration-slow) var(--ease-default);
}

.category-card:hover img {
  transform: scale(1.05);
}

.category-card-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: flex-end;
  padding: var(--space-6);
  background: linear-gradient(to top, rgba(0,0,0,0.7) 0%, transparent 60%);
}

/* Seção de confiança */
.trust-badges {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: var(--space-8);
  padding: var(--space-12) var(--space-6);
}

.trust-badge {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
  text-align: center;
  max-width: 180px;
}

.trust-badge-icon {
  width: 48px;
  height: 48px;
  color: var(--color-accent);
}

.trust-badge-title {
  font-weight: 600;
  font-size: var(--font-size-base);
  color: var(--text-primary);
}

.trust-badge-text {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}
```

---

## SEÇÃO 3 — LISTAGEM DE PRODUTOS (PLP — Product Listing Page)

### 3.1 — Estrutura Obrigatória

```
PLP Layout:
├── Breadcrumbs
├── Título da Categoria (h1)
├── Barra de Controle:
│   ├── Contagem de Resultados ("128 produtos encontrados")
│   ├── Ordenação (select: relevância, preço ↑, preço ↓, novidades, mais vendidos)
│   └── Toggle de visualização (grid / lista) — OPCIONAL
├── Área de Conteúdo:
│   ├── Sidebar de Filtros (desktop) / Bottom Sheet de Filtros (mobile)
│   │   ├── Filtro por Preço (range)
│   │   ├── Filtro por Subcategoria
│   │   ├── Filtro por Disponibilidade
│   │   └── Filtro por Atributos Específicos
│   └── Grid de Produtos
└── Paginação ou Infinite Scroll
```

### 3.2 — Card de Produto na Listagem

O card de produto é o componente mais crítico da listagem. DEVE conter, nesta ordem visual:

1. **Imagem do Produto** — aspect-ratio 1:1 ou 3:4, `object-fit: cover`
2. **Badges** (sobrepostos na imagem) — "Novo", "Promoção", "-20%", "Esgotado"
3. **Nome do Produto** — máximo 2 linhas com `text-overflow: ellipsis` via `-webkit-line-clamp: 2`
4. **Preço** — preço atual em destaque; preço original riscado se em promoção
5. **Botão de Ação** — "Adicionar ao Carrinho" ou "Ver Detalhes"
6. **Botão de Wishlist** — ícone de coração no canto superior direito da imagem

```css
/* Card de Produto */
.product-card {
  display: flex;
  flex-direction: column;
  background-color: var(--surface-raised);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: border-color var(--duration-normal) var(--ease-default),
              transform var(--duration-normal) var(--ease-default);
}

.product-card:hover {
  border-color: var(--border-default);
  transform: translateY(-2px);
}

/* Imagem do Produto */
.product-card-image-wrapper {
  position: relative;
  aspect-ratio: 1 / 1;
  overflow: hidden;
}

.product-card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--duration-slow) var(--ease-default);
}

.product-card:hover .product-card-image {
  transform: scale(1.05);
}

/* Badge */
.product-badge {
  position: absolute;
  top: var(--space-3);
  left: var(--space-3);
  padding: var(--space-1) var(--space-3);
  font-size: var(--font-size-xs);
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  border-radius: var(--radius-sm);
}

.product-badge--new {
  background-color: var(--color-accent);
  color: var(--surface-base);
}

.product-badge--sale {
  background-color: var(--color-error);
  color: #ffffff;
}

.product-badge--out-of-stock {
  background-color: var(--color-gray-700);
  color: var(--text-secondary);
}

/* Wishlist */
.product-card-wishlist {
  position: absolute;
  top: var(--space-3);
  right: var(--space-3);
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: var(--radius-full);
  color: var(--text-secondary);
  border: none;
  cursor: pointer;
  transition: color var(--duration-normal) var(--ease-default),
              background-color var(--duration-normal) var(--ease-default);
}

.product-card-wishlist:hover,
.product-card-wishlist[aria-pressed="true"] {
  color: var(--color-error);
  background-color: rgba(0, 0, 0, 0.7);
}

/* Info do Produto */
.product-card-body {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
  padding: var(--space-4);
  flex: 1;
}

.product-card-name {
  font-size: var(--font-size-base);
  font-weight: 500;
  color: var(--text-primary);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

/* Preço */
.product-price {
  display: flex;
  align-items: baseline;
  gap: var(--space-2);
}

.product-price-current {
  font-size: var(--font-size-md);
  font-weight: 700;
  color: var(--color-accent);
}

.product-price-original {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  text-decoration: line-through;
}

.product-price-discount {
  font-size: var(--font-size-xs);
  font-weight: 600;
  color: var(--color-error);
}

/* Botão de adicionar ao carrinho */
.product-card-action {
  margin-top: auto;
  padding-top: var(--space-3);
}
```

### 3.3 — Filtros

```css
/* Sidebar de Filtros */
.filters-sidebar {
  position: sticky;
  top: calc(var(--header-height) + var(--space-6));
  max-height: calc(100dvh - var(--header-height) - var(--space-12));
  overflow-y: auto;
  padding-right: var(--space-4);
}

/* Grupo de Filtro */
.filter-group {
  padding: var(--space-4) 0;
  border-bottom: 1px solid var(--border-subtle);
}

.filter-group-title {
  font-size: var(--font-size-sm);
  font-weight: 600;
  color: var(--text-primary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: var(--space-3);
}

/* Filtro checkbox */
.filter-option {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-1) 0;
  cursor: pointer;
}

.filter-option-count {
  margin-left: auto;
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
}

/* Filtros ativos */
.active-filters {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
}

.active-filter-tag {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-1) var(--space-3);
  font-size: var(--font-size-xs);
  background-color: var(--surface-overlay);
  border: 1px solid var(--border-default);
  border-radius: var(--radius-full);
  color: var(--text-secondary);
}

.active-filter-tag-remove {
  cursor: pointer;
  color: var(--text-tertiary);
  transition: color var(--duration-fast) var(--ease-default);
}

.active-filter-tag-remove:hover {
  color: var(--color-error);
}
```

### 3.4 — Paginação

```css
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-1);
  padding: var(--space-8) 0;
}

.pagination-item {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 44px;
  min-height: 44px;
  padding: var(--space-2);
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  background-color: transparent;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
}

.pagination-item:hover {
  background-color: var(--surface-overlay);
  border-color: var(--border-default);
  color: var(--text-primary);
}

.pagination-item[aria-current="page"] {
  background-color: var(--color-accent);
  border-color: var(--color-accent);
  color: var(--surface-base);
  font-weight: 600;
}
```

---

## SEÇÃO 4 — PÁGINA DE PRODUTO (PDP — Product Detail Page)

### 4.1 — Estrutura Obrigatória

```
PDP Layout:
├── Breadcrumbs
├── Área Principal (2 colunas em desktop):
│   ├── COLUNA ESQUERDA — Galeria de Imagens:
│   │   ├── Imagem Principal (ampliável)
│   │   └── Miniaturas (thumbnails)
│   └── COLUNA DIREITA — Informações:
│       ├── Nome do Produto (h1)
│       ├── Avaliação (estrelas + contagem)
│       ├── Preço (atual, original se em promoção, parcelas)
│       ├── Descrição Curta (2-3 linhas)
│       ├── Variantes (cor, tamanho — seletores)
│       ├── Quantidade (input numérico com +/-)
│       ├── Botão "Adicionar ao Carrinho" (CTA PRIMÁRIO — maior botão da página)
│       ├── Botão "Adicionar à Wishlist" (secundário)
│       ├── Info de Entrega (prazo estimado)
│       └── Garantias / Selos
├── Tabs ou Acordeão:
│   ├── Descrição Completa
│   ├── Especificações Técnicas
│   └── Avaliações dos Clientes
└── Seção: Produtos Relacionados
```

### 4.2 — CSS da Galeria de Imagens

```css
.product-gallery {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
  position: sticky;
  top: calc(var(--header-height) + var(--space-6));
}

.product-gallery-main {
  aspect-ratio: 1 / 1;
  border-radius: var(--radius-lg);
  overflow: hidden;
  background-color: var(--surface-overlay);
  cursor: zoom-in;
}

.product-gallery-main img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.product-gallery-thumbs {
  display: flex;
  gap: var(--space-2);
  overflow-x: auto;
  scrollbar-width: thin;
}

.product-gallery-thumb {
  flex-shrink: 0;
  width: 72px;
  height: 72px;
  border-radius: var(--radius-md);
  overflow: hidden;
  border: 2px solid transparent;
  cursor: pointer;
  transition: border-color var(--duration-normal) var(--ease-default);
  opacity: 0.6;
}

.product-gallery-thumb:hover,
.product-gallery-thumb[aria-selected="true"] {
  border-color: var(--color-accent);
  opacity: 1;
}

.product-gallery-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
```

### 4.3 — Seletores de Variantes

```css
/* Seletor de Cor */
.variant-colors {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.variant-color-swatch {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-full);
  border: 2px solid transparent;
  cursor: pointer;
  transition: border-color var(--duration-normal) var(--ease-default),
              box-shadow var(--duration-normal) var(--ease-default);
  outline-offset: 2px;
}

.variant-color-swatch:hover {
  border-color: var(--border-strong);
}

.variant-color-swatch[aria-checked="true"] {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 2px var(--surface-base), 0 0 0 4px var(--color-accent);
}

/* Seletor de Tamanho */
.variant-sizes {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.variant-size-option {
  min-width: 44px;
  min-height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-2) var(--space-3);
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--text-secondary);
  background-color: var(--surface-overlay);
  border: 1px solid var(--border-default);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
}

.variant-size-option:hover {
  border-color: var(--border-strong);
  color: var(--text-primary);
}

.variant-size-option[aria-checked="true"] {
  background-color: var(--color-accent);
  border-color: var(--color-accent);
  color: var(--surface-base);
}

.variant-size-option[aria-disabled="true"] {
  opacity: 0.3;
  cursor: not-allowed;
  text-decoration: line-through;
}

/* Seletor de Quantidade */
.quantity-selector {
  display: inline-flex;
  align-items: center;
  border: 1px solid var(--border-default);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.quantity-btn {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-md);
  color: var(--text-secondary);
  background-color: var(--surface-overlay);
  border: none;
  cursor: pointer;
  transition: background-color var(--duration-normal) var(--ease-default),
              color var(--duration-normal) var(--ease-default);
}

.quantity-btn:hover {
  background-color: var(--surface-raised);
  color: var(--text-primary);
}

.quantity-input {
  width: 56px;
  height: 44px;
  text-align: center;
  font-size: var(--font-size-base);
  font-weight: 600;
  color: var(--text-primary);
  background-color: var(--surface-raised);
  border: none;
  border-left: 1px solid var(--border-default);
  border-right: 1px solid var(--border-default);
  -moz-appearance: textfield;
}

.quantity-input::-webkit-inner-spin-button,
.quantity-input::-webkit-outer-spin-button {
  -webkit-appearance: none;
}
```

### 4.4 — CTA de Adição ao Carrinho

O botão "Adicionar ao Carrinho" é o elemento mais importante da PDP. DEVE:
- Ser o botão de maior dimensão na página
- Ter cor de destaque máximo (accent color)
- Estar sempre visível sem necessidade de scroll em desktop
- Em mobile, considerar fixá-lo na base da tela com `position: sticky; bottom: 0`
- Fornecer feedback visual e textual ao ser clicado (trocar texto para "Adicionado ✓" por 2 segundos)

```css
.add-to-cart-btn {
  width: 100%;
  padding: var(--space-4) var(--space-8);
  font-size: var(--font-size-md);
  font-weight: 700;
  color: var(--surface-base);
  background-color: var(--color-accent);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background-color var(--duration-normal) var(--ease-default),
              transform var(--duration-fast) var(--ease-default);
  min-height: 56px;
}

.add-to-cart-btn:hover {
  filter: brightness(1.1);
}

.add-to-cart-btn:active {
  transform: scale(0.98);
}

/* Estado de sucesso após adição */
.add-to-cart-btn--success {
  background-color: var(--color-success);
  pointer-events: none;
}

/* Sticky em mobile */
@media (max-width: 767px) {
  .product-actions-sticky {
    position: sticky;
    bottom: 0;
    left: 0;
    right: 0;
    padding: var(--space-4) var(--space-6);
    background-color: var(--surface-raised);
    border-top: 1px solid var(--border-subtle);
    z-index: var(--z-sticky);
  }
}
```

---

## SEÇÃO 5 — CARRINHO DE COMPRAS

### 5.1 — Estrutura do Carrinho

```
Carrinho Layout:
├── Título "Seu Carrinho" (h1) + contagem de itens
├── Lista de Itens:
│   └── Item do Carrinho:
│       ├── Imagem (thumbnail 80x80)
│       ├── Info:
│       │   ├── Nome do Produto
│       │   ├── Variantes (cor, tamanho)
│       │   └── Preço Unitário
│       ├── Seletor de Quantidade
│       ├── Subtotal do Item
│       └── Botão Remover
├── Resumo do Pedido:
│   ├── Subtotal
│   ├── Frete (estimativa ou calculado)
│   ├── Desconto (se houver cupom)
│   ├── Total
│   └── Botão "Finalizar Compra" (CTA PRIMÁRIO)
├── Campo de Cupom de Desconto
└── Link "Continuar Comprando"
```

### 5.2 — Interações HTMX no Carrinho

```html
<!-- Atualizar quantidade via HTMX -->
<div class="cart-item" id="cart-item-${item.id}">
  <div class="quantity-selector">
    <button hx-patch="/cart/items/${item.id}"
            hx-vals='{"quantity": ${item.quantity - 1}}'
            hx-target="#cart-item-${item.id}"
            hx-swap="outerHTML"
            hx-indicator="#cart-loading"
            class="quantity-btn"
            aria-label="Diminuir quantidade">
      −
    </button>
    <input type="number"
           value="${item.quantity}"
           min="1"
           max="99"
           class="quantity-input"
           aria-label="Quantidade"
           hx-patch="/cart/items/${item.id}"
           hx-trigger="change"
           hx-include="this"
           hx-target="#cart-item-${item.id}"
           hx-swap="outerHTML">
    <button hx-patch="/cart/items/${item.id}"
            hx-vals='{"quantity": ${item.quantity + 1}}'
            hx-target="#cart-item-${item.id}"
            hx-swap="outerHTML"
            hx-indicator="#cart-loading"
            class="quantity-btn"
            aria-label="Aumentar quantidade">
      +
    </button>
  </div>

  <!-- Remover item -->
  <button hx-delete="/cart/items/${item.id}"
          hx-target="#cart-item-${item.id}"
          hx-swap="outerHTML swap:200ms"
          hx-confirm="Remover este item do carrinho?"
          class="cart-item-remove"
          aria-label="Remover ${item.name} do carrinho">
    ×
  </button>
</div>

<!-- Resumo do carrinho atualizado automaticamente -->
<div id="cart-summary"
     hx-get="/cart/summary"
     hx-trigger="cart-updated from:body"
     aria-live="polite">
  <!-- Conteúdo do resumo -->
</div>
```

### 5.3 — CSS do Carrinho

```css
/* Item do Carrinho */
.cart-item {
  display: grid;
  grid-template-columns: 80px 1fr auto auto auto;
  gap: var(--space-4);
  align-items: center;
  padding: var(--space-4) 0;
  border-bottom: 1px solid var(--border-subtle);
}

@media (max-width: 767px) {
  .cart-item {
    grid-template-columns: 72px 1fr;
    grid-template-rows: auto auto;
    gap: var(--space-3);
  }
}

.cart-item-image {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-md);
  object-fit: cover;
  background-color: var(--surface-overlay);
}

.cart-item-name {
  font-weight: 500;
  color: var(--text-primary);
}

.cart-item-variant {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
}

.cart-item-subtotal {
  font-weight: 600;
  color: var(--text-primary);
  white-space: nowrap;
}

.cart-item-remove {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-tertiary);
  border: none;
  background: none;
  cursor: pointer;
  border-radius: var(--radius-full);
  transition: color var(--duration-normal) var(--ease-default),
              background-color var(--duration-normal) var(--ease-default);
}

.cart-item-remove:hover {
  color: var(--color-error);
  background-color: rgba(var(--color-error-rgb), 0.1);
}

/* Resumo */
.cart-summary {
  padding: var(--space-6);
  background-color: var(--surface-raised);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  position: sticky;
  top: calc(var(--header-height) + var(--space-6));
}

.cart-summary-row {
  display: flex;
  justify-content: space-between;
  padding: var(--space-2) 0;
  font-size: var(--font-size-base);
  color: var(--text-secondary);
}

.cart-summary-total {
  display: flex;
  justify-content: space-between;
  padding: var(--space-4) 0;
  margin-top: var(--space-4);
  border-top: 1px solid var(--border-default);
  font-size: var(--font-size-lg);
  font-weight: 700;
  color: var(--text-primary);
}
```

---

## SEÇÃO 6 — CHECKOUT

### 6.1 — Princípios do Checkout

O checkout é a área de maior atrito de um e-commerce. As seguintes regras são OBRIGATÓRIAS:

1. **Checkout Linear:** O fluxo DEVE ser linear (etapa 1 → 2 → 3 → 4). NUNCA forçar o utilizador a retroceder involuntariamente.
2. **Indicador de Progresso:** Uma barra ou stepper DEVE ser visível no topo indicando a etapa atual.
3. **Campos Mínimos:** Solicitar APENAS informações estritamente necessárias. Cada campo adicional aumenta a taxa de abandono.
4. **Campos Obrigatórios e Opcionais:** DEVEM ser explicitamente marcados. Usar "(obrigatório)" ou "(opcional)" ao lado do label.
5. **Guest Checkout:** SEMPRE permitir compra sem criação de conta.
6. **Validação Inline:** Validar campos em tempo real (on blur), não apenas no submit. Usar `hx-trigger="blur"` para validação server-side.
7. **Formato de Entrada:** Usar `inputmode` e `autocomplete` corretos em cada campo:
   - Email: `type="email" autocomplete="email" inputmode="email"`
   - Telefone: `type="tel" autocomplete="tel" inputmode="tel"`
   - CEP: `inputmode="numeric" autocomplete="postal-code"`
   - Cartão de crédito: `inputmode="numeric" autocomplete="cc-number"`
8. **Persistência:** Dados preenchidos DEVEM sobreviver a erros de validação e navegação entre etapas.

### 6.2 — Stepper de Progresso

```css
.checkout-stepper {
  display: flex;
  justify-content: center;
  gap: var(--space-1);
  padding: var(--space-6) 0;
}

.checkout-step {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.checkout-step-number {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-sm);
  font-weight: 600;
  border-radius: var(--radius-full);
  border: 2px solid var(--border-default);
  color: var(--text-tertiary);
  transition: all var(--duration-normal) var(--ease-default);
}

.checkout-step--active .checkout-step-number {
  background-color: var(--color-accent);
  border-color: var(--color-accent);
  color: var(--surface-base);
}

.checkout-step--completed .checkout-step-number {
  background-color: var(--color-success);
  border-color: var(--color-success);
  color: #ffffff;
}

.checkout-step-label {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
}

.checkout-step--active .checkout-step-label {
  color: var(--text-primary);
  font-weight: 600;
}

.checkout-step-connector {
  width: 40px;
  height: 2px;
  background-color: var(--border-default);
  margin: 0 var(--space-2);
}

.checkout-step--completed + .checkout-step-connector {
  background-color: var(--color-success);
}
```

---

## SEÇÃO 7 — BUSCA

### 7.1 — Barra de Busca

A busca é um dos componentes mais utilizados em e-commerce. DEVE:

- Estar sempre visível no header em desktop
- Em mobile, abrir como overlay ao tocar no ícone de busca
- Exibir sugestões de autocomplete em tempo real via HTMX
- Incluir sugestões de termos populares quando vazia
- Exibir resultados de produtos com thumbnail, nome e preço

```html
<!-- Busca com Autocomplete HTMX -->
<div class="search-wrapper" role="search">
  <input type="search"
         name="q"
         placeholder="Buscar produtos..."
         autocomplete="off"
         aria-label="Buscar produtos"
         aria-expanded="false"
         aria-controls="search-results"
         aria-autocomplete="list"
         hx-get="/search/suggestions"
         hx-trigger="input changed delay:300ms, search"
         hx-target="#search-results"
         hx-indicator="#search-spinner"
         class="search-input">
  <span id="search-spinner" class="htmx-indicator spinner"></span>
  <div id="search-results"
       role="listbox"
       class="search-dropdown"
       aria-label="Sugestões de busca">
  </div>
</div>
```

---

## SEÇÃO 8 — AVALIAÇÕES E SOCIAL PROOF

### 8.1 — Exibição de Avaliações

Avaliações são o principal fator de confiança em e-commerce. DEVEM:

- Exibir estrelas de forma visual (SVG ou CSS, não texto)
- Mostrar a nota média e o total de avaliações
- Apresentar a distribuição (5★: 68%, 4★: 20%, etc.)
- Permitir ordenação e filtro de avaliações
- Exibir nome do avaliador, data, e variante comprada

### 8.2 — CSS de Estrelas

```css
.rating-stars {
  display: inline-flex;
  gap: 2px;
}

.rating-star {
  width: 16px;
  height: 16px;
  color: var(--color-gray-600);
}

.rating-star--filled {
  color: var(--color-warning);
}

.rating-star--half {
  /* Implementar com gradient ou clip-path */
  background: linear-gradient(90deg, var(--color-warning) 50%, var(--color-gray-600) 50%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.rating-summary {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.rating-score {
  font-size: var(--font-size-lg);
  font-weight: 700;
  color: var(--text-primary);
}

.rating-count {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
}

/* Distribuição de notas */
.rating-distribution {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.rating-bar {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.rating-bar-label {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
  min-width: 20px;
}

.rating-bar-track {
  flex: 1;
  height: 8px;
  background-color: var(--surface-overlay);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.rating-bar-fill {
  height: 100%;
  background-color: var(--color-warning);
  border-radius: var(--radius-full);
  transition: width var(--duration-slow) var(--ease-out);
}

.rating-bar-count {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
  min-width: 30px;
  text-align: right;
}
```

---

## SEÇÃO 9 — PADRÕES DE PERFORMANCE

### 9.1 — Imagens

- Todas as imagens de produto DEVEM usar `loading="lazy"` exceto as above-the-fold
- Definir `width` e `height` explícitos ou `aspect-ratio` para evitar layout shift
- Usar `<picture>` com `srcset` para servir imagens em diferentes resoluções quando possível
- Imagens na listagem: máximo 400px de largura
- Imagens na PDP: máximo 800px de largura para a principal, 100px para thumbnails

### 9.2 — Skeleton Loading

```css
/* Skeleton para cards de produto durante carregamento */
.skeleton {
  background: linear-gradient(
    90deg,
    var(--surface-overlay) 25%,
    var(--surface-raised) 50%,
    var(--surface-overlay) 75%
  );
  background-size: 200% 100%;
  animation: skeleton-shimmer 1.5s infinite;
  border-radius: var(--radius-md);
}

@keyframes skeleton-shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

.skeleton-image {
  aspect-ratio: 1 / 1;
  border-radius: var(--radius-lg);
}

.skeleton-text {
  height: 1em;
  border-radius: var(--radius-sm);
}

.skeleton-text--short {
  width: 60%;
}

.skeleton-text--medium {
  width: 80%;
}
```

---

## SEÇÃO 10 — BREADCRUMBS

Breadcrumbs são OBRIGATÓRIOS em todas as páginas exceto a homepage.

```html
<nav aria-label="Breadcrumb">
  <ol class="breadcrumb" itemscope itemtype="https://schema.org/BreadcrumbList">
    <li class="breadcrumb-item" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
      <a href="/" itemprop="item"><span itemprop="name">Home</span></a>
      <meta itemprop="position" content="1">
    </li>
    <li class="breadcrumb-item" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
      <a href="/categoria" itemprop="item"><span itemprop="name">Card Games</span></a>
      <meta itemprop="position" content="2">
    </li>
    <li class="breadcrumb-item" aria-current="page" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
      <span itemprop="name">Magic: The Gathering - Booster</span>
      <meta itemprop="position" content="3">
    </li>
  </ol>
</nav>
```

```css
.breadcrumb {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-4) 0;
  font-size: var(--font-size-sm);
}

.breadcrumb-item {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  color: var(--text-tertiary);
}

.breadcrumb-item a {
  color: var(--text-secondary);
  text-decoration: none;
  transition: color var(--duration-normal) var(--ease-default);
}

.breadcrumb-item a:hover {
  color: var(--color-accent);
}

.breadcrumb-item[aria-current="page"] {
  color: var(--text-primary);
  font-weight: 500;
}

.breadcrumb-item + .breadcrumb-item::before {
  content: "›";
  color: var(--text-tertiary);
  margin-right: var(--space-1);
}
```
