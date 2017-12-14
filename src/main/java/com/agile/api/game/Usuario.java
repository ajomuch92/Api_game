package com.agile.api.game;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario {
	@Id
	@GeneratedValue
	private int id;
	private String nombre;
	private int nivel;
	private int xp;
	@ManyToOne
	@JsonIgnore
	private Categoria categoria;
	
	public Usuario() {}

	public Usuario(String nombre, int nivel, int xp) {
		super();
		this.nombre = nombre;
		this.nivel = nivel;
		this.xp = xp;
	}

	public Usuario(String nombre, int nivel, int xp, Categoria categoria) {
		super();
		this.nombre = nombre;
		this.nivel = nivel;
		this.xp = xp;
		this.categoria = categoria;
	}
	
	public Usuario(int id, String nombre, int nivel, int xp, Categoria categoria) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.nivel = nivel;
		this.xp = xp;
		this.categoria = categoria;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", nivel=" + nivel + ", xp=" + xp + "]";
	}
	
	
}
