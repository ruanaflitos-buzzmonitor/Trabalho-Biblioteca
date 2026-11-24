package fabrica;

import modelo.Ebook;
import modelo.Livro;
import modelo.LivroFisico;

/**
 * PADRAO FACTORY METHOD (criacional) - na forma "parametrizada".
 *
 * Problema: livros sao criados em DOIS lugares do sistema: no cadastro
 * feito pelo menu e na importacao do catalogo antigo. Sem a fabrica,
 * os dois lugares precisariam conhecer LivroFisico e Ebook e repetir o
 * mesmo if/else para decidir qual classe criar.
 *
 * Solucao: so esta classe decide qual subclasse de Livro instanciar.
 * Quem chama recebe um Livro pronto e nao precisa saber se e fisico ou
 * e-book. Para criar um novo tipo (ex.: Audiobook), mudamos so aqui.
 */
public class LivroFactory {

    // Tipos aceitos pela fabrica (as constantes evitam erro de digitacao)
    public static final String FISICO = "fisico";
    public static final String EBOOK = "ebook";

    /**
     * Metodo fabrica: cria o tipo certo de livro.
     * informacaoExtra = numero de paginas (livro fisico) ou tamanho em MB (e-book).
     * Retorna null quando o tipo informado nao e conhecido.
     */
    public Livro criarLivro(String tipo, String titulo, String autor, int ano, int informacaoExtra) {
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
}
