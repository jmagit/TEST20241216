package com.example.domains.entities;

import java.io.Serializable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the vets database table.
 * 
 */
@Entity
@Table(name="vets")
@NamedQuery(name="Vet.findAll", query="SELECT v FROM Vet v")
public class Vet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="first_name", length=30)
	private String firstName;

	@Column(name="last_name", length=30)
	private String lastName;

	//bi-directional many-to-many association to Specialty
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name="vet_specialties"
			, joinColumns={
				@JoinColumn(name="vet_id", nullable=false)
				}
			, inverseJoinColumns={
				@JoinColumn(name="specialty_id", nullable=false)
				}
			)
	private List<Specialty> specialties = new ArrayList<>();

	public Vet() {
	}


	public Vet(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Specialty> getSpecialties() {
		return this.specialties;
	}

	public void setSpecialties(List<Specialty> specialties) {
		this.specialties = specialties;
	}


	@Override
	public String toString() {
		return "Vet [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}