# Sistema de Biblioteca explicado para estudantes

O Sistema de Biblioteca é um programa Java que funciona pelo terminal. Ele permite cadastrar livros e usuários, buscar livros, registrar empréstimos e devoluções e avisar quem está esperando um livro.

Este material explica as classes e os conceitos apresentados nos comentários do código, junto com as dúvidas discutidas durante o estudo. Os exemplos seguem a implementação atual, na qual a fábrica retorna `null` quando recebe um tipo de livro inválido.

Leia com a pasta `BibliotecaLivros/src` aberta no editor. Os trechos pequenos mostram partes de métodos; para acompanhar o programa completo, comece por `Main.java`.

## 1 Visão geral do sistema

O programa organiza o trabalho em etapas:

```text
Pessoa escolhe uma opção no MenuConsole
                  ↓
BibliotecaFacade recebe o pedido e coordena a operação
                  ↓
Biblioteca e objetos do modelo consultam ou alteram os dados
                  ↓
MenuConsole mostra o resultado
```

As estratégias de busca, a fábrica e o adaptador ajudam nessas operações. Cada um tem uma responsabilidade específica.

Os dados ficam na memória durante a execução. Ao fechar e abrir o programa, o acervo inicial e os usuários de exemplo são carregados novamente. Não há banco de dados nem conexão real com um sistema antigo.

### As 19 classes e interfaces

| Arquivo | Responsabilidade |
|---|---|
| [Main.java](BibliotecaLivros/src/app/Main.java) | Iniciar o programa. |
| [MenuConsole.java](BibliotecaLivros/src/app/MenuConsole.java) | Ler as entradas e mostrar as respostas no terminal. |
| [Biblioteca.java](BibliotecaLivros/src/servico/Biblioteca.java) | Guardar os livros e os usuários em uma instância compartilhada. |
| [BibliotecaFacade.java](BibliotecaLivros/src/servico/BibliotecaFacade.java) | Coordenar cadastro, busca, empréstimo, devolução e lista de espera. |
| [Livro.java](BibliotecaLivros/src/modelo/Livro.java) | Representar os dados comuns dos livros e avisar a lista de espera. |
| [LivroFisico.java](BibliotecaLivros/src/modelo/LivroFisico.java) | Representar um livro de papel, com quantidade de páginas. |
| [Ebook.java](BibliotecaLivros/src/modelo/Ebook.java) | Representar um livro digital, com tamanho em MB. |
| [Usuario.java](BibliotecaLivros/src/modelo/Usuario.java) | Guardar dados, empréstimos e avisos de uma pessoa. |
| [Aluno.java](BibliotecaLivros/src/modelo/Aluno.java) | Definir as regras de empréstimo para alunos. |
| [Professor.java](BibliotecaLivros/src/modelo/Professor.java) | Definir as regras de empréstimo para professores. |
| [Emprestimo.java](BibliotecaLivros/src/modelo/Emprestimo.java) | Registrar o livro, a data do empréstimo e a data prevista de devolução. |
| [LivroFactory.java](BibliotecaLivros/src/fabrica/LivroFactory.java) | Criar um livro físico ou um e-book de acordo com o tipo recebido. |
| [CatalogoAntigo.java](BibliotecaLivros/src/integracao/CatalogoAntigo.java) | Fornecer os livros iniciais como registros de texto. |
| [FonteDeLivros.java](BibliotecaLivros/src/integracao/FonteDeLivros.java) | Definir o contrato para uma fonte fornecer uma lista de livros. |
| [CatalogoAntigoAdapter.java](BibliotecaLivros/src/integracao/CatalogoAntigoAdapter.java) | Transformar os registros antigos em objetos `Livro`. |
| [BuscaStrategy.java](BibliotecaLivros/src/busca/BuscaStrategy.java) | Definir o contrato de um critério de busca. |
| [BuscaPorTitulo.java](BibliotecaLivros/src/busca/BuscaPorTitulo.java) | Verificar se o título contém o texto pesquisado. |
| [BuscaPorAutor.java](BibliotecaLivros/src/busca/BuscaPorAutor.java) | Verificar se o autor contém o texto pesquisado. |
| [Observador.java](BibliotecaLivros/src/observador/Observador.java) | Definir como um objeto recebe uma notificação. |

## 2 Palavras de Java usadas no projeto

Uma **classe** descreve os dados e os comportamentos de um tipo de objeto. Um **objeto** é uma instância criada a partir dessa classe. Por exemplo, `Aluno` é uma classe e `new Aluno("Ana Souza", "A01")` cria um objeto dessa classe.

Um **atributo** guarda uma informação. Um **método** executa uma ação ou devolve uma informação. Em `Usuario`, `nome` é um atributo e `getNome()` é um método.

| Palavra ou estrutura | Significado no projeto |
|---|---|
| `public` | Permite que outras classes acessem o membro, respeitando a visibilidade da classe. |
| `private` | Restringe o acesso direto ao interior da própria classe. |
| `protected` | Permite acesso pelo mesmo pacote e pelas subclasses, conforme as regras de herança. Os construtores de `Livro` e `Usuario` são usados pelas subclasses. |
| `static` | Faz o membro pertencer à classe. Um atributo estático é compartilhado pelos objetos dessa classe. |
| `final` | Impede uma nova atribuição à variável depois de inicializada. Aparece nas constantes do projeto. |
| `abstract` | Indica uma classe que não pode ser instanciada diretamente ou um método cuja implementação será fornecida por uma subclasse. |
| `extends` | Indica herança entre classes, como `Aluno extends Usuario`. |
| `implements` | Indica que uma classe assume o contrato de uma interface. |
| `new` | Cria um objeto. |
| `this` | Representa o objeto atual. `this.nome = nome` coloca o parâmetro no atributo do objeto. |
| `super(...)` | Chama o construtor da classe mãe. |
| `return` | Encerra o método e, quando necessário, devolve um resultado. |
| `void` | Indica que o método não devolve um valor. |
| `boolean` | Guarda ou retorna `true` ou `false`. |
| `null` | Indica ausência de referência a um objeto. |
| `@Override` | Indica que o método implementa um contrato de interface ou sobrescreve um método herdado. |

### Vetor e lista

`String[]` é um vetor de textos. Depois de criado, seu tamanho é fixo. O catálogo antigo usa esse formato para entregar seus sete registros.

`List<Livro>` é uma lista que aceita objetos do tipo `Livro` e de suas subclasses. No projeto, `ArrayList` é a implementação usada para criar essas listas:

```java
List<Livro> livros = new ArrayList<>();
```

`add()` adiciona um elemento, `size()` informa a quantidade e `isEmpty()` verifica se a lista está vazia. O laço abaixo visita um livro de cada vez:

```java
for (Livro livro : livros) {
    System.out.println(livro);
}
```

### Comparações que aparecem no código

`livro == null` pergunta se a variável está sem um objeto. `livro != null` pergunta se existe um objeto referenciado.

Para textos, o projeto usa `equals()` e `equalsIgnoreCase()`. O segundo compara sem diferenciar letras maiúsculas de minúsculas. Por isso, a busca por matrícula reconhece tanto `A01` quanto `a01`.

## 3 Como o programa começa

### Main

O comentário de `Main` informa que essa é a classe principal. Seu método `main()` é o ponto de entrada da aplicação:

```java
public static void main(String[] args) {
    MenuConsole menu = new MenuConsole();
    menu.iniciar();
}
```

Primeiro, ele cria um `MenuConsole`. Depois, chama `iniciar()`, que carrega os dados de exemplo e começa a mostrar as opções.

### MenuConsole

O comentário principal dessa classe separa duas tarefas: ler o que a pessoa digita e mostrar as respostas. As operações do sistema são encaminhadas à `BibliotecaFacade`.

No construtor, são preparados o leitor do teclado e a fachada:

```java
this.teclado = new Scanner(System.in);
this.facade = new BibliotecaFacade();
```

`Scanner(System.in)` lê a entrada do terminal. A variável `facade` dá acesso às operações da biblioteca.

O método `carregarDadosIniciais()` importa sete livros usando o adaptador e cadastra três usuários:

| Matrícula | Nome | Tipo |
|---|---|---|
| `A01` | Ana Souza | Aluno |
| `A02` | Bruno Lima | Aluno |
| `P01` | Carla Mendes | Professor |

O `do/while` mostra o menu pelo menos uma vez e continua enquanto a opção for diferente de zero. O `switch` escolhe o método correspondente à opção digitada. Cada `break` encerra aquele caso do `switch`.

| Opção | Ação |
|---|---|
| 1 | Listar livros. |
| 2 | Buscar por título ou autor. |
| 3 | Cadastrar livro físico ou e-book. |
| 4 | Listar usuários e seus empréstimos. |
| 5 | Cadastrar aluno ou professor. |
| 6 | Emprestar livro. |
| 7 | Devolver livro. |
| 8 | Entrar na lista de espera. |
| 9 | Consultar os avisos de um usuário. |
| 0 | Encerrar o programa e fechar o leitor do teclado. |

### Métodos auxiliares e sobrecarga

`lerTexto()` mostra uma mensagem, lê uma linha e usa `trim()` para retirar espaços das extremidades. `mostrarLivros()` percorre uma lista ou informa que nenhum livro foi encontrado.

Os comentários identificam duas versões de `lerInteiro()`:

```java
lerInteiro(String mensagem)
lerInteiro(String mensagem, int minimo, int maximo)
```

A primeira tenta transformar o texto em inteiro com `Integer.parseInt()`. Se a pessoa digitar uma letra, o `catch (NumberFormatException e)` mostra uma mensagem e o laço tenta novamente.

A segunda também exige que o número esteja dentro de um intervalo. No menu principal, por exemplo, aceita apenas valores entre 0 e 9. Isso é **sobrecarga**: métodos com o mesmo nome e parâmetros diferentes.

O menu ainda pede ano de publicação entre 1 e o ano atual, páginas entre 1 e 10000 e tamanho de e-book entre 1 e 2000 MB. Essas verificações orientam a entrada pelo console.

## 4 Biblioteca e o padrão Singleton

O comentário de `Biblioteca` apresenta o problema que o Singleton resolve: o acervo e os usuários precisam ficar em um lugar compartilhado. Se cada parte do programa criasse sua própria biblioteca, os dados poderiam ficar diferentes.

### O atributo instancia

```java
private static Biblioteca instancia;
```

Essa linha declara um **atributo**, não um método:

- `private`: só a própria classe acessa esse atributo diretamente.
- `static`: existe uma referência compartilhada na classe.
- `Biblioteca`: é o tipo do objeto que pode ser referenciado.
- `instancia`: é o nome do atributo.

Inicialmente, o atributo está com o valor `null`. Ainda não foi criado um objeto `Biblioteca` para guardar ali.

### O construtor privado

```java
private Biblioteca() {
    livros = new ArrayList<>();
    usuarios = new ArrayList<>();
}
```

O construtor prepara as duas listas. Como ele é privado, outras classes não podem executar `new Biblioteca()`. Elas precisam pedir a instância pelo método público.

### O método getInstancia

```java
public static Biblioteca getInstancia() {
    if (instancia == null) {
        instancia = new Biblioteca();
    }
    return instancia;
}
```

Na primeira chamada, `instancia` está `null`. O método cria a biblioteca, guarda a referência e a devolve.

Nas chamadas seguintes, a condição do `if` é falsa. O método devolve o objeto já criado. O comentário ao lado do `return` destaca exatamente esse reaproveitamento.

```java
Biblioteca biblioteca1 = Biblioteca.getInstancia();
Biblioteca biblioteca2 = Biblioteca.getInstancia();

System.out.println(biblioteca1 == biblioteca2); // resultado esperado: true
```

Aqui, `==` compara se as duas variáveis apontam para o mesmo objeto. Elas compartilham o mesmo acervo. No projeto, a fachada obtém esse objeto com `Biblioteca.getInstancia()`.

### Cadastro, consulta e cópia das listas

`Biblioteca` adiciona usuários e livros, procura um livro pelo ID e procura um usuário pela matrícula. Quando uma busca não encontra o objeto, devolve `null`, como indicam os comentários.

Também há sobrecarga em `adicionarLivro()`:

```java
public void adicionarLivro(Livro livro)
public void adicionarLivro(List<Livro> novosLivros)
```

A primeira versão recebe um livro. A segunda recebe uma lista e adiciona cada livro usando a primeira versão.

`getLivros()` e `getUsuarios()` devolvem uma nova lista:

```java
return new ArrayList<>(livros);
```

Isso protege a estrutura da lista original. Remover um elemento da cópia não remove o elemento da lista interna da biblioteca. Os objetos dentro das duas listas continuam sendo os mesmos; copiar a lista não cria cópias de cada livro.

## 5 Livros usuários e empréstimos

### Livro e suas subclasses

`Livro` é uma classe abstrata. Ela reúne os atributos comuns: ID, título, autor, ano, disponibilidade e lista de espera. O programa cria objetos concretos de `LivroFisico` ou `Ebook`.

Os métodos abaixo são abstratos porque cada tipo de livro fornece sua própria resposta:

```java
public abstract String getTipo();
public abstract String getDetalhes();
```

| Classe | Informação específica | Resposta de getDetalhes |
|---|---|---|
| `LivroFisico` | Número de páginas. | Por exemplo, `256 paginas`. |
| `Ebook` | Tamanho do arquivo em MB. | Por exemplo, `5 MB`. |

As subclasses chamam `super(titulo, autor, ano)` para inicializar os dados comuns. Depois, guardam seu atributo específico. Isso demonstra **herança** e **sobrescrita**.

O contador abaixo é compartilhado por todos os livros:

```java
private static int proximoId = 1;
```

No construtor, o livro recebe o valor atual e o contador aumenta com `proximoId++`. Assim, livros físicos e e-books participam da mesma sequência de IDs durante a execução.

`emprestar()` muda `disponivel` para `false`. `devolver()` muda para `true` e chama a notificação da lista de espera. As verificações de quem pode emprestar e devolver ficam na fachada e no usuário.

O método `toString()` monta o texto usado na listagem. Ele apresenta os dados, o tipo, os detalhes e uma das situações: disponível, emprestado ou emprestado com pessoas esperando.

### Usuario e suas subclasses

`Usuario` também é uma classe abstrata. Ela guarda nome, matrícula, empréstimos atuais e avisos recebidos. `Aluno` e `Professor` herdam essa estrutura e definem regras diferentes:

| Classe | Limite de livros ao mesmo tempo | Prazo de cada empréstimo |
|---|---|---|
| `Aluno` | 2 | 7 dias |
| `Professor` | 5 | 14 dias |

Esses valores vêm dos métodos `getLimiteDeLivros()` e `getPrazoEmDias()`. O método `getTipo()` identifica se o usuário é aluno ou professor.

```java
public boolean podePegarEmprestado() {
    return emprestimos.size() < getLimiteDeLivros();
}
```

Para Ana, que é aluna, `getLimiteDeLivros()` retorna 2. Para Carla, que é professora, retorna 5. A mesma lógica usa respostas diferentes conforme o objeto. Isso é **polimorfismo**.

`estaComLivro()` consulta os empréstimos e verifica se o usuário está com aquele livro. A busca interna compara os IDs. `registrarDevolucao()` encontra o empréstimo e o remove da lista, caso exista.

Os comentários de `getEmprestimos()` e `getAvisos()` explicam o encapsulamento: os métodos entregam cópias das listas. Assim, adicionar ou remover elementos nessas cópias não altera as listas internas do usuário.

### Quem cria o Emprestimo

O próprio usuário cria o registro:

```java
public Emprestimo registrarEmprestimo(Livro livro) {
    Emprestimo emprestimo = new Emprestimo(livro, getPrazoEmDias());
    emprestimos.add(emprestimo);
    return emprestimo;
}
```

O usuário conhece seus empréstimos e seu prazo. Por isso, recebe essa responsabilidade, conforme o comentário sobre o princípio GRASP Creator.

### Emprestimo e as datas

`Emprestimo` guarda qual livro foi emprestado, a data atual e a data prevista para devolução:

```java
this.dataEmprestimo = LocalDate.now();
this.dataDevolucaoPrevista = dataEmprestimo.plusDays(prazoEmDias);
```

`LocalDate.now()` obtém a data atual. `plusDays()` acrescenta o prazo informado pelo usuário. O formato `dd/MM/yyyy` apresenta dia, mês e ano, como `23/11/2026`.

`getDataDevolucaoFormatada()` devolve o prazo em texto. `toString()` reúne título, ID e datas para mostrar o empréstimo ao listar os usuários.

## 6 LivroFactory e a criação de livros

Os comentários apresentam `LivroFactory` como a fábrica que centraliza a criação. Ela é usada em dois lugares: no cadastro pela fachada e na importação pelo adaptador. Isso evita repetir a decisão de criar `LivroFisico` ou `Ebook` nesses dois locais.

As constantes representam os tipos aceitos:

```java
public static final String FISICO = "fisico";
public static final String EBOOK = "ebook";
```

Usar `LivroFactory.FISICO` evita espalhar a palavra `"fisico"` pelo programa e reduz erros de digitação.

O método atual é:

```java
public Livro criarLivro(String tipo, String titulo, String autor,
                        int ano, int informacaoExtra) {
    Livro livro;

    if (FISICO.equals(tipo)) {
        livro = new LivroFisico(titulo, autor, ano, informacaoExtra);
    } else if (EBOOK.equals(tipo)) {
        livro = new Ebook(titulo, autor, ano, informacaoExtra);
    } else {
        livro = null; // nenhum livro foi criado, pois o tipo e invalido
    }

    return livro;
}
```

`informacaoExtra` representa páginas para um livro físico e tamanho em MB para um e-book. O retorno é declarado como `Livro`, mas o objeto criado pode ser de qualquer uma dessas duas subclasses.

A variável `livro` recebe o resultado escolhido pelo `if/else`. Depois da decisão, o único `return livro` devolve esse resultado para quem chamou o método.

O guia chama esse uso de **Factory Method parametrizado**. A implementação concreta do projeto concentra a escolha em um `if/else` dentro de `LivroFactory`; não há subclasses de fábrica sobrescrevendo esse método no código atual.

Para incluir um novo tipo, como `Audiobook`, seria necessário criar a subclasse de `Livro` e acrescentar sua criação na fábrica. Para permitir escolhê-lo pelo console, também seria necessário incluir essa opção no menu. Os lugares que recebem um `Livro` podem continuar trabalhando com o tipo comum.

### Como o tipo inválido é tratado

A versão anterior tinha `throw new IllegalArgumentException(...)`. Essa instrução lançava uma exceção para informar que o tipo recebido era inválido. Ela foi substituída, durante o estudo, por um retorno mais simples de acompanhar.

Quando o tipo é inválido, a variável `livro` recebe `null`, que significa: **nenhum livro foi criado**. Ao final, `return livro` devolve esse valor. O retorno não mostra uma mensagem sozinho. Quem chama a fábrica precisa verificar o resultado.

Na `BibliotecaFacade`, isso acontece antes de adicionar o livro ao acervo:

```java
Livro livro = fabrica.criarLivro(tipo, titulo, autor, ano, informacaoExtra);

if (livro == null) {
    return "Erro: tipo de livro desconhecido: " + tipo + ".";
}

biblioteca.adicionarLivro(livro);
```

O adaptador também verifica `livro != null` antes de adicionar o resultado à lista importada. Assim, os dois locais tratam a possibilidade de não haver um livro.

## 7 Catálogo antigo e o padrão Adapter

### O que é o CatalogoAntigo

`CatalogoAntigo` simula um sistema de biblioteca anterior. O exercício considera que seu formato já existe e deve ser aproveitado por meio de um adaptador.

Neste projeto, ele é uma classe com dados escritos no próprio código. Ele não lê um banco externo e não acessa a internet. Seu papel é fornecer livros iniciais e demonstrar a adaptação entre formatos.

### O método exportarRegistros

```java
public String[] exportarRegistros() {
    String[] registros = {
        "F;Dom Casmurro;Machado de Assis;1899;256",
        "F;Memorias Postumas de Bras Cubas;Machado de Assis;1881;368",
        "F;O Cortico;Aluisio Azevedo;1890;304",
        "F;Capitaes da Areia;Jorge Amado;1937;280",
        "F;A Hora da Estrela;Clarice Lispector;1977;96",
        "E;Clean Code;Robert C. Martin;2008;5",
        "E;Use a Cabeca! Java;Kathy Sierra;2005;40"
    };
    return registros;
}
```

`public` permite a chamada por outras classes. `String[]` informa que o retorno é um vetor de textos. `registros` é a variável local que guarda esse vetor. `return registros` entrega o vetor para quem chamou.

Cada elemento representa um livro com cinco campos separados por ponto e vírgula:

```text
TIPO;TITULO;AUTOR;ANO;EXTRA
```

| Campo do primeiro registro | Significado |
|---|---|
| `F` | Livro físico. |
| `Dom Casmurro` | Título. |
| `Machado de Assis` | Autor. |
| `1899` | Ano de publicação. |
| `256` | Número de páginas. |

Quando o primeiro campo é `E`, o último campo representa o tamanho do e-book em MB. Por exemplo, o registro de `Clean Code` informa 5 MB.

O método apenas devolve os registros. Ele não imprime os textos e não cria os objetos `Livro`.

### A interface FonteDeLivros

```java
public interface FonteDeLivros {
    List<Livro> carregarLivros();
}
```

Essa interface define um contrato: qualquer classe que a implementar precisa oferecer `carregarLivros()`, devolvendo uma lista de objetos `Livro`.

No padrão Adapter, ela faz o papel de **Target**, a interface esperada pelo sistema novo. A fachada recebe esse contrato:

```java
public int importarLivros(FonteDeLivros fonte) {
    List<Livro> livrosImportados = fonte.carregarLivros();
    biblioteca.adicionarLivro(livrosImportados);
    return livrosImportados.size();
}
```

A fachada pede os livros sem precisar conhecer os detalhes da origem. Uma futura classe que lesse um arquivo poderia implementar o mesmo contrato. Essa leitura de arquivo é uma possibilidade de extensão, não uma função já implementada.

### O trabalho de CatalogoAntigoAdapter

```java
public class CatalogoAntigoAdapter implements FonteDeLivros
```

O adaptador guarda uma referência ao `CatalogoAntigo` e usa uma `LivroFactory`. Seu método `carregarLivros()` percorre os registros e separa os campos:

```java
String[] campos = registro.split(";");
```

Para o registro de Dom Casmurro:

| Posição | Conteúdo |
|---|---|
| `campos[0]` | `F` |
| `campos[1]` | `Dom Casmurro` |
| `campos[2]` | `Machado de Assis` |
| `campos[3]` | `1899` |
| `campos[4]` | `256` |

As posições começam em zero. `Integer.parseInt()` transforma o ano e a informação extra, que vieram como texto, em números inteiros.

O método `converterTipo()` traduz os códigos para os valores aceitos pela fábrica. No código atual, `E` vira `LivroFactory.EBOOK` e os demais valores viram `LivroFactory.FISICO`, incluindo o `F` usado nos registros fornecidos.

Depois, a fábrica cria o objeto e o adaptador o adiciona à lista, se o retorno for diferente de `null`. Ao terminar, devolve essa lista à fachada.

```text
CatalogoAntigo fornece String[]
                  ↓
CatalogoAntigoAdapter separa e converte os campos
                  ↓
LivroFactory cria LivroFisico ou Ebook
                  ↓
O adaptador devolve List<Livro>
                  ↓
BibliotecaFacade adiciona os livros à Biblioteca
```

Esse caminho acontece em `MenuConsole.carregarDadosIniciais()`. A **Facade simplifica operações** do sistema; o **Adapter adapta a forma de fornecer os dados**.

## 8 BibliotecaFacade e as operações

O comentário da fachada explica que uma operação como emprestar envolve várias tarefas. É preciso localizar usuário e livro, verificar as regras, mudar a disponibilidade e registrar o empréstimo.

`BibliotecaFacade` coordena essas tarefas e oferece chamadas simples ao menu. Seu construtor obtém a biblioteca compartilhada e cria a fábrica:

```java
this.biblioteca = Biblioteca.getInstancia();
this.fabrica = new LivroFactory();
```

| Método | O que faz |
|---|---|
| `importarLivros()` | Pede livros a uma `FonteDeLivros`, adiciona ao acervo e devolve a quantidade. |
| `cadastrarLivro()` | Verifica título e autor, pede a criação à fábrica e cadastra se houver um livro. |
| `listarLivros()` | Obtém a lista de livros da biblioteca. |
| `buscarLivros()` | Aplica a estratégia recebida e reúne os livros encontrados. |
| `cadastrarAluno()` | Cria um `Aluno` e encaminha para a verificação de cadastro. |
| `cadastrarProfessor()` | Cria um `Professor` e encaminha para a mesma verificação. |
| `cadastrarUsuario()` | Verifica nome, matrícula e repetição de matrícula antes de adicionar. É um método privado. |
| `listarUsuarios()` | Obtém a lista de usuários. |
| `consultarAvisos()` | Localiza o usuário e reúne seus avisos em um texto. |
| `emprestarLivro()` | Verifica as condições, marca o livro e registra o empréstimo. |
| `devolverLivro()` | Verifica quem está com o livro, remove o empréstimo e chama a devolução. |
| `entrarNaListaDeEspera()` | Verifica as condições e registra o usuário como observador do livro. |
| `verificarUsuarioELivro()` | Verifica se o usuário e o livro foram encontrados. É um método privado. |

O comentário de `cadastrarUsuario(Usuario usuario)` destaca o polimorfismo: esse método recebe tanto um `Aluno` quanto um `Professor`.

### O significado de null depende do retorno

Na fábrica, `null` significa que nenhum livro foi criado. Nas buscas da biblioteca, significa que nenhum objeto foi encontrado.

Em `verificarUsuarioELivro()`, o retorno é uma possível mensagem de erro. Se retorna `null`, significa que não há mensagem de erro e a operação pode continuar:

```java
String erro = verificarUsuarioELivro(usuario, livro);
if (erro != null) {
    return erro;
}
```

Esse uso aparece nas operações de empréstimo, devolução e lista de espera.

## 9 BuscaStrategy e o padrão Strategy

Os comentários de `BuscaStrategy` explicam a separação entre os critérios. Cada forma de buscar tem uma classe própria, e a fachada recebe a estratégia escolhida pelo menu.

### O contrato da busca

```java
public interface BuscaStrategy {
    boolean corresponde(Livro livro, String termo);
}
```

`livro` é o livro que será examinado. `termo` é o texto digitado na busca. O retorno responde à pergunta: **este livro corresponde ao texto pesquisado?**

- `true`: o livro deve entrar no resultado.
- `false`: o livro não deve entrar no resultado.

### As duas implementações

Em `BuscaPorTitulo`, o método examina o título:

```java
return livro.getTitulo().toLowerCase().contains(termo.toLowerCase());
```

Em `BuscaPorAutor`, examina o autor:

```java
return livro.getAutor().toLowerCase().contains(termo.toLowerCase());
```

`toLowerCase()` transforma os textos em letras minúsculas para a comparação. `contains()` verifica se um texto está contido no outro. Assim, pesquisar `DOM` encontra o título `Dom Casmurro`.

### Onde a interface é usada

Na opção 2 do menu, `MenuConsole` escolhe qual objeto criar:

```java
BuscaStrategy estrategia;
if (tipoBusca == 1) {
    estrategia = new BuscaPorTitulo();
} else {
    estrategia = new BuscaPorAutor();
}
```

A variável tem o tipo da interface, mas guarda um objeto de uma das implementações. O menu envia esse objeto à fachada:

```java
mostrarLivros(facade.buscarLivros(termo, estrategia));
```

Em `BibliotecaFacade.buscarLivros()`, cada livro é examinado usando a estratégia recebida:

```java
for (Livro livro : biblioteca.getLivros()) {
    if (estrategia.corresponde(livro, termo)) {
        encontrados.add(livro);
    }
}
```

A fachada é o **contexto** da estratégia nessa operação. Ela mantém o mesmo percurso da lista e deixa o critério de correspondência com o objeto recebido.

Por exemplo, buscar por autor com o termo `machado` encontra os dois livros de Machado de Assis do acervo inicial. Buscar por título com `java` encontra `Use a Cabeca! Java`.

Uma busca por ano exigiria uma nova classe que implementasse `BuscaStrategy` e uma opção para escolhê-la no menu. Essa classe é uma proposta de exercício do guia e ainda não faz parte dos fontes.

## 10 Observador e o padrão Observer

O Observer é usado quando alguém quer ser avisado sobre a devolução de um livro. O **livro é o sujeito observado** e o **usuário é o observador**.

### A interface Observador

```java
public interface Observador {
    void notificar(String mensagem);
}
```

A interface define como receber um aviso. `void` indica que o método não devolve um valor. O texto do aviso chega pelo parâmetro `mensagem`.

`Usuario` implementa essa interface:

```java
public abstract class Usuario implements Observador
```

Seu método guarda a mensagem na lista de avisos:

```java
@Override
public void notificar(String mensagem) {
    avisos.add(mensagem);
}
```

`Aluno` e `Professor` herdam essa implementação. Portanto, os dois tipos de usuário podem receber avisos.

### Como o usuário entra na lista

`Livro` guarda uma lista com o tipo da interface:

```java
private List<Observador> listaDeEspera;
```

O método `adicionarObservador()` recebe quem deseja acompanhar o livro:

```java
public boolean adicionarObservador(Observador observador) {
    if (listaDeEspera.contains(observador)) {
        return false;
    }
    listaDeEspera.add(observador);
    return true;
}
```

Se esse observador já estiver na lista, retorna `false` e não adiciona novamente. Caso contrário, adiciona e retorna `true`.

A fachada chama esse método com um `Usuario`. Isso é permitido porque `Usuario` implementa `Observador`.

Antes da inclusão, a fachada verifica se o usuário e o livro existem, se o livro está emprestado e se o usuário já está com ele. Um livro disponível pode ser emprestado diretamente. Quem já está com o livro não entra na lista de espera dele.

### O que acontece na devolução

```java
public void devolver() {
    disponivel = true;
    notificarObservadores();
}
```

O método privado percorre os observadores:

```java
private void notificarObservadores() {
    for (Observador observador : listaDeEspera) {
        observador.notificar("O livro \"" + titulo + "\" (id " + id
                + ") foi devolvido e esta disponivel!");
    }
    listaDeEspera.clear();
}
```

Cada chamada de `notificar()` executa o comportamento do objeto referenciado pela variável. Para um usuário, esse comportamento é guardar a mensagem em `avisos`.

Depois de avisar todos, `clear()` esvazia a lista de espera do livro. Os avisos já guardados nos usuários continuam lá. A notificação informa disponibilidade; ela não faz um novo empréstimo automaticamente e não envia e-mail.

O comentário sobre baixo acoplamento explica por que `Livro` trabalha com `Observador`: basta saber que cada objeto oferece `notificar()`. Outra classe poderia implementar esse mesmo contrato.

### Exemplo com Ana e Bruno

Os comandos abaixo representam o uso da fachada em uma sessão recém-iniciada pelo menu, com os livros e usuários iniciais já carregados:

```java
// Ana pega Dom Casmurro, cujo ID inicial é 1.
facade.emprestarLivro("A01", 1);

// Bruno entra na lista de espera desse livro.
facade.entrarNaListaDeEspera("A02", 1);

// Ana devolve o livro e Bruno recebe o aviso.
facade.devolverLivro("A01", 1);

// A mensagem guardada para Bruno pode ser consultada.
System.out.println(facade.consultarAvisos("A02"));
```

O texto esperado na consulta é:

```text
Avisos de Bruno Lima:
  - O livro "Dom Casmurro" (id 1) foi devolvido e esta disponivel!
```

Nesse caminho, `devolverLivro()` retira o empréstimo de Ana antes de chamar `livro.devolver()`. A chamada chega a `notificarObservadores()`, que chama o `notificar()` de Bruno e guarda a mensagem.

## 11 Onde estão os seis padrões de projeto

| Padrão | Categoria | Participantes no projeto | Ideia para lembrar |
|---|---|---|---|
| Singleton | Criacional | `Biblioteca`. | Uma biblioteca compartilhada, obtida por `getInstancia()`. |
| Factory Method parametrizado, conforme o guia | Criacional | `LivroFactory`, `Livro`, `LivroFisico` e `Ebook`. | A fábrica decide qual tipo de livro criar. |
| Facade | Estrutural | `BibliotecaFacade`, chamada pelo `MenuConsole`. | Uma operação simples coordena várias tarefas internas. |
| Adapter | Estrutural | `CatalogoAntigoAdapter`, `CatalogoAntigo` e `FonteDeLivros`. | O adaptador transforma registros antigos na lista esperada pelo sistema. |
| Strategy | Comportamental | `BuscaStrategy`, `BuscaPorTitulo`, `BuscaPorAutor`, `BibliotecaFacade` e `MenuConsole`. | Cada classe define um critério de busca; o menu escolhe e a fachada usa. |
| Observer | Comportamental | `Observador`, `Usuario` e `Livro`. | O livro avisa os usuários que estavam esperando sua devolução. |

No Adapter, `FonteDeLivros` é o contrato esperado, `CatalogoAntigo` é o componente adaptado e `CatalogoAntigoAdapter` faz a conversão. No Observer e no Strategy, a colaboração entre as classes e as interfaces forma o padrão.

## 12 Princípios GRASP e conceitos de orientação a objetos

### GRASP nos comentários

GRASP ajuda a decidir qual classe deve receber cada responsabilidade.

| Princípio | Onde aparece | Por que essa classe recebe a responsabilidade |
|---|---|---|
| Controller | `BibliotecaFacade`. | Recebe os pedidos do menu e coordena as operações. |
| Information Expert, ou Especialista na Informação | `Usuario.podePegarEmprestado()`. | O usuário possui seus empréstimos e sabe seu limite. |
| Creator, ou Criador | `Usuario.registrarEmprestimo()`. | O usuário guarda os empréstimos e conhece o prazo necessário para criá-los. |

Os comentários também destacam o baixo acoplamento ao usar interfaces. A dependência fica concentrada no contrato: o livro depende de `Observador`, a busca depende de `BuscaStrategy` e a importação depende de `FonteDeLivros`.

### Conceitos de POO para identificar no código

| Conceito | Explicação com o projeto |
|---|---|
| Abstração | `Livro` reúne o que os livros têm em comum e deixa detalhes para suas subclasses. |
| Encapsulamento | Atributos privados e métodos de acesso protegem a organização interna dos dados. As cópias protegem a estrutura das listas. |
| Herança | `LivroFisico` e `Ebook` herdam de `Livro`; `Aluno` e `Professor` herdam de `Usuario`. |
| Polimorfismo | Uma variável `Livro` pode referenciar um livro físico ou um e-book, e uma variável `BuscaStrategy` pode referenciar critérios diferentes. |
| Sobrescrita | As subclasses definem suas versões de `getTipo()`, `getDetalhes()` e das regras de empréstimo. |
| Sobrecarga | Há duas versões de `adicionarLivro()` e duas de `lerInteiro()`, com parâmetros diferentes. |
| Interface | Define métodos que as implementações precisam oferecer, como `notificar()` e `corresponde()`. |
| Relação entre objetos | A biblioteca guarda livros e usuários; o usuário guarda empréstimos; cada empréstimo referencia um livro. |

`toString()` também é sobrescrito para fornecer uma representação em texto dos objetos. Por isso, `System.out.println(livro)` consegue mostrar os dados preparados pelo método da classe `Livro`.

## 13 Caminhos completos das operações

### Caminho de um empréstimo

1. O menu lê matrícula e ID.
2. Chama `BibliotecaFacade.emprestarLivro()`.
3. A fachada procura usuário e livro na `Biblioteca`.
4. Se algum deles não existir, devolve uma mensagem de erro.
5. Verifica se o livro está disponível.
6. Pergunta ao usuário se ele ainda pode pegar livros.
7. Chama `livro.emprestar()` para mudar a disponibilidade.
8. Chama `usuario.registrarEmprestimo(livro)` para criar o registro com o prazo correto.
9. Devolve uma mensagem com a data prevista, que o menu mostra na tela.

### Caminho de uma devolução

1. O menu lê matrícula e ID e chama a fachada.
2. A fachada verifica se usuário e livro existem.
3. Confere se esse usuário está com esse livro.
4. Guarda a quantidade de pessoas na lista de espera.
5. Remove o empréstimo do usuário.
6. Chama `livro.devolver()`.
7. O livro fica disponível, avisa os observadores e esvazia sua lista de espera.
8. A fachada devolve a confirmação e, quando houver avisos, informa quantos usuários foram notificados.

### Caminho de uma busca

1. O menu lê a escolha entre título e autor e o texto pesquisado.
2. Cria `BuscaPorTitulo` ou `BuscaPorAutor`.
3. Envia a estratégia e o termo para `BibliotecaFacade.buscarLivros()`.
4. A fachada visita cada livro e chama `estrategia.corresponde()`.
5. Adiciona ao resultado apenas os livros que receberam `true`.
6. O menu mostra a lista resultante.

## 14 Exercício pelo menu

Abra uma nova execução para usar o acervo inicial. A sequência abaixo é um roteiro de estudo, com os resultados esperados para o código atual:

| Passo | Opção do menu | Dados para digitar em seguida | Resultado esperado |
|---|---|---|---|
| 1 | `1` | Nenhum. | Mostrar os sete livros importados. |
| 2 | `2` | `2` para autor e depois `machado`. | Encontrar dois livros de Machado de Assis. |
| 3 | `2` | `1` para título e depois `java`. | Encontrar `Use a Cabeca! Java`. |
| 4 | `6` | Matrícula `A01` e ID `1`. | Emprestar Dom Casmurro para Ana por sete dias. |
| 5 | `6` | Matrícula `A02` e ID `1`. | Informar que o livro já está emprestado. |
| 6 | `8` | Matrícula `A02` e ID `1`. | Colocar Bruno na lista de espera. |
| 7 | `8` | Matrícula `A02` e ID `1` novamente. | Informar que Bruno já está na lista, sem duplicar a inscrição. |
| 8 | `7` | Matrícula `A01` e ID `1`. | Registrar a devolução e avisar um usuário. |
| 9 | `9` | Matrícula `A02`. | Mostrar o aviso recebido por Bruno. |
| 10 | `4` | Nenhum. | Listar usuários, empréstimos atuais e quantidade de avisos. |
| 11 | `0` | Nenhum. | Encerrar o programa. |

Para iniciar no Windows, use [executar.bat](BibliotecaLivros/executar.bat). No Eclipse, abra `src/app/Main.java` e escolha **Run As > Java Application**. As instruções de importação e execução estão no [README](BibliotecaLivros/README.md).

## 15 Perguntas para revisar

**`private static Biblioteca instancia` é um método?** Não. É um atributo que guarda a referência à biblioteca compartilhada. O método é `getInstancia()`.

**O que acontece na primeira chamada de `getInstancia()`?** A biblioteca ainda não existe, então o método cria o objeto, guarda a referência e a devolve. Nas próximas chamadas, devolve o mesmo objeto.

**O catálogo antigo é outro programa rodando?** Neste projeto, é uma simulação com sete registros escritos em Java. O adaptador demonstra como aproveitar um formato anterior.

**O método `exportarRegistros()` mostra os livros na tela?** Não. Ele entrega um vetor de textos ao adaptador. A exibição pertence ao menu.

**O que `FonteDeLivros` faz?** Define que uma fonte deve oferecer `carregarLivros()` e retornar `List<Livro>`. O adaptador realiza esse trabalho para o catálogo antigo.

**Onde `BuscaStrategy` é usada?** As duas classes de busca implementam a interface; o menu escolhe uma delas; a fachada chama `corresponde()` para examinar cada livro.

**Onde `Observador` é usada?** `Usuario` implementa a interface, e `Livro` guarda uma lista desses observadores para chamar `notificar()` na devolução.

**Quem recebe e guarda o aviso?** O usuário. O livro produz a mensagem e chama a notificação; o método de `Usuario` adiciona o texto à lista `avisos`.

**Por que a fábrica retorna `null`?** Para indicar que o tipo não permitiu criar um livro. A fachada verifica o resultado e devolve uma mensagem; o adaptador só adiciona um retorno válido à lista.

**O programa ainda usa exceções?** Sim. A simplificação foi feita na fábrica. O menu continua usando `try/catch` para tratar uma entrada que não pode ser convertida em inteiro.

**Onde uma regra muda para aluno ou professor?** Nos métodos de `Aluno` e `Professor`, como `getLimiteDeLivros()` e `getPrazoEmDias()`. A lógica do empréstimo consulta essas respostas.

**Quais classes eu devo abrir para mostrar os padrões?** `Biblioteca` para Singleton; `LivroFactory` para a fábrica; `BibliotecaFacade` para Facade; `CatalogoAntigoAdapter` e `FonteDeLivros` para Adapter; as três classes e interfaces de `busca` para Strategy; `Livro`, `Usuario` e `Observador` para Observer.

## 16 Materiais do projeto

- [Guia de estudo original](GUIA_DE_ESTUDO.md).
- [Documentação técnica](BibliotecaLivros/docs/DOCUMENTACAO.md).
- [Instruções de execução](BibliotecaLivros/README.md).
- [Diagrama de classes](BibliotecaLivros/docs/diagrama-uml.png).
