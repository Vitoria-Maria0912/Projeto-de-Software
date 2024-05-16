public class Produto {

    private String nome;
    private Double preço;

    public Produto(String nome, Double preço, int quantidade) {
        this.nome = nome;
        this.preço = preço;
    }

    public String getNome() {
        return this.nome;
    }

    public Double getPreço() {
        return this.preço;
    }
}
