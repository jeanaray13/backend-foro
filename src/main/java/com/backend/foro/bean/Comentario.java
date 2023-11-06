package com.backend.foro.bean;

import java.util.List;

public class Comentario {
	
	//Variables
	public String id;
	public String contenido;
	public String fecha_publicacion;
	public String hora_publicacion;
	public int likes;
	public List<Comentario> subcomentarios;
	public Usuario usuario;
	
	//Getters y setters
	public String getContenido() {
		return contenido;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public String getFecha_publicacion() {
		return fecha_publicacion;
	}
	public void setFecha_publicacion(String fecha_publicacion) {
		this.fecha_publicacion = fecha_publicacion;
	}
	public String getHora_publicacion() {
		return hora_publicacion;
	}
	public void setHora_publicacion(String hora_publicacion) {
		this.hora_publicacion = hora_publicacion;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public List<Comentario> getSubcomentarios() {
		return subcomentarios;
	}
	public void setSubcomentarios(List<Comentario> subcomentarios) {
		this.subcomentarios = subcomentarios;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	//Constructor
	public Comentario() {}
}
