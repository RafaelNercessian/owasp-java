package br.com.alura.owasp.dao;

import java.util.List;

import br.com.alura.owasp.model.BlogPost;

public interface BlogDao {
	
	public void salvaBlogPost(BlogPost post);
	public List<BlogPost> buscaMensagens();
}
