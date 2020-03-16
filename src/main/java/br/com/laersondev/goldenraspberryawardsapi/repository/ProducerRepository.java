package br.com.laersondev.goldenraspberryawardsapi.repository;

import java.util.List;
import java.util.Optional;

//import org.springframework.data.jpa.repository.Query;

import br.com.laersondev.goldenraspberryawardsapi.model.Producer;
import br.com.laersondev.goldenraspberryawardsapi.repository.dto.ProducerMovieWinRs;

public interface ProducerRepository extends BaseCrudRepository<Producer, Integer> {

	//@Query(nativeQuery = true, name = "ProducerMovieWinRs.findProducersWithWinMoviesAtLeastTwice")
	List<ProducerMovieWinRs> findProducersWithWinMoviesAtLeastTwiceOrdened();

	Optional<Producer> findByName(final String name );

}
