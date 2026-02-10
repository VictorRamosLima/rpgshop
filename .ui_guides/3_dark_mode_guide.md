# GUIA NORMATIVO DE UI/UX — ESPECIALIZAÇÃO EM DARK MODE

**Versão:** 1.0
**Stack Tecnológica Alvo:** Spring Boot, Thymeleaf, HTML5 semântico, HTMX, CSS puro
**Pré-requisito:** O Guia 01 (UI/UX Geral) DEVE ser lido e obedecido em conjunção com este documento
**Classificação:** Documento normativo — todas as diretrizes DEVEM ser seguidas na íntegra
**Nota Crítica:** Este projeto é EXCLUSIVAMENTE dark mode. NÃO existe tema claro. Todas as decisões de cor, contraste e elevação são otimizadas para superfícies escuras.

---

## SEÇÃO 1 — FUNDAMENTOS DO DARK MODE

### 1.1 — O Que Dark Mode NÃO É

Antes de definir o que DEVE ser feito, é imperativo estabelecer o que NUNCA DEVE ser feito:

1. **Dark mode NÃO é inversão de cores.** Inverter um tema claro resulta em cores distorcidas, imagens negativas, e hierarquia visual destruída.
2. **Dark mode NÃO é fundo preto (#000000) com texto branco (#FFFFFF).** Preto puro sobre branco puro produz contraste excessivo (21:1) que causa fadiga ocular, "halation" (efeito de brilho fantasma nas bordas do texto), e desconforto em sessões prolongadas.
3. **Dark mode NÃO é simplesmente escurecer tudo.** A hierarquia visual, a profundidade, e a navegabilidade DEVEM ser repensadas para funcionar corretamente em superfícies escuras.
4. **Dark mode NÃO significa ausência de cor.** Cores de destaque (accent), estados de feedback (sucesso, erro, alerta), e badges continuam obrigatórios.

### 1.2 — O Que Dark Mode É

Dark mode é um esquema cromático onde superfícies escuras (não pretas) são usadas como fundo, e textos e elementos de interface utilizam cores claras (não brancas puras) para estabelecer hierarquia e legibilidade. O objetivo é:

- Reduzir emissão de luz para conforto em ambientes com pouca iluminação
- Criar foco no conteúdo ao reduzir ruído visual nas bordas
- Permitir que cores de destaque "brilhem" contra fundos escuros
- Economizar energia em telas OLED
- Transmitir uma estética moderna, imersiva e premium

---

## SEÇÃO 2 — PALETA DE CORES PARA DARK MODE

### 2.1 — Superfícies (Fundo)

As superfícies em dark mode utilizam uma escala de cinzas escuros com MATIZ SUTIL para evitar a frieza do cinza neutro puro. Cada nível de superfície DEVE ser ligeiramente mais claro que o anterior para comunicar elevação.

**Sistema de Elevação por Luminosidade:**

| Nível        | Token CSS              | Hex       | Uso                                           |
|-------------|------------------------|-----------|-----------------------------------------------|
| -1 (sunken) | `--surface-sunken`     | `#050506` | Fundos de inputs, áreas rebaixadas             |
| 0 (base)    | `--surface-base`       | `#0a0a0b` | Fundo principal da página (body)               |
| 1 (raised)  | `--surface-raised`     | `#121214` | Cards, containers, sidebars                    |
| 2 (overlay) | `--surface-overlay`    | `#1a1a1e` | Dropdowns, modais, tooltips, popovers          |
| 3 (elevated)| `--surface-elevated`   | `#222228` | Elementos sobrepostos a overlays (raro)        |

**Regra fundamental:** cada nível de elevação DEVE ser 6-10 pontos mais claro que o anterior na escala L* (luminosidade perceptual). Isto cria uma distinção visual clara sem necessidade de sombras.

### 2.2 — Texto

| Função        | Token CSS            | Hex       | Contraste vs Base | Uso                              |
|--------------|---------------------|-----------|-------------------|----------------------------------|
| Primário     | `--text-primary`    | `#ededf0` | ~15:1             | Títulos, corpo principal          |
| Secundário   | `--text-secondary`  | `#8a8a96` | ~6:1              | Labels, descrições, metadata      |
| Terciário    | `--text-tertiary`   | `#5a5a66` | ~3.5:1            | Placeholders, timestamps, hints   |
| Desativado   | `--text-disabled`   | `#3d3d47` | ~2:1              | Texto de elementos disabled       |

**NUNCA usar `#FFFFFF` puro como cor de texto.** O branco puro contra superfícies escuras causa halation — um efeito óptico onde as letras parecem "sangrar" luz, tornando-se borradas e difíceis de ler em sessões longas. Usar SEMPRE off-white: `#ededf0` a `#e0e0e6`.

**NUNCA usar texto com contraste inferior a 4.5:1 para corpo e 3:1 para texto grande.** Verificar com calculadora WCAG.

### 2.3 — Cores de Destaque (Accent)

Em dark mode, cores saturadas DEVEM ter sua saturação REDUZIDA em 10-30% em comparação com o que seria usado em light mode. Cores altamente saturadas contra fundos escuros parecem "vibrar" e causam desconforto visual.

**Regra de Dessaturação:**
```css
:root {
  /* ❌ INCORRETO — saturação excessiva em dark mode */
  --color-accent: #ff0000;       /* Vermelho puro — vibra contra fundo escuro */
  --color-accent: #00ff00;       /* Verde puro — extremamente agressivo */
  --color-accent: #0000ff;       /* Azul puro — difícil de ler */

  /* ✅ CORRETO — saturação reduzida e ajustada */
  --color-accent: #c9a55a;       /* Dourado dessaturado — elegante, legível */
  --color-success: #4a9e6e;      /* Verde médio dessaturado */
  --color-error: #c85a5a;        /* Vermelho médio dessaturado */
  --color-warning: #c9a03a;      /* Âmbar dessaturado */
  --color-info: #5a8ec8;         /* Azul médio dessaturado */
}
```

### 2.4 — Bordas

Em dark mode, bordas DEVEM ser sutis e usar cores ligeiramente mais claras que a superfície. Bordas muito evidentes criam um visual de "grade" que fragmenta a interface.

```css
:root {
  --border-subtle:  rgba(255, 255, 255, 0.06);  /* Quase invisível — separadores suaves */
  --border-default: rgba(255, 255, 255, 0.10);  /* Padrão — cards, inputs */
  --border-strong:  rgba(255, 255, 255, 0.16);  /* Hover, focus */
}
```

Usar `rgba` em vez de hex permite que as bordas se adaptem visualmente a qualquer superfície por trás.

---

## SEÇÃO 3 — ELEVAÇÃO E PROFUNDIDADE

### 3.1 — Sombras em Dark Mode

Em dark mode, sombras tradicionais (drop-shadow com rgba preto) são POUCO EFICAZES porque o fundo já é escuro. Sombras não criam o mesmo contraste visual que em light mode. Em vez disso, a profundidade DEVE ser comunicada por:

1. **Luminosidade da superfície** — elementos mais altos são mais claros (ver Seção 2.1)
2. **Bordas sutis** — uma borda de 1px com `rgba(255,255,255,0.06)` a `0.10`
3. **Glow sutil** — para elementos muito elevados (modais, dropdowns):

```css
/* Elevação via borda + glow (substitui sombras) */
.modal {
  background-color: var(--surface-overlay);
  border: 1px solid var(--border-default);
  /* Glow sutil ao invés de drop-shadow */
  box-shadow: 0 0 60px -10px rgba(0, 0, 0, 0.8),
              0 0 0 1px rgba(255, 255, 255, 0.05);
}

.dropdown {
  background-color: var(--surface-overlay);
  border: 1px solid var(--border-default);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.6);
}

/* Cards — sem sombra, elevação por cor */
.card {
  background-color: var(--surface-raised);  /* Mais claro que base = elevado */
  border: 1px solid var(--border-subtle);
}
```

### 3.2 — Hierarquia de Superfícies (Diagrama)

```
Visualização de camadas (de trás para frente):

╔═══════════════════════════════════════════════╗
║  BODY — --surface-base (#0a0a0b)              ║
║  ┌─────────────────────────────────────────┐  ║
║  │  CARD — --surface-raised (#121214)      │  ║
║  │  ┌───────────────────────────────────┐  │  ║
║  │  │  INPUT — --surface-sunken (#050506)│  │  ║
║  │  └───────────────────────────────────┘  │  ║
║  └─────────────────────────────────────────┘  ║
║                                               ║
║  ┌─────────────────────────────────────────┐  ║
║  │  MODAL — --surface-overlay (#1a1a1e)    │  ║
║  │  ┌───────────────────────────────────┐  │  ║
║  │  │  DROPDOWN — --surface-elevated    │  │  ║
║  │  │  (#222228)                        │  │  ║
║  │  └───────────────────────────────────┘  │  ║
║  └─────────────────────────────────────────┘  ║
╚═══════════════════════════════════════════════╝

Regra: sunken < base < raised < overlay < elevated
Cada nível é ~6-10 L* mais claro que o anterior
```

---

## SEÇÃO 4 — TIPOGRAFIA EM DARK MODE

### 4.1 — Peso Tipográfico

Em dark mode, texto claro em fundo escuro parece MAIS PESADO (mais bold) do que realmente é. Isto é um fenômeno óptico chamado "irradiation illusion". Implicações:

- **Corpo de texto:** usar `font-weight: 400` (regular). NUNCA usar `300` (light) pois será ilegível.
- **Títulos:** `font-weight: 600` (semibold) geralmente é suficiente. `700` (bold) APENAS para h1.
- **Texto pequeno (captions, hints):** usar `font-weight: 400` com cor `--text-secondary` ou `--text-tertiary`.

### 4.2 — Espaçamento entre Letras (Letter-Spacing)

Texto claro em fundo escuro beneficia-se de letter-spacing LIGEIRAMENTE aumentado:

```css
body {
  letter-spacing: 0.01em;  /* Sutil, mas melhora legibilidade em dark mode */
}

h1, h2, h3 {
  letter-spacing: -0.02em;  /* Títulos podem ter tracking negativo */
}

.text-sm, .caption {
  letter-spacing: 0.02em;  /* Texto pequeno precisa de mais espaço */
}
```

### 4.3 — Font Smoothing

Em dark mode, o antialiasing DEVE ser configurado para evitar texto "pesado":

```css
body {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
```

Isto utiliza antialiasing subpixel que produz texto mais fino e nítido em dark mode. É OBRIGATÓRIO.

---

## SEÇÃO 5 — IMAGENS EM DARK MODE

### 5.1 — Brilho e Contraste

Imagens muito brilhantes em uma interface escura criam um flash visual desconfortável. As seguintes regras DEVEM ser aplicadas:

```css
/* Reduzir brilho de todas as imagens de conteúdo em dark mode */
.content-image {
  filter: brightness(0.9);
  transition: filter var(--duration-normal) var(--ease-default);
}

.content-image:hover {
  filter: brightness(1);  /* Restaurar no hover para visualização completa */
}

/* Imagens de produto — NÃO reduzir brilho (precisão de cor é importante) */
.product-image {
  /* Sem filtro de brightness — o comprador precisa ver a cor real */
  background-color: var(--surface-overlay);  /* Fundo para imagens com transparência */
  border-radius: var(--radius-lg);
}
```

### 5.2 — Imagens com Fundo Branco

Muitas imagens de produto vêm com fundo branco. Em dark mode, isto cria um retângulo branco brilhante na interface. Soluções:

1. **Preferida:** Solicitar imagens com fundo transparente (PNG) e usar `background-color: var(--surface-overlay)` como fundo do container
2. **Alternativa:** Usar `border-radius` generoso no container da imagem para suavizar as bordas
3. **Alternativa 2:** Aplicar `mix-blend-mode: multiply` quando a imagem tiver fundo branco (só funciona bem se o produto for escuro)

### 5.3 — Ícones e Ilustrações

Ícones SVG DEVEM utilizar `currentColor` para herdar a cor do texto contexto. NUNCA hardcodar cores em SVGs inline:

```html
<!-- ✅ CORRETO -->
<svg width="24" height="24" fill="none" stroke="currentColor" stroke-width="2">
  <path d="..."/>
</svg>

<!-- ❌ INCORRETO -->
<svg width="24" height="24" fill="none" stroke="#333333" stroke-width="2">
  <path d="..."/>
</svg>
```

---

## SEÇÃO 6 — ESTADOS DE INTERAÇÃO EM DARK MODE

### 6.1 — Hover

Em dark mode, o hover NÃO DEVE escurecer (o fundo já é escuro). Em vez disso, DEVE iluminar:

```css
/* ❌ INCORRETO em dark mode — escurecer no hover */
.card:hover {
  background-color: #000000;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}

/* ✅ CORRETO em dark mode — iluminar no hover */
.card:hover {
  background-color: var(--surface-overlay);  /* Mais claro que raised */
  border-color: var(--border-default);       /* Borda mais evidente */
}

/* Botões */
.btn-primary:hover {
  filter: brightness(1.15);  /* Iluminar a cor do botão */
}

.btn-secondary:hover {
  background-color: rgba(255, 255, 255, 0.06);  /* Glow sutil */
}
```

### 6.2 — Focus

O indicador de focus DEVE ser MAIS VISÍVEL em dark mode. Um outline fino pode se perder:

```css
:focus-visible {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
}

/* Para inputs, usar ring com box-shadow */
input:focus,
textarea:focus,
select:focus {
  outline: none;
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(201, 165, 90, 0.2);  /* Glow com cor do accent */
}
```

### 6.3 — Seleção de Texto

A seleção padrão (azul brilhante) pode ser muito agressiva em dark mode:

```css
::selection {
  background-color: rgba(201, 165, 90, 0.3);  /* Accent com opacidade */
  color: var(--text-primary);
}
```

---

## SEÇÃO 7 — FORMULÁRIOS EM DARK MODE

### 7.1 — Campos de Input

```css
.form-input {
  background-color: var(--surface-sunken);     /* Mais escuro que o card = rebaixado */
  border: 1px solid var(--border-default);
  color: var(--text-primary);
  caret-color: var(--color-accent);            /* Cursor de digitação na cor accent */
}

.form-input::placeholder {
  color: var(--text-tertiary);
}

/* Autocomplete — forçar estilo dark nos campos preenchidos pelo browser */
.form-input:-webkit-autofill,
.form-input:-webkit-autofill:hover,
.form-input:-webkit-autofill:focus {
  -webkit-box-shadow: 0 0 0 1000px var(--surface-sunken) inset;
  -webkit-text-fill-color: var(--text-primary);
  border-color: var(--border-default);
  transition: background-color 5000s ease-in-out 0s;
}
```

O truque do `-webkit-autofill` é CRÍTICO. Sem ele, campos preenchidos automaticamente pelo navegador ficarão com fundo amarelo/branco, quebrando completamente o visual dark.

### 7.2 — Select / Dropdown

```css
.form-select {
  background-color: var(--surface-sunken);
  border: 1px solid var(--border-default);
  color: var(--text-primary);
  /* Custom arrow para dark mode */
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%238a8a96' d='M6 8L1 3h10z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right var(--space-4) center;
  background-size: 12px;
  appearance: none;
  padding-right: var(--space-10);
}
```

### 7.3 — Checkbox e Radio Customizados

```css
/* Checkbox custom para dark mode */
.checkbox-custom {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  cursor: pointer;
}

.checkbox-custom input[type="checkbox"] {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.checkbox-custom-indicator {
  width: 20px;
  height: 20px;
  border: 2px solid var(--border-default);
  border-radius: var(--radius-sm);
  background-color: var(--surface-sunken);
  transition: all var(--duration-normal) var(--ease-default);
  display: flex;
  align-items: center;
  justify-content: center;
}

.checkbox-custom input:checked + .checkbox-custom-indicator {
  background-color: var(--color-accent);
  border-color: var(--color-accent);
}

.checkbox-custom input:focus-visible + .checkbox-custom-indicator {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
}

/* Checkmark SVG inline */
.checkbox-custom input:checked + .checkbox-custom-indicator::after {
  content: "";
  width: 12px;
  height: 12px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 12 12'%3E%3Cpath fill='%230a0a0b' d='M10 3L4.5 8.5 2 6'/%3E%3C/svg%3E");
  background-size: contain;
  background-repeat: no-repeat;
}
```

---

## SEÇÃO 8 — SCROLLBAR CUSTOMIZADA

A scrollbar padrão do navegador é clara e destoa completamente de uma interface dark. DEVE ser customizada:

```css
/* Scrollbar para Webkit (Chrome, Safari, Edge) */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: var(--surface-base);
}

::-webkit-scrollbar-thumb {
  background: var(--color-gray-700);
  border-radius: var(--radius-full);
  border: 2px solid var(--surface-base);
}

::-webkit-scrollbar-thumb:hover {
  background: var(--color-gray-600);
}

/* Scrollbar para Firefox */
* {
  scrollbar-width: thin;
  scrollbar-color: var(--color-gray-700) var(--surface-base);
}
```

---

## SEÇÃO 9 — OVERLAYS E MODAIS

### 9.1 — Backdrop

O backdrop (fundo escurecido atrás de modais) em dark mode precisa ser mais escuro que em light mode para criar contraste:

```css
.modal-backdrop {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.75);  /* 75% opacidade em dark mode (vs 50% em light) */
  backdrop-filter: blur(4px);
  z-index: var(--z-overlay);
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: var(--surface-overlay);
  border: 1px solid var(--border-default);
  border-radius: var(--radius-xl);
  padding: var(--space-8);
  z-index: var(--z-modal);
  max-width: 560px;
  width: calc(100% - var(--space-8));
  max-height: calc(100dvh - var(--space-16));
  overflow-y: auto;
}
```

---

## SEÇÃO 10 — TABELAS EM DARK MODE

```css
.table {
  width: 100%;
  border-collapse: collapse;
}

.table th {
  padding: var(--space-3) var(--space-4);
  text-align: left;
  font-size: var(--font-size-xs);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-tertiary);
  background-color: var(--surface-raised);
  border-bottom: 1px solid var(--border-default);
}

.table td {
  padding: var(--space-3) var(--space-4);
  color: var(--text-primary);
  border-bottom: 1px solid var(--border-subtle);
}

.table tr:hover td {
  background-color: rgba(255, 255, 255, 0.02);
}

/* Linhas zebradas — MUITO sutis em dark mode */
.table--striped tbody tr:nth-child(even) td {
  background-color: rgba(255, 255, 255, 0.015);
}
```

---

## SEÇÃO 11 — TOASTS E NOTIFICAÇÕES

```css
.toast {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4) var(--space-6);
  background-color: var(--surface-elevated);
  border: 1px solid var(--border-default);
  border-radius: var(--radius-lg);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.5);
  animation: toast-in var(--duration-slow) var(--ease-out);
}

.toast--success {
  border-left: 3px solid var(--color-success);
}

.toast--error {
  border-left: 3px solid var(--color-error);
}

.toast--warning {
  border-left: 3px solid var(--color-warning);
}

@keyframes toast-in {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
```

---

## SEÇÃO 12 — CHECKLIST DARK MODE

Antes de considerar qualquer página finalizada em dark mode, verificar TODOS os itens:

- [ ] NENHUM uso de `#000000` como fundo de superfície
- [ ] NENHUM uso de `#FFFFFF` como cor de texto
- [ ] Contraste de texto >= 4.5:1 para corpo, >= 3:1 para texto grande
- [ ] Cores de accent dessaturadas (sem vermelho puro, verde puro, azul puro)
- [ ] Hierarquia de elevação clara: sunken < base < raised < overlay < elevated
- [ ] Hover ilumina (não escurece)
- [ ] Scrollbar customizada
- [ ] Campos autocomplete com `-webkit-autofill` tratado
- [ ] Bordas usando `rgba(255,255,255,...)` ao invés de hex
- [ ] Imagens de conteúdo com `brightness(0.9)`
- [ ] Imagens de produto SEM filtro de brightness
- [ ] SVGs usando `currentColor`
- [ ] `::selection` customizado
- [ ] `-webkit-font-smoothing: antialiased` aplicado
- [ ] Nenhuma sombra drop-shadow como mecanismo primário de elevação
- [ ] Backdrop de modais com opacidade >= 0.7
- [ ] Indicador de focus visível em todos os elementos interativos
