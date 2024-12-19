package com.example.domains.core.contracts.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<E, K> {
	List<E> findAll();
	
	Optional<E> findById(K id);

	boolean existsById(K id);

	E save(E item);
	
	void delete(E item);
	
	void deleteById(K id);
}
