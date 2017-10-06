package br.com.alura.owasp.dao;

import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import br.com.alura.owasp.model.Role;
import br.com.alura.owasp.model.Usuario;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Usuario usuario) {
		transformaASenhaDoUsuarioEmHash(usuario);
		manager.persist(usuario);
	}

	public Usuario procuraUsuario(Usuario usuario) {
		TypedQuery<Usuario> query = manager.createQuery(
				"select u from Usuario u where u.email =:email", Usuario.class)
				.setParameter("email", usuario.getEmail());
		Usuario usuarioRetornado = query.getResultList().stream().findFirst()
				.orElse(null);
		if (!validaSenhaDoUsuarioComOHAshDoBanco(usuario, usuarioRetornado)) {
			return null;
		}
		return usuarioRetornado;
	}

	@Override
	public boolean verificaSeUsuarioEhAdmin(Usuario usuario) {
		TypedQuery<Usuario> query = manager.createQuery(
				"select u from Usuario u where u.email =:email", Usuario.class);
		query.setParameter("email", usuario.getEmail());
		Usuario usuarioRetornado = query.getResultList().stream().findFirst()
				.orElse(null);

		if (!validaSenhaDoUsuarioComOHAshDoBanco(usuario, usuarioRetornado)) {
			return false;
		}

		return verificaSeUsuarioTemRoleAdmin(usuarioRetornado);
	}

	private boolean verificaSeUsuarioTemRoleAdmin(Usuario usuarioRetornado) {
		return usuarioRetornado.getRoles().stream().map(Role::getName)
				.collect(Collectors.toList()).contains("ROLE_ADMIN");

	}

	private void transformaASenhaDoUsuarioEmHash(Usuario usuario) {
		String nivelAlgoritmo = BCrypt.gensalt(12);
		String senhaHashed = BCrypt.hashpw(usuario.getSenha(), nivelAlgoritmo);
		usuario.setSenha(senhaHashed);
	}

	private boolean validaSenhaDoUsuarioComOHAshDoBanco(Usuario usuario,
			Usuario usuarioRetornado) {
		if (usuarioRetornado == null) {
			return false;
		}
		boolean comparaSenhaComHashESemHash = BCrypt.checkpw(
				usuario.getSenha(), usuarioRetornado.getSenha());
		return comparaSenhaComHashESemHash;
	}
}
