package integracao;

/**
 * Simula o SISTEMA ANTIGO da biblioteca (ele ja existia antes do nosso).
 *
 * Ele exporta o acervo como linhas de TEXTO, neste formato:
 *     TIPO;TITULO;AUTOR;ANO;EXTRA
 * onde TIPO = "F" (livro fisico, EXTRA = paginas) ou "E" (e-book, EXTRA = MB).
 *
 * Faz de conta que nao podemos alterar esta classe. Por isso usamos
 * um Adapter (CatalogoAntigoAdapter) para aproveitar esses dados.
 */
public class CatalogoAntigo {

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
}
