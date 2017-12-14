package com.agile.api.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
	
	@Autowired
	CategoriaRepository categoriaRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	
	private static final Logger logger=Logger.getLogger(GameService.class);
	
	public List<Categoria> getAllCategorias() {
		List<Categoria> lista=new ArrayList<Categoria>();
		categoriaRepository.findAll().forEach(lista::add);
		logger.info("Obteniendo lista de categorías");
		return lista;
	}
	public void addCategory(Categoria categoria) {
		logger.info("Insertando categoria: "+ categoria.getNombre());
		Categoria c=categoriaRepository.save(categoria);
		c.getUsuarios().forEach(u->usuarioRepository.save(new Usuario(u.getNombre(),u.getNivel(),u.getXp(),c)));
	}
	public List<Usuario> getAllUsuarios() {
		List<Usuario> lista=new ArrayList<Usuario>();
		usuarioRepository.findAll().forEach(lista::add);
		logger.info("Obteniendo lista de jugadores");
		return lista;
	}
	public Categoria getOneCategorias(int id) {
		logger.info("Obteniendo categoria con id "+id);
		return categoriaRepository.findOne(id);
	}
	
	public void validateCategoria(int id) {
		categoriaRepository.findById(id).orElseThrow(()->new CategoriaNotFoundException(id));
	}
	public void updateCategory(int id,Categoria categoria) {
		logger.info("Actualizando categoria: "+categoria.getNombre());
		categoria.setId(id);
		categoriaRepository.save(categoria);
		if(!categoria.getUsuarios().isEmpty())
			categoria.getUsuarios().forEach(u->usuarioRepository.save(new Usuario(u.getId(),u.getNombre(),u.getNivel(),u.getXp(),categoria)));		
	}
	public void deleteCategory(int id) {
		logger.warn("Borrando categoria con id "+ id);
		if(categoriaRepository.findOne(id).getUsuarios().size()>0)
			logger.warn("La categoría con id "+ id+" tenía jugadores añadidos, se borrarán en cascada");
		categoriaRepository.delete(id);
	}
	public List<Usuario> getCategoriasPlayers(int id) {
		logger.info("Obteniendo jugadores de la categoría con id "+ id);
		Categoria c=categoriaRepository.findOne(id);
		List<Usuario> lista=new ArrayList<Usuario>();
		c.getUsuarios().forEach(lista::add);
		return lista;
	}
	public List<Usuario> getTopPlayers(int id) {
		logger.info("Obteniendo mejores jugadores de la categoría con id: "+id);
		Categoria c=categoriaRepository.findOne(id);
		List<Usuario> lista=new ArrayList<Usuario>();
		c.getUsuarios().stream()
				.sorted((u1,u2)->u2.getXp()-u1.getXp())
				.limit(10)
				.forEach(lista::add);
		return lista;
	}
	
	public List<Usuario> getAllTopPlayers() {
		logger.info( "Obteniendo todos los jugadores ordenados");
		List<Usuario> lista=new ArrayList<Usuario>();
		usuarioRepository.findAll().forEach(lista::add);
		return lista.stream()
				.sorted((u1,u2)->u2.getXp()-u1.getXp())
				.limit(10)
				.collect(Collectors.toList());
	}
	public void validateJugador(int id) {
		usuarioRepository.findById(id).orElseThrow(()->new JugadorNotFoundException(id));
	}
	public Usuario getOneJugador(int id) {
		logger.info( "Obteniendo jugador con id "+id);
		return usuarioRepository.findOne(id);
	}
	public void deleteJugador(int id) {
		logger.warn("Borrando jugador con id "+id);
		usuarioRepository.delete(id);		
	}
	public void updateJugador(int id,Usuario usuario) {
		logger.info("Actualizando jugador con id "+id);
		usuario.setId(id);
		if(usuario.getCategoria()==null) {
			usuario.setCategoria(usuarioRepository.findOne(id).getCategoria());
		}
		usuarioRepository.save(usuario);
	}
	
	public void addJugador(Usuario usuario) {
		logger.info("Insertando jugador "+usuario.getNombre());
		if(usuario.getCategoria()!=null) {
			usuario.setCategoria(categoriaRepository.findByNombre(usuario.getCategoria().getNombre()).get());
		}else
			logger.warn("El jugador con nombre "+usuario.getNombre()+" no tiene una categoría asignada");
		usuarioRepository.save(usuario);
	}
	public List<Usuario> searchJugador(String nombre) {
		logger.info("Buscando jugador con nombre "+nombre);
		return usuarioRepository.findByNombre(nombre);
	}
	public void validateJugador(String name) {
		if(usuarioRepository.findByNombre(name).isEmpty()) {
			throw new JugadorNotFoundException(name);
		}
	}
	public List<Usuario> compareJugadores(int id_1, int id_2) {
		logger.info("Comparando jugadores con ids={"+id_1+","+id_2+"}");
		List<Usuario> lista=new ArrayList<Usuario>();
		lista.add(getOneJugador(id_1));
		lista.add(getOneJugador(id_2));
		return lista.stream().sorted((j1,j2)->j2.getXp()-j1.getXp()).collect(Collectors.toList());
	}
	
}
