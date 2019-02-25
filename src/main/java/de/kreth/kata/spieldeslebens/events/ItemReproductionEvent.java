package de.kreth.kata.spieldeslebens.events;

import java.util.List;

import de.kreth.kata.spieldeslebens.items.AbstractFisch;

public class ItemReproductionEvent<T extends AbstractFisch<?, ?>> extends ItemEvent<T> {

	private List<T> children;

	public ItemReproductionEvent(T item, List<T> children) {
		super(item);
		this.children = children;
	}

	public List<T> getChildren() {
		return children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ItemReproductionEvent<?> other = (ItemReproductionEvent<?>) obj;
		if (children == null) {
			if (other.children != null) {
				return false;
			}
		}
		else if (!children.equals(other.children)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ItemReproductionEvent [Died=" + super.toString() + ", children=" + children + "]";
	}

}
