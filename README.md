# Trabalho-Biblioteca

Sistema de biblioteca em Java para estudo de orientação a objetos, padrões de projeto e GRASP. Funciona pelo console e permite cadastrar livros e usuários, fazer buscas, registrar empréstimos e devoluções e consultar avisos da lista de espera.

## Executar

O código requer JDK 8 ou superior, com `java` e `javac`, e usa apenas a biblioteca padrão do Java.

No Windows, abra a pasta `BibliotecaLivros` e dê dois cliques em [executar.bat](BibliotecaLivros/executar.bat). O arquivo procura um JDK instalado, compila o projeto e inicia o menu. Digite `0` para sair.

No Eclipse, importe a pasta `BibliotecaLivros` como projeto existente e execute [Main.java](BibliotecaLivros/src/app/Main.java) com **Run As > Java Application**.

Os dados ficam na memória. Cada nova execução carrega sete livros e três usuários de exemplo.

## Material de estudo

- [Explicação do código para estudantes](EXPLICACAO_DO_CODIGO.md).
- [Guia de estudo](GUIA_DE_ESTUDO.md).
- [Documentação técnica](BibliotecaLivros/docs/DOCUMENTACAO.md).
- [Diagrama de classes](BibliotecaLivros/docs/diagrama-uml.png).
- [Detalhes do projeto e instruções de execução](BibliotecaLivros/README.md).

## Código

Os fontes estão em [BibliotecaLivros/src](BibliotecaLivros/src). O projeto apresenta Singleton, fábrica parametrizada, Facade, Adapter, Strategy e Observer.
