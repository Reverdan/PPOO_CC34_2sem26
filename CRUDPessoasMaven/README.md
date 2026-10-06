# CRUDPessoasMaven

## Sobre o projeto

Este repositório contém o projeto **CRUDPessoasMaven**, uma aplicação desktop Java com interface Swing para realizar operações de **cadastro, pesquisa, edição e exclusão de pessoas** em um banco de dados SQL Server.

O sistema foi organizado para demonstrar:

- uso de **interface gráfica** com Swing;
- separação por **3 camadas**;
- aplicação do padrão **DAO (Data Access Object)**;
- validação de dados antes do acesso ao banco.

## Objetivo funcional

O projeto mantém registros de pessoas com os seguintes campos:

- `id`
- `nome`
- `rg`
- `cpf`

As operações implementadas são:

- cadastrar uma pessoa;
- pesquisar pessoa por `id`;
- editar uma pessoa existente;
- excluir uma pessoa;
- pesquisar pessoas por nome.

## Tecnologias utilizadas

- **Java 21**
- **Maven**
- **Swing**
- **SQL Server**
- **Driver JDBC da Microsoft para SQL Server**

Dependência principal no `pom.xml`:

- `com.microsoft.sqlserver:mssql-jdbc:13.2.1.jre11`

## Estrutura geral do projeto

O código principal está em:

- `/home/runner/work/PPOO_CC34_2sem26/PPOO_CC34_2sem26/CRUDPessoasMaven`

### Árvore de pacotes e classes

```text
CRUDPessoasMaven
└── src/main/java/com/mycompany/crudpessoasmaven
    ├── CRUDPessoasMaven.java
    ├── apresentacao
    │   ├── frmPrincipal.java
    │   ├── frmCadastro.java
    │   └── frmPEE.java
    ├── modelo
    │   ├── Pessoa.java
    │   ├── Controle.java
    │   └── Validacao.java
    └── DAL
        ├── Conexao.java
        └── PessoaDAO.java
```

## Arquitetura em 3 camadas

O projeto segue uma divisão em **três camadas**, mesmo em uma aplicação simples:

### 1. Camada de Apresentação

Responsável pela interação com o usuário.

Classes:

- `frmPrincipal`
- `frmCadastro`
- `frmPEE`

Responsabilidades:

- mostrar janelas e campos;
- capturar dados digitados;
- acionar a camada de controle;
- exibir mensagens de retorno.

### 2. Camada de Modelo / Negócio

Responsável pelas regras do sistema e pelo fluxo entre tela e persistência.

Classes:

- `Pessoa`
- `Controle`
- `Validacao`

Responsabilidades:

- representar a entidade `Pessoa`;
- validar entradas;
- decidir quando chamar o DAO;
- transportar mensagens de erro ou sucesso.

### 3. Camada de Acesso a Dados (DAL)

Responsável pela comunicação com o banco.

Classes:

- `Conexao`
- `PessoaDAO`

Responsabilidades:

- abrir e fechar conexão;
- executar comandos SQL;
- transformar registros do banco em objetos `Pessoa`.

## Conceito de DAO aplicado no projeto

O padrão **DAO (Data Access Object)** separa a lógica de acesso ao banco da lógica de negócio.

Neste projeto:

- a classe `PessoaDAO` concentra os comandos SQL;
- a classe `Controle` **não escreve SQL diretamente**;
- as telas também **não acessam o banco diretamente**.

Isso traz algumas vantagens:

- maior organização do código;
- menor acoplamento entre interface e banco;
- facilidade para manutenção;
- possibilidade de trocar a estratégia de persistência com menos impacto.

### Exemplo do fluxo com DAO

No cadastro:

1. `frmCadastro` coleta os dados digitados;
2. `Controle.cadastrarPessoa(...)` recebe os dados;
3. `Validacao.validarDadosPessoa(...)` valida a entrada;
4. `PessoaDAO.cadastrarPessoa(...)` executa o `INSERT`;
5. a mensagem de retorno volta para a tela.

## Papel de cada classe

### `CRUDPessoasMaven`

Classe inicial da aplicação.

- contém o método `main`;
- abre a janela principal `frmPrincipal`.

### `apresentacao.frmPrincipal`

Janela principal do sistema.

- apresenta o menu **Pessoas**;
- abre a tela de cadastro;
- abre a tela de pesquisa/edição/exclusão.

### `apresentacao.frmCadastro`

Tela de cadastro de pessoas.

- recebe `nome`, `rg` e `cpf`;
- monta a lista de dados;
- chama a camada de controle para persistir;
- exibe a mensagem devolvida.

### `apresentacao.frmPEE`

Tela de **Pesquisar, Editar e Excluir**.

- pesquisa pessoa por `id`;
- preenche os campos com os dados encontrados;
- permite editar o registro;
- permite excluir após confirmação.

### `modelo.Pessoa`

Classe de entidade.

- representa uma pessoa no sistema;
- encapsula os atributos `id`, `nome`, `rg` e `cpf`;
- fornece getters e setters.

### `modelo.Controle`

Classe central da regra de negócio.

- coordena validação e persistência;
- cria objetos `Pessoa`;
- chama os métodos do DAO;
- mantém a mensagem final da operação.

### `modelo.Validacao`

Classe responsável por validar entradas.

- valida o `id`;
- valida a lista de dados da pessoa;
- centraliza mensagens de erro de validação.

### `DAL.Conexao`

Classe utilitária de conexão.

- mantém uma conexão JDBC estática;
- abre a conexão com o SQL Server;
- fecha a conexão;
- armazena mensagens sobre o estado da operação.

### `DAL.PessoaDAO`

Classe DAO da entidade `Pessoa`.

- executa `INSERT`, `SELECT`, `UPDATE` e `DELETE`;
- converte resultados do banco em objetos `Pessoa`;
- concentra toda a persistência da entidade.

## Função dos principais métodos

## Classe `CRUDPessoasMaven`

### `main(String[] args)`

Ponto de entrada da aplicação. Cria a janela `frmPrincipal` e a torna visível.

## Classe `frmPrincipal`

### `mniCadastrarActionPerformed(...)`

Abre a janela `frmCadastro`, usada para inserir novos registros.

### `mniPEEActionPerformed(...)`

Abre a janela `frmPEE`, usada para pesquisar, editar e excluir.

### `initComponents()`

Método gerado pelo editor visual do Swing. Monta o menu e os componentes da janela principal.

## Classe `frmCadastro`

### `btnCadastrarActionPerformed(...)`

Lê os campos da tela, monta a lista de dados da pessoa, chama `Controle.cadastrarPessoa(...)` e exibe o resultado ao usuário.

### `initComponents()`

Cria os campos da tela e o botão de cadastro.

## Classe `frmPEE`

### `btnPesquisarPorIdActionPerformed(...)`

Recebe o `id` informado, chama `Controle.pesquisarPessoa(...)`, preenche os campos da tela com o objeto retornado e mostra a mensagem da operação.

### `btnEditarActionPerformed(...)`

Monta a lista com `id`, `nome`, `rg` e `cpf`, envia para `Controle.editarPessoa(...)` e exibe o retorno.

### `btnExcluirActionPerformed(...)`

Pede confirmação ao usuário e, se confirmada, chama `Controle.excluirPessoa(...)`.

### `initComponents()`

Monta a interface da tela de pesquisa, edição e exclusão.

## Classe `Pessoa`

### `getId()` / `setId(int id)`

Lê e altera o identificador da pessoa.

### `getNome()` / `setNome(String nome)`

Lê e altera o nome.

### `getRg()` / `setRg(String rg)`

Lê e altera o RG.

### `getCpf()` / `setCpf(String cpf)`

Lê e altera o CPF.

## Classe `Controle`

### `cadastrarPessoa(List<String> listaDadosPessoa)`

Valida os dados recebidos, cria um objeto `Pessoa` e delega o cadastro ao DAO.

### `pesquisarPessoa(String numId)`

Valida o `id`, consulta o DAO e devolve o objeto `Pessoa` encontrado.

### `editarPessoa(List<String> listaDadosPessoa)`

Valida os dados, recria o objeto `Pessoa` com o `id` informado e delega a atualização ao DAO.

### `excluirPessoa(String numId)`

Valida o `id` e delega a exclusão ao DAO.

### `pesquisarPessoaPorNome(String nome)`

Valida o nome informado e retorna uma lista de pessoas cujo nome contém o trecho pesquisado.

### `getMensagem()`

Retorna a mensagem final da última operação executada.

## Classe `Validacao`

### `validarId(String numId)`

Verifica se o valor do `id` foi informado e se pode ser convertido para inteiro.

### `validarDadosPessoa(List<String> listaDadosPessoa)`

Valida a estrutura da lista e aplica regras para `id`, `nome`, `rg` e `cpf`.

### `getId()`

Retorna o `id` convertido e validado.

### `getMensagem()`

Retorna a mensagem acumulada de validação.

## Classe `Conexao`

### `conectar()`

Abre a conexão JDBC com o SQL Server caso ainda não exista ou esteja fechada.

### `desconectar()`

Fecha a conexão ativa com o banco.

## Classe `PessoaDAO`

### `cadastrarPessoa(Pessoa pessoa)`

Executa o `INSERT` na tabela `Pessoas`.

### `pesquisarPessoa(Pessoa pessoa)`

Executa o `SELECT` por `id`, preenche o objeto recebido e o devolve.

### `editarPessoa(Pessoa pessoa)`

Executa o `UPDATE` do registro correspondente ao `id`.

### `excluirPessoa(Pessoa pessoa)`

Executa o `DELETE` do registro correspondente ao `id`.

### `pesquisarPessoaporNome(Pessoa pessoa)`

Executa um `SELECT` com `LIKE` no campo `nome` e devolve uma lista de resultados.

## Fluxo resumido de uma operação

Em uma operação comum, o fluxo é:

1. usuário interage com a tela;
2. a tela chama `Controle`;
3. `Controle` usa `Validacao`;
4. se os dados forem válidos, `Controle` chama `PessoaDAO`;
5. `PessoaDAO` usa `Conexao` para executar SQL;
6. o resultado volta em forma de mensagem ou objeto;
7. a tela mostra a resposta ao usuário.

## Banco de dados

A própria classe `Conexao` documenta a estrutura esperada para o banco:

```sql
create database cc34
go
use cc34
go
create table Pessoas
(
    id int primary key identity(1,1),
    nome varchar(50) not null,
    rg varchar(11),
    cpf varchar(13)
)
```

Também há uma string de conexão fixa para SQL Server no código, apontando para uma instância local.

## Observações importantes

- o projeto usa componentes Swing gerados pelo editor visual, por isso vários métodos `initComponents()` são automáticos;
- a separação em camadas está presente mesmo em um projeto pequeno, com boa distinção entre interface, regra e persistência;
- `PessoaDAO` é o principal exemplo do padrão DAO no código;
- a aplicação depende de um SQL Server configurado de forma compatível com a string de conexão definida em `Conexao.java`.