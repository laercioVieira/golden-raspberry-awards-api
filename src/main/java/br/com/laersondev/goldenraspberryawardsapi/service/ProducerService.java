package br.com.laersondev.goldenraspberryawardsapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.laersondev.goldenraspberryawardsapi.dto.ProducerWinInfo;
import br.com.laersondev.goldenraspberryawardsapi.dto.ProducerWinInterval;
import br.com.laersondev.goldenraspberryawardsapi.repository.ProducerRepository;
import br.com.laersondev.goldenraspberryawardsapi.repository.dto.ProducerMovieWinRs;
import br.com.laersondev.goldenraspberryawardsapi.service.exception.ServiceException;

/**
 * ProducerService
 */
@Named
@RequestScoped
public class ProducerService {

	@Inject
	private ProducerRepository repository;

	protected ProducerService() {
		super();
	}

	public ProducerWinInterval getProducerWinInterval() {

		final List<ProducerMovieWinRs> winMoviesAtLeastTwice = this.repository.findProducersWithWinMoviesAtLeastTwice();

		ProducerWinInfo producerWinInfo = null;
		ProducerWinInfo producerWinInfoAnterior = null;

		final List<ProducerWinInfo> producerWinn = new ArrayList<>();

		for (final ProducerMovieWinRs producerMovieWinRs : winMoviesAtLeastTwice) {
			producerWinInfo = new ProducerWinInfo(producerMovieWinRs.getProducerName(), 0, null, producerMovieWinRs.getMovieYear());
			if(producerWinInfoAnterior != null) {
				producerWinInfo = new ProducerWinInfo(producerMovieWinRs.getProducerName(), //
						(producerMovieWinRs.getMovieYear() - producerWinInfoAnterior.getFollowingWin()),//
						producerWinInfoAnterior.getFollowingWin(),//
						producerMovieWinRs.getMovieYear());
			}
			producerWinInfoAnterior = producerWinInfo;
			producerWinn.add(producerWinInfo);
		}

		//producerWinn.sort(Comparator.comparingInt(ProducerWinInfo::getInterval).reversed());

		//		producerWinn.stream()
		//		.sorted(Comparator.comparingInt(ProducerWinInfo::getInterval))
		//		filter(prodToFilt -> prodToFilt.get)
		//		.collect(
		//				Collectors.groupingBy(prod -> { return prod; }, new Supplier<Map>() {
		//
		//				},//
		//						Collectors.summarizingInt((ProducerWinInfo prodWin) -> {
		//							return prodWin.getInterval();
		//						}) //
		//						);

		return new ProducerWinInterval();
	}

	private <T, R> R tryDo(final Function<T, R> action, final T params) {
		try {
			return action.apply(params);
		} catch (final Exception e) {
			throw new ServiceException(e);
		}
	}

}