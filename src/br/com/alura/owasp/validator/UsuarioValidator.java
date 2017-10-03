package br.com.alura.owasp.validator;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.alura.owasp.model.Usuario;

@Component
public class UsuarioValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Usuario.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Usuario usuario = (Usuario) target;
		
		try {
			InputStream inputStream = usuario.getImagem().getInputStream();
			ImageIO.read(inputStream).toString();
		} catch (Exception e) {
			errors.rejectValue("imagem", "errors.imagem", "O formato da imagem não é válido!");
		}

	}

}
