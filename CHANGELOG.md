# Changelog

Marcos da evolução do projeto, do mais recente para o mais antigo. As versões foram
definidas depois, para organizar o histórico; o formato segue o
[Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/).

## [1.0.0] — Persistência e testes

### Adicionado
- Persistência do estado da planta em XML (`dados/planta.xml`), com as opções de salvar,
  carregar e reiniciar no menu e a pergunta de salvar ao sair.
- Salvamento atômico (arquivo temporário e troca no final) e carregamento que só substitui a
  planta em uso depois de validar o arquivo inteiro.
- Validação do XML na leitura (estrutura, versão do formato e valores do domínio), com
  proteção contra XXE (DOCTYPE é recusado).
- Hierarquia `PersistenciaException`: arquivo não encontrado, arquivo corrompido e dados
  inválidos. Um arquivo ruim nunca encerra a aplicação nem é apagado.
- `EstadoMaquina` (record): a máquina entrega e recebe o estado sem expor as Partes internas.
- Mais de 120 testes automatizados com JUnit 5 (domínio, exceções e persistência), todos
  independentes, repetíveis e sem tocar no arquivo real.
- Integração contínua com GitHub Actions e licença MIT.

### Alterado
- Código organizado em pacotes (`model`, `service`, `strategy`, `persistence`, `exception`,
  `ui`), de modo que o domínio não conhece a interface nem o disco.

## [0.4.0] — Composição das máquinas

### Adicionado
- Cada `Maquina` passou a ser composta por `MotorDeAcionamento`, `SensorDeCalibracao` e
  `AtuadorPneumatico`, criados pela própria máquina (composição) e nunca expostos.
- As Partes influenciam a falha: motor superaquecido, sensor descalibrado e atuadores
  quebrados aumentam a probabilidade de falha efetiva.
- `LinhaDeProducao`: agregação de máquinas, com lista imutável e auditoria em cascata
  (linha, máquinas e Partes).
- Menu da linha de produção e diagrama de classes em `docs/`.

## [0.3.0] — Gradle, Javadoc e tratamento de erros

### Adicionado
- Build com Gradle e Gradle Wrapper: compilar, executar e gerar documentação com um comando.
- Javadoc em todo o código.
- Exceções de domínio a partir de `PlantaException` (recurso insuficiente, demanda inválida,
  máquina indisponível, quantidade inválida).
- Opção de reparar as máquinas na Auditoria.

### Alterado
- As validações acontecem antes de qualquer alteração: um erro nunca deixa a planta
  parcialmente modificada. O menu mostra o motivo e volta ao início.

## [0.2.0] — Cenários, estratégias e auditoria

### Adicionado
- Cenários de operação (Ideal e Apocalíptico), que definem budget, falha e desgaste.
- Saúde das máquinas, que cai a cada uso.
- Padrão Strategy para escolher a próxima demanda: fila de chegada, maior lote e máximo
  rendimento do orçamento.
- Tela de auditoria (interface `Auditavel`) com o estado de máquinas e produtos.

## [0.1.0] — Linha de produção

### Adicionado
- Produtos em três tiers de qualidade (Entrada, Performance e Flagship) nas categorias placa
  de vídeo, processador e placa-mãe.
- Demandas, estoque de matéria-prima e budget.
- Linha com esteira, Insersora SMT, Montadora e Inspetora, e menu de terminal.
