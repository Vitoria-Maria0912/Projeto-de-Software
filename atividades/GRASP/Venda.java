import java.util.ArrayList;

public class Venda {
    
    private String dataDaVenda;
    private ArrayList<ItemDeVenda> itens; 

    public Venda(String dataVenda) {
        this.dataDaVenda = dataVenda;
        this.itens = new ArrayList<ItemDeVenda>();
    }

    public Venda() {
        this.itens = new ArrayList<ItemDeVenda>();
    }

    public void fazerPagamento(){
        Pagamento pagamento = new Pagamento();
        pagamento.criarPagamento();
    }

    public void addItem(Produto produto, int quantidade){
        this.itens.add(new ItemDeVenda(produto, quantidade));
    }

    public Double totalvenda(){
        Double preço = 0.0;
        int quantidade = 0;
        for(ItemDeVenda item : this.itens){
            preço += item.subtotal();
            quantidade += item.getQuantidade();
        }
        if(quantidade >= 100){
            preço *= 0.9;
        }

        return preço;
    }

}