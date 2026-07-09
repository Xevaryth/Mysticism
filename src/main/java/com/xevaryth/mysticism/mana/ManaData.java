package com.xevaryth.mysticism.mana;

public final class ManaData {
    public static final int DEFAULT_MAX_MANA = 40;
    public static final int HARD_CAP = 9999;

    private int currentMana = DEFAULT_MAX_MANA;
    private int maxMana = DEFAULT_MAX_MANA;
    private double regenAccumulator = 0.0D;

    public int currentMana() { return currentMana; }
    public int maxMana() { return maxMana; }

    public void setCurrentMana(int value) {
        currentMana = clamp(value, 0, maxMana);
    }

    public void setMaxMana(int value) {
        maxMana = clamp(value, 0, HARD_CAP);
        currentMana = clamp(currentMana, 0, maxMana);
    }

    public void addMaxMana(int amount, boolean fillGainedMana) {
        int oldMax = maxMana;
        setMaxMana(maxMana + amount);
        if (fillGainedMana && maxMana > oldMax) {
            setCurrentMana(currentMana + (maxMana - oldMax));
        }
    }

    public void fill() { currentMana = maxMana; }
    public void empty() { currentMana = 0; }

    public boolean tickRegen(double manaPerSecond) {
        if (manaPerSecond <= 0.0D || currentMana >= maxMana) {
            regenAccumulator = 0.0D;
            return false;
        }
        regenAccumulator += manaPerSecond / 20.0D;
        int wholeMana = (int) Math.floor(regenAccumulator);
        if (wholeMana <= 0) return false;
        regenAccumulator -= wholeMana;
        int before = currentMana;
        setCurrentMana(currentMana + wholeMana);
        return currentMana != before;
    }

    public ManaData copy() {
        ManaData copy = new ManaData();
        copy.currentMana = currentMana;
        copy.maxMana = maxMana;
        copy.regenAccumulator = regenAccumulator;
        return copy;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
