package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.io.Serializable;
import java.util.Optional;

import br.com.laersondev.goldenraspberryawardsapi.model.Entity;

public interface BaseCrudRepository<E extends Entity<I>, I extends Serializable> {

	void deleteById(I id);

	void delete(E entity);

	Optional<E> findById(I id);

	E save(E entity);

}