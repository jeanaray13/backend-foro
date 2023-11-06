package com.backend.foro.bean;

import java.util.List;

public class Post {
	
	//Variable
	public List<Usuario> usuarios;
	
	//Getters y setters
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuario) {
		this.usuarios = usuario;
	}
	
	//Constructor
	public Post() {}
	
}
