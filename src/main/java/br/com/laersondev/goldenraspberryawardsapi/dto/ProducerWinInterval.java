package br.com.laersondev.goldenraspberryawardsapi.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProducerWinInterval implements Serializable {
	private static final long serialVersionUID = 1L;

	private Set<ProducerWinInfo> min = new HashSet<>();
	private Set<ProducerWinInfo> max = new HashSet<>();

	public ProducerWinInterval() {
		super();
	}

	public Set<ProducerWinInfo> getMin() {
		return this.min;
	}

	public Set<ProducerWinInfo> getMax() {
		return this.max;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "WinProducersInterval [min: " + (this.min != null ? toString(this.min, maxLen) : null) + ", max: "
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
