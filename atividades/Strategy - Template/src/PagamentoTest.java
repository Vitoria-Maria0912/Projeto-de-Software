

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagamentoTest {

    PagamentoService pagamentoService;

    @BeforeEach
    void setUp() {
        this.pagamentoService = new PagamentoService();
    }

    @Test
    void realizarCompraNoBoleto(){
        assertEquals(950,(this.pagamentoService.processarPagamento(new Boleto(), 1000)));
    }

    @Test
    void realizarCompraNoCredito(){
        assertEquals(1100,(this.pagamentoService.processarPagamento(new Credito(), 1000)));
    }

    @Test
    void realizarCompraNoDebito(){
        assertEquals(1000,(this.pagamentoService.processarPagamento(new Debito(), 1000)));
    }

    @Test
    void realizarCompraNoPix(){
        assertEquals(900,(this.pagamentoService.processarPagamento(new Pix(), 1000)));
    }
}