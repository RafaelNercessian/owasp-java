package br.com.alura.owasp.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class VerificaRecaptcha {

	public static boolean valido(String resposta) throws IOException {

		if (resposta.isEmpty()) {
			return false;
		}

		URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
		HttpsURLConnection conexao = (HttpsURLConnection) url.openConnection();

		conexao.setRequestMethod("POST");
		String parametros = "secret=6LeF1DMUAAAAADlfRa9Y-BuroxGZMX31iSTkgglU&response="
				+ resposta;
		enviaOPost(conexao, parametros);

	}

	private static void enviaOPost(HttpsURLConnection conexao, String parametros)
			throws IOException {
		conexao.setDoOutput(true);
		DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
		saida.writeBytes(parametros);
		saida.flush();
		saida.close();
	}

}
