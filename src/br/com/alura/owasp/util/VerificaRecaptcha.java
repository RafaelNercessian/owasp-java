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

	public static final String url = "https://www.google.com/recaptcha/api/siteverify";
	public static final String secret = "6LfmCzMUAAAAAEmQZsWi5qBYOUi3TkjGeC627Tbm";

	public static boolean validacao(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
			return false;
		}
		
		try{
		URL objetoURL = new URL(url);
		HttpsURLConnection conexao = (HttpsURLConnection) objetoURL.openConnection();

		// Header request
		conexao.setRequestMethod("POST");

		String parametros = "secret=" + secret + "&response="
				+ gRecaptchaResponse;

		// Envia request Post
		conexao.setDoOutput(true);
		DataOutputStream dadosSaida = new DataOutputStream(conexao.getOutputStream());
		dadosSaida.writeBytes(parametros);
		dadosSaida.flush();
		dadosSaida.close();

		//Pega dados de retorno
		BufferedReader bufferDeleitura = new BufferedReader(new InputStreamReader(
				conexao.getInputStream()));
		String linhaDeEntrada;
		StringBuffer resposta = new StringBuffer();

		while ((linhaDeEntrada = bufferDeleitura.readLine()) != null) {
			resposta.append(linhaDeEntrada);
		}
		bufferDeleitura.close();
		
		//Faz o parse do JSON e retorna 'sucesso'
		JsonReader leitorJson = Json.createReader(new StringReader(resposta.toString()));
		JsonObject objetoJson = leitorJson.readObject();
		leitorJson.close();
		
		return objetoJson.getBoolean("success");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
