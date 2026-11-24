package integracao;

import java.util.List;

import modelo.Livro;

/**
 * Interface que o NOSSO sistema espera para importar livros
 * (no padrao Adapter, este e o papel de "Alvo" / Target).
 *
 * A BibliotecaFacade so conhece esta interface. Ela nao sabe de onde os
 * livros vem: hoje do catalogo antigo, amanha poderia ser de um arquivo.
 */
public interface FonteDeLivros {

    List<Livro> carregarLivros();
}
