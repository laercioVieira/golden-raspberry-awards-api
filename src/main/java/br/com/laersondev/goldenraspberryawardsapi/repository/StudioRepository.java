package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.util.Optional;

import br.com.laersondev.goldenraspberryawardsapi.model.Studio;

public interface StudioRepository extends BaseCrudRepository<Studio, Integer>{

	Optional<Studio> findByName(final String name );
}
