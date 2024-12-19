package com.example.domains.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the owners database table.
 * 
 */
@Entity
@Table(name="owners")
@NamedQuery(name="Owner.findAll", query="SELECT o FROM Owner o")
public class Owner implements Serializable {
	@Override
	public String toString() {
		return "Owner [id=" + id + ", address=" + address + ", city=" + city + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", telephone=" + telephone + "]";
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=255)
	private String address;

	@Column(length=80)
	private String city;

	@Column(name="first_name", length=30)
	private String firstName;

	@Column(name="last_name", length=30)
	private String lastName;

	@Column(length=20)
	private String telephone;

	//bi-directional many-to-one association to Pet
	@OneToMany(mappedBy="owner")
	private List<Pet> pets;

	public Owner() {
	}

	public Owner(int id) {
		super();
		this.id = id;
	}

	public Owner(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<Pet> getPets() {
		return this.pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public Pet addPet(Pet pet) {
		getPets().add(pet);
		pet.setOwner(this);

		return pet;
	}

	public Pet removePet(Pet pet) {
		getPets().remove(pet);
		pet.setOwner(null);

		return pet;
	}

}