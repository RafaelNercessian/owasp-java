package br.com.alura.owasp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.alura.owasp.model.BlogPost;

@Component
public class BlogPostValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return BlogPost.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BlogPost blog = (BlogPost) target;
		String titulo = blog.getTitulo();
		String mensagem = blog.getMensagem();

		if (titulo.contains("<") || titulo.contains(">")) {
			errors.rejectValue("titulo", "errors.titulo", "O título não é válido");
		}

		if (mensagem.contains("<") || mensagem.contains(">")) {
			errors.rejectValue("mensagem", "errors.mensagem","A mensagem passada não é válida");
		}
	}

}
