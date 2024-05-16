public class Cliente {

    public static void main(String[] args) {
		// Mensagens de Texto
		MnSG mensagemSlackTexto = new Slack(new Texto());
		mensagemSlackTexto.enviarMensagemAoAplicativo();
		System.out.println();

		MnSG mensagemEmailTexto = new Email(new Texto());
		mensagemEmailTexto.enviarMensagemAoAplicativo();
		System.out.println();

		MnSG mensagemSms = new Sms(new Texto());
		mensagemSms.enviarMensagemAoAplicativo();
		System.out.println();

		// Mensagens de Voz
		MnSG mensagemSlackVoz = new Slack(new Voz());
		mensagemSlackVoz.enviarMensagemAoAplicativo();
		System.out.println();

		MnSG mensagemEmailVoz = new Email(new Voz());
		mensagemEmailVoz.enviarMensagemAoAplicativo();
		System.out.println();

		MnSG mensagemSmsVoz = new Sms(new Voz());
		mensagemSmsVoz.enviarMensagemAoAplicativo();
		System.out.println();

		// Mensagens de Imagem
		MnSG mensagemSlackImagem = new Slack(new Imagem());
		mensagemSlackImagem.enviarMensagemAoAplicativo();
		System.out.println();

		MnSG mensagemEmailImagem = new Email(new Texto());
		mensagemEmailImagem.enviarMensagemAoAplicativo();
		System.out.println();

		MnSG mensagemSmsImagem = new Sms(new Imagem());
		mensagemSmsImagem.enviarMensagemAoAplicativo();
		System.out.println();
	}
    
}
