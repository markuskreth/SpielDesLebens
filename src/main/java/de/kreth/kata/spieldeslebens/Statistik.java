package de.kreth.kata.spieldeslebens;

public class Statistik {

	private final int fishCount;

	private final int sharkCount;

	private Statistik(Builder builder) {
		this.fishCount = builder.fishCount;
		this.sharkCount = builder.sharkCount;
	}

	public int getFishCount() {
		return fishCount;
	}

	public int getSharkCount() {
		return sharkCount;
	}

	@Override
	public String toString() {
		return "Statistik [fishCount=" + fishCount + ", sharkCount=" + sharkCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fishCount;
		result = prime * result + sharkCount;
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
		Statistik other = (Statistik) obj;
		if (fishCount != other.fishCount) {
			return false;
		}
		if (sharkCount != other.sharkCount) {
			return false;
		}
		return true;
	}

	public static class Builder {

		private int fishCount;

		private int sharkCount;

		public Builder setFishCount(int fishCount) {
			this.fishCount = fishCount;
			return this;
		}

		public Builder setSharkCount(int sharkCount) {
			this.sharkCount = sharkCount;
			return this;
		}

		public Statistik build() {
			return new Statistik(this);
		}
	}
}
