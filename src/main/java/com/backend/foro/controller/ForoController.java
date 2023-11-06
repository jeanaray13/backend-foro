package com.backend.foro.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.foro.bean.Comentario;
import com.backend.foro.bean.Post;
import com.backend.foro.bean.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class ForoController {
	
	//Variables iniciales
	private static final String JSON_FILE_PATH = "post.json"; //Archivo de almacenamiento
    private Post post;
    
    //Constructor
    public ForoController() {
    	try {
            cargarDatosDesdeJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Método para subir datos al archivo JSON
    private void cargarDatosDesdeJSON() {
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(JSON_FILE_PATH);

            //Si el archivo existe
            if (resourceAsStream != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                post = objectMapper.readValue(resourceAsStream, Post.class);
                
                // Asignación de IDs a los comentarios
                asignarId(post.getUsuarios().get(0).getComentarios());
            } else {
                post = new Post();
            }
        } catch (IOException e) {
        	throw new RuntimeException("Error al cargar datos desde JSON", e);
        }
    }
    
    //Método para asignar un ID al archivo JSON
    private void asignarId(List<Comentario> comentarios) {
    	//Si los comentarios no son nulos
    	if (comentarios != null) {
            for (Comentario comentario : comentarios) {
                if (comentario.getId() == null || comentario.getId().isEmpty()) {
                    comentario.setId(generarIdUnico());
                }
                // Asignación de IDs a los subcomentarios de manera recursiva
                asignarId(comentario.getSubcomentarios());
            }
        }
    	
    }
    
    //Método para generar un ID único
    private String generarIdUnico() {
    	return "ID" + System.currentTimeMillis();
    }
    
    //Método para guardar los datos al archivo JSON
    private void guardarDatosEnJSON() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(Paths.get(JSON_FILE_PATH).toFile(), post);
        } catch (IOException e) {
        	throw new RuntimeException("Error al guardar datos en JSON", e);
        }
    }
    
    //GET
    @ApiOperation(value = "Obtener todos los comentarios del foro", response = Post.class)
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public Post obtenerPost() {
        return post;
    }
    
    //GET by ID
    @ApiOperation(value = "Obtener comentario por ID", response = Comentario.class)
    @RequestMapping(value = "/post/comentario/{id}", method = RequestMethod.GET)
    public ResponseEntity<Comentario> obtenerComentarioPorId(@PathVariable String id) {
        return post.getUsuarios().stream()
        		.flatMap(usuario -> usuario.getComentarios().stream())
                .filter(comentario -> comentario.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    //POST
    @ApiOperation(value = "Agregar un nuevo comentario mediante el usuario", response = String.class)
    @RequestMapping(value = "/post/comentario", method = RequestMethod.POST)
    public String agregarComentario(@RequestBody Usuario comentario) {
    	Post postActual = post;
    	//Agrega un post
    	postActual.getUsuarios().add(comentario);

        // Asigna ID al nuevo comentario
        asignarId(Collections.singletonList(comentario.getComentarios().get(0)));
        
    	guardarDatosEnJSON();
        return "Comentario agregado exitosamente";
    }
    
    @ApiOperation(value = "Agregar subcomentario a un comentario por ID", response = String.class)
    @RequestMapping(value = "/post/comentario/{id}/subcomentario", method = RequestMethod.POST)
    public String agregarSubcomentario(@PathVariable String id, @RequestBody Comentario subcomentario) {
        //Búsqueda de los comentarios
    	Comentario comentarioPadre = encontrarComentarioPorIdEnUsuarios(post.getUsuarios(), id);

    	//Si existe la búsqueda
        if (comentarioPadre != null) {
            subcomentario.setId(generarIdUnico()); // Asigna ID al nuevo subcomentario

            //Agrega el nuevo subcomentario al comentario padre
            comentarioPadre.getSubcomentarios().add(subcomentario);
            
            guardarDatosEnJSON();
            return "Subcomentario agregado exitosamente";
        } else {
            return "Comentario padre no encontrado";
        }
    }
    
    // Método para encontrar un comentario por ID recursivamente en la lista de usuarios
    private Comentario encontrarComentarioPorIdEnUsuarios(List<Usuario> usuarios, String id) {
    	//Búsqueda por cada usuario encontrado
        for (Usuario usuario : usuarios) {
            Comentario comentarioEncontrado = encontrarComentarioPorId(usuario.getComentarios(), id);
            //Si encuentra un comentario
            if (comentarioEncontrado != null) {
                return comentarioEncontrado;
            }
        }
        return null;
    }

    // Método para encontrar un comentario por ID recursivamente
    private Comentario encontrarComentarioPorId(List<Comentario> comentarios, String id) {
    	//Búsqueda por cada comentario encontrado
        for (Comentario comentario : comentarios) {
            if (comentario.getId().equals(id)) {
                return comentario;
            }

            //Si encuentra un Subcomentario
            Comentario subcomentarioEncontrado = encontrarComentarioPorId(comentario.getSubcomentarios(), id);
            if (subcomentarioEncontrado != null) {
                return subcomentarioEncontrado;
            }
        }
        return null;
    }
    
    //PUT (UPDATE)
    @ApiOperation(value = "Actualizar comentario por ID", response = String.class)
    @RequestMapping(value = "/post/comentario/{id}", method = RequestMethod.PUT)
    public String actualizarComentario(@PathVariable String id, @RequestBody Comentario comentario) {
        for (Comentario c : post.getUsuarios().get(0).getComentarios()) {
        	//Si el ID existe
            if (c.getId().equals(id)) {
                c.setContenido(comentario.getContenido());
                c.setLikes(comentario.getLikes());
                c.setFecha_publicacion(comentario.getFecha_publicacion());
                c.setHora_publicacion(comentario.getHora_publicacion());
                
                guardarDatosEnJSON();
                return "Comentario actualizado exitosamente";
            }
        }
        return "Comentario no actualizado";
    }
    
    //DELETE
    @ApiOperation(value = "Eliminar comentario por ID", response = String.class)
    @RequestMapping(value = "/post/comentario/{id}", method = RequestMethod.DELETE)
    public String eliminarComentario(@PathVariable String id) {
    	//Busca el comentario y elimina
    	if (post.getUsuarios().get(0).getComentarios().removeIf(comentario -> comentario.getId().equals(id))) {
            guardarDatosEnJSON();
            return "Comentario eliminado exitosamente";
        } else {
            return "Comentario no eliminado";
        }
    }
}