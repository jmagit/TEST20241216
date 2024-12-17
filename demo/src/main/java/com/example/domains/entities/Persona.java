package com.example.domains.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import com.example.domains.core.entities.EntityValidatable;

public class Persona implements EntityValidatable {
	private int id;
	private String nombre;
	private String apellidos;
	private LocalDate nacimiento;
	
	private Persona() { }
	
	public Persona(int id, String nombre) {
		this.id = id;
		setNombre(nombre);
	}
	
	public Persona(int id, String nombre, String apellidos) {
		this(id, nombre);
		setApellidos(apellidos);
	}
	
	public Persona(int id, String nombre, LocalDate nacimiento) {
		this(id, nombre);
		setNacimiento(nacimiento);
	}
	
	public Persona(int id, String nombre, String apellidos, LocalDate nacimiento) {
		this(id, nombre, apellidos);
		setNacimiento(nacimiento);
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
		if(nombre == null)
			throw new IllegalArgumentException("El nombre no puede ser nulo.");
		this.nombre = nombre;
	}

	public Optional<String> getApellidos() {
		return Optional.ofNullable(apellidos);
	}
	public void setApellidos(String apellidos) {
		if(apellidos == null)
			throw new IllegalArgumentException("Los apellidos no pueden ser nulo.");
		this.apellidos = apellidos;
	}
	public void clearApellidos() {
		this.apellidos = null;
	}
	
	public boolean hasNacimiento() {
		 return nacimiento != null;
	}
	public LocalDate getNacimiento() {
		 if(!hasEdad())
			 throw new NoSuchElementException("La fecha de nacimiento no pueden ser nula.");
		return nacimiento;
	}
	public void setNacimiento(LocalDate nacimiento) {
		if(nacimiento == null)
			throw new IllegalArgumentException("La fecha de nacimiento no pueden ser nula.");
		if(nacimiento.isAfter(LocalDate.now()))
			throw new IllegalArgumentException("La fecha de nacimiento no pueden ser futura.");
		this.nacimiento = nacimiento;
	}
	public void clearNacimiento() {
		this.nacimiento = null;
	}
	
	public boolean hasEdad() {
		 return hasNacimiento();
	}
	public byte getEdad() {
		 if(!hasEdad())
			 throw new NoSuchElementException("La fecha de nacimiento no pueden ser nula.");
		 return (byte) ChronoUnit.YEARS.between(nacimiento, LocalDate.now());		
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", nacimiento=" + nacimiento
				+ "]";
	}

	public static Persona creaPersona(String nombre) {
//		return null;
		var p = new Persona();
		p.setNombre(nombre);
		return p;
	}

	public static Persona creaPersona(String nombre, String apellidos) {
//		return null;
		return new Persona(0, nombre, apellidos);
	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(id);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj instanceof Persona p) {
//			return id == p.id;
//		} else {
//			return false;
//		}
//	}

	@Override
	public String getErrorsMessage() {
		var msg = new StringBuilder();
		if(nombre.isBlank())
			msg.append(" nombre: no puede estar en blanco.");
		if(apellidos != null && apellidos.isBlank())
			msg.append(" apellidos: no puede estar en blanco.");
		return msg.isEmpty() ? "" : "ERRORES:" + msg.toString();
	}
}
