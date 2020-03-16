package br.com.laersondev.goldenraspberryawardsapi.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "RangeProducerAwards" )
public class RangeProducerAwards implements Serializable {
	private static final long serialVersionUID = 1L;

	private Set<ProducerAwardDetail> min = new HashSet<>();
	private Set<ProducerAwardDetail> max = new HashSet<>();

	public RangeProducerAwards() {
		super();
	}

	public Set<ProducerAwardDetail> getMin() {
		return this.min;
	}

	public Set<ProducerAwardDetail> getMax() {
		return this.max;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "RangeProducerAwards [min: " + (this.min != null ? toString(this.min, maxLen) : null) + ", max: "
		+ (this.max != null ? toString(this.max, maxLen) : null) + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		final StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}
