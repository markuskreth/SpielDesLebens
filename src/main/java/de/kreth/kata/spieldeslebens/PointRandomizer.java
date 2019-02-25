package de.kreth.kata.spieldeslebens;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import de.kreth.kata.spieldeslebens.ozean.Point;

public class PointRandomizer {

  private final Random random = new Random();

  private final Set<Point> occupied = new HashSet<>();

  private final int maxX;

  private final int maxY;

  public PointRandomizer(int maxX, int maxY) {
    this.maxX = maxX;
    this.maxY = maxY;
  }

  public Point next() {
    Point point = create();
    while (occupied.contains(point)) {
      point = create();
    }
    occupied.add(point);
    return point;

  }

  public Point create() {
    int x = random.nextInt(maxX);
    int y = random.nextInt(maxY);
    Point point = new Point(x, y);
    return point;
  }
}
