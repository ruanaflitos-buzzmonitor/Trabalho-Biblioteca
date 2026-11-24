package modelo;

/**
 * Livro de papel. HERDA de Livro e acrescenta o numero de paginas.
 */
public class LivroFisico extends Livro {

    private int numeroPaginas;

    public LivroFisico(String titulo, String autor, int ano, int numeroPaginas) {
        super(titulo, autor, ano);
        this.numeroPaginas = numeroPaginas;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    @Override
    public String getTipo() {
        return "Livro fisico";
    }

    @Override
    public String getDetalhes() {
        return numeroPaginas + " paginas";
    }
}
