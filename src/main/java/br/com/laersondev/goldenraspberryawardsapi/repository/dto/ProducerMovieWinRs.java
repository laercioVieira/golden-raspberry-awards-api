package br.com.laersondev.goldenraspberryawardsapi.repository.dto;

import java.io.Serializable;

public class ProducerMovieWinRs implements Serializable {
	private static final long serialVersionUID = 1L;
	private String producerName;
	private String movieTitle;
	private Integer movieYear;

	public ProducerMovieWinRs() {
		super();
	}

	public ProducerMovieWinRs(String producerName, String movieTitle, Integer movieYear) {
		super();
		this.producerName = producerName;
		this.movieTitle = movieTitle;
		this.movieYear = movieYear;
	}

	public String getProducerName() {
		return this.producerName;
	}

	public String getMovieTitle() {
		return this.movieTitle;
	}

	public Integer getMovieYear() {
		return this.movieYear;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public void setMovieYear(Integer movieYear) {
		this.movieYear = movieYear;
	}

	@Override
	public String toString() {
		return "ProducerMovieWinRs [producerName=" + this.producerName + ", movieTitle=" + this.movieTitle + ", movieYear="
				+ this.movieYear + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.movieTitle == null) ? 0 : this.movieTitle.hashCode());
		result = prime * result + ((this.movieYear == null) ? 0 : this.movieYear.hashCode());
		result = prime * result + ((this.producerName == null) ? 0 : this.producerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ProducerMovieWinRs ) {
			final ProducerMovieWinRs other = (ProducerMovieWinRs) obj;
			return this.movieTitle != null && other.getMovieTitle() != null
					&& this.movieTitle.equals(other.getMovieTitle()) && this.movieYear != null
					&& other.getMovieYear() != null && this.movieYear.equals(other.getMovieYear())
					&& this.producerName != null && other.getProducerName() != null
					&& this.producerName.equals(other.getProducerName());
		}

		return false;
	}


}
