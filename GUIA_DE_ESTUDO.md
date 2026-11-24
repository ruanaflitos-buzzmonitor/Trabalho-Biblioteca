# Guia de estudo — Sistema de Biblioteca

> Este guia é para **o grupo estudar**, não para entregar. O professor vai fazer perguntas para cada integrante, individualmente, então todo mundo precisa conseguir explicar o código. Leia com o projeto aberto no Eclipse.

## 1. O projeto em 30 segundos

> "É um sistema de biblioteca em Java, no console. Ele cadastra livros e usuários, faz empréstimos e devoluções com limite e prazo diferentes para aluno e professor, busca livros por título ou autor e avisa quem está na lista de espera quando um livro é devolvido. Os livros iniciais vêm de um sistema antigo, em formato de texto."

## 2. Cada padrão em uma frase

| Padrão | Frase para lembrar | Arquivo para abrir |
|---|---|---|
| Singleton | "Só existe uma Biblioteca: construtor privado e `getInstancia()`." | `servico/Biblioteca.java` |
| Factory Method | "Só a fábrica decide se cria `LivroFisico` ou `Ebook`; o resto do sistema recebe um `Livro`." | `fabrica/LivroFactory.java` |
| Facade | "O menu só conversa com a Facade, que esconde as várias classes envolvidas em cada operação." | `servico/BibliotecaFacade.java` |
| Adapter | "Converte o texto do sistema antigo em objetos `Livro`, como um adaptador de tomada." | `integracao/CatalogoAntigoAdapter.java` |
| Strategy | "Cada jeito de buscar é uma classe; a busca recebe a estratégia escolhida." | `busca/BuscaStrategy.java` |
| Observer | "Quem está na lista de espera é avisado sozinho quando o livro é devolvido." | `modelo/Livro.java` (`devolver` e `notificarObservadores`) |

## 3. Siga o caminho de um empréstimo no código

1. `MenuConsole.emprestarLivro()` lê a matrícula e o id e chama `facade.emprestarLivro(...)`.
2. `BibliotecaFacade.emprestarLivro()` busca o usuário e o livro na `Biblioteca` (Singleton).
3. Ela confere: o usuário e o livro existem? (`verificarUsuarioELivro`). O livro está disponível? (`livro.isDisponivel()`). O usuário ainda pode pegar livros? (`usuario.podePegarEmprestado()`).
4. `livro.emprestar()` marca o livro como emprestado.
5. `usuario.registrarEmprestimo(livro)` cria o `Emprestimo` com o prazo daquele tipo de usuário (`getPrazoEmDias()`, que é polimorfismo).
6. A Facade devolve a mensagem com a data de devolução, e o menu mostra na tela.

Depois faça o mesmo com a devolução (opção 7, onde o Observer entra em ação) e com a busca (opção 2, onde o Strategy entra em ação).

## 4. Perguntas que podem cair

**Singleton**

- *Por que não fazer `new Biblioteca()` normalmente?* Porque cada `new` criaria um acervo diferente. Com o Singleton, todo mundo usa a mesma instância e os dados ficam consistentes.
- *O que garante que só existe uma?* O construtor `private` e o atributo `static instancia`, que o `getInstancia()` só cria se ainda estiver `null`.

**Factory Method**

- *Isso não é só uma "Simple Factory"?* É a variação parametrizada do Factory Method, que o próprio livro do GoF descreve: um método fábrica recebe o tipo e decide qual subclasse criar, e quem chama só conhece a classe abstrata `Livro`. Além disso, a `LivroFactory` poderia ser estendida (uma subclasse sobrescrevendo `criarLivro`) para criar novos tipos.
- *Por que não usaram fábrica para Aluno e Professor?* Usuários só são criados em um lugar (o cadastro na Facade). Livros são criados em dois (cadastro e importação), e foi isso que justificou a fábrica.

**Facade x Adapter**

- *Qual a diferença?* A Facade **simplifica** o uso de várias classes do nosso próprio sistema. O Adapter **converte** uma interface incompatível (o texto do sistema antigo) para a interface que o nosso sistema espera (`FonteDeLivros`).

**Strategy**

- *Onde está o "contexto"?* Em `BibliotecaFacade.buscarLivros`, que recebe a estratégia. Quem escolhe qual estratégia usar é o `MenuConsole`.
- *Como adicionar uma busca por ano?* Criando uma classe nova, sem mexer nas outras (e colocando mais uma opção no menu de busca):

  ```java
  public class BuscaPorAno implements BuscaStrategy {
      @Override
      public boolean corresponde(Livro livro, String termo) {
          return String.valueOf(livro.getAno()).equals(termo);
      }
  }
  ```

**Observer**

- *Quem é o sujeito e quem é o observador?* O `Livro` é o sujeito (o observado). O `Usuario` é o observador, porque implementa `Observador`.
- *Por que o `Livro` não importa a classe `Usuario`?* Porque ele guarda uma `List<Observador>`. Ele só conhece a interface, então qualquer classe que a implemente pode ser avisada.

**POO**

- *Classe abstrata x interface?* `Livro` e `Usuario` são abstratas: têm atributos, métodos prontos e alguns métodos abstratos para as filhas completarem. `Observador`, `BuscaStrategy` e `FonteDeLivros` são interfaces: no nosso projeto, elas só declaram o que deve ser feito, sem nenhum código.
- *Onde tem sobrecarga e onde tem sobrescrita?* Sobrecarga: `adicionarLivro(Livro)` e `adicionarLivro(List<Livro>)` na `Biblioteca`, e os dois `lerInteiro(...)` no `MenuConsole`. Sobrescrita: `getTipo()`, `getDetalhes()`, `getLimiteDeLivros()`, `getPrazoEmDias()`, `toString()`, `notificar()` e `corresponde()`.
- *Por que `getLivros()` devolve `new ArrayList<>(livros)`?* Para devolver uma cópia: quem está de fora consegue ler, mas não consegue adicionar ou remover itens direto na lista original (encapsulamento).
- *Por que o construtor de `Livro` é `protected`?* Porque só as subclasses (`LivroFisico` e `Ebook`) devem usá-lo. Como `Livro` é abstrata, ela não pode ser instanciada sozinha.
- *Por que `proximoId` é `static`?* Porque o contador é compartilhado por todos os livros: cada livro novo pega o próximo número.

**GRASP**

- *Quem cria o `Emprestimo` e por quê?* O `Usuario`, no método `registrarEmprestimo`, porque ele guarda os seus empréstimos e conhece o próprio prazo (princípio Creator).
- *Por que `podePegarEmprestado()` fica no `Usuario` e não na Facade?* Porque é o usuário que tem a informação necessária, a lista de empréstimos e o limite (Information Expert).
- *Quem é o Controller?* A `BibliotecaFacade`.

**Diagrama e execução**

- *Agregação x composição?* Agregação (losango vazio): a `Biblioteca` guarda os livros, mas um livro continua fazendo sentido sozinho. Composição (losango cheio): o `Emprestimo` pertence ao `Usuario` e é removido quando o livro é devolvido.
- *O que acontece se alguém digitar uma letra no menu?* O `lerInteiro` captura a `NumberFormatException` com `try/catch`, mostra "Valor invalido!" e pergunta de novo.

## 5. Para treinar (opcional)

1. Criar a `BuscaPorAno` acima e colocá-la no menu de busca.
2. Criar um tipo de usuário `Funcionario` (limite de 3 livros, prazo de 10 dias). É uma classe nova que herda de `Usuario`, mais um método `cadastrarFuncionario` na Facade e uma opção no menu de cadastro. Repare que a regra de empréstimo não precisa mudar nada.
3. Mudar o prazo do aluno para 10 dias e perceber que só um lugar muda: `Aluno.getPrazoEmDias()`.
