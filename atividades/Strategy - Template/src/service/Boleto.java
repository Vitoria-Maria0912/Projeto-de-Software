package service;

public class Boleto extends PagamentoService implements TipoDePagamento {

    @Override
    public double processarPagamento (double valor) {
        double valorFinal = valor * 0.95;
        checagensIniciais();
        verificacaoDeSeguranca();
        realizarPagamento(valorFinal);
        atualizaSaldo();
        notificaCompraConcluida();
        return valorFinal;
    }

    @Override
    public void checagensIniciais() {
        System.out.println("Checando chave Boleto do destinatário.");
    }

    @Override
    public void realizarPagamento(double valorCompra){
        System.out.println("Realizando pagamento via Boleto. Valor da compra = " + valorCompra);
    }

    @Override
    public void atualizaSaldo() {
        System.out.println("Realizando agendamento para atualização do saldo.");
    }
}
