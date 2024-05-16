package service;

public class Pix extends PagamentoService implements TipoDePagamento {

    @Override
    public double processarPagamento (double valor) {
        double valorFinal = valor * 0.9;
        checagensIniciais();
        verificacaoDeSeguranca();
        realizarPagamento(valorFinal);
        atualizaSaldo();
        notificaCompraConcluida();
        return valorFinal;
    }

    @Override
    public void checagensIniciais() {
        System.out.println("Checando chave PIX do destinatário.");
    }

    @Override
    public void realizarPagamento(double valorCompra){
        System.out.println("Realizando pagamento via PIX. Valor da compra = " + valorCompra);
    }

    @Override
    public void atualizaSaldo() {
        System.out.println("Realizando atualização do saldo da conta.");
    }    
}
