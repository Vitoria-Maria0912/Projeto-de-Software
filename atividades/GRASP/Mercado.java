public class Mercado {

    public static void main(String[] args) {
        fazerPagamento();
    }

    public static void fazerPagamento(){
        Venda venda = new Venda(null);
        venda.fazerPagamento();
    }
}