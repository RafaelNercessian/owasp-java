package br.com.alura.owasp.model;

import org.springframework.web.multipart.MultipartFile;

public class UsuarioDTO {
	private String email;
	private String senha;
	private String nome;
	private MultipartFile imagem;
	private String nomeImagem;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public MultipartFile getImagem() {
		return imagem;
	}

	public void setImagem(MultipartFile imagem) {
		this.imagem = imagem;
	}

	public String getNomeImagem() {
		return nomeImagem;
	}

	public void setNomeImagem(String nomeImagem) {
		this.nomeImagem = nomeImagem;
	}

	public Usuario montaUsuario() {
		Usuario usuario = new Usuario();
		usuario.setEmail(this.email);
		usuario.setNome(this.nome);
		usuario.setImagem(this.imagem);
		usuario.setSenha(this.senha);
		usuario.setNomeImagem(this.nomeImagem);
		return usuario;
	}
}
