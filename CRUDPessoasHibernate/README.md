# CRUDPessoasHibernate

## Sobre o exercício

Este exercício é uma aplicação desktop em Java para cadastrar e administrar pessoas em um banco de dados SQL Server. A interface foi construída com Swing e a persistência é feita com Hibernate: em vez de escrever comandos SQL para cada operação CRUD, o programa trabalha principalmente com objetos Java e o ORM traduz essas operações para instruções SQL.

O exemplo permite:

- cadastrar uma pessoa;
- pesquisar pelo ID;
- pesquisar pelo nome, inclusive por parte do nome e sem diferenciar maiúsculas de minúsculas;
- editar uma pessoa;
- excluir uma pessoa após confirmação.

Cada pessoa possui `id`, `nome`, `rg` e `cpf`. O ID é gerado pelo banco.

## Tecnologias

- Java 21;
- Maven;
- Swing;
- Hibernate ORM 7;
- Jakarta Persistence (anotações de mapeamento);
- Microsoft JDBC Driver para SQL Server;
- SQL Server.

As dependências e a versão do Java estão declaradas em [`pom.xml`](pom.xml).

## Como o Hibernate é usado

Hibernate é um **ORM (Object-Relational Mapper)**. Ele faz o mapeamento entre:

- classes e objetos Java, como `Pessoa`;
- tabelas e linhas relacionais, como a tabela `Pessoas`.

Assim, o código da aplicação não precisa montar manualmente cada `INSERT`, `UPDATE`, `DELETE` ou `SELECT`. O Hibernate conhece o mapeamento da entidade, gera as operações SQL necessárias e usa o driver JDBC do SQL Server para executá-las. JDBC continua sendo a tecnologia de comunicação com o banco por baixo do ORM; Hibernate trabalha acima dela.

### Mapeamento da entidade

[`modelo/Pessoa.java`](src/main/java/com/mycompany/crudpessoasmaven/modelo/Pessoa.java) representa a entidade persistida:

- `@Entity` informa que a classe é uma entidade gerenciada pelo ORM;
- `@Table(name = "Pessoas")` associa a entidade à tabela `Pessoas`;
- `@Id` marca a chave primária;
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` indica que o ID é gerado pelo mecanismo `IDENTITY` do banco;
- `@Column` associa os atributos Java às colunas correspondentes.

Ao cadastrar, a aplicação cria uma `Pessoa` sem definir o ID; o banco gera esse valor. A classe também declara consultas nomeadas com `@NamedQueries`. No DAO atual, porém, a pesquisa por ID usa `Session.find` e a pesquisa por nome usa HQL.

### Inicialização e configuração

[`DAL/HibernateUtil.java`](src/main/java/com/mycompany/crudpessoasmaven/DAL/HibernateUtil.java) inicializa uma única `SessionFactory` para a aplicação. A `SessionFactory` é um recurso compartilhado e mais custoso de criar; a partir dela, cada operação abre uma `Session`, que representa uma unidade de trabalho com o banco.

O utilitário carrega [`hibernate.cfg.xml`](src/main/resources/hibernate.cfg.xml), que informa:

- a classe do driver JDBC;
- a URL JDBC do SQL Server;
- o usuário e a senha;
- a entidade mapeada;
- o comportamento de geração/atualização do esquema.

O arquivo está configurado com `hbm2ddl.auto=update`. Isso pede ao Hibernate para atualizar o esquema necessário ao mapeamento quando a aplicação inicia. O banco de dados indicado na URL ainda precisa existir e estar acessível; confira também a instância, a porta, as permissões e a configuração de segurança do SQL Server.

**Configuração local:** ajuste URL, usuário e senha em `hibernate.cfg.xml` para seu ambiente. Não publique credenciais reais nem as inclua em commits; em um projeto compartilhado, mantenha os segredos fora do repositório.

O arquivo [`META-INF/persistence.xml`](src/main/resources/META-INF/persistence.xml) também existe no projeto, mas o fluxo atual não inicializa JPA por meio dele: `HibernateUtil` usa diretamente `Configuration.configure()` e `hibernate.cfg.xml`. Portanto, para executar este programa, a configuração efetivamente utilizada é `hibernate.cfg.xml`.

### Sessões, transações e operações

[`DAL/PessoaDAO.java`](src/main/java/com/mycompany/crudpessoasmaven/DAL/PessoaDAO.java) contém as operações de persistência. Em cada operação, o DAO abre uma `Session`; nas alterações, inicia uma transação, executa a operação e confirma a transação com `commit()`.

| Ação | API do Hibernate | O que acontece |
|---|---|---|
| Cadastrar | `session.persist(pessoa)` | Torna a nova entidade persistente; com o `commit`, o Hibernate grava o registro. |
| Pesquisar por ID | `session.find(Pessoa.class, id)` | Busca uma entidade pelo tipo e sua chave primária. |
| Editar | `session.merge(pessoa)` | Copia o estado do objeto para uma entidade gerenciada; a transação confirma a atualização. |
| Excluir | `session.remove(pessoa)` | Marca a entidade para remoção; a transação confirma a exclusão. |
| Pesquisar por nome | HQL com `createQuery` | Executa uma consulta sobre a entidade e seus atributos, retornando uma lista de `Pessoa`. |

As operações de escrita (`persist`, `merge` e `remove`) usam transações porque alterações no banco devem ser confirmadas como uma unidade. A pesquisa por nome usa **HQL**, a linguagem de consulta do Hibernate: `FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(:nome)`. Ela se refere à entidade e ao atributo Java, e não diretamente à tabela e à coluna. O valor da busca é passado como parâmetro nomeado (`:nome`).

O DAO guarda uma mensagem de resultado que a camada de controle devolve à interface. Nos caminhos de execução normais, as sessões são fechadas depois das operações; o exemplo não usa `finally` para garantir o fechamento se ocorrer uma exceção antes do `close()`.

## Fluxo de dados pela aplicação

O fluxo geral é:

```text
Interface Swing
      ↓ dados digitados
Controle
      ↓ validação
Validacao
      ↓ objeto Pessoa
PessoaDAO
      ↓ Session / transação / consulta HQL
Hibernate
      ↓ JDBC
SQL Server
```

Na leitura, o resultado percorre o caminho inverso: o Hibernate materializa objetos `Pessoa`, o DAO os devolve ao controle, e a tela exibe seus dados.

### Responsabilidade das camadas

1. **Apresentação (`apresentacao`)** — [`frmPrincipal.java`](src/main/java/com/mycompany/crudpessoasmaven/apresentacao/frmPrincipal.java) apresenta o menu. [`frmCadastro.java`](src/main/java/com/mycompany/crudpessoasmaven/apresentacao/frmCadastro.java) coleta dados para cadastro. [`frmPEE.java`](src/main/java/com/mycompany/crudpessoasmaven/apresentacao/frmPEE.java) reúne pesquisa, edição e exclusão. As telas chamam `Controle` e mostram as mensagens ao usuário.
2. **Modelo e regras (`modelo`)** — [`Controle.java`](src/main/java/com/mycompany/crudpessoasmaven/modelo/Controle.java) coordena validação e chamadas ao DAO. [`Validacao.java`](src/main/java/com/mycompany/crudpessoasmaven/modelo/Validacao.java) verifica as entradas. `Pessoa` representa os dados e seu mapeamento para o banco.
3. **Acesso a dados (`DAL`)** — `PessoaDAO` concentra as operações usando Hibernate. `HibernateUtil` fornece a `SessionFactory` compartilhada.

### Exemplo: cadastro

1. A tela `frmCadastro` lê nome, RG e CPF e os envia a `Controle`.
2. `Controle` pede a `Validacao` que confira os dados. O nome é obrigatório e deve ter de 3 a 50 caracteres; RG e CPF são opcionais, com limites de 11 e 13 caracteres, respectivamente.
3. Se a validação passar, `Controle` cria um objeto `Pessoa` com esses campos. O ID não é informado, pois é gerado pelo banco.
4. `PessoaDAO` abre uma sessão e uma transação, chama `persist` e confirma a transação.
5. Hibernate converte a operação do objeto em SQL, o driver a envia ao SQL Server e a mensagem de resultado retorna à tela.

### Pesquisa e seleção de resultados

A pesquisa por ID valida que o valor informado seja um inteiro e chama `find`. A pesquisa por nome usa correspondência parcial, sem diferenciar caixa. Se houver apenas um resultado, a tela preenche os campos diretamente; se houver vários, `frmSelecao` mostra uma tabela para o usuário escolher. Depois de selecionar, a pessoa escolhida pode ser editada ou excluída.

## Estrutura relevante

```text
CRUDPessoasHibernate/
├── pom.xml
└── src/main/
    ├── java/com/mycompany/crudpessoasmaven/
    │   ├── apresentacao/   # Janelas Swing
    │   ├── modelo/         # Entidade, controle e validação
    │   └── DAL/            # DAO e inicialização Hibernate
    └── resources/
        ├── hibernate.cfg.xml
        └── META-INF/persistence.xml
```

## Preparar e executar

Pré-requisitos:

- JDK 21;
- Maven;
- SQL Server ativo e acessível;
- banco definido na URL de `hibernate.cfg.xml` criado e acessível pelo usuário configurado.

Na pasta `CRUDPessoasHibernate`, compile o projeto e resolva as dependências Maven:

```bash
mvn clean package
```

Para executar, abra o projeto em uma IDE com suporte a Maven e rode a classe `com.mycompany.crudpessoasmaven.CRUDPessoasMaven`, que inicia a interface principal. Antes da primeira execução, ajuste a conexão no arquivo `src/main/resources/hibernate.cfg.xml` para sua instância do SQL Server.

## Observações sobre o exemplo

- [`DAL/Conexao.java`](src/main/java/com/mycompany/crudpessoasmaven/DAL/Conexao.java) mantém uma implementação de conexão JDBC direta, mas o DAO usado pelo fluxo atual executa as operações com Hibernate.
- O `hbm2ddl.auto=update` é conveniente para o exercício, mas em sistemas reais a evolução do esquema deve ser controlada com migrações e a configuração do Hibernate deve ser adequada ao ambiente.
- A aplicação separa interface, regras de negócio e acesso a dados para deixar claro onde cada responsabilidade deve ficar.
