package de.kreth.kata.spieldeslebens.events;

import de.kreth.kata.spieldeslebens.items.WithPosition;

public class ItemEvent<T extends WithPosition> {
  private final T item;

  public ItemEvent(T item) {
    super();
    this.item = item;
  }

  public T getItem() {
    return item;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((item == null) ? 0 : item.hashCode());
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
    ItemEvent<?> other = (ItemEvent<?>) obj;
    if (item == null) {
      if (other.item != null) {
        return false;
      }
    } else if (!item.equals(other.item)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return getClass().getSimpleName() + " [item=" + item + "]";
  }


}
