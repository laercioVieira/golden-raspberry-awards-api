package br.com.laersondev.goldenraspberryawardsapi.dto;

import java.io.Serializable;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ProducerAwardDetail")
public class ProducerAwardDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	private String producer;
	private int interval = 0;
	private Integer previousWin;
	private Integer followingWin;

	protected ProducerAwardDetail() {
		super();
	}

	public ProducerAwardDetail(final String producer, final int interval, final Integer previousWin, final Integer followingWin) {
		super();
		this.producer = producer;
		this.interval = interval;
		this.previousWin = previousWin;
		this.followingWin = followingWin;
	}

	public String getProducer() {
		return this.producer;
	}

	public int getInterval() {
		return this.interval;
	}

	public Integer getPreviousWin() {
		return this.previousWin;
	}

	public Integer getFollowingWin() {
		return this.followingWin;
	}

	public void setProducer(final String producer) {
		this.producer = producer;
	}

	public void setInterval(final int interval) {
		this.interval = interval;
	}

	public void setPreviousWin(final Integer previousWin) {
		this.previousWin = previousWin;
	}

	public void setFollowingWin(final Integer followingWin) {
		this.followingWin = followingWin;
	}

	@Override
	public String toString() {
		return "ProducerAwardDetail [producer=" + this.producer + ", interval=" + this.interval + ", previousWin=" + this.previousWin
				+ ", followingWin=" + this.followingWin + "]";
	}

}
