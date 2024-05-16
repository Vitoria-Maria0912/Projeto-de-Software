public abstract class MnSG {

    Mensagem mensagem;

    public MnSG(Mensagem mensagem){
        this.mensagem = mensagem;
    }

    public abstract void enviarMensagemAoAplicativo();
}
