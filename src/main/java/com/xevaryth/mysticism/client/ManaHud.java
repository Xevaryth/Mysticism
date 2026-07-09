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

public final class ManaHud {
    private ManaHud() {}

    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(Mysticism.MODID, "mana");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Mysticism.MODID, "textures/gui/mana_stars.png");

    private static final int ICON_SIZE = 9;
    private static final int SLOT_SPACING = 8;
    private static final int STARS = 10;
    private static final int MANA_PER_STAR = 4;
    private static final int MANA_PER_BAR = STARS * MANA_PER_STAR;

    private static final int BLUE_BASE_U = 0;
    private static final int PURPLE_BASE_U = 45;
    private static final int EMPTY_U = 90;
    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 32;

    public static void register(IEventBus modBus) {
        modBus.addListener(ManaHud::registerLayer);
    }

    private static void registerLayer(RegisterGuiLayersEvent event) {
        LayeredDraw.Layer layer = ManaHud::render;
        event.registerBelow(VanillaGuiLayers.AIR_LEVEL, LAYER, layer);
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null) return;

        GameType mode = mc.gameMode.getPlayerMode();
        if (mode == GameType.CREATIVE || mode == GameType.SPECTATOR) return;

        ManaData data = mc.player.getData(ManaAttachments.MANA.get());
        int current = Math.max(0, Math.min(data.currentMana(), data.maxMana()));
        int max = Math.max(0, data.maxMana());
        if (max <= 0) return;

        boolean airVisible = mc.player.getAirSupply() < mc.player.getMaxAirSupply();
        int right = graphics.guiWidth() / 2 + 91 - 4;
        int y = graphics.guiHeight() - (airVisible ? 49 : 39);

        int visibleLayer = current <= MANA_PER_BAR ? 0 : Math.max(1, (current - 1) / MANA_PER_BAR);
        int layerMana = current - visibleLayer * MANA_PER_BAR;
        if (visibleLayer > 0 && layerMana == 0) layerMana = MANA_PER_BAR;

        int baseU = visibleLayer > 0 ? PURPLE_BASE_U : BLUE_BASE_U;

        for (int slot = 0; slot < STARS; slot++) {
            int x = right - 8 - slot * SLOT_SPACING;
            blitRotated(graphics, x, y, EMPTY_U, 0);
        }

        for (int slot = 0; slot < STARS; slot++) {
            int fill = layerMana - slot * MANA_PER_STAR;
            if (fill <= 0) continue;
            fill = Math.min(MANA_PER_STAR, fill);
            int u = baseU + (MANA_PER_STAR - fill) * ICON_SIZE;
            int x = right - 8 - slot * SLOT_SPACING;
            blitRotated(graphics, x, y, u, 0);
        }
    }

    private static void blitRotated(GuiGraphics graphics, int x, int y, int u, int v) {
        graphics.pose().pushPose();
        graphics.pose().translate(x + ICON_SIZE, y + ICON_SIZE, 0);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(180.0F));
        graphics.blit(TEXTURE, 0, 0, u, v, ICON_SIZE, ICON_SIZE, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        graphics.pose().popPose();
    }
}
