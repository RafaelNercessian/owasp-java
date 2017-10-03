package br.com.alura.owasp.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.alura.owasp.dao.UsuarioDao;
import br.com.alura.owasp.model.Role;
import br.com.alura.owasp.model.Usuario;

@Controller
@Transactional
public class UsuarioController {

	@Autowired
	private UsuarioDao dao;

	//Segunda opção contra Mass Assignment, utilizar o setAllowedFields do Spring
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.setAllowedFields("email", "senha", "nome", "imagem",
				"nomeImagem");
	}

	@RequestMapping("/usuario")
	public String usuario(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario", usuario);
		return "usuario";
	}

	@RequestMapping("/usuarioLogado")
	public String usuarioLogado() {
		return "usuarioLogado";
	}

	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
	public String registrar(@ModelAttribute("usuario") Usuario usuario,
			BindingResult result, RedirectAttributes redirect,
			HttpServletRequest request, Model model, HttpSession session) {

		//Primeira opção contra Mass Assignment, usando DTO
		// Usuario usuario = usuarioDTO.montaUsuario();
		chamaLogicaParaTratarImagem(usuario, request);
		usuario.getRoles().add(new Role("ROLE_USER"));
		dao.salva(usuario);
		session.setAttribute("usuario", usuario);
		model.addAttribute("usuario", usuario);
		return "usuarioLogado";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("usuario") Usuario usuario,
			RedirectAttributes redirect, Model model, HttpSession session) {
		Usuario usuarioRetornado = dao.procuraUsuario(usuario);
		if (usuarioRetornado == null) {
			return "usuario";
		} else {
			session.setAttribute("usuario", usuarioRetornado);
			model.addAttribute("usuario", usuarioRetornado);
			return "usuarioLogado";
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("usuario");
		return "usuario";
	}

	private void chamaLogicaParaTratarImagem(Usuario usuario,
			HttpServletRequest request) {
		usuario.setNomeImagem(usuario.getImagem().getOriginalFilename());
		File imageFile = new File(request.getServletContext().getRealPath(
				"/image"), usuario.getNomeImagem());
		try {
			usuario.getImagem().transferTo(imageFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
