public class Sprint {
    
    private Cargo cargo;
    
    public void defineEquipe(Gerente gerente, Lider lider, Dev dev) {
        setGerente(gerente);
        setLider(lider); 
        setDesenvolvedor(dev);
    }
    
    private void setGerente(Gerente novoGerente) {
        this.cargo = novoGerente;
    }
    
    private void setLider(Lider novoLider) {
        this.cargo = novoLider;
    }
    
    private void setDesenvolvedor(Dev novoDev) {
        this.cargo = novoDev;
    }
}
