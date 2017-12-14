package com.agile.api.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGameApplication {
	
	private static final Logger logger = Logger.getLogger(ApiGameApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ApiGameApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
	return (evt) -> Arrays.asList("Overall, Attack, Defense, Magic, Cooking, Crafting".split(","))
	.forEach(a -> {
		logger.info("Insertando categoria inicial: "+a);
		Categoria k=new Categoria();
		k.setNombre(a.trim());
		if(!a.trim().equals("Overall"))
			k.setUsuarios(randomUsers());
		Categoria c=categoriaRepository.save(k);
		c.getUsuarios().forEach(u->usuarioRepository.save(new Usuario(u.getNombre(),u.getNivel(),u.getXp(),c)));
	});
	}
	
	public Set<Usuario> randomUsers(){
		Set<Usuario> usuarios=new HashSet<Usuario>();
		Random random = new Random(System.currentTimeMillis());
		int limite=random.nextInt(50);
		for(int i=0;i<limite;i++) {
			int xp=random.nextInt(1000000);
			usuarios.add(new Usuario(randomName().trim(),xp/100,xp));
		}
		return usuarios;
	}
	
	public String randomName() {
		String lista="Aarón, Marco, María, Flor, Carlos, Karla, Miguel, Miriam, Oscar, Fernando";
		int random = (int) (Math.random() * lista.split(",").length) ;
		return lista.split(",")[random]+" "+randomLetter();
	}
	
	public String randomLetter() {
		String letras="A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		int random = (int) (Math.random() * letras.split(",").length); 
		return letras.split(",")[random];
	}
}
