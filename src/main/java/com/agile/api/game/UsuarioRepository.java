package com.agile.api.game;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario,Integer> {
	Optional<Usuario> findById(int id);
	List<Usuario> findByNombre(String nombre);
}
