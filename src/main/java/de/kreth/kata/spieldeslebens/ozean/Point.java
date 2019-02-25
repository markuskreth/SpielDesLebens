package de.kreth.kata.spieldeslebens.ozean;

public class Point implements Comparable<Point> {

  private final int x;

  private final int y;

  /**
   * Default Koordinate 0,0
   */
  public Point() {
    this(0, 0);
  }

  public Point(final int x, final int y) {
    super();
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Point other = (Point) obj;
    if (x != other.x) {
      return false;
    }
    if (y != other.y) {
      return false;
    }
    return true;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  public Point move(final Himmelsrichtung richtung) {
    int x = this.x;
    int y = this.y;
    switch (richtung) {
      case NORDEN:
        y--;
        break;
      case NORD_OST:
        y--;
        x++;
        break;
      case NORD_WEST:
        y--;
        x--;
        break;
      case OSTEN:
        x++;
        break;
      case SUEDEN:
        y++;
        break;
      case SUED_OST:
        y++;
        x++;
        break;
      case SUED_WEST:
        y++;
        x--;
        break;
      case WESTEN:
        x--;
        break;
      default:
        break;

    }
    return new Point(x, y);
  }

  public Himmelsrichtung to(Point target) {

    if (this.equals(target)) {
      throw new IllegalArgumentException(target + " must not equal this");
    }

    int x = this.x - target.x;
    int y = this.y - target.y;

    if (x == 0) {
      if (y > 0) {
        return Himmelsrichtung.NORDEN;
      } else {
        return Himmelsrichtung.SUEDEN;
      }
    } else if (x < 0) {
      if (y == 0) {
        return Himmelsrichtung.OSTEN;
      } else if (y > 0) {
        return Himmelsrichtung.NORD_OST;
      } else if (y < 0) {
        return Himmelsrichtung.SUED_OST;
      }
    } else if (x > 0) {
      if (y == 0) {
        return Himmelsrichtung.WESTEN;
      } else if (y > 0) {
        return Himmelsrichtung.NORD_WEST;
      } else if (y < 0) {
        return Himmelsrichtung.SUED_WEST;
      }
    }
    throw new IllegalStateException("unable to determine Direction from " + this + " to " + target);
  }

  @Override
  public String toString() {
    return "Point [x=" + x + ", y=" + y + "]";
  }

  @Override
  public int compareTo(Point o) {
    if (o == null) {
      return -1;
    }
    if (Integer.compare(x, o.x) == 0) {
      return Integer.compare(y, o.y);
    } else {
      return Integer.compare(x, o.x);
    }
  }

}
