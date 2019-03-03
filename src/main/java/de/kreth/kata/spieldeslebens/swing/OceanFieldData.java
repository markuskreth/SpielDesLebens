package de.kreth.kata.spieldeslebens.swing;

import de.kreth.kata.spieldeslebens.items.AbstractLebewesen;
import de.kreth.kata.spieldeslebens.ozean.Point;

public class OceanFieldData {

	private final Point point;

	private final OceanField.Icon icon;

	private final AbstractLebewesen<?> lebewesen;

	public OceanFieldData(Builder builder) {
		this.point = builder.point;
		this.icon = builder.icon;
		this.lebewesen = builder.lebewesen;
	}

	public Point getPoint() {
		return point;
	}

	public OceanField.Icon getIcon() {
		return icon;
	}

	public AbstractLebewesen<?> getLebewesen() {
		return lebewesen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((lebewesen == null) ? 0 : lebewesen.hashCode());
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OceanFieldData other = (OceanFieldData) obj;
		if (icon != other.icon) {
			return false;
		}
		if (lebewesen == null) {
			if (other.lebewesen != null) {
				return false;
			}
		}
		else if (!lebewesen.equals(other.lebewesen)) {
			return false;
		}
		if (point == null) {
			if (other.point != null) {
				return false;
			}
		}
		else if (!point.equals(other.point)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OceanFieldData [point=" + point + ", icon=" + icon + ", lebewesen=" + lebewesen + "]";
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Point point;

		private OceanField.Icon icon;

		private AbstractLebewesen<?> lebewesen;

		private Builder() {
		}

		public Builder setPoint(Point point) {
			this.point = point;
			return this;
		}

		public Builder setIcon(OceanField.Icon icon) {
			this.icon = icon;
			return this;
		}

		public Builder setLebewesen(AbstractLebewesen<?> lebewesen) {
			this.lebewesen = lebewesen;
			return this;
		}

		public OceanFieldData build() {
			return new OceanFieldData(this);
		}
	}
}
