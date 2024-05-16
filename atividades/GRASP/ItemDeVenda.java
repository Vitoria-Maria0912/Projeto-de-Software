public class ItemDeVenda {

    private Produto produto;
    private int quantidade;

    public ItemDeVenda(Produto produto, int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public Double subtotal(){
        return this.produto.getPreço() * this.quantidade;
    }

}