# GUIA NORMATIVO DE UI/UX — PRINCÍPIOS FUNDAMENTAIS DE DESIGN DE INTERFACE E EXPERIÊNCIA DO USUÁRIO

**Versão:** 1.0
**Stack Tecnológica Alvo:** Spring Boot, Thymeleaf, HTML5 semântico, HTMX, CSS puro (sem frameworks CSS)
**Classificação:** Documento normativo — todas as diretrizes aqui contidas DEVEM ser seguidas na íntegra

---

## SEÇÃO 1 — ARQUITETURA DE CSS COM CUSTOM PROPERTIES (DESIGN TOKENS)

### 1.1 — Estrutura Obrigatória de Variáveis CSS

Todo arquivo CSS do projeto DEVE iniciar com a declaração de design tokens na pseudo-classe `:root`. Estes tokens constituem a fonte única de verdade para todos os valores visuais do sistema. NUNCA utilizar valores hardcoded diretamente em seletores de componentes.

```css
:root {
  /* ============================================================
     CAMADA 1 — TOKENS PRIMITIVOS (valores brutos, sem semântica)
     ============================================================ */

  /* Paleta de Cores Primitivas */
  --color-gray-950: #0a0a0b;
  --color-gray-900: #121214;
  --color-gray-850: #1a1a1e;
  --color-gray-800: #222228;
  --color-gray-700: #2e2e36;
  --color-gray-600: #3d3d47;
  --color-gray-500: #5a5a66;
  --color-gray-400: #8a8a96;
  --color-gray-300: #b0b0ba;
  --color-gray-200: #d4d4dc;
  --color-gray-100: #ededf0;
  --color-gray-50:  #f7f7f9;

  /* Escala Tipográfica (base 16px, escala 1.250 — Major Third) */
  --font-size-xs:   0.64rem;   /* 10.24px */
  --font-size-sm:   0.8rem;    /* 12.8px  */
  --font-size-base: 1rem;      /* 16px    */
  --font-size-md:   1.25rem;   /* 20px    */
  --font-size-lg:   1.563rem;  /* 25px    */
  --font-size-xl:   1.953rem;  /* 31.25px */
  --font-size-2xl:  2.441rem;  /* 39.06px */
  --font-size-3xl:  3.052rem;  /* 48.83px */

  /* Escala de Espaçamento (base 4px) */
  --space-0:  0;
  --space-1:  0.25rem;  /* 4px   */
  --space-2:  0.5rem;   /* 8px   */
  --space-3:  0.75rem;  /* 12px  */
  --space-4:  1rem;     /* 16px  */
  --space-5:  1.25rem;  /* 20px  */
  --space-6:  1.5rem;   /* 24px  */
  --space-8:  2rem;     /* 32px  */
  --space-10: 2.5rem;   /* 40px  */
  --space-12: 3rem;     /* 48px  */
  --space-16: 4rem;     /* 64px  */
  --space-20: 5rem;     /* 80px  */
  --space-24: 6rem;     /* 96px  */

  /* Escala de Border Radius */
  --radius-none: 0;
  --radius-sm:   0.25rem;
  --radius-md:   0.5rem;
  --radius-lg:   0.75rem;
  --radius-xl:   1rem;
  --radius-2xl:  1.5rem;
  --radius-full: 9999px;

  /* Escala de Sombras (usadas com moderação em dark mode) */
  --shadow-sm:  0 1px 2px 0 rgba(0, 0, 0, 0.3);
  --shadow-md:  0 4px 6px -1px rgba(0, 0, 0, 0.4);
  --shadow-lg:  0 10px 15px -3px rgba(0, 0, 0, 0.5);
  --shadow-xl:  0 20px 25px -5px rgba(0, 0, 0, 0.5);

  /* Durações de Transição */
  --duration-fast:   100ms;
  --duration-normal: 200ms;
  --duration-slow:   300ms;
  --duration-slower: 500ms;

  /* Curvas de Easing */
  --ease-default: cubic-bezier(0.4, 0, 0.2, 1);
  --ease-in:      cubic-bezier(0.4, 0, 1, 1);
  --ease-out:     cubic-bezier(0, 0, 0.2, 1);
  --ease-bounce:  cubic-bezier(0.34, 1.56, 0.64, 1);

  /* ============================================================
     CAMADA 2 — TOKENS SEMÂNTICOS (propósito funcional)
     ============================================================ */

  /* Superfícies */
  --surface-base:     var(--color-gray-950);
  --surface-raised:   var(--color-gray-900);
  --surface-overlay:  var(--color-gray-850);
  --surface-sunken:   #050506;

  /* Bordas */
  --border-default:   var(--color-gray-700);
  --border-subtle:    var(--color-gray-800);
  --border-strong:    var(--color-gray-600);

  /* Texto */
  --text-primary:     var(--color-gray-100);
  --text-secondary:   var(--color-gray-400);
  --text-tertiary:    var(--color-gray-500);
  --text-disabled:    var(--color-gray-600);

  /* Layout */
  --content-max-width: 1280px;
  --sidebar-width:     280px;
  --header-height:     64px;
  --footer-height:     auto;

  /* Z-Index Scale */
  --z-base:     1;
  --z-dropdown: 100;
  --z-sticky:   200;
  --z-overlay:  300;
  --z-modal:    400;
  --z-toast:    500;
  --z-tooltip:  600;
}
```

### 1.2 — Regra de Uso de Tokens

- NÍVEL 1 (Primitivos): NUNCA referenciar diretamente em componentes. Servem apenas como base para tokens semânticos.
- NÍVEL 2 (Semânticos): UTILIZAR em componentes. Exemplo: `background-color: var(--surface-raised);`
- Quando um componente necessitar de um valor único, criar um token de componente: `--card-padding: var(--space-6);`

---

## SEÇÃO 2 — PRINCÍPIOS FUNDAMENTAIS DE UI

### 2.1 — Hierarquia Visual

A hierarquia visual é o princípio mais crítico de todo o design de interface. Ela determina a ordem em que o olho do utilizador processa a informação. Os seguintes mecanismos DEVEM ser empregados:

**Tamanho e Peso Tipográfico:**
- Títulos de página: `var(--font-size-2xl)` com `font-weight: 700`
- Subtítulos de seção: `var(--font-size-lg)` com `font-weight: 600`
- Corpo de texto: `var(--font-size-base)` com `font-weight: 400`
- Texto auxiliar / caption: `var(--font-size-sm)` com `font-weight: 400` e cor `var(--text-secondary)`

**Contraste Cromático:**
- Elementos primários (CTAs, títulos): máximo contraste contra o fundo
- Elementos secundários (descrições, labels): contraste médio, usando `var(--text-secondary)`
- Elementos terciários (timestamps, metadados): contraste reduzido, usando `var(--text-tertiary)`

**Espaçamento como Agrupador:**
- Elementos que pertencem ao mesmo grupo semântico DEVEM ter espaçamento interno menor (ex: `var(--space-2)`)
- Grupos distintos DEVEM ser separados por espaçamento maior (ex: `var(--space-8)` ou `var(--space-12)`)
- Esta regra implementa o princípio da proximidade da Gestalt

### 2.2 — Consistência

- Todo botão primário do sistema DEVE ter a mesma aparência, dimensão mínima de toque (44x44px), e comportamento de hover/focus/active
- Todo campo de formulário DEVE seguir o mesmo padrão visual: mesma altura, padding, border-radius, cores de estado
- Todo card DEVE seguir a mesma estrutura de superfície, sombra, e espaçamento interno
- Ícones DEVEM manter o mesmo tamanho e stroke-width dentro de um mesmo contexto

### 2.3 — Feedback Visual

Todo elemento interativo DEVE fornecer feedback visual em TODOS os seguintes estados:

```css
/* PADRÃO OBRIGATÓRIO PARA ELEMENTOS INTERATIVOS */

.interactive-element {
  transition: all var(--duration-normal) var(--ease-default);
}

/* Estado: Hover (mouse sobre o elemento) */
.interactive-element:hover {
  /* Alterar pelo menos UMA propriedade visual: cor, opacidade, transform, border */
}

/* Estado: Focus-visible (navegação por teclado) */
.interactive-element:focus-visible {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
}

/* Estado: Active (clique/toque) */
.interactive-element:active {
  transform: scale(0.97);
  /* OU redução de brilho/opacidade */
}

/* Estado: Disabled */
.interactive-element:disabled,
.interactive-element[aria-disabled="true"] {
  opacity: 0.4;
  cursor: not-allowed;
  pointer-events: none;
}
```

### 2.4 — Espaçamento e Ritmo Vertical

O ritmo vertical consiste em manter um espaçamento previsível e harmônico entre blocos de conteúdo. As seguintes regras são obrigatórias:

- Todo conteúdo de texto corrido DEVE usar `line-height: 1.6` para corpo e `line-height: 1.2` para títulos
- O espaçamento entre parágrafos DEVE ser `var(--space-4)`
- O espaçamento entre seções DEVE ser `var(--space-12)` ou `var(--space-16)`
- Dentro de cards, o padding DEVE ser `var(--space-6)` em todas as direções
- O gap entre cards em um grid DEVE ser `var(--space-6)` ou `var(--space-8)`

---

## SEÇÃO 3 — PRINCÍPIOS FUNDAMENTAIS DE UX

### 3.1 — Regra dos 3 Cliques (Adaptada)

Todo utilizador DEVE ser capaz de alcançar qualquer conteúdo do sistema em no máximo 3 interações a partir da página inicial. Isto se traduz em:

- Navegação principal DEVE conter no máximo 7±2 itens (Lei de Miller)
- Subcategorias DEVEM ser acessíveis em 1 clique a partir da navegação principal
- Produtos individuais DEVEM ser acessíveis em no máximo 2 cliques a partir da navegação

### 3.2 — Lei de Fitts

O tempo para alcançar um alvo é função da distância até o alvo e do tamanho do alvo. Implicações práticas:

- Botões de ação primária DEVEM ser os maiores da tela (mínimo 44x44px, recomendado 48x48px)
- Botões de ação primária DEVEM estar posicionados em zonas de fácil acesso (canto inferior direito em desktop, base da tela em mobile)
- Links em texto corrido DEVEM ter padding vertical adicional para aumentar a área de toque
- Alvos de clique adjacentes DEVEM ter pelo menos `var(--space-2)` de separação

### 3.3 — Lei de Hick

O tempo de decisão aumenta logaritmicamente com o número de opções. Implicações:

- Menus DEVEM agrupar itens em categorias semânticas
- Formulários DEVEM ser divididos em etapas (checkout multi-step)
- Filtros DEVEM ser colapsáveis e apresentar contagens de resultados
- Nunca apresentar mais de 5-7 opções visíveis simultaneamente sem agrupamento

### 3.4 — Carga Cognitiva

- Todo formulário DEVE ter labels visíveis acima dos campos (NUNCA usar apenas placeholder como label)
- Campos de formulário DEVEM ter texto auxiliar explicando formato esperado quando não óbvio
- Mensagens de erro DEVEM ser posicionadas imediatamente abaixo do campo com erro
- Mensagens de erro DEVEM explicar como corrigir, não apenas que há erro
- Indicadores de progresso DEVEM ser exibidos em processos multi-step

### 3.5 — Acessibilidade (WCAG 2.1 AA)

As seguintes regras são OBRIGATÓRIAS:

**Contraste:**
- Texto normal: razão mínima de contraste 4.5:1 contra o fundo
- Texto grande (≥18pt ou ≥14pt bold): razão mínima 3:1
- Elementos de interface (bordas de campos, ícones): razão mínima 3:1

**Navegação por Teclado:**
- Todo elemento interativo DEVE ser alcançável via Tab
- A ordem de tabulação DEVE seguir a ordem visual de leitura
- Focus DEVE ser sempre visível (`:focus-visible`)
- Modais DEVEM implementar focus trap

**Semântica HTML:**
```html
<!-- CORRETO: uso de landmarks e semântica -->
<header role="banner">
  <nav role="navigation" aria-label="Navegação principal">
    <ul>
      <li><a href="/produtos">Produtos</a></li>
    </ul>
  </nav>
</header>
<main role="main">
  <section aria-labelledby="section-title">
    <h2 id="section-title">Novidades</h2>
  </section>
</main>
<footer role="contentinfo"></footer>

<!-- INCORRETO: divs genéricas sem semântica -->
<div class="header">
  <div class="nav">
    <div class="link">Produtos</div>
  </div>
</div>
```

**ARIA quando necessário:**
- `aria-label` em botões que contenham apenas ícones
- `aria-expanded` em menus colapsáveis
- `aria-current="page"` no link de navegação ativo
- `aria-live="polite"` em regiões de conteúdo dinâmico (respostas HTMX)
- `role="alert"` em mensagens de erro de formulário

---

## SEÇÃO 4 — LAYOUT E RESPONSIVIDADE

### 4.1 — Sistema de Grid com CSS Grid

```css
/* Grid principal do site */
.page-grid {
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: var(--header-height) 1fr auto;
  min-height: 100dvh;
}

/* Grid de conteúdo com sidebar */
.content-with-sidebar {
  display: grid;
  grid-template-columns: var(--sidebar-width) 1fr;
  gap: var(--space-8);
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: 0 var(--space-6);
}

/* Grid de produtos — adaptativo */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: var(--space-6);
}

/* Container centralizado */
.container {
  width: 100%;
  max-width: var(--content-max-width);
  margin-inline: auto;
  padding-inline: var(--space-6);
}
```

### 4.2 — Breakpoints

Os breakpoints do projeto DEVEM ser definidos conforme a seguinte escala:

```css
/* Mobile first — os estilos base são para mobile */

/* Tablets pequenos */
@media (min-width: 640px) { /* sm */ }

/* Tablets */
@media (min-width: 768px) { /* md */ }

/* Laptops */
@media (min-width: 1024px) { /* lg */ }

/* Desktops */
@media (min-width: 1280px) { /* xl */ }

/* Monitores grandes */
@media (min-width: 1536px) { /* 2xl */ }
```

A abordagem DEVE ser mobile-first: estilos base para telas pequenas, media queries expandem para telas maiores.

### 4.3 — Tipografia Responsiva com clamp()

```css
:root {
  --font-size-hero: clamp(2rem, 5vw + 1rem, 3.5rem);
  --font-size-h1:   clamp(1.75rem, 3vw + 0.5rem, 2.5rem);
  --font-size-h2:   clamp(1.25rem, 2vw + 0.5rem, 1.75rem);
}
```

---

## SEÇÃO 5 — COMPONENTES BASE (PADRÕES OBRIGATÓRIOS)

### 5.1 — Botões

```css
/* Reset e base */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-6);
  font-family: inherit;
  font-size: var(--font-size-base);
  font-weight: 600;
  line-height: 1;
  text-decoration: none;
  border: 1px solid transparent;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  min-height: 44px;
  min-width: 44px;
}

.btn:focus-visible {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
}

.btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  pointer-events: none;
}
```

### 5.2 — Campos de Formulário

```css
.form-field {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.form-label {
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--text-secondary);
}

.form-input {
  padding: var(--space-3) var(--space-4);
  font-family: inherit;
  font-size: var(--font-size-base);
  color: var(--text-primary);
  background-color: var(--surface-sunken);
  border: 1px solid var(--border-default);
  border-radius: var(--radius-md);
  transition: border-color var(--duration-normal) var(--ease-default);
  min-height: 44px;
}

.form-input:hover {
  border-color: var(--border-strong);
}

.form-input:focus {
  outline: none;
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(var(--color-accent-rgb), 0.15);
}

.form-input[aria-invalid="true"] {
  border-color: var(--color-error);
}

.form-error {
  font-size: var(--font-size-sm);
  color: var(--color-error);
}

.form-hint {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
}
```

### 5.3 — Cards

```css
.card {
  background-color: var(--surface-raised);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: border-color var(--duration-normal) var(--ease-default),
              transform var(--duration-normal) var(--ease-default);
}

.card:hover {
  border-color: var(--border-default);
}

.card-body {
  padding: var(--space-6);
}

.card-image {
  width: 100%;
  aspect-ratio: 4/3;
  object-fit: cover;
}
```

---

## SEÇÃO 6 — HTMX — PADRÕES DE INTERAÇÃO E FEEDBACK

### 6.1 — Indicadores de Carregamento

Toda requisição HTMX DEVE fornecer feedback visual durante o carregamento:

```html
<!-- Botão com indicador de carregamento -->
<button hx-post="/api/acao"
        hx-target="#resultado"
        hx-indicator="#spinner-acao"
        class="btn btn-primary">
  Confirmar
  <span id="spinner-acao" class="htmx-indicator">
    <!-- SVG spinner ou CSS animation -->
  </span>
</button>
```

```css
/* Indicador HTMX — oculto por padrão */
.htmx-indicator {
  display: none;
}

/* Visível quando HTMX adiciona a classe */
.htmx-request .htmx-indicator,
.htmx-request.htmx-indicator {
  display: inline-flex;
}

/* Animação de carregamento */
@keyframes spin {
  to { transform: rotate(360deg); }
}

.spinner {
  width: 1em;
  height: 1em;
  border: 2px solid var(--border-default);
  border-top-color: var(--color-accent);
  border-radius: var(--radius-full);
  animation: spin 0.6s linear infinite;
}
```

### 6.2 — Transições de Conteúdo

```css
/* Fade-in para conteúdo carregado via HTMX */
.htmx-added {
  animation: fadeIn var(--duration-slow) var(--ease-out);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(4px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Swap com transição suave */
.htmx-swapping {
  opacity: 0.5;
  transition: opacity var(--duration-fast) var(--ease-default);
}
```

### 6.3 — Regiões Acessíveis para Conteúdo Dinâmico

```html
<!-- Região que recebe atualizações HTMX -->
<div id="resultado"
     aria-live="polite"
     aria-atomic="true"
     role="region"
     aria-label="Resultados da pesquisa">
</div>

<!-- Toast de notificação para ações -->
<div id="toast-container"
     aria-live="assertive"
     role="alert"
     class="toast-container">
</div>
```

---

## SEÇÃO 7 — MICRO-INTERAÇÕES OBRIGATÓRIAS

Micro-interações são pequenas animações ou transições que fornecem feedback, orientação e delícia ao utilizador. As seguintes micro-interações são OBRIGATÓRIAS:

1. **Hover em links e botões:** mudança de cor com transição de 200ms
2. **Adição ao carrinho:** animação de confirmação (checkmark, pulse, ou slide)
3. **Campos de formulário em focus:** glow sutil ou mudança de borda
4. **Loading de conteúdo HTMX:** skeleton ou spinner
5. **Troca de quantidade no carrinho:** highlight temporário no subtotal
6. **Mensagens de sucesso/erro:** slide-in com auto-dismiss após 5 segundos
7. **Scroll suave:** `scroll-behavior: smooth` no `html`
8. **Imagens de produto:** transição suave de carregamento com placeholder de baixa resolução ou skeleton

---

## SEÇÃO 8 — RESET CSS OBRIGATÓRIO

O seguinte reset DEVE ser aplicado antes de qualquer outro estilo:

```css
/* Reset moderno e mínimo */
*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html {
  scroll-behavior: smooth;
  -webkit-text-size-adjust: 100%;
  text-size-adjust: 100%;
}

body {
  min-height: 100dvh;
  font-family: var(--font-body);
  font-size: var(--font-size-base);
  line-height: 1.6;
  color: var(--text-primary);
  background-color: var(--surface-base);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

img,
picture,
video,
canvas,
svg {
  display: block;
  max-width: 100%;
  height: auto;
}

input,
button,
textarea,
select {
  font: inherit;
  color: inherit;
}

button {
  cursor: pointer;
  background: none;
  border: none;
}

a {
  color: inherit;
  text-decoration: none;
}

ul,
ol {
  list-style: none;
}

h1, h2, h3, h4, h5, h6 {
  font-size: inherit;
  font-weight: inherit;
  line-height: 1.2;
  text-wrap: balance;
}

p {
  text-wrap: pretty;
}

table {
  border-collapse: collapse;
  border-spacing: 0;
}

/* Remover animações para quem prefere movimento reduzido */
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}
```

---

## SEÇÃO 9 — CHECKLIST DE QUALIDADE

Antes de considerar qualquer página como finalizada, verificar TODOS os itens:

- [ ] Hierarquia visual clara: títulos > subtítulos > corpo > auxiliar
- [ ] Contraste WCAG AA em todos os textos e elementos de interface
- [ ] Todos os elementos interativos têm estados hover, focus-visible, active, disabled
- [ ] Navegação por teclado funcional em toda a página
- [ ] Labels visíveis em todos os campos de formulário
- [ ] Mensagens de erro contextuais e construtivas
- [ ] Indicadores de carregamento em todas as requisições HTMX
- [ ] `aria-live` em regiões de conteúdo dinâmico
- [ ] Layout responsivo testado em 320px, 768px, 1024px, 1280px
- [ ] Tipografia legível: line-height 1.6, comprimento de linha 45-75 caracteres
- [ ] Espaçamento consistente usando tokens do sistema
- [ ] Imagens com `alt` descritivo ou `alt=""` se decorativas
- [ ] Sem scroll horizontal em nenhum breakpoint
- [ ] Touch targets mínimos de 44x44px em mobile
- [ ] Tempo de transição nunca superior a 500ms
- [ ] Cores de accent com saturação reduzida (para dark mode)
