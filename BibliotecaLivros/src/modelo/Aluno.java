package modelo;

/**
 * Aluno: pode ficar com ate 2 livros ao mesmo tempo, por 7 dias cada.
 * HERDA de Usuario e SOBRESCREVE as regras que mudam para alunos.
 */
public class Aluno extends Usuario {

    public Aluno(String nome, String matricula) {
        super(nome, matricula);
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }

    @Override
    public int getLimiteDeLivros() {
        return 2;
    }

    @Override
    public int getPrazoEmDias() {
        return 7;
    }
}
