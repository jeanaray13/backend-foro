package com.backend.foro.bean;

import java.util.List;

public class Post {
	public Usuario usuario;
	public List<Comentario> comentarios;
	
	//Getters y setters
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	public Post() {
		
	}
	
}
