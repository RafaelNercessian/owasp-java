package br.com.alura.owasp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "USUARIO")
@Scope("session")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String email;
	private String senha;
	private String nome;
	@Transient
	private MultipartFile imagem;
	private String nomeImagem;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USUARIO_ROLE", joinColumns = { @JoinColumn(name = "EMAIL") }, inverseJoinColumns = { @JoinColumn(name = "NAME") })
	private final List<Role> roles = new ArrayList<>();

	/*
	 * Deprecated
	 */
	public Usuario() {
	}

	public Usuario(String email, String nome, MultipartFile imagem,
			String senha, String nomeImagem) {
		this.email = email;
		this.nome = nome;
		this.imagem = imagem;
		this.senha = senha;
		this.nomeImagem = nomeImagem;
	}
	
	public String getNome() {
		return nome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setImagem(MultipartFile imagem) {
		this.imagem = imagem;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNomeImagem() {
		return nomeImagem;
	}
	
	public MultipartFile getImagem() {
		return imagem;
	}
	
	public void setNomeImagem(String nomeImagem) {
		this.nomeImagem = nomeImagem;
	}

}
