package com.xevaryth.mysticism.client;

import com.mojang.math.Axis;
import com.xevaryth.mysticism.Mysticism;
import com.xevaryth.mysticism.mana.ManaAttachments;
import com.xevaryth.mysticism.mana.ManaData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import com.xevaryth.mysticism.registry.MysticismAttributes;

public final class ManaHud {
    private ManaHud() {}

    private static final ResourceLocation LAYER =
        ResourceLocation.fromNamespaceAndPath(
            Mysticism.MODID,
            "mana"
        );

    private static final ResourceLocation TEXTURE =
        ResourceLocation.fromNamespaceAndPath(
            Mysticism.MODID,
            "textures/gui/mana_stars.png"
        );

    private static final int ICON_SIZE = 9;
    private static final int SLOT_SPACING = 8;

    private static final int STARS = 10;
    private static final int MANA_PER_STAR = 4;
    private static final int MANA_PER_LAYER =
        STARS * MANA_PER_STAR;

    /*
     * Sprite sheet:
     *
     * Width: 36 pixels
     * Height: 45 pixels
     *
     * Columns:
     * 0 = full
     * 1 = 3/4
     * 2 = 1/2
     * 3 = 1/4
     *
     * Rows:
     * 0 = blue
     * 1 = gold
     * 2 = red
     * 3 = purple
     * 4 = empty
     */
    private static final int TEXTURE_WIDTH = 36;
    private static final int TEXTURE_HEIGHT = 45;

    private static final int BLUE_ROW = 0;
    private static final int GOLD_ROW = 1;
    private static final int RED_ROW = 2;
    private static final int PURPLE_ROW = 3;
    private static final int EMPTY_ROW = 4;

    private static final int[] COLOR_ROWS = {
        BLUE_ROW,
        GOLD_ROW,
        RED_ROW,
        PURPLE_ROW
    };

    public static void register(IEventBus modBus) {
        modBus.addListener(ManaHud::registerLayer);
    }

    private static void registerLayer(
        RegisterGuiLayersEvent event
    ) {
        LayeredDraw.Layer layer = ManaHud::render;

        event.registerBelow(
            VanillaGuiLayers.AIR_LEVEL,
            LAYER,
            layer
        );
    }

    private static void render(
        GuiGraphics graphics,
        DeltaTracker deltaTracker
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (
            minecraft.player == null ||
                minecraft.gameMode == null
        ) {
            return;
        }

        GameType gameType =
            minecraft.gameMode.getPlayerMode();

        if (
            gameType == GameType.CREATIVE ||
                gameType == GameType.SPECTATOR
        ) {
            return;
        }

        ManaData manaData = minecraft.player.getData(
            ManaAttachments.MANA.get()
        );

        int maxMana = Math.max(
            0,
            (int) Math.floor(
                minecraft.player.getAttributeValue(
                    MysticismAttributes.MAX_MANA.getDelegate()
                )
            )
        );

        int currentMana = Math.max(
            0,
            Math.min(
                manaData.currentMana(),
                maxMana
            )
        );

        int right = graphics.guiWidth() / 2 + 91;
        int y = graphics.guiHeight() - 50;

        /*
         * First pass:
         *
         * Always draw all ten empty stars.
         * These remain behind every filled mana layer.
         */
        for (int slot = 0; slot < STARS; slot++) {
            int x =
                right -
                    ICON_SIZE -
                    slot * SLOT_SPACING;

            drawStar(
                graphics,
                x,
                y,
                EMPTY_ROW,
                MANA_PER_STAR
            );
        }

        /*
         * No filled mana needs to be drawn at zero.
         * The empty bar remains visible.
         */
        if (currentMana <= 0) {
            return;
        }

        /*
         * Draw each current-mana layer from bottom to top.
         *
         * 1–40   = blue
         * 41–80  = gold over blue
         * 81–120 = red over gold
         * 121+   = purple
         */
        int highestCurrentLayer =
            (currentMana - 1) / MANA_PER_LAYER;

        for (
            int layer = 0;
            layer <= highestCurrentLayer;
            layer++
        ) {
            int colorRow = getColorRow(layer);

            for (int slot = 0; slot < STARS; slot++) {
                int fill = getSlotFill(
                    currentMana,
                    layer,
                    slot
                );

                if (fill <= 0) {
                    continue;
                }

                int x =
                    right -
                        ICON_SIZE -
                        slot * SLOT_SPACING;

                drawStar(
                    graphics,
                    x,
                    y,
                    colorRow,
                    fill
                );
            }
        }
    }

    private static int getSlotFill(
        int currentMana,
        int layer,
        int slot
    ) {
        int layerStart =
            layer * MANA_PER_LAYER;

        int slotStart =
            layerStart +
                slot * MANA_PER_STAR;

        int manaInSlot =
            currentMana - slotStart;

        return Math.max(
            0,
            Math.min(
                MANA_PER_STAR,
                manaInSlot
            )
        );
    }

    private static int getColorRow(int layer) {
        return COLOR_ROWS[
            Math.min(
                layer,
                COLOR_ROWS.length - 1
            )
            ];
    }

    private static void drawStar(
        GuiGraphics graphics,
        int x,
        int y,
        int row,
        int fill
    ) {
        int column = switch (fill) {
            case 4 -> 0;
            case 3 -> 1;
            case 2 -> 2;
            case 1 -> 3;
            default -> -1;
        };

        if (column < 0) {
            return;
        }

        int u = column * ICON_SIZE;
        int v = row * ICON_SIZE;

        blitRotated(
            graphics,
            x,
            y,
            u,
            v
        );
    }

    private static void blitRotated(
        GuiGraphics graphics,
        int x,
        int y,
        int u,
        int v
    ) {
        graphics.pose().pushPose();

        graphics.pose().translate(
            x + ICON_SIZE,
            y + ICON_SIZE,
            0
        );

        graphics.pose().mulPose(
            Axis.ZP.rotationDegrees(180.0F)
        );

        graphics.blit(
            TEXTURE,
            0,
            0,
            u,
            v,
            ICON_SIZE,
            ICON_SIZE,
            TEXTURE_WIDTH,
            TEXTURE_HEIGHT
        );

        graphics.pose().popPose();
    }
}
