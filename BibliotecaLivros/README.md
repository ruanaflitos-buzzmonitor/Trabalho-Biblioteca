# BibliotecaLivros

Sistema de biblioteca em Java (console): cadastro de livros e usuários, empréstimos, devoluções, busca e lista de espera com aviso.

**Requisitos:** JDK 8 ou superior (incluindo `java` e `javac`). Não precisa de Maven, Gradle ou bibliotecas externas.

**Como executar no Windows:** dê dois cliques em `executar.bat`. Ele procura um JDK em `JAVA_HOME`, na instalação Java 21 da extensão do VS Code, em `%USERPROFILE%\.jdks` e no `PATH`. Depois compila os fontes na pasta `bin` e inicia o menu. Digite `0` para sair. Os cadastros ficam apenas na memória e são reiniciados ao fechar o programa.

**Como executar no IntelliJ IDEA:** abra a pasta `Trabalho Biblioteca`, aguarde o carregamento do projeto e execute a configuração **BibliotecaLivros**. A pasta `BibliotecaLivros/src` está marcada como código-fonte. O JDK selecionado é o **26**, já configurado no projeto; se ele não estiver disponível em outro computador, selecione um JDK instalado em **File > Project Structure > Project SDK**.

**Como executar no Eclipse:** `File > Import > General > Existing Projects into Workspace`, selecione esta pasta e clique em **Finish**. Depois clique com o botão direito em `src/app/Main.java` e escolha **Run As > Java Application**.

**Documentação técnica:** [docs/DOCUMENTACAO.md](docs/DOCUMENTACAO.md) · **Diagrama UML:** [docs/diagrama-uml.png](docs/diagrama-uml.png)

| Padrão | Categoria | Classe principal |
|---|---|---|
| Singleton | Criacional | `servico/Biblioteca` |
| Factory Method | Criacional | `fabrica/LivroFactory` |
| Facade | Estrutural | `servico/BibliotecaFacade` |
| Adapter | Estrutural | `integracao/CatalogoAntigoAdapter` |
| Strategy | Comportamental | `busca/BuscaStrategy` |
| Observer | Comportamental | `observador/Observador` e `modelo/Livro` |
