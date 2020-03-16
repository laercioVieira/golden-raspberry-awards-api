package br.com.laersondev.goldenraspberryawardsapi.service;

import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotNull;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.laersondev.goldenraspberryawardsapi.dto.ProducerAwardDetail;
import br.com.laersondev.goldenraspberryawardsapi.dto.RangeProducerAwards;
import br.com.laersondev.goldenraspberryawardsapi.model.Producer;
import br.com.laersondev.goldenraspberryawardsapi.repository.ProducerRepository;
import br.com.laersondev.goldenraspberryawardsapi.repository.dto.ProducerMovieWinRs;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.ServiceException;

/**
 * ProducerService
 */
@Named
public class ProducerService {

	@Inject
	private ProducerRepository repository;

	protected ProducerService() {
		super();
	}

	public Set<Producer> findOrCreate(Set<String> producers) {
		return checkIfNotNull(producers, "producers").stream()//
				.map(producer -> this.repository.findByName(producer)//
						.orElseGet(() -> this.repository.save(new Producer(producer))))
				.collect(toSet());
	}

	public RangeProducerAwards getRangeProducerAwards() {
		final List<ProducerMovieWinRs> winMoviesAtLeastTwice = this.repository.findProducersWithWinMoviesAtLeastTwice();
		final List<ProducerAwardDetail> producerAwardsDts = makeProducersAwardsDetailsWithInterval(winMoviesAtLeastTwice);

		final Comparator<ProducerAwardDetail> compareByInterval = Comparator.comparingInt(ProducerAwardDetail::getInterval);
		final Predicate<? super ProducerAwardDetail> intervalGreaterThanZero = prodToFilter -> prodToFilter.getInterval() > 0;
		final Optional<ProducerAwardDetail> min = producerAwardsDts.stream().filter(intervalGreaterThanZero).min(compareByInterval);
		final Optional<ProducerAwardDetail> max = producerAwardsDts.stream().filter(intervalGreaterThanZero).max(compareByInterval);

		final RangeProducerAwards rangeProducerAwards = new RangeProducerAwards();
		min.ifPresent(minProducer -> {//
			rangeProducerAwards.getMin().addAll(producerWihSameInterval(producerAwardsDts, minProducer).collect(toSet()));
		});
		max.ifPresent(maxProducer -> {//
			rangeProducerAwards.getMax().addAll(producerWihSameInterval(producerAwardsDts, maxProducer).collect(toSet()));
		});

		return rangeProducerAwards;
	}

	private List<ProducerAwardDetail> makeProducersAwardsDetailsWithInterval(
			final List<ProducerMovieWinRs> winMoviesAtLeastTwice) {
		ProducerAwardDetail producerAwardDetail = null;
		ProducerAwardDetail producerAwardDetailBefore = null;

		final List<ProducerAwardDetail> producerWinn = new ArrayList<>(0);
		for (final ProducerMovieWinRs producerMovieWinRs : winMoviesAtLeastTwice) {
			if (producerAwardDetailBefore != null) {
				producerAwardDetail = new ProducerAwardDetail(producerMovieWinRs.getProducerName(), //
						(producerMovieWinRs.getMovieYear() - producerAwardDetailBefore.getFollowingWin()), //
						producerAwardDetailBefore.getFollowingWin(), //
						producerMovieWinRs.getMovieYear());
			} else {
				producerAwardDetail = new ProducerAwardDetail(producerMovieWinRs.getProducerName(), 0, null,
						producerMovieWinRs.getMovieYear());
			}
			producerAwardDetailBefore = producerAwardDetail;
			producerWinn.add(producerAwardDetail);
		}
		return producerWinn;
	}

	private Stream<ProducerAwardDetail> producerWihSameInterval(final List<ProducerAwardDetail> producerWinn,
			ProducerAwardDetail otherProducer) {
		return producerWinn.stream().filter( prod -> prod.getInterval() == otherProducer.getInterval() );
	}

	private <T, R> R tryDo(final Function<T, R> action, final T params) {
		try {
			return action.apply(params);
		} catch (final Exception e) {
			throw new ServiceException(e);
		}
	}

}