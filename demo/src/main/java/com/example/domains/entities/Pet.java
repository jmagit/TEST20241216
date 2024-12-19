package com.example.domains.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.domains.core.entities.AbstractEntity;


/**
 * The persistent class for the pets database table.
 * 
 */
@Entity
@Table(name="pets")
@NamedQuery(name="Pet.findAll", query="SELECT p FROM Pet p")
@Builder @AllArgsConstructor
public class Pet extends AbstractEntity<Pet> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="birth_date")
	@PastOrPresent
	private Date birthDate;

	@Column(length=30)
	@NotBlank
	@Size(max = 30)
	private String name;

	//bi-directional many-to-one association to Owner
	@ManyToOne
	@JoinColumn(name="owner_id")
	private Owner owner;

	//bi-directional many-to-one association to Type
	@ManyToOne
	@JoinColumn(name="type_id", nullable=false)
	private Type type;

	//bi-directional many-to-one association to Visit
	@OneToMany(mappedBy="pet")
	private List<@Valid Visit> visits;

	public Pet() {
	}

	public Pet(int id, String name, Date birthDate) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
	}

	public Pet(int id, String name, String birthDate) {
		this.id = id;
		this.name = name;
		try {
			this.birthDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(birthDate);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	public Pet(int id, String name, String birthDate, int ownerId, int typeId) {
		this.id = id;
		this.name = name;
		try {
			this.birthDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(birthDate);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		this.owner = new Owner(ownerId);
		this.type = new Type(typeId);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Optional<Date> getBirthDate() {
		return Optional.ofNullable(this.birthDate);
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Owner getOwner() {
		return this.owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Visit> getVisits() {
		return this.visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	public Visit addVisit(Visit visit) {
		getVisits().add(visit);
		visit.setPet(this);

		return visit;
	}

	public Visit removeVisit(Visit visit) {
		getVisits().remove(visit);
		visit.setPet(null);

		return visit;
	}

	@Override
	public String toString() {
		return "Pet [id=" + id + ", birthDate=" + birthDate + ", name=" + name + ", owner=" + owner + ", type=" + type
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Pet)) {
			return false;
		}
		Pet other = (Pet) obj;
		return id == other.id;
	}


}