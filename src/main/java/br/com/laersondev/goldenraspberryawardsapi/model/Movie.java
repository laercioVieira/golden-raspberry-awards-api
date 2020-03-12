package br.com.laersondev.goldenraspberryawardsapi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.laersondev.goldenraspberryawardsapi.dto.MovieDto;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "year")
	private int year;

	public Movie() {
		super();
	}

	public Movie(final int id, final String title, final int year) {
		this.id = id;
		this.title = title;
		this.year = year;
	}

	public static Movie newFrom(final MovieDto movieDto) {
		return new Movie(movieDto.getId(), movieDto.getTitle(), movieDto.getYear());
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	public void updateFrom(final MovieDto dto) {
		this.title = dto.getTitle();
		this.year = dto.getYear();
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(final int year) {
		this.year = year;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("Movie [id: %s, title: %s, year: %s]", this.id, this.title, this.year);
	}

}
