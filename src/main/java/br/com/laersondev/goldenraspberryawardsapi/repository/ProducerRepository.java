package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.laersondev.goldenraspberryawardsapi.model.Producer;
import br.com.laersondev.goldenraspberryawardsapi.repository.dto.ProducerMovieWinRs;

public interface ProducerRepository extends JpaRepository<Producer, Integer> {

	@Query(nativeQuery = true)
	List<ProducerMovieWinRs> findProducersWithWinMoviesAtLeastTwice();

}
