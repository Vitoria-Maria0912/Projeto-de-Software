public class Livro {

    private String titulo;

    private Disponibilidade disponibilidade;

    // Us3
    private int idLivro;

    public Livro(int idLivro, String titulo) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.disponibilidade = Disponibilidade.DISPONIVEL;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public int getIdLivro() {
        return this.idLivro;
    }

    public Disponibilidade getDisponibilidade() {
        return this.disponibilidade;
    }

    public void alugarLivro() {
        this.disponibilidade = Disponibilidade.ALUGADO;
    }


    @Override
    public String toString(){
        return "Titulo: " + this.titulo + "Disponibilidade: " + this.disponibilidade;
    }
}
