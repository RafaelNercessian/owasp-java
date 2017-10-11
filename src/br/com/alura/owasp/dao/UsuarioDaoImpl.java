package br.com.alura.owasp.dao;

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
				"select u from Usuario u where u.email=:email", Usuario.class);
		query.setParameter("email", usuario.getEmail());
		Usuario usuarioRetornado = query.getResultList().stream().findFirst()
				.orElse(null);

		if(validaSenhaDoUsuarioComOHAshDoBanco(usuario, usuarioRetornado)){
			return usuarioRetornado;
		}

		return null;
	}

	@Override
	public boolean verificaSeUsuarioEhAdmin(Usuario usuario) {
		TypedQuery<Usuario> query = manager
				.createQuery(
						"select u from Usuario u where u.email =:email and u.senha =:senha",
						Usuario.class);
		query.setParameter("email", usuario.getEmail());
		query.setParameter("senha", usuario.getSenha());
		Usuario retornoUsuario = query.getResultList().stream().findFirst()
				.orElse(null);
		if (retornoUsuario != null) {
			for (Role role : retornoUsuario.getRoles()) {
				if (role.getName().equals("ROLE_ADMIN")) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	private void transformaASenhaDoUsuarioEmHash(Usuario usuario) {
		String salt = BCrypt.gensalt();
		String senhaHashed = BCrypt.hashpw(usuario.getSenha(), salt);
		usuario.setSenha(senhaHashed);
	}

	private boolean validaSenhaDoUsuarioComOHAshDoBanco(Usuario usuario,
			Usuario usuarioRetornado) {

		if (usuarioRetornado == null) {
			return false;
		}

		return BCrypt.checkpw(usuario.getSenha(), usuarioRetornado.getSenha());

	}

}
