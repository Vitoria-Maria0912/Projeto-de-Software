package service;

public class PagamentoService {

    public double processarPagamento(TipoDePagamento metodoDePagamento, double valor) {
        return metodoDePagamento.processarPagamento(valor);
    }

    public void verificacaoDeSeguranca() {
        System.out.println("Realizando verificações de segurança.");
    }

    public void notificaCompraConcluida() {
        System.out.println("Notifica compra concluída.");
    }
}

