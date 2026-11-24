package modelo;

/**
 * Livro digital. HERDA de Livro e acrescenta o tamanho do arquivo.
 */
public class Ebook extends Livro {

    private int tamanhoMB;

    public Ebook(String titulo, String autor, int ano, int tamanhoMB) {
        super(titulo, autor, ano);
        this.tamanhoMB = tamanhoMB;
    }

    public int getTamanhoMB() {
        return tamanhoMB;
    }

    @Override
    public String getTipo() {
        return "E-book";
    }

    @Override
    public String getDetalhes() {
        return tamanhoMB + " MB";
    }
}
