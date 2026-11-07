package pulsefx;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;

public class FxScreen extends Screen {
    private int y;

    public FxScreen() { super(Text.literal("PulseFX")); }

    @Override
    protected void init() {
        int w = 200, x = width / 2 - w / 2;
        y = height / 2 - 90;
        toggle(x, w, "Звёздочки", () -> FxConfig.stars, () -> FxConfig.stars = !FxConfig.stars);
        toggle(x, w, "Шлейф", () -> FxConfig.trail, () -> FxConfig.trail = !FxConfig.trail);
        toggle(x, w, "Кольца при прыжке", () -> FxConfig.jumpRings, () -> FxConfig.jumpRings = !FxConfig.jumpRings);
        toggle(x, w, "Искры при ударе", () -> FxConfig.hitSparks, () -> FxConfig.hitSparks = !FxConfig.hitSparks);
        toggle(x, w, "Падающие лепестки", () -> FxConfig.petals, () -> FxConfig.petals = !FxConfig.petals);

        ButtonWidget sky = ButtonWidget.builder(skyLabel(), b -> {
            FxConfig.sky = (FxConfig.sky + 1) % SkyPresets.NAMES.length;
            b.setMessage(skyLabel());
        }).dimensions(x, y, w, 20).build();
        addDrawableChild(sky);
        y += 28;

        addDrawableChild(ButtonWidget.builder(Text.literal("Закрыть"), b -> close())
            .dimensions(x, y, w, 20).build());
    }

    private Text skyLabel() { return Text.literal("Небо: " + SkyPresets.name()); }

    private void toggle(int x, int w, String name, BooleanSupplier get, Runnable flip) {
        ButtonWidget btn = ButtonWidget.builder(label(name, get.getAsBoolean()), b -> {
            flip.run();
            b.setMessage(label(name, get.getAsBoolean()));
        }).dimensions(x, y, w, 20).build();
        addDrawableChild(btn);
        y += 24;
    }

    private static Text label(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "§aВКЛ" : "§cВЫКЛ"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("PulseFX — визуальные эффекты"),
            width / 2, height / 2 - 112, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() { return false; }
}
