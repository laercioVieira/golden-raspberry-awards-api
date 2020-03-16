package br.com.laersondev.goldenraspberryawardsapi.service;

import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotNull;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.laersondev.goldenraspberryawardsapi.model.Studio;
import br.com.laersondev.goldenraspberryawardsapi.repository.StudioRepository;

@Named
public class StudioService {

	@Inject
	private StudioRepository studioRepository;

	protected StudioService() {
		super();
	}

	public Set<Studio> findOrCreate(Set<String> studios) {
		return checkIfNotNull(studios, "studios").stream()//
				.map(studioName -> this.studioRepository
						.findByName(studioName)//
						.orElseGet(() -> this.studioRepository.save(new Studio(studioName))))
				.collect(toSet());
	}
}
