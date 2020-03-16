package br.com.laersondev.goldenraspberryawardsapi.model;

import java.io.Serializable;

public interface Entity<I extends Serializable> {

	I getId();
}
