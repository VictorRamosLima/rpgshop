# GUIA NORMATIVO DE UI/UX — ESTÉTICA MEDIEVAL, RPG E CULTURA NERD

**Versão:** 1.0
**Stack Tecnológica Alvo:** Spring Boot, Thymeleaf, HTML5 semântico, HTMX, CSS puro
**Pré-requisito:** Os Guias 01 (UI/UX Geral), 02 (E-Commerce), e 03 (Dark Mode) DEVEM ser lidos e obedecidos em conjunção com este documento
**Classificação:** Documento normativo — todas as diretrizes DEVEM ser seguidas na íntegra
**Contexto do Projeto:** E-commerce temático de produtos nerds, incluindo RPG de mesa (Dungeons & Dragons, Pathfinder, Call of Cthulhu), card games (Magic: The Gathering, Pokémon TCG, Yu-Gi-Oh!), board games, miniaturas, dados, acessórios, livros de fantasia e ficção científica, camisetas temáticas, action figures, e demais produtos do universo geek/nerd.

---

## SEÇÃO 1 — IDENTIDADE VISUAL: DARK FANTASY MEDIEVAL

### 1.1 — Conceito Estético

A estética deste e-commerce é denominada **"Dark Fantasy Medieval"** — uma fusão entre:

- **Medieval tardio europeu** (séculos XII–XV): pedra, couro, ferro forjado, pergaminho, heráldica
- **Alta fantasia** (Tolkien, D&D, Warhammer): magia, runas, guildas, tavernas
- **Dark fantasy** (Dark Souls, The Witcher, Diablo): atmosfera sombria, cores profundas, texturas rústicas

O resultado visual DEVE evocar a sensação de estar dentro de uma **guilda de aventureiros** ou de uma **loja de artefatos mágicos em uma cidade medieval** — escura, misteriosa, rica em detalhes, mas funcional e organizada.

### 1.2 — Princípio de Equilíbrio: Temático vs. Funcional

**REGRA CRÍTICA:** A estética medieval DEVE ser aplicada como CAMADA DECORATIVA sobre uma base funcional sólida de e-commerce moderno. NUNCA sacrificar usabilidade, acessibilidade, ou performance em nome da estética.

A proporção ideal é:
- **80% funcionalidade e-commerce moderna** — grid limpo, tipografia legível, fluxos claros
- **20% decoração temática** — bordas ornamentadas, texturas sutis, tipografia estilizada em pontos estratégicos, paleta medieval

Os elementos temáticos DEVEM ser concentrados em:
- Cabeçalhos e títulos de seção
- Bordas decorativas em containers principais
- Ícones e separadores temáticos
- Cores e texturas de fundo
- Elementos de gamificação (se houver)

Os elementos temáticos NÃO DEVEM afetar:
- Legibilidade do texto de corpo
- Funcionalidade de formulários
- Processo de checkout
- Navegação principal
- Campos de busca

---

## SEÇÃO 2 — PALETA DE CORES: DARK FANTASY

### 2.1 — Cores Definidas

As cores do projeto DEVEM ser EXATAMENTE estas (sobrescrevendo os tokens genéricos do Guia 01):

```css
:root {
  /* ============================================================
     PALETA DARK FANTASY — CORES PRIMITIVAS
     ============================================================ */

  /* Superfícies — Tons de marrom escuríssimo com matiz quente
     (NÃO cinza neutro — a matiz quente evoca madeira escura, couro, e pedra) */
  --color-bg-deepest:   #07060a;  /* Escuridão absoluta — como uma masmorra */
  --color-bg-deep:      #0c0b10;  /* Fundo principal — como pedra escura */
  --color-bg-base:      #13111a;  /* Cards — como couro envelhecido escuro */
  --color-bg-raised:    #1b1824;  /* Overlay — como madeira de carvalho escura */
  --color-bg-elevated:  #24202f;  /* Hover/dropdown — como veludo escuro */

  /* Dourado — cor primária/accent (ouro, moedas, recompensas, heráldica)
     ESTA É A COR MAIS IMPORTANTE DO PROJETO */
  --color-gold-100: #fdf4dc;   /* Quase branco dourado — highlights */
  --color-gold-200: #f5e1a4;   /* Dourado claro — texto de destaque */
  --color-gold-300: #e8c86a;   /* Dourado médio — ícones ativos */
  --color-gold-400: #d4a83a;   /* Dourado principal — botões primários, links */
  --color-gold-500: #b8912e;   /* Dourado escuro — hover de botões */
  --color-gold-600: #96741f;   /* Dourado profundo — bordas ativas */
  --color-gold-700: #6b5216;   /* Dourado apagado — bordas sutis */
  --color-gold-800: #3d2f0d;   /* Dourado sombra — fundo de badges */

  /* Carmesim — cor secundária (sangue, fogo, magia ofensiva, promoções) */
  --color-crimson-300: #e8605a;
  --color-crimson-400: #c44040;
  --color-crimson-500: #9c2d2d;
  --color-crimson-600: #731f1f;
  --color-crimson-700: #4a1414;

  /* Esmeralda — cor terciária (natureza, cura, sucesso, disponibilidade) */
  --color-emerald-300: #5ab87a;
  --color-emerald-400: #3d8f5a;
  --color-emerald-500: #2a6b40;
  --color-emerald-600: #1a4d2c;

  /* Ametista — cor quaternária (magia, mistério, raridade, épico) */
  --color-amethyst-300: #9b7ed4;
  --color-amethyst-400: #7c5cb8;
  --color-amethyst-500: #5e3f96;
  --color-amethyst-600: #432b6e;

  /* Âmbar — alertas, avisos, estoque baixo */
  --color-amber-300: #e8b84a;
  --color-amber-400: #c9981a;
  --color-amber-500: #a67c10;

  /* Azul Gelo — informação, links, arcano frio */
  --color-ice-300: #6aaad4;
  --color-ice-400: #4a8ab8;
  --color-ice-500: #2d6a96;

  /* Textos */
  --color-text-bright:  #ede8d8;  /* Off-white com matiz de pergaminho */
  --color-text-normal:  #c4bda8;  /* Pergaminho médio */
  --color-text-muted:   #8a8272;  /* Pedra erodida */
  --color-text-faint:   #5c5648;  /* Sombra de texto */

  /* Bordas */
  --color-border-subtle:  rgba(212, 168, 58, 0.06);  /* Borda dourada quase invisível */
  --color-border-default: rgba(212, 168, 58, 0.12);  /* Borda dourada sutil */
  --color-border-strong:  rgba(212, 168, 58, 0.20);  /* Borda dourada evidente */
  --color-border-accent:  rgba(212, 168, 58, 0.35);  /* Borda dourada decorativa */

  /* ============================================================
     MAPEAMENTO SEMÂNTICO
     ============================================================ */

  --surface-base:     var(--color-bg-deep);
  --surface-raised:   var(--color-bg-base);
  --surface-overlay:  var(--color-bg-raised);
  --surface-elevated: var(--color-bg-elevated);
  --surface-sunken:   var(--color-bg-deepest);

  --text-primary:    var(--color-text-bright);
  --text-secondary:  var(--color-text-normal);
  --text-tertiary:   var(--color-text-muted);
  --text-disabled:   var(--color-text-faint);

  --border-subtle:   var(--color-border-subtle);
  --border-default:  var(--color-border-default);
  --border-strong:   var(--color-border-strong);

  --color-accent:      var(--color-gold-400);
  --color-accent-hover: var(--color-gold-500);
  --color-accent-glow:  rgba(212, 168, 58, 0.15);
  --color-accent-rgb:   212, 168, 58;

  --color-success:  var(--color-emerald-400);
  --color-error:    var(--color-crimson-400);
  --color-warning:  var(--color-amber-400);
  --color-info:     var(--color-ice-400);
}
```

### 2.2 — Uso Semântico das Cores Temáticas

| Cor          | Uso no E-Commerce                                              | Equivalente em RPG                   |
|-------------|----------------------------------------------------------------|--------------------------------------|
| Dourado     | CTA primário, preços, links, badges de destaque, favoritos     | Ouro, recompensa, loot, épico        |
| Carmesim    | Promoções, desconto, alertas críticos, "esgotado", remover     | Fogo, sangue, dano, perigo           |
| Esmeralda   | Disponível, sucesso, confirmação, "em estoque"                 | Cura, natureza, vida, stamina        |
| Ametista    | Itens premium, exclusivos, pré-venda, colecionáveis            | Magia, raridade, arcano, épico       |
| Âmbar       | Aviso, estoque baixo, "últimas unidades", atenção              | Alerta, quest marker, loot raro      |
| Azul Gelo   | Informação neutra, links secundários, filtros                  | Mana, gelo, arcano frio, sabedoria   |

---

## SEÇÃO 3 — TIPOGRAFIA MEDIEVAL

### 3.1 — Seleção de Fontes

O projeto DEVE utilizar fontes do Google Fonts para garantir carregamento confiável e gratuito:

```css
/* Importação de Fontes */
@import url('https://fonts.googleapis.com/css2?family=Cinzel:wght@400;500;600;700&family=Cinzel+Decorative:wght@400;700&family=Crimson+Pro:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400;1,500&family=Inter:wght@400;500;600;700&display=swap');

:root {
  /* Fonte display — para títulos de página, nomes de seção, logotipo
     Cinzel: inspirada em inscrições romanas e capitulares medievais.
     Letras ALL-CAPS com serifas clássicas e elegantes. */
  --font-display: 'Cinzel', 'Georgia', serif;

  /* Fonte display decorativa — APENAS para o título do site (logotipo) e
     ocasionais headers hero. NÃO usar em texto extenso.
     Cinzel Decorative: versão ornamentada da Cinzel. */
  --font-display-ornate: 'Cinzel Decorative', 'Cinzel', serif;

  /* Fonte de corpo — para todo texto de leitura contínua
     Crimson Pro: serif elegante com excelente legibilidade em corpo.
     Evoca livros antigos e manuscritos sem sacrificar legibilidade moderna. */
  --font-body: 'Crimson Pro', 'Georgia', 'Times New Roman', serif;

  /* Fonte utilitária — para UI elements, labels de formulários, contadores,
     preços, dados técnicos, botões.
     Inter: sans-serif de altíssima legibilidade para informação funcional. */
  --font-ui: 'Inter', system-ui, -apple-system, sans-serif;
}
```

### 3.2 — Aplicação das Fontes

```css
/* Títulos de Página (h1) */
h1, .page-title {
  font-family: var(--font-display);
  font-weight: 700;
  font-size: var(--font-size-2xl);
  color: var(--color-gold-200);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  line-height: 1.2;
}

/* Subtítulos de Seção (h2) */
h2, .section-title {
  font-family: var(--font-display);
  font-weight: 600;
  font-size: var(--font-size-lg);
  color: var(--color-text-bright);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  line-height: 1.3;
}

/* Subtítulos menores (h3) */
h3 {
  font-family: var(--font-display);
  font-weight: 500;
  font-size: var(--font-size-md);
  color: var(--color-text-bright);
  letter-spacing: 0.03em;
}

/* Corpo de texto */
p, .body-text {
  font-family: var(--font-body);
  font-weight: 400;
  font-size: var(--font-size-base);
  color: var(--color-text-normal);
  line-height: 1.7;
}

/* Nomes de produto (no card e na PDP) */
.product-name {
  font-family: var(--font-display);
  font-weight: 600;
  font-size: var(--font-size-base);
  color: var(--color-text-bright);
  letter-spacing: 0.02em;
}

/* Preços */
.price {
  font-family: var(--font-ui);
  font-weight: 700;
  color: var(--color-gold-300);
}

/* Labels de formulário, botões, UI funcional */
.form-label,
.btn,
.badge,
.nav-link,
.breadcrumb,
.filter-label,
.pagination {
  font-family: var(--font-ui);
}

/* Logotipo do site */
.site-logo {
  font-family: var(--font-display-ornate);
  font-weight: 700;
  font-size: var(--font-size-xl);
  color: var(--color-gold-300);
  text-transform: uppercase;
  letter-spacing: 0.12em;
}
```

### 3.3 — Regras Tipográficas Estritas

1. `--font-display-ornate` é EXCLUSIVA para o logotipo e, opcionalmente, o título hero da homepage. NUNCA usar em mais nada.
2. `--font-display` (Cinzel) é para títulos h1, h2, h3, nomes de produto, e nomes de categoria. SEMPRE em uppercase ou small-caps.
3. `--font-body` (Crimson Pro) é para descrições de produto, textos informativos, reviews, e qualquer bloco de leitura contínua.
4. `--font-ui` (Inter) é para TODA informação funcional: botões, labels, preços, badges, navegação, breadcrumbs, filtros, contadores.
5. NUNCA usar mais de 2 fontes na mesma "zona visual" (ex: um card não deve ter mais de 2 famílias tipográficas).

---

## SEÇÃO 4 — ELEMENTOS DECORATIVOS MEDIEVAIS (CSS PURO)

### 4.1 — Separadores Ornamentados

```css
/* Separador temático entre seções — losango dourado */
.divider-ornate {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-8) 0;
}

.divider-ornate::before,
.divider-ornate::after {
  content: "";
  flex: 1;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent 0%,
    var(--color-gold-700) 20%,
    var(--color-gold-600) 50%,
    var(--color-gold-700) 80%,
    transparent 100%
  );
}

.divider-ornate-symbol {
  width: 12px;
  height: 12px;
  background-color: var(--color-gold-600);
  transform: rotate(45deg);
  flex-shrink: 0;
}

/* Variante com três losangos */
.divider-ornate--triple .divider-ornate-symbol {
  box-shadow:
    -20px 0 0 -2px var(--color-gold-700),
     20px 0 0 -2px var(--color-gold-700);
}
```

### 4.2 — Bordas Decorativas para Containers Principais

```css
/* Borda dupla estilo medieval — para containers hero e seções principais */
.medieval-border {
  border: 1px solid var(--color-border-accent);
  padding: 3px;
  background-color: transparent;
}

.medieval-border-inner {
  border: 1px solid var(--color-border-default);
  padding: var(--space-8);
  background-color: var(--surface-raised);
}

/* Borda com cantos decorativos (CSS puro com pseudo-elementos) */
.corner-decorated {
  position: relative;
  border: 1px solid var(--color-border-default);
}

.corner-decorated::before,
.corner-decorated::after {
  content: "";
  position: absolute;
  width: 16px;
  height: 16px;
  border: 2px solid var(--color-gold-600);
}

.corner-decorated::before {
  top: -2px;
  left: -2px;
  border-right: none;
  border-bottom: none;
}

.corner-decorated::after {
  top: -2px;
  right: -2px;
  border-left: none;
  border-bottom: none;
}

/* Pseudo-elementos adicionais para cantos inferiores (requer wrapper) */
.corner-decorated-bottom::before {
  bottom: -2px;
  left: -2px;
  top: auto;
  border-right: none;
  border-top: none;
  border-bottom: 2px solid var(--color-gold-600);
  border-left: 2px solid var(--color-gold-600);
}

.corner-decorated-bottom::after {
  bottom: -2px;
  right: -2px;
  top: auto;
  border-left: none;
  border-top: none;
  border-bottom: 2px solid var(--color-gold-600);
  border-right: 2px solid var(--color-gold-600);
}
```

### 4.3 — Efeito de Brilho Dourado (Glow)

```css
/* Glow dourado para elementos de destaque */
.golden-glow {
  box-shadow: 0 0 20px -5px rgba(212, 168, 58, 0.15),
              0 0 40px -10px rgba(212, 168, 58, 0.08);
}

/* Glow em texto (para títulos hero) */
.text-glow {
  text-shadow: 0 0 30px rgba(212, 168, 58, 0.2),
               0 0 60px rgba(212, 168, 58, 0.08);
}

/* Glow animado sutil (para CTAs e itens especiais) */
@keyframes pulse-glow {
  0%, 100% {
    box-shadow: 0 0 15px -5px rgba(212, 168, 58, 0.15);
  }
  50% {
    box-shadow: 0 0 25px -5px rgba(212, 168, 58, 0.25);
  }
}

.glow-pulse {
  animation: pulse-glow 3s ease-in-out infinite;
}
```

### 4.4 — Texturas Sutis via CSS

```css
/* Textura de ruído sutil — simula pergaminho/pedra */
.texture-noise {
  position: relative;
}

.texture-noise::after {
  content: "";
  position: absolute;
  inset: 0;
  opacity: 0.015;  /* MUITO sutil — quase imperceptível conscientemente */
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)'/%3E%3C/svg%3E");
  pointer-events: none;
  z-index: 0;
}

/* Gradiente atmosférico no body — simula profundidade de masmorra */
body {
  background-color: var(--surface-base);
  background-image:
    radial-gradient(
      ellipse at 50% 0%,
      rgba(212, 168, 58, 0.03) 0%,
      transparent 60%
    );
  background-attachment: fixed;
}
```

---

## SEÇÃO 5 — COMPONENTES TEMÁTICOS

### 5.1 — Botão Primário (Estilo Ouro Medieval)

```css
.btn-primary {
  font-family: var(--font-ui);
  font-weight: 600;
  font-size: var(--font-size-base);
  padding: var(--space-3) var(--space-6);
  color: var(--color-bg-deepest);
  background: linear-gradient(
    180deg,
    var(--color-gold-300) 0%,
    var(--color-gold-400) 50%,
    var(--color-gold-500) 100%
  );
  border: 1px solid var(--color-gold-500);
  border-radius: var(--radius-sm);
  text-transform: uppercase;
  letter-spacing: 0.06em;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3),
              inset 0 1px 0 rgba(255, 255, 255, 0.15);
  min-height: 44px;
}

.btn-primary:hover {
  background: linear-gradient(
    180deg,
    var(--color-gold-200) 0%,
    var(--color-gold-300) 50%,
    var(--color-gold-400) 100%
  );
  box-shadow: 0 2px 8px rgba(212, 168, 58, 0.25),
              0 1px 3px rgba(0, 0, 0, 0.3),
              inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.btn-primary:active {
  background: linear-gradient(
    180deg,
    var(--color-gold-500) 0%,
    var(--color-gold-400) 100%
  );
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
  transform: translateY(1px);
}

.btn-primary:focus-visible {
  outline: 2px solid var(--color-gold-300);
  outline-offset: 2px;
}
```

### 5.2 — Botão Secundário (Estilo Pedra/Borda)

```css
.btn-secondary {
  font-family: var(--font-ui);
  font-weight: 500;
  font-size: var(--font-size-base);
  padding: var(--space-3) var(--space-6);
  color: var(--color-gold-300);
  background-color: transparent;
  border: 1px solid var(--color-gold-700);
  border-radius: var(--radius-sm);
  text-transform: uppercase;
  letter-spacing: 0.04em;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  min-height: 44px;
}

.btn-secondary:hover {
  border-color: var(--color-gold-500);
  background-color: rgba(212, 168, 58, 0.06);
  color: var(--color-gold-200);
}

.btn-secondary:active {
  background-color: rgba(212, 168, 58, 0.10);
}
```

### 5.3 — Card de Produto Temático

```css
.product-card {
  background-color: var(--surface-raised);
  border: 1px solid var(--color-border-default);
  border-radius: var(--radius-md);
  overflow: hidden;
  transition: all var(--duration-normal) var(--ease-default);
  position: relative;
}

.product-card:hover {
  border-color: var(--color-border-strong);
  box-shadow: 0 0 20px -5px rgba(212, 168, 58, 0.10);
  transform: translateY(-2px);
}

.product-card-name {
  font-family: var(--font-display);
  font-weight: 600;
  font-size: var(--font-size-base);
  color: var(--color-text-bright);
  text-transform: uppercase;
  letter-spacing: 0.02em;
}

.product-card-price {
  font-family: var(--font-ui);
  font-weight: 700;
  font-size: var(--font-size-md);
  color: var(--color-gold-300);
}
```

### 5.4 — Badges de Categoria/Raridade (Inspirados em RPG)

Badges que categorizam produtos por tipo ou destaque, usando terminologia e cor de RPG:

```css
/* Badge base */
.badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-1) var(--space-3);
  font-family: var(--font-ui);
  font-size: var(--font-size-xs);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  border-radius: var(--radius-sm);
  border: 1px solid;
}

/* Comum (cinza) */
.badge--common {
  color: var(--color-text-muted);
  background-color: rgba(138, 130, 114, 0.08);
  border-color: rgba(138, 130, 114, 0.20);
}

/* Incomum (verde) */
.badge--uncommon {
  color: var(--color-emerald-300);
  background-color: rgba(90, 184, 122, 0.08);
  border-color: rgba(90, 184, 122, 0.20);
}

/* Raro (azul) */
.badge--rare {
  color: var(--color-ice-300);
  background-color: rgba(106, 170, 212, 0.08);
  border-color: rgba(106, 170, 212, 0.20);
}

/* Épico (roxo) */
.badge--epic {
  color: var(--color-amethyst-300);
  background-color: rgba(155, 126, 212, 0.08);
  border-color: rgba(155, 126, 212, 0.20);
}

/* Lendário (dourado) */
.badge--legendary {
  color: var(--color-gold-200);
  background-color: rgba(212, 168, 58, 0.10);
  border-color: rgba(212, 168, 58, 0.25);
  box-shadow: 0 0 8px -2px rgba(212, 168, 58, 0.15);
}

/* Promoção / Desconto (vermelho) */
.badge--sale {
  color: #ffffff;
  background-color: var(--color-crimson-500);
  border-color: var(--color-crimson-400);
}

/* Novo */
.badge--new {
  color: var(--color-bg-deepest);
  background-color: var(--color-gold-400);
  border-color: var(--color-gold-500);
}

/* Esgotado */
.badge--sold-out {
  color: var(--color-text-muted);
  background-color: rgba(92, 86, 72, 0.15);
  border-color: rgba(92, 86, 72, 0.25);
}

/* Pré-venda */
.badge--preorder {
  color: var(--color-amethyst-300);
  background-color: rgba(155, 126, 212, 0.12);
  border-color: rgba(155, 126, 212, 0.25);
}
```

### 5.5 — Cabeçalho de Seção Temático

```css
.section-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: var(--space-3);
  padding: var(--space-12) 0 var(--space-8);
}

.section-header-title {
  font-family: var(--font-display);
  font-weight: 700;
  font-size: var(--font-size-xl);
  color: var(--color-gold-200);
  text-transform: uppercase;
  letter-spacing: 0.10em;
  text-shadow: 0 0 40px rgba(212, 168, 58, 0.15);
}

.section-header-subtitle {
  font-family: var(--font-body);
  font-size: var(--font-size-base);
  color: var(--color-text-muted);
  font-style: italic;
  max-width: 500px;
}

/* Decoração abaixo do título */
.section-header-decoration {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  width: 200px;
}

.section-header-decoration::before,
.section-header-decoration::after {
  content: "";
  flex: 1;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    var(--color-gold-700),
    transparent
  );
}

.section-header-decoration-dot {
  width: 6px;
  height: 6px;
  background-color: var(--color-gold-600);
  transform: rotate(45deg);
  flex-shrink: 0;
}
```

---

## SEÇÃO 6 — NAVEGAÇÃO TEMÁTICA

### 6.1 — Header

```css
.site-header {
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  background-color: rgba(12, 11, 16, 0.90);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--color-border-subtle);
}

.site-header-inner {
  display: flex;
  align-items: center;
  gap: var(--space-6);
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: 0 var(--space-6);
  height: var(--header-height);
}

.site-logo {
  font-family: var(--font-display-ornate);
  font-weight: 700;
  font-size: var(--font-size-lg);
  color: var(--color-gold-300);
  text-transform: uppercase;
  letter-spacing: 0.12em;
  text-decoration: none;
  white-space: nowrap;
}

.site-logo:hover {
  color: var(--color-gold-200);
  text-shadow: 0 0 20px rgba(212, 168, 58, 0.2);
}

/* Links de navegação */
.nav-link {
  font-family: var(--font-ui);
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--color-text-normal);
  text-transform: uppercase;
  letter-spacing: 0.04em;
  padding: var(--space-2) var(--space-3);
  text-decoration: none;
  transition: color var(--duration-normal) var(--ease-default);
  position: relative;
}

.nav-link:hover {
  color: var(--color-gold-300);
}

.nav-link[aria-current="page"] {
  color: var(--color-gold-300);
}

.nav-link[aria-current="page"]::after {
  content: "";
  position: absolute;
  bottom: -2px;
  left: var(--space-3);
  right: var(--space-3);
  height: 2px;
  background-color: var(--color-gold-400);
  border-radius: 1px;
}
```

### 6.2 — Footer Temático

```css
.site-footer {
  margin-top: var(--space-24);
  padding: var(--space-16) var(--space-6) var(--space-8);
  background-color: var(--surface-sunken);
  border-top: 1px solid var(--color-border-subtle);
}

.footer-top-decoration {
  width: 100%;
  height: 1px;
  margin-bottom: var(--space-16);
  background: linear-gradient(
    90deg,
    transparent 0%,
    var(--color-gold-800) 15%,
    var(--color-gold-600) 50%,
    var(--color-gold-800) 85%,
    transparent 100%
  );
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr repeat(3, 1fr);
  gap: var(--space-12);
  max-width: var(--content-max-width);
  margin: 0 auto;
}

@media (max-width: 767px) {
  .footer-grid {
    grid-template-columns: 1fr;
    gap: var(--space-8);
  }
}

.footer-heading {
  font-family: var(--font-display);
  font-size: var(--font-size-sm);
  font-weight: 600;
  color: var(--color-gold-300);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: var(--space-4);
}

.footer-link {
  font-family: var(--font-ui);
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  text-decoration: none;
  transition: color var(--duration-normal) var(--ease-default);
  display: block;
  padding: var(--space-1) 0;
}

.footer-link:hover {
  color: var(--color-gold-300);
}

.footer-bottom {
  margin-top: var(--space-12);
  padding-top: var(--space-6);
  border-top: 1px solid var(--color-border-subtle);
  text-align: center;
  font-family: var(--font-ui);
  font-size: var(--font-size-xs);
  color: var(--color-text-faint);
}
```

---

## SEÇÃO 7 — CATEGORIAS ESPECÍFICAS E SUA ESTÉTICA

### 7.1 — Card Games (Magic: The Gathering, Pokémon TCG, Yu-Gi-Oh!)

Produtos de card games DEVEM receber tratamento especial:
- Imagens de cartas DEVEM manter `aspect-ratio: 63/88` (proporção padrão de cartas de jogo)
- Hover em cartas PODE incluir sutil rotação 3D (tilt effect):

```css
.card-game-product:hover .product-card-image {
  transform: perspective(600px) rotateY(-3deg) scale(1.02);
  transition: transform var(--duration-slow) var(--ease-out);
}
```

### 7.2 — RPG de Mesa (D&D, Pathfinder, Call of Cthulhu)

- Produtos de RPG DEVEM usar badges de classificação: "Manual do Jogador", "Manual do Mestre", "Aventura", "Suplemento", "Acessório"
- Dados de RPG DEVEM ser exibidos com imagem em fundo escuro para destacar transparências e cores
- Livros de RPG DEVEM usar `aspect-ratio: 17/22` (proporção comum de livros A4 verticais)

### 7.3 — Miniaturas e Action Figures

- Imagens DEVEM ter fundo escuro uniforme para destacar detalhes
- Exibir informação de escala (28mm, 1:100, etc.)
- Imagens DEVEM ser de alta resolução para mostrar detalhes de pintura

### 7.4 — Board Games

- Exibir número de jogadores, tempo de jogo, e faixa etária como badges visuais
- Usar ícones temáticos para essas informações

---

## SEÇÃO 8 — ESTADOS VAZIOS TEMÁTICOS

Quando uma lista está vazia (carrinho, wishlist, resultados de busca), o texto DEVE ser temático:

```html
<!-- Carrinho Vazio -->
<div class="empty-state">
  <div class="empty-state-icon">⚔️</div>
  <h2 class="empty-state-title">Sua Bolsa de Inventário está Vazia</h2>
  <p class="empty-state-text">
    Nenhum item foi adicionado à sua jornada ainda.
    Explore nosso catálogo e encontre tesouros dignos de um verdadeiro aventureiro.
  </p>
  <a href="/produtos" class="btn btn-primary">Explorar Catálogo</a>
</div>

<!-- Wishlist Vazia -->
<div class="empty-state">
  <div class="empty-state-icon">📜</div>
  <h2 class="empty-state-title">Sua Lista de Desejos Aguarda</h2>
  <p class="empty-state-text">
    Ainda não há itens marcados. Navegue e salve seus artefatos favoritos
    para quando estiver pronto para a aventura.
  </p>
</div>

<!-- Busca Sem Resultados -->
<div class="empty-state">
  <div class="empty-state-icon">🔮</div>
  <h2 class="empty-state-title">Nenhum Artefato Encontrado</h2>
  <p class="empty-state-text">
    Nem mesmo a bola de cristal conseguiu localizar o que procura.
    Tente termos diferentes ou explore nossas categorias.
  </p>
</div>
```

```css
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: var(--space-4);
  padding: var(--space-16) var(--space-6);
  max-width: 480px;
  margin: 0 auto;
}

.empty-state-icon {
  font-size: 3rem;
  line-height: 1;
  opacity: 0.6;
}

.empty-state-title {
  font-family: var(--font-display);
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--color-text-bright);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.empty-state-text {
  font-family: var(--font-body);
  font-size: var(--font-size-base);
  color: var(--color-text-muted);
  line-height: 1.6;
  font-style: italic;
}
```

---

## SEÇÃO 9 — TERMINOLOGIA TEMÁTICA

A linguagem do site PODE (não obrigatório em áreas funcionais) utilizar terminologia fantasy nos seguintes contextos:

| Termo Padrão E-Commerce     | Termo Temático Sugerido            | Uso Permitido                          |
|-----------------------------|------------------------------------|----------------------------------------|
| Carrinho de Compras         | Inventário / Bolsa de Itens        | Título da página, estado vazio         |
| Wishlist                    | Lista de Desejos / Grimório        | Título da página, estado vazio         |
| Conta do Usuário            | Ficha do Aventureiro               | Título da página dashboard             |
| Finalizar Compra            | Completar Missão                   | NÃO — manter "Finalizar Compra"       |
| Buscar                      | Buscar                             | NÃO alterar — funcionalidade crítica   |
| Filtros                     | Filtros                            | NÃO alterar — funcionalidade crítica   |
| Adicionar ao Carrinho       | Adicionar ao Inventário            | Opcional — pode confundir              |
| Meus Pedidos                | Missões Completadas                | Opcional — título da página             |
| Newsletter                  | Pergaminho de Novidades            | Footer, popup                          |
| Cupom de Desconto           | Código Arcano                      | Campo de input, placeholder            |

**REGRA:** Terminologia temática DEVE ser usada APENAS em títulos, estados vazios, e textos decorativos. NUNCA em labels de formulário, botões de ação crítica (checkout, pagamento), ou mensagens de erro. Nestes casos, usar terminologia padrão clara e inequívoca.

---

## SEÇÃO 10 — GAMIFICAÇÃO VISUAL (OPCIONAL)

Se o sistema de backend suportar, os seguintes elementos de gamificação PODEM ser implementados:

### 10.1 — Barra de Progresso de Frete Grátis

```html
<div class="free-shipping-bar">
  <div class="free-shipping-bar-text">
    Faltam <strong>R$ 45,00</strong> para frete grátis!
  </div>
  <div class="free-shipping-bar-track">
    <div class="free-shipping-bar-fill" style="width: 70%;"></div>
  </div>
</div>
```

```css
.free-shipping-bar {
  padding: var(--space-3) var(--space-6);
  background-color: rgba(212, 168, 58, 0.05);
  border: 1px solid var(--color-border-subtle);
  border-radius: var(--radius-md);
  text-align: center;
}

.free-shipping-bar-text {
  font-family: var(--font-ui);
  font-size: var(--font-size-sm);
  color: var(--color-text-normal);
  margin-bottom: var(--space-2);
}

.free-shipping-bar-text strong {
  color: var(--color-gold-300);
}

.free-shipping-bar-track {
  height: 6px;
  background-color: var(--surface-sunken);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.free-shipping-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--color-gold-600), var(--color-gold-400));
  border-radius: var(--radius-full);
  transition: width var(--duration-slow) var(--ease-out);
}
```

---

## SEÇÃO 11 — RESPONSIVIDADE TEMÁTICA

Em mobile, elementos decorativos DEVEM ser REDUZIDOS:

```css
@media (max-width: 767px) {
  /* Reduzir letter-spacing dos títulos */
  h1, .page-title {
    letter-spacing: 0.04em;
    font-size: var(--font-size-xl);
  }

  /* Simplificar separadores ornamentados */
  .divider-ornate {
    padding: var(--space-6) 0;
  }

  /* Remover bordas decorativas (cantos) */
  .corner-decorated::before,
  .corner-decorated::after {
    display: none;
  }

  /* Reduzir glow effects (performance) */
  .golden-glow {
    box-shadow: none;
  }

  /* Logo menor */
  .site-logo {
    font-size: var(--font-size-base);
    letter-spacing: 0.08em;
  }
}
```

---

## SEÇÃO 12 — CHECKLIST ESTÉTICA MEDIEVAL

- [ ] Paleta de cores Dark Fantasy aplicada (dourado, carmesim, esmeralda, ametista)
- [ ] Nenhuma cor genérica (azul corporativo, cinza puro) no design
- [ ] Cinzas SEMPRE com matiz quente (tendendo ao marrom/roxo)
- [ ] Dourado como cor accent em CTAs, preços, links ativos
- [ ] Fontes carregadas: Cinzel, Cinzel Decorative, Crimson Pro, Inter
- [ ] h1-h3 em Cinzel (uppercase, letter-spacing)
- [ ] Corpo em Crimson Pro (serif legível)
- [ ] UI funcional em Inter (sans-serif)
- [ ] Separadores ornamentados entre seções
- [ ] Bordas com matiz dourado sutil (`rgba(212, 168, 58, ...)`)
- [ ] Glow dourado em elementos de destaque
- [ ] Badges com sistema de raridade (common → legendary)
- [ ] Estados vazios com linguagem temática
- [ ] Decoração REDUZIDA em mobile
- [ ] Legibilidade NUNCA comprometida pela estética
- [ ] Checkout e formulários 100% funcionais e claros (sem tematização excessiva)
- [ ] Textura de ruído MUITO sutil (opacity < 0.02)
- [ ] Gradiente atmosférico no body (dourado sutil no topo)
