package service;

public class Credito extends PagamentoService implements TipoDePagamento {

    @Override
    public double processarPagamento (double valor) {
        double valorFinal = valor * 1.1;
        checagensIniciais();
        verificacaoDeSeguranca();
        realizarPagamento(valorFinal);
        atualizaSaldo();
        notificaCompraConcluida();
        return valorFinal;
    }

    @Override
    public void checagensIniciais() {
        System.out.println("Checando limite de crédito do usuário");
    }

    @Override
    public void realizarPagamento(double valorCompra){
        System.out.println("Realizando pagamento via cartão de crédito. Valor da compra = " + valorCompra);
    }

    @Override
    public void atualizaSaldo() {
        System.out.println("Realizando atualização do limite de crédito.");
    }
}
