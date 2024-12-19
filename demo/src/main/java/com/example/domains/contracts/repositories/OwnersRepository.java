package com.example.domains.contracts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domains.entities.Owner;

public interface OwnersRepository extends JpaRepository<Owner, Integer> {

}
