package com.backend.foro.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.foro.bean.Comentario;
import com.backend.foro.bean.Post;
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

            if (resourceAsStream != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                post = objectMapper.readValue(resourceAsStream, Post.class);
                
                // Asigna IDs a los comentarios si aún no tienen
                asignarId(post.getUsuarios().get(0).getComentarios());
            } else {
                post = new Post();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            //post = new Post();
        	
        	throw new RuntimeException("Error al cargar datos desde JSON", e);
        }
    }
    
    //Método para asignar un ID al archivo JSON
    private void asignarId(List<Comentario> comentarios) {
    	if (comentarios != null) {
            for (Comentario comentario : comentarios) {
                if (comentario.getId() == null || comentario.getId().isEmpty()) {
                    comentario.setId(generarIdUnico());
                }

                // Asignar IDs a los subcomentarios recursivamente
                asignarId(comentario.getSubcomentarios());
            }
        }
    	
    }
    
    //Método para generar un ID único
    private String generarIdUnico() {
        return UUID.randomUUID().toString();
    }
    
    //Método para guardar los datos al archivo JSON
    private void guardarDatosEnJSON() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(Paths.get(JSON_FILE_PATH).toFile(), post);
        } catch (IOException e) {
            //e.printStackTrace();
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
    @ApiOperation(value = "Agregar un nuevo comentario", response = String.class)
    @RequestMapping(value = "/post/comentario", method = RequestMethod.POST)
    public String agregarComentario(@RequestBody Comentario comentario) {
        comentario.setId(generarIdUnico());
        post.getUsuarios().get(0).getComentarios().add(comentario);
        
        guardarDatosEnJSON();
        return "Comentario agregado exitosamente";
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
    	if (post.getUsuarios().get(0).getComentarios().removeIf(comentario -> comentario.getId().equals(id))) {
            guardarDatosEnJSON();
            return "Comentario eliminado exitosamente";
        } else {
            return "Comentario no eliminado";
        }
    }
}
