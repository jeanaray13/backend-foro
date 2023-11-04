package com.backend.foro.bean;

import java.util.List;

public class Post {
	public List<Usuario> usuarios;
	//public List<Comentario> comentarios;
	
	//Getters y setters
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuario) {
		this.usuarios = usuario;
	}
	/*public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}*/
	
	public Post() {
		
	}
	
}
