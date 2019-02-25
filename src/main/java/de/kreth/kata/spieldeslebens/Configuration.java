package de.kreth.kata.spieldeslebens;

import java.util.ArrayList;
import java.util.List;
import de.kreth.kata.spieldeslebens.items.Felsen;
import de.kreth.kata.spieldeslebens.items.Fisch;
import de.kreth.kata.spieldeslebens.items.Hai;

public class Configuration {

  private int width;

  private int height;

  private long tickPeriod;

  private int planktonPerTick;

  private List<Felsen> rocks;

  private List<Hai> sharks;

  private List<Fisch> fishes;

  private int reproductionPercent;

  private Configuration(Builder builder) {
    this.width = builder.width;
    this.height = builder.height;
    this.tickPeriod = builder.tickPeriod;
    this.planktonPerTick = builder.planktonPerTick;
    this.reproductionPercent = builder.reproductionPercent;

    PointRandomizer randomizer = new PointRandomizer(width, height);
    createRocks(randomizer, builder.rockCount);
    createSharks(randomizer, builder.sharkCount);
    createFish(randomizer, builder.fishCount);
  }

  private void createFish(PointRandomizer randomizer, int fishCount) {
    fishes = new ArrayList<>();

    for (int i = 0; i < fishCount; i++) {
      fishes.add(new Fisch(randomizer.next()));
    }
  }

  private void createSharks(PointRandomizer randomizer, int sharkCount) {
    sharks = new ArrayList<>();
    for (int i = 0; i < sharkCount; i++) {
      sharks.add(new Hai(randomizer.next()));
    }
  }

  private void createRocks(PointRandomizer randomizer, int rockCount) {
    rocks = new ArrayList<>();
    for (int i = 0; i < rockCount; i++) {
      rocks.add(new Felsen(randomizer.next()));
    }
  }

  public List<Felsen> getRocks() {
    return rocks;
  }

  public List<Hai> getSharks() {
    return sharks;
  }

  public List<Fisch> getFishes() {
    return fishes;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public long getTickPeriod() {
    return tickPeriod;
  }

  public int getPlanktonPerTick() {
    return planktonPerTick;
  }

  public int getReproductionPercent() {
    return reproductionPercent;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private int width;

    private int height;

    private long tickPeriod;

    private int fishCount;

    private int sharkCount;

    private int rockCount;

    private int planktonPerTick;
    private int reproductionPercent;

    private Builder() {}

    public Builder setReproductionPercent(int reproductionPercent) {
      this.reproductionPercent = reproductionPercent;
      return this;
    }

    public Builder setWidth(int width) {
      this.width = width;
      return this;
    }

    public Builder setHeight(int height) {
      this.height = height;
      return this;
    }

    public Builder setTickPeriod(long tickPeriod) {
      this.tickPeriod = tickPeriod;
      return this;
    }

    public Builder setFishCount(int fishCount) {
      this.fishCount = fishCount;
      return this;
    }

    public Builder setSharkCount(int sharkCount) {
      this.sharkCount = sharkCount;
      return this;
    }

    public Builder setRockCount(int rockCount) {
      this.rockCount = rockCount;
      return this;
    }

    public Builder setPlanktonPerTick(int planktonPerTick) {
      this.planktonPerTick = planktonPerTick;
      return this;
    }

    public Configuration build() {
      return new Configuration(this);
    }
  }
}
