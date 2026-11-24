package modelo;

/**
 * Professor: pode ficar com ate 5 livros ao mesmo tempo, por 14 dias cada.
 * HERDA de Usuario e SOBRESCREVE as regras que mudam para professores.
 */
public class Professor extends Usuario {

    public Professor(String nome, String matricula) {
        super(nome, matricula);
    }

    @Override
    public String getTipo() {
        return "Professor";
    }

    @Override
    public int getLimiteDeLivros() {
        return 5;
    }

    @Override
    public int getPrazoEmDias() {
        return 14;
    }
}
