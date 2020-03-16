package br.com.laersondev.goldenraspberryawardsapi.dto;

import java.io.Serializable;
import java.util.Set;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import br.com.laersondev.goldenraspberryawardsapi.model.Movie;

@Schema(name="Movie")
public class MovieDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private int year;
	private Set<String> studios;
	private Set<String> producers;
	private boolean winner;

	public MovieDto() {
		super();
	}

	public MovieDto(int id, String title, int year, Set<String> studios, Set<String> producers, boolean winner) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.studios = studios;
		this.producers = producers;
		this.winner = winner;
	}

	public int getYear() {
		return this.year;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Set<String> getStudios() {
		return this.studios;
	}

	public void setStudios(Set<String> studios) {
		this.studios = studios;
	}

	public Set<String> getProducers() {
		return this.producers;
	}

	public void setProducers(Set<String> producers) {
		this.producers = producers;
	}

	public boolean getWinner() {
		return this.winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	public static MovieDto newFrom(final Movie movie) {
		return new MovieDto(movie.getId(), movie.getTitle(), movie.getYear(),
				movie.getStudioNames(),
				movie.getProducerNames(),
				movie.getWinner());
	}

	@Override
	public String toString() {
		return String.format("MovieDto [id: %s, title: %s, year: %s, studios: %s, producers: %s, winner: %s]", this.id,
				this.title, this.year, this.studios, this.producers, this.winner);
	}

}
