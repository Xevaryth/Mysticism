package com.xevaryth.simpermanalibrary.mana;

public final class ManaData {
    private int currentMana = 0;
    private double regenAccumulator = 0.0D;

    /*
     * Tracks whether this player has received the configured
     * starting attribute values.
     */
    private boolean attributesInitialized = false;

    public int currentMana() {
        return currentMana;
    }

    public boolean attributesInitialized() {
        return attributesInitialized;
    }

    public void markAttributesInitialized() {
        attributesInitialized = true;
    }

    public void setCurrentMana(
        int value,
        int maxMana
    ) {
        currentMana = clamp(
            value,
            0,
            Math.max(0, maxMana)
        );
    }

    public void addCurrentMana(
        int amount,
        int maxMana
    ) {
        setCurrentMana(
            currentMana + amount,
            maxMana
        );
    }

    public void fill(int maxMana) {
        currentMana = Math.max(0, maxMana);
    }

    public void empty() {
        currentMana = 0;
    }

    public boolean tickRegen(
        double manaPerSecond,
        int maxMana
    ) {
        int safeMaxMana = Math.max(0, maxMana);

        if (
            manaPerSecond <= 0.0D ||
                currentMana >= safeMaxMana
        ) {
            regenAccumulator = 0.0D;
            return false;
        }

        regenAccumulator += manaPerSecond / 20.0D;

        int wholeMana =
            (int) Math.floor(regenAccumulator);

        if (wholeMana <= 0) {
            return false;
        }

        regenAccumulator -= wholeMana;

        int before = currentMana;

        setCurrentMana(
            currentMana + wholeMana,
            safeMaxMana
        );

        return currentMana != before;
    }

    public ManaData copy() {
        ManaData copy = new ManaData();

        copy.currentMana = currentMana;
        copy.regenAccumulator = regenAccumulator;
        copy.attributesInitialized =
            attributesInitialized;

        return copy;
    }

    private static int clamp(
        int value,
        int minimum,
        int maximum
    ) {
        return Math.max(
            minimum,
            Math.min(maximum, value)
        );
    }
}
