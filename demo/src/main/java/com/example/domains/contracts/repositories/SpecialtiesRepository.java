package com.example.domains.contracts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domains.entities.Specialty;

public interface SpecialtiesRepository extends JpaRepository<Specialty, Integer> {

}
