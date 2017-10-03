package br.com.alura.owasp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.alura.owasp.model.Role;
import br.com.alura.owasp.model.Usuario;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Usuario usuario) {
		manager.persist(usuario);
	}

	public Usuario procuraUsuario(Usuario usuario) {
		TypedQuery<Usuario> query = manager.createQuery(
				"select u from Usuario u where u.email =:email AND u.senha =:senha", Usuario.class)
				.setParameter("email", usuario.getEmail())
				.setParameter("senha", usuario.getSenha());
		Usuario usuarioRetornado = query.getResultList().stream().findFirst()
				.orElse(null);
		return usuarioRetornado;
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
			List<Role> roles = retornoUsuario.getRoles();
			for (Role role : roles) {
				if(role.getName().equals("ROLE_ADMIN")) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
}
