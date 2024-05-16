package service;

public interface TipoDePagamento {
    
    public double processarPagamento(double valor);

    public void realizarPagamento(double valorCompra);

    public void checagensIniciais();

    public void atualizaSaldo();
}
