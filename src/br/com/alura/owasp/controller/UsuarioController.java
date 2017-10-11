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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.alura.owasp.dao.UsuarioDao;
import br.com.alura.owasp.model.Role;
import br.com.alura.owasp.model.Usuario;
import br.com.alura.owasp.util.VerificaRecaptcha;

@Controller
@Transactional
public class UsuarioController {

	@Autowired
	private UsuarioDao dao;

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
		chamaLogicaParaTratarImagem(usuario, request);
		usuario.getRoles().add(new Role("ROLE_USER"));
		dao.salva(usuario);
		model.addAttribute("usuario", usuario);
		session.setAttribute("usuario", usuario);
		return "usuarioLogado";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("usuario") Usuario usuario,
			RedirectAttributes redirect, Model model, HttpSession session, HttpServletRequest request) {
		
		String resposta = request.getParameter("g-recaptcha-response");
		
		VerificaRecaptcha.valido(resposta);

		return pesquisaUsuario(usuario, redirect, model, session);

	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("usuario");
		return "usuario";
	}

	private void chamaLogicaParaTratarImagem(Usuario usuario,
			HttpServletRequest request) {
		usuario.setNomeImagem(usuario.getImagem().getOriginalFilename());
		File arquivoDeImagem = new File(request.getServletContext()
				.getRealPath("/image"), usuario.getNomeImagem());
		try {
			usuario.getImagem().transferTo(arquivoDeImagem);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String pesquisaUsuario(Usuario usuario,
			RedirectAttributes redirect, Model model, HttpSession session) {
		Usuario usuarioRetornado = dao.procuraUsuario(usuario);
		model.addAttribute("usuario", usuarioRetornado);
		
		if (usuarioRetornado == null) {
			redirect.addFlashAttribute("mensagem", "Usuário não encontrado!");
			return "redirect:/usuario";
		}

		session.setAttribute("usuario", usuarioRetornado);
		return "usuarioLogado";
	}
}
