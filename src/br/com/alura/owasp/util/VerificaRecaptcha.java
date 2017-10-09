package br.com.alura.owasp.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

public class VerificaRecaptcha {

	public static boolean valido(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
			return false;
		}

		URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
		HttpsURLConnection conexao = (HttpsURLConnection) url.openConnection();

		try {
			conexao.setRequestMethod("POST");

			String parametros = "secret=6LfmCzMUAAAAAEmQZsWi5qBYOUi3TkjGeC627Tbm&response="
					+ gRecaptchaResponse;

			enviaOPost(conexao, parametros);
			String resposta = leAResposta(conexao);
			return parse(resposta);

		} finally {
			conexao.disconnect();
		}
	}

	private static void enviaOPost(HttpsURLConnection conexao, String parametros)
			throws IOException {
		conexao.setDoOutput(true);
		DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
		saida.writeBytes(parametros);
		saida.flush();
		saida.close();
	}

	private static String leAResposta(HttpsURLConnection conexao)
			throws IOException {

		InputStreamReader is = new InputStreamReader(conexao.getInputStream());

		try (BufferedReader buffer = new BufferedReader(is)) {

			StringBuilder resposta = new StringBuilder();
			
			while (true) {
				String linhaAtual = buffer.readLine();
				if (linhaAtual == null) {
					return resposta.toString();
				}
				resposta.append(linhaAtual);
			}

		}
	}

	private static boolean parse(String resposta) {
		JsonReader leitor = Json.createReader(new StringReader(resposta));
		JsonObject objeto = leitor.readObject();
		return objeto.getBoolean("success");
	}
}
