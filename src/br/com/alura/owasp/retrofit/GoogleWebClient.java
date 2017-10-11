package br.com.alura.owasp.retrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import br.com.alura.owasp.model.Resposta;

public class GoogleWebClient {

	public void verifica(String resposta) throws IOException {
		String secret="6LeF1DMUAAAAADlfRa9Y-BuroxGZMX31iSTkgglU";
		Call<Resposta> call = new RetrofitInicializador().getGoogleService().enviaToken(secret,resposta);
		Response<Resposta> execute = call.execute();
		System.out.println(execute.body().isSuccess());
	}
	
}
