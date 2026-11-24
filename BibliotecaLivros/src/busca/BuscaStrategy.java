package busca;

import modelo.Livro;

/**
 * PADRAO STRATEGY (comportamental) - papel de "Estrategia".
 *
 * Problema: da para buscar livros de jeitos diferentes (por titulo, por
 * autor...). Se tudo ficasse num metodo so, ele viraria um monte de
 * if/else que cresce toda vez que surge um tipo novo de busca.
 *
 * Solucao: cada jeito de buscar e uma classe separada que implementa esta
 * interface. Quem faz a busca (BibliotecaFacade.buscarLivros) recebe a
 * estrategia escolhida e so pergunta: "esse livro corresponde ao termo?".
 * Para criar uma busca nova (ex.: por ano), basta criar outra classe.
 */
public interface BuscaStrategy {

    boolean corresponde(Livro livro, String termo);
}
