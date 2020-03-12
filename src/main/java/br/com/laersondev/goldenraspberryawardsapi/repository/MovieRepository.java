package br.com.laersondev.goldenraspberryawardsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.laersondev.goldenraspberryawardsapi.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
