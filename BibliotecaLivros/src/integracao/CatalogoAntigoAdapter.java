package integracao;

import java.util.ArrayList;
import java.util.List;

import fabrica.LivroFactory;
import modelo.Livro;

/**
 * PADRAO ADAPTER (estrutural)
 *
 * Problema: o catalogo antigo entrega TEXTO ("F;Dom Casmurro;..."), mas o
 * nosso sistema trabalha com OBJETOS Livro, atraves da interface
 * FonteDeLivros. As duas "tomadas" nao encaixam.
 *
 * Solucao: este adaptador implementa FonteDeLivros (o que o nosso sistema
 * espera) e, por dentro, chama o CatalogoAntigo e converte cada linha de
 * texto em um Livro. Nenhum dos dois lados precisou ser alterado.
 */
public class CatalogoAntigoAdapter implements FonteDeLivros {

    private CatalogoAntigo catalogoAntigo; // a classe que esta sendo adaptada
    private LivroFactory fabrica;

    public CatalogoAntigoAdapter(CatalogoAntigo catalogoAntigo) {
        this.catalogoAntigo = catalogoAntigo;
        this.fabrica = new LivroFactory();
    }

    @Override
    public List<Livro> carregarLivros() {
        List<Livro> livros = new ArrayList<>();

        for (String registro : catalogoAntigo.exportarRegistros()) {
            String[] campos = registro.split(";");

            String tipo = converterTipo(campos[0]);
            String titulo = campos[1];
            String autor = campos[2];
            int ano = Integer.parseInt(campos[3]);
            int informacaoExtra = Integer.parseInt(campos[4]);

            // Factory Method: a fabrica decide se cria LivroFisico ou Ebook
            Livro livro = fabrica.criarLivro(tipo, titulo, autor, ano, informacaoExtra);
            if (livro != null) {
                livros.add(livro);
            }
        }

        return livros;
    }

    // O sistema antigo usa "F" e "E"; o nosso usa os tipos da LivroFactory
    private String converterTipo(String codigoAntigo) {
        if (codigoAntigo.equals("E")) {
            return LivroFactory.EBOOK;
        }
        return LivroFactory.FISICO;
    }
}
