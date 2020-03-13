package br.com.laersondev.goldenraspberryawardsapi.model;

import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotBlank;
import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotNull;
import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfPositive;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "year")
	private int year;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_studio", joinColumns = { @JoinColumn(name = "studios_id") }, inverseJoinColumns = {
			@JoinColumn(name = "movie_id") })
	private Set<Studio> studios;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_producer", joinColumns = { @JoinColumn(name = "producer_id") }, inverseJoinColumns = {
			@JoinColumn(name = "movie_id") })
	private Set<Producer> producers;

	@Column(name = "winner")
	private boolean winner;

	public Movie() {
		super();
	}

	public Movie(int id, String title, int year,
			Set<String> studios,
			Set<String> producers, boolean winner) {
		super();
		this.setId(id);
		this.setTitle(title);
		this.setYear(year);
		this.setStudios(checkIfNotNull(studios, "studio").stream()
				.map(Studio::new).collect(Collectors.toSet()));
		this.setProducers(checkIfNotNull(producers, "producers").stream()
				.map(Producer::new).collect(Collectors.toSet()));
		this.setWinner(winner);
	}

	public static Movie newFrom(final MovieDto movieDto) {
		return new Movie(0, movieDto.getTitle(), movieDto.getYear(), movieDto.getStudios(), movieDto.getProducers(),
				movieDto.getWinner());
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	public void updateFrom(final MovieDto dto) {
		checkIfNotNull(dto, "movieDto");
		this.setTitle(dto.getTitle());
		this.setYear(dto.getYear());

		getStudios().removeIf(mystd -> !dto.getStudios().contains(mystd.getName()));
		final Set<String> studioNames = this.getStudioNames();
		dto.getStudios().forEach(studio -> {
			if(!studioNames.contains(studio)) {
				getStudios().add(new Studio(studio));
			}
		});

		getProducers().removeIf(myProd -> !dto.getProducers().contains(myProd.getName()));
		final Set<String> producerNames = this.getProducerNames();
		dto.getProducers().forEach(producer -> {
			if(!producerNames.contains(producer)) {
				getProducers().add(new Producer(producer));
			}
		});

		this.setWinner(dto.getWinner());
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = checkIfNotBlank(title, "title");
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(final int year) {
		this.year = checkIfPositive(year, "year");
	}

	public void setId(final int id) {
		this.id = checkIfPositive(id, "id");
	}

	public Set<Studio> getStudios() {
		return this.studios;
	}

	public Set<String> getStudioNames() {
		return this.getStudios().stream().map(Studio::getName).collect(Collectors.toSet());
	}

	public void setStudios(Set<Studio> studios) {
		this.studios = checkIfNotNull(studios, "studios");
	}

	public Set<Producer> getProducers() {
		return this.producers;
	}

	public Set<String> getProducerNames() {
		return this.getProducers().stream().map(Producer::getName).collect(Collectors.toSet());
	}

	public void setProducers(Set<Producer> producers) {
		this.producers = checkIfNotNull(producers, "producers");
	}

	public boolean getWinner() {
		return this.winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Movie) {
			return getId() == ((Movie) obj).getId();
		}

		return false;
	}
}
