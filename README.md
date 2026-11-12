# Andesite Hardware Co.

*"Silício, solda e ambição"*

![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/build-Gradle-02303A?logo=gradle&logoColor=white)
![JUnit 6](https://img.shields.io/badge/tests-JUnit%206-25A162?logo=junit5&logoColor=white)
[![CI](https://github.com/arielzs/andesite-factory/actions/workflows/ci.yml/badge.svg)](https://github.com/arielzs/andesite-factory/actions/workflows/ci.yml)
[![Licença: MIT](https://img.shields.io/badge/licen%C3%A7a-MIT-blue)](LICENSE)

Simulador de uma fábrica de hardware (placas de vídeo, processadores e placas-mãe) em
**Java 17**, executado no terminal. Gerencia demandas, estoque de matéria-prima, orçamento
e uma linha de produção com esteiras e máquinas que se desgastam, falham e podem ser
reparadas. O estado da fábrica é salvo e carregado em **XML**.

Construí o projeto para praticar **orientação a objetos de verdade**: hierarquias de
classes, composição, polimorfismo, o padrão Strategy, tratamento de erros com exceções de
domínio, persistência e uma suíte de testes automatizados.

## Conceitos de POO demonstrados

| Conceito | Onde aparece |
|----------|--------------|
| **Classes abstratas e herança** | `Maquina` (abstrata) → `InsersoraSMT`, `Montadora`, `Inspetora`; `Produto` (abstrata) → `ComponenteEntrada`, `ComponentePerformance`, `ComponenteFlagship` |
| **Polimorfismo** | A linha de produção processa qualquer `Maquina` sem saber o tipo concreto; cada produto tem qualidade e tempo de produção próprios |
| **Interfaces** | `Auditavel` padroniza o diagnóstico de máquinas, produtos e partes; `EstrategiaProducao` define o contrato do Strategy |
| **Padrão Strategy** | `GerenciadorProducao` (contexto) delega a escolha da próxima demanda a uma `EstrategiaProducao` trocável em tempo de execução: `EstrategiaFilaDaFundicao`, `EstrategiaMaiorLote`, `EstrategiaMaximoRendimento` |
| **Composição** | `Maquina` cria e possui `MotorDeAcionamento`, `SensorDeCalibracao` e `AtuadorPneumatico`; as partes não são expostas (Lei de Deméter) |
| **Agregação** | `LinhaDeProducao` agrupa máquinas que existem independentemente dela |
| **Enums** | `Cenario` e `CategoriaProduto` carregam parâmetros (budget, falha, desgaste, tempo base); `StatusDemanda` e `StatusProduto` modelam o ciclo de vida |
| **Encapsulamento e imutabilidade** | Coleções devolvidas como listas imutáveis; construtores das partes visíveis só no pacote; `EstadoMaquina` é um `record` |
| **Exceções customizadas** | Hierarquia a partir de `PlantaException`, com os dados do erro guardados nas subclasses |
| **Princípios SOLID** | Responsabilidades separadas em pacotes (`model`, `service`, `strategy`, `persistence`, `exception`, `ui`); o domínio não conhece a interface nem o disco |
| **Documentação** | Javadoc completo, gerado em HTML pelo Gradle |

## Exemplo de execução

```
======================================================
                ANDESITE HARDWARE CO.
======================================================
              "Silício, solda e ambição"
======================================================
ESTRATEGIA ATUAL: Fila da Fundição (FIFO)
CENARIO ATIVO: Ideal
BUDGET ATUAL: R$3000.00
------------------------------------------------------
 [1] - Demandas
 [2] - Fabricação
 [3] - Consultar
 [4] - Comprar matéria-prima
 [5] - Gerenciar estratégia de produção
 [6] - Auditoria
 [7] - Linha de produção (máquinas e partes)
 [8] - Salvar estado da planta
 [9] - Carregar estado da planta
 [10] - Reiniciar planta (volta ao estado inicial)

 [0] - SAIR
------------------------------------------------------
```

Processando uma demanda de 3 placas Andesite A8000 (trecho):

```
[ESTRATEGIA: Fila da Fundição (FIFO)] Selecionada: Andesite A8000

==================================================
INICIANDO PRODUCAO DE 3 UNIDADE(S) DE ANDESITE A8000
==================================================
Checando estoque de Wafer de Silício Andesite 5nm: 10.0 unidade(s)
Checando budget disponivel: R$3000.00
Estoque e budget suficientes. Consumindo 6.0 unidade(s) de Wafer de Silício Andesite 5nm
Reservando R$99.00 do budget para custo de operacao do lote

--- Fabricando unidade 1/3 de Andesite A8000 ---
Andesite A8000 colocado na esteira
Andesite A8000 chegou na Insersora SMT-01. Desligando esteira...
Realizando processamento (Insersora SMT)...
Andesite A8000 -> status: PROCESSADO
Andesite A8000 chegou na Montadora MT-01. Desligando esteira...
Realizando processamento (Montadora)...
Andesite A8000 chegou na Inspetora INS-01. Desligando esteira...
Andesite A8000 -> status: INSPECIONADO
Andesite A8000 (PROD-VGA-002-1) aprovado na inspecao. Enviando para o armazem.
[...]
```

## Como compilar e executar

O projeto usa **Gradle** por meio do Gradle Wrapper, então não é preciso instalar o
Gradle. Requer apenas **Java 17 ou superior**.

```bash
./gradlew build     # compila, roda os testes e constrói o projeto
./gradlew run       # executa a aplicação
./gradlew test      # roda só os testes automatizados (JUnit 6)
./gradlew javadoc   # gera a documentação em build/docs/javadoc
```

No Windows, use `gradlew.bat` no lugar de `./gradlew`.

O relatório dos testes fica em `build/reports/tests/test/index.html` depois de
`./gradlew test`. O programa cria a pasta `dados/` sozinho ao salvar pela primeira vez, e
ela não entra no repositório (está no `.gitignore`).

## A fábrica

A Andesite Hardware Co. é uma fábrica fictícia de hardware. **Andesite** é a linha de
produtos: placas de vídeo, processadores e placas-mãe. Cada categoria é fabricada em três
tiers de qualidade (Entrada, Performance e Flagship), que se diferenciam pela qualidade do
componente e pela quantidade de matéria-prima consumida por unidade.

A produção é guiada por demandas: o usuário informa quantas unidades de cada produto
deseja, e o `GerenciadorProducao` só inicia a fabricação se houver estoque de
matéria-prima e budget suficientes, debitando o budget tanto na compra de matéria-prima
quanto no custo de operação de cada máquina utilizada. A escolha de qual demanda atender a
seguir é delegada a uma estratégia de produção plugável (padrão Strategy), e o usuário
escolhe entre dois cenários de operação, Ideal e Apocalíptico, no início da execução.

### Fluxo de produção

Cada unidade fabricada passa pela esteira entre uma máquina e outra, respeitando o estado
de cada equipamento (ligado/desligado, em movimento/parado):

```
Estoque de Matéria-Prima → Esteira → Insersora SMT → Esteira → Montadora → Esteira → Inspetora → Armazém
```

1. A demanda, a linha de produção, o estoque de matéria-prima e o budget são verificados
   antes de qualquer lote começar. Se alguma verificação falhar, nada é alterado.
2. A matéria-prima necessária é consumida e o custo de operação do lote é debitado do budget.
3. A demanda muda de `PENDENTE` para `EM_PRODUCAO` (enum `StatusDemanda`).
4. Cada unidade é transportada pela esteira até a Insersora SMT, que solda o die
   (GPU/CPU/chipset) no substrato e muda o status do produto para PROCESSADO.
5. Em seguida, a Montadora realiza o encaixe final (cooler/heatsink em GPUs e CPUs, ou
   conectores/soquetes em placas-mãe).
6. A cada uso, a máquina sofre desgaste: sua saúde (0 a 100) cai um pouco, e a
   probabilidade de falha efetiva cresce conforme a saúde diminui. Insersora e Montadora
   não falham diretamente: elas podem aumentar a probabilidade de falha acumulada do
   produto.
7. Por fim, a Inspetora avalia o produto. A chance de rejeição cresce com a probabilidade
   de falha acumulada, com a saúde da própria Inspetora e com a qualidade do produto
   (quanto mais rigoroso o tier, maior a exigência da inspeção).
8. Produtos aprovados vão para o armazém e a demanda é marcada como `CONCLUIDA`; produtos
   rejeitados ou que travam na esteira são descartados do lote.

### Cenários de operação

Escolhido no início da execução, o cenário (`Cenario.IDEAL` ou `Cenario.APOCALIPTICO`)
define o budget inicial da fábrica e escala a probabilidade de falha e o desgaste por uso
de todas as máquinas da linha, simulando desde uma operação tranquila até uma sob forte
estresse financeiro e mecânico.

## Decisões de projeto

### Composição das máquinas

Cada `Maquina` (o Todo) é composta por Partes que ela mesma cria no construtor (composição
estrita, sem receber Partes de fora): um `MotorDeAcionamento` (1:1), um
`SensorDeCalibracao` (1:1) e uma `List` de `AtuadorPneumatico` (1:N). Os construtores das
Partes são visíveis só no pacote e nenhuma Parte é exposta: o restante do sistema usa
métodos de delegação da máquina (`lerTemperaturaMotor()`, `lerCalibracaoSensor()`,
`contarAtuadoresOperacionais()`), seguindo a Lei de Deméter.

As máquinas ficam em uma `LinhaDeProducao` (o Agrupador). Essa relação é uma **agregação**:
a máquina é criada fora da linha e pode ser retirada e colocada em outra, então não depende
dela. `getMaquinas()` devolve uma lista imutável (`Collections.unmodifiableList`), assim
como os getters de coleção do `GerenciadorProducao`.

As Partes afetam a máquina: motor superaquecido (+10%), sensor descalibrado (+10%) e cada
atuador quebrado (+5%) somam à probabilidade de falha efetiva. A auditoria desce em cascata
(Linha > Máquina > Partes) e aparece indentada no terminal.

### Diagramas

**Diagrama de classes** ([`docs/uml-classes.png`](docs/uml-classes.png)): todas as classes dos
pacotes `model`, `strategy` e `service`, com herança, interfaces, composição, agregação,
multiplicidades e enums.

![Diagrama de classes](docs/uml-classes.png)

**Diagrama de pacotes** ([`docs/uml-pacotes.png`](docs/uml-pacotes.png)): dependências entre
`ui`, `service`, `strategy`, `persistence`, `model` e `exception`, a hierarquia de exceções e
quem lança ou captura cada uma.

![Diagrama de pacotes](docs/uml-pacotes.png)

As fontes editáveis são `docs/uml-classes.dot` e `docs/uml-pacotes.dot`
(Graphviz: `dot -Tpng arquivo.dot -o arquivo.png`).

### Tratamento de erros

Os erros de regra de negócio (matéria-prima ou budget insuficientes, demanda inexistente ou
em estado incompatível, máquina quebrada ou desligada, quantidade inválida) são
representados por exceções que herdam de `PlantaException`. O `GerenciadorProducao` valida
tudo antes de alterar qualquer estado, então um erro nunca deixa a planta parcialmente
modificada. O `MenuConsole` captura essas exceções, mostra um bloco de erro explicando o
motivo e devolve o usuário ao menu. Entradas inválidas (texto no lugar de número, opção
inexistente) repetem a pergunta.

### Persistência em XML

O estado da planta é salvo e carregado em um arquivo XML, por padrão `dados/planta.xml`
(relativo à pasta onde o programa roda). No menu principal, `[8] Salvar estado da planta`
grava o arquivo e `[9] Carregar estado da planta` o lê de volta. `[10] Reiniciar planta`
volta a planta ao estado inicial (pede o cenário de novo) e só altera a memória: o arquivo
salvo só muda se o usuário salvar depois. Ao sair, o programa pergunta se o estado deve ser
salvo.

Ao iniciar, a aplicação procura o arquivo:

- **Não existe:** é a primeira execução. A aplicação avisa, pede o cenário e monta uma
  planta nova. Não é preciso criar nenhum arquivo antes.
- **Existe e é válido:** o estado anterior é recuperado e a escolha de cenário é pulada.
- **Existe, mas está vazio, corrompido ou incompatível:** a aplicação mostra o motivo e
  oferece iniciar uma planta nova ou sair para corrigir o arquivo. O arquivo nunca é
  apagado nem alterado por conta própria; só é substituído se o usuário salvar depois.

Garantias do salvamento e do carregamento:

- **Salvar é atômico:** o XML é montado inteiro em memória, gravado em um arquivo temporário
  e só então troca o arquivo de destino. Se algo falhar, o arquivo salvo anteriormente
  continua intacto.
- **Carregar não altera a planta em uso:** `carregar` devolve uma planta nova e só depois de
  validar tudo. A planta atual só é trocada se o carregamento inteiro der certo.
- **O arquivo é validado:** estrutura, versão do formato e valores (saúde entre 0 e 100,
  budget não negativo, status de demanda válido, ids do armazém coerentes etc.). A leitura
  recusa DOCTYPE, então um XML malicioso não consegue ler outros arquivos (proteção contra
  XXE).
- **Erros são exceções:** `PersistenciaException` e suas subclasses, tratadas pelo
  `MenuConsole` com uma mensagem clara. A aplicação não encerra por causa de um arquivo ruim.

Só a classe `PersistenciaPlanta` (e as auxiliares do pacote `persistence`) conhece arquivos.
As classes de domínio não leem nem escrevem nada em disco.

## Estrutura das classes

| Classe / Interface / Enum | Pacote | Responsabilidade |
|--------|--------|------------------|
| `Produto` (abstrata) | `model` | Estado comum a todo produto: id, nome, categoria, status, qualidade e probabilidade de falha acumulada. Implementa `Auditavel` |
| `ComponenteEntrada`, `ComponentePerformance`, `ComponenteFlagship` | `model` | Subclasses de `Produto` com qualidade (0.5 / 0.7 / 0.9) e tempo de produção próprios |
| `CategoriaProduto` | `model` | Enum das linhas de produto (Placa de Vídeo, Processador, Placa-Mãe) e seu tempo base de produção |
| `StatusProduto` | `model` | Enum dos estados de um produto (aguardando, processado, inspecionado, rejeitado) |
| `Maquina` (abstrata) | `model` | Estado comum a todo equipamento: ligado/desligado, capacidade, custo de operação, saúde e desgaste. Implementa `Auditavel` |
| `InsersoraSMT`, `Montadora`, `Inspetora` | `model` | Subclasses de `Maquina`; as duas primeiras só aumentam a falha acumulada do produto, a Inspetora é a única que falha (rejeita) diretamente |
| `Auditavel` | `model` | Interface que padroniza diagnóstico (`gerarRelatorioDiagnostico`) e necessidade de manutenção (`precisaManutencao`) entre `Maquina` e `Produto` |
| `MateriaPrima` | `model` | Controle de estoque, consumo e reposições |
| `Demanda` | `model` | Quantidade de produtos finais solicitada pelo usuário, controlada pelo enum `StatusDemanda` |
| `StatusDemanda` | `model` | Enum dos estados de uma demanda (pendente, em produção, concluída, cancelada) |
| `EstrategiaProducao` | `strategy` | Interface do padrão Strategy: seleciona a próxima demanda a ser atendida |
| `EstrategiaFilaDaFundicao`, `EstrategiaMaiorLote`, `EstrategiaMaximoRendimento` | `strategy` | Implementações concretas de `EstrategiaProducao` |
| `Cenario` | `model` | Enum dos cenários de operação (Ideal / Apocalíptico) e seus parâmetros de budget, falha e desgaste |
| `LinhaDeProducao` | `model` | Agrupador (agregação) das máquinas; lista imutável, auditoria em cascata e reparo |
| `MotorDeAcionamento`, `SensorDeCalibracao`, `AtuadorPneumatico` | `model` | Partes internas de `Maquina` (composição 1:1, 1:1 e 1:N); implementam `Auditavel` |
| `Esteira` | `model` | Transporta um item por vez entre as etapas da linha, respeitando capacidade |
| `EstadoMaquina` | `model` | Registro (record) com o retrato do estado variável de uma máquina e das suas Partes; é como a máquina entrega e recebe o estado sem expor as Partes |
| `GerenciadorProducao` | `service` | Contexto do Strategy; orquestra demandas, budget, matéria-prima, máquinas, cenário e o armazém de produtos fabricados |
| `PlantaException` | `exception` | Exceção base (`RuntimeException`) dos erros de regra de negócio da planta |
| `RecursoInsuficienteException` | `exception` | Falta de matéria-prima ou de budget |
| `DemandaInvalidaException` | `exception` | Demanda inexistente ou em estado incompatível com a operação |
| `MaquinaIndisponivelException` | `exception` | Máquina ou esteira quebrada, desligada, ocupada ou sem capacidade para o produto |
| `QuantidadeInvalidaException` | `exception` | Quantidade menor ou igual a zero, ou abaixo do lote mínimo |
| `PersistenciaException` | `exception` | Base dos erros de persistência (subclasse de `PlantaException`) |
| `ArquivoNaoEncontradoException` | `exception` | O arquivo de estado não existe (caso normal na primeira execução) |
| `ArquivoCorrompidoException` | `exception` | Arquivo vazio, XML malformado, estrutura ou versão do formato inesperadas |
| `DadosInvalidosException` | `exception` | O XML é bem formado, mas algum valor viola as regras do domínio |
| `PersistenciaPlanta` | `persistence` | Salva e carrega a planta em XML: cuida do arquivo, da gravação atômica e das falhas de entrada e saída |
| `EscritorXmlPlanta`, `LeitorXmlPlanta`, `FormatoXml` | `persistence` | Conversão entre a planta e o documento XML, com validação na leitura; `FormatoXml` guarda os nomes dos elementos |
| `MenuConsole` | `ui` | Interface via terminal, organizada em submenus: Demandas, Fabricação, Consultar, Comprar matéria-prima, Gerenciar estratégia, Auditoria (que também permite reparar as máquinas), Linha de produção. Salvar e carregar o estado. Valida as entradas e trata as exceções |
| `Main` | `(raiz)` | Ponto de entrada do programa |

## Estrutura do projeto

```
build.gradle
settings.gradle
gradle.properties
gradlew / gradlew.bat
gradle/wrapper/
LICENSE
CHANGELOG.md
README.md
.github/workflows/ci.yml                (build e testes a cada push)
docs/                                   (diagramas UML em .dot e .png)
src/main/java/io/github/arielzs/andesite/
    Main.java                           (ponto de entrada)
    model/                              (entidades de domínio e componentes)
    service/                            (GerenciadorProducao, regras de negócio)
    strategy/                           (estratégias de produção)
    persistence/                        (leitura e escrita em XML)
    exception/                          (exceções de domínio e de persistência)
    ui/                                 (MenuConsole, interface de terminal)
src/test/java/io/github/arielzs/andesite/
    model/ service/ strategy/ exception/ persistence/   (testes JUnit 6)
    suporte/                            (montagem das plantas usadas nos testes)
```

As dependências vão em um só sentido: `ui` conhece todos os outros pacotes; `persistence`
conhece `service`, `strategy`, `model` e `exception`; `service` conhece `strategy`, `model` e
`exception`; `strategy` conhece `model`; e `model` só conhece `exception`. O domínio
(`model`, `service`, `strategy`) não conhece nem `persistence` nem `ui`.

## Testes

Mais de 120 testes com **JUnit 6**, executados por `./gradlew test` (e também por
`./gradlew build`). Eles são **independentes**: cada teste monta a própria planta
(`suporte/PlantaDeTeste` e `suporte/PlantaPersistivel`), não usa teclado, não espera tempo
(as pausas da produção são desligadas com `setPausasAtivas(false)`) e usa `Random` com
semente fixa nas máquinas, então o resultado é repetível. Os testes de persistência usam
`@TempDir` e **nunca** tocam no `dados/planta.xml` real.

**Regras de negócio e domínio**

| Classe de teste | O que avalia |
|-----------------|--------------|
| `DemandaTest` | Ciclo de vida da demanda (pendente, em produção, concluída, cancelada), reabertura, cálculo do lote e `Demanda.restaurar` |
| `MateriaPrimaTest` | Consumo, estoque, lote mínimo e validação dos dados |
| `EsteiraTest` | Um item por vez, esteira parada ou ocupada, peso acima da capacidade |
| `ProdutoTest` | Os três tiers (qualidade, tempo, tipo), `criarUnidade` e chance de falha acumulada |
| `MaquinaTest` | Operação, quebra, desgaste, reparo, Partes e relatório; `obterEstado` e `restaurarEstado` |
| `LinhaDeProducaoTest` | Agregação das máquinas, ordem, custo por unidade, lista imutável e reparo |
| `EstrategiasTest` | As três estratégias de produção e o comportamento polimórfico |
| `GerenciadorDemandasECompraTest` | Criação da planta, efeito do cenário, demandas, troca de estratégia, compra e API de restauração |

**Casos de exceção (erros de regra de negócio)**

| Classe de teste | O que avalia |
|-----------------|--------------|
| `GerenciadorFabricacaoTest` | Fabricação de um lote e cada erro (`DemandaInvalidaException`, `MaquinaIndisponivelException`, `QuantidadeInvalidaException`, `RecursoInsuficienteException`), conferindo que a planta fica exatamente como estava; quebra de máquina no meio do lote |
| `ExcecoesTest` | Hierarquia das exceções, dados guardados (recurso, necessário, disponível, caminho) e causa preservada |

**Persistência**

| Classe de teste | O que avalia |
|-----------------|--------------|
| `PersistenciaIdaEVoltaTest` | Salvar e carregar devolve o mesmo estado (máquinas com Partes, demandas, armazém); a planta carregada continua produzindo; atualizar o arquivo; criar pastas; formato; tolerância a espaços, CRLF e BOM |
| `PersistenciaArquivoInvalidoTest` | Arquivo inexistente, vazio, XML malformado, estrutura errada, DOCTYPE e dezenas de valores que violam o domínio; mensagens úteis |
| `PersistenciaFalhasTest` | Falhas ao salvar (destino é pasta, demanda em produção), arquivo anterior intacto, e falha ao carregar sem alterar a planta em uso nem o arquivo |

## Escopo e limitações

### Dados persistidos e não persistidos

**Persistidos** (salvos em `dados/planta.xml`):

| Dado | Por que é salvo |
|------|-----------------|
| Cenário e estratégia de produção | Definem as regras da planta; sem eles a planta carregada se comportaria de outro jeito |
| Budget, capacidade da esteira e contador de unidades | O budget é o estado financeiro; o contador garante que os ids das próximas unidades não repitam os do armazém |
| Estoque de matéria-prima (id, nome, quantidade, unidade, custo, lote mínimo) | É o estoque atual, que muda a cada compra e produção |
| Máquinas na ordem da linha, com configuração, saúde, desgaste, chance de falha e todas as Partes (temperatura do motor, calibração do sensor, integridade de cada atuador) | É o desgaste acumulado da fábrica; a ordem define a ordem de processamento |
| Demandas com quantidade e `StatusDemanda` | Representam o que o usuário pediu e o que já foi atendido |
| Unidades do armazém (id, produto, chance de falha acumulada) | São o resultado da produção |

**Não persistidos** (ficam só em memória), com o motivo:

| Dado | Motivo |
|------|--------|
| Catálogo de produtos | É configuração fixa da aplicação, não estado que muda. Ele é informado ao carregar, e o arquivo é conferido contra ele (demanda ou item de produto desconhecido é recusado) |
| Se uma máquina ou a esteira está ligada | Cada máquina é ligada e desligada dentro da produção de cada unidade e termina sempre desligada. Não há estado "ligada" para guardar entre execuções |
| Contadores estáticos (`Produto.totalProdutosFabricados`, `Inspetora.totalInspecionados`) | São estatísticas globais da execução, não pertencem a uma planta específica |
| Fator do cenário já aplicado às máquinas | O valor final da chance de falha e do desgaste já é salvo em cada máquina; reaplicar o fator ao carregar o multiplicaria de novo |
| Uma demanda `EM_PRODUCAO` | A produção de um lote roda de uma vez, então esse estado só existe durante a fabricação. Salvar nesse estado é recusado, e um arquivo com ele é considerado inválido, pois a demanda ficaria travada |
| Unidades rejeitadas ou perdidas na linha | São descartadas do lote e não existem mais na planta |

### Limitações

- Só há um arquivo de estado (`dados/planta.xml`). Não há múltiplos slots nem histórico.
- Salvar substitui o arquivo anterior. Não existe "desfazer".
- As máquinas são recriadas com a quantidade padrão de atuadores (2); o arquivo precisa
  descrever exatamente essa quantidade.
- O arquivo é conferido contra o catálogo atual. Se o catálogo da aplicação mudar, um
  arquivo antigo pode ser recusado com `DadosInvalidosException`.
- Os testes não cobrem o caso de arquivo sem permissão de leitura, por depender do sistema
  operacional e do usuário que executa.

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE).
