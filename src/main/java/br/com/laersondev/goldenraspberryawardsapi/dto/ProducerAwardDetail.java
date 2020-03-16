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

	public ProducerAwardDetail(String producer, int interval, Integer previousWin, Integer followingWin) {
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

	@Override
	public String toString() {
		return "ProducerAwardDetail [producer=" + this.producer + ", interval=" + this.interval + ", previousWin=" + this.previousWin
				+ ", followingWin=" + this.followingWin + "]";
	}

}
