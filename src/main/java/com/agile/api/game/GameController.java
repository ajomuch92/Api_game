package com.agile.api.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@Autowired
	GameService gameService;
	
	@RequestMapping("/categorias")
	public ResponseEntity<List<Categoria>> getAllCategorias(){
		return new ResponseEntity<List<Categoria>>(gameService.getAllCategorias(),HttpStatus.OK);
	}
	
	@RequestMapping("/categorias/{id}")
	public Categoria getOneCategorias(@PathVariable int id){
		gameService.validateCategoria(id);
		return gameService.getOneCategorias(id);
	}
	
	@RequestMapping("/categorias/{id}/jugadores")
	public List<Usuario> getCategoriasPlayers(@PathVariable int id){
		gameService.validateCategoria(id);
		return gameService.getCategoriasPlayers(id);
	}
	
	@RequestMapping("/categorias/{id}/top")
	public List<Usuario> getTopPlayers(@PathVariable int id){
		gameService.validateCategoria(id);
		if(id>1)
			return gameService.getTopPlayers(id);
		else
			return gameService.getAllTopPlayers();
	}
	
	
	@RequestMapping(value="/categorias",method=RequestMethod.POST)
	public void addCategory(@RequestBody Categoria categoria){
		gameService.addCategory(categoria);
	}
	
	@RequestMapping(value="/categorias/{id}",method=RequestMethod.PUT)
	public void updateCategory(@RequestBody Categoria categoria, @PathVariable int id){
		gameService.validateCategoria(id);
		gameService.updateCategory(id,categoria);
	}
	@RequestMapping(value="/categorias/{id}",method=RequestMethod.DELETE)
	public void deleteCategory(@PathVariable int id){
		gameService.validateCategoria(id);
		gameService.deleteCategory(id);
	}
	
	@RequestMapping("/jugadores")
	public ResponseEntity<List<Usuario>> getAllUsuarios(){
		return new ResponseEntity<List<Usuario>>(gameService.getAllUsuarios(),HttpStatus.OK);
	}
	
	@RequestMapping("/jugadores/{id}")
	public Usuario getOneJugadores(@PathVariable int id){
		gameService.validateJugador(id);
		return gameService.getOneJugador(id);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/jugadores/{id}")
	public void deleteJugador(@PathVariable int id) {
		gameService.validateJugador(id);
		gameService.deleteJugador(id);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/jugadores/{id}")
	public void updateJugador(@PathVariable int id,@RequestBody Usuario usuario) {
		gameService.validateJugador(id);
		gameService.updateJugador(id,usuario);
	}
	
	@RequestMapping(value="/jugadores", method=RequestMethod.POST)
	public void addJugador(@RequestBody Usuario usuario) {
		gameService.addJugador(usuario);
	}
	
	@RequestMapping("/jugadores/search/{name}")
	public List<Usuario> searchJugador(@PathVariable String name) {
		gameService.validateJugador(name);
		return gameService.searchJugador(name);
	}
	@RequestMapping("/jugadores/compare/{id_1}/{id_2}")
	public List<Usuario> compareJugadores(@PathVariable int id_1, @PathVariable int id_2){
		gameService.validateJugador(id_1);
		gameService.validateJugador(id_2);
		return gameService.compareJugadores(id_1,id_2);
	}
}
