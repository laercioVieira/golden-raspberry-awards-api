package br.com.laersondev.goldenraspberryawardsapi.model;

import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotBlank;
import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotNull;
import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfPositive;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.UniqueConstraint;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;

@Entity
@Table(name = "movie", uniqueConstraints = @UniqueConstraint(columnNames = { "title" }))
public class Movie implements Serializable, br.com.laersondev.goldenraspberryawardsapi.model.Entity<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "year")
	private int year;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "movie_studio", joinColumns = { @JoinColumn(name = "movie_id") }, inverseJoinColumns = {
			@JoinColumn(name = "studio_id") })
	private final Set<Studio> studios;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "movie_producer", joinColumns = { @JoinColumn(name = "movie_id") }, inverseJoinColumns = {
			@JoinColumn(name = "producer_id") })
	private final Set<Producer> producers;

	@Column(name = "winner")
	private boolean winner;

	public Movie() {
		super();
		this.studios = new HashSet<>();
		this.producers = new HashSet<>();
	}

	public Movie(final int id, final String title, final int year, final boolean winner) {
		super();
		this.setId(id);
		this.setTitle(title);
		this.setYear(year);
		this.setWinner(winner);
		this.studios = new HashSet<>();
		this.producers = new HashSet<>();
	}

	public static Movie newFrom(final MovieDto movieDto) {
		return new Movie(0, movieDto.getTitle(), movieDto.getYear(), movieDto.getWinner());
	}

	/**
	 * @return the id
	 */
	@Override
	public Integer getId() {
		return this.id;
	}

	public void updateFrom(final MovieDto dto) {
		checkIfNotNull(dto, "movieDto");
		this.setTitle(dto.getTitle());
		this.setYear(dto.getYear());

		this.getStudios().removeIf(mystd -> !dto.getStudios().contains(mystd.getName()));
		final Set<String> studioNames = this.getStudioNames();
		dto.getStudios().forEach(studio -> {
			if (!studioNames.contains(studio)) {
				this.getStudios().add(new Studio(studio));
			}
		});

		this.getProducers().removeIf(myProd -> !dto.getProducers().contains(myProd.getName()));
		final Set<String> producerNames = this.getProducerNames();
		dto.getProducers().forEach(producer -> {
			if (!producerNames.contains(producer)) {
				this.getProducers().add(new Producer(producer));
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

	public void addStudios(final Collection<Studio> studios) {
		this.studios.addAll(checkIfNotNull(studios, "studios"));
	}

	public Set<Producer> getProducers() {
		return this.producers;
	}

	public Set<String> getProducerNames() {
		return this.getProducers().stream().map(Producer::getName).collect(Collectors.toSet());
	}

	public void addProducers(final Collection<Producer> producers) {
		this.producers.addAll(checkIfNotNull(producers, "producers"));
	}

	public boolean getWinner() {
		return this.winner;
	}

	public void setWinner(final boolean winner) {
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
	public boolean equals(final Object obj) {
		if (obj instanceof Movie) {
			return this.getId() == ((Movie) obj).getId();
		}

		return false;
	}

	@Override
	public String toString() {
		final int maxLen = 5;
		return "Movie [id=" + this.id + ", title=" + this.title + ", year=" + this.year + ", studios="
		+ (this.studios != null ? this.toString(this.studios, maxLen) : null) + ", producers="
		+ (this.producers != null ? this.toString(this.producers, maxLen) : null) + ", winner=" + this.winner
		+ "]";
	}

	private String toString(final Collection<?> collection, final int maxLen) {
		final StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(iterator.next());
		}
		if (collection.size() > maxLen) {
			builder.append(" ...");
		}
		builder.append("]");
		return builder.toString();
	}
}
