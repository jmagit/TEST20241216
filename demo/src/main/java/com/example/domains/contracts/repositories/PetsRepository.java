package com.example.domains.contracts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domains.entities.Pet;
import com.example.domains.core.contracts.repositories.Repository;

public interface PetsRepository extends JpaRepository<Pet, Integer> {

}
//public interface PetsRepository extends Repository<Pet, Integer> {
//
//}
