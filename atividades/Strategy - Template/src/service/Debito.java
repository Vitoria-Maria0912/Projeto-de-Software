package service;

public class Debito extends PagamentoService implements TipoDePagamento {

    @Override
    public double processarPagamento (double valor) {
        checagensIniciais();
        verificacaoDeSeguranca();
        realizarPagamento(valor);
        atualizaSaldo();
        notificaCompraConcluida();
        return valor;
    }

    @Override
    public void checagensIniciais() {
        System.out.println("Checando saldo da conta do usuário.");
    }

    @Override
    public void realizarPagamento(double valorCompra){
        System.out.println("Realizando pagamento via cartão de débito. Valor da compra = " + valorCompra);
    }

    @Override
    public void atualizaSaldo() {
        System.out.println("Realizando atualização do saldo da conta.");
    }
}
