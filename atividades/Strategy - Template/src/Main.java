import service.Boleto;
import service.Credito;
import service.Debito;
import service.PagamentoService;
import service.Pix;

public class Main {
    public static void main(String[] args) {

        PagamentoService pagamentoService = new PagamentoService();

        pagamentoService.processarPagamento(new Debito(), 500);
        System.out.println("============================================");
        pagamentoService.processarPagamento(new Credito(), 1122);
        System.out.println("============================================");
        pagamentoService.processarPagamento(new Pix(), 600);
        System.out.println("============================================");
        pagamentoService.processarPagamento(new Boleto(), 160);
        System.out.println("============================================");
    }
}