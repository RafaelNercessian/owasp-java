package br.com.alura.owasp.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.alura.owasp.dao.BlogDao;
import br.com.alura.owasp.model.BlogPost;
import br.com.alura.owasp.validator.BlogPostValidator;

@Controller
@Transactional
public class BlogController {

	@Autowired
	private BlogDao dao;

	@Autowired
	private BlogPostValidator validator;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.setValidator(validator);
	}

	@RequestMapping("/blog")
	public String blog(Model model) {
		chamaPostsDoBanco(model);
		return "blog";
	}

	@RequestMapping(value = "/enviaMensagem", method = RequestMethod.POST)
	public String enviaMensagem(
			@Valid @ModelAttribute(value = "blog") BlogPost blog,
			BindingResult result, RedirectAttributes redirect, Model model) {
		chamaPostsDoBanco(model);
		
		//Segunda versão, usando a classe BlogPostValidator
		if (result.hasErrors()) {
			return "blog";
		}

		// Primeira versão de correção, usamos o escapeHtml
		blog.setMensagem(StringEscapeUtils.escapeHtml(blog.getMensagem()));
		blog.setTitulo(StringEscapeUtils.escapeHtml(blog.getTitulo()));
		dao.salvaBlogPost(blog);
		return "redirect:/blog";
	}

	private void chamaPostsDoBanco(Model model) {
		BlogPost blogPost = new BlogPost();
		model.addAttribute(blogPost);
		List<BlogPost> mensagens = dao.buscaMensagens();
		model.addAttribute("lista", mensagens);
	}

}
