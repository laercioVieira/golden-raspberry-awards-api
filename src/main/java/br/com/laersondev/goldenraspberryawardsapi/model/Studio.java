package br.com.laersondev.goldenraspberryawardsapi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.laersondev.goldenraspberryawardsapi.util.Precondition;

@Entity
@Table(name = "studio", uniqueConstraints=@UniqueConstraint(columnNames = {"name"}))
public class Studio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	public Studio() {
		super();
	}

	public Studio(String name) {
		this(0, name);
	}

	public Studio(int id, String name) {
		super();
		this.setId(id);
		this.setName(name);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = Precondition.checkIfPositive(id, "id");
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = Precondition.checkIfNotBlank(name, "name");
	}

	@Override
	public String toString() {
		return String.format("Studio [id: %s, name: %s]", this.id, this.name);
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
		if (obj instanceof Studio) {
			return getId() == ((Studio) obj).getId();
		}

		return false;
	}

}
