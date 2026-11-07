package pulsefx;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

/**
 * Only the local player's own particles are drawn. Nothing here touches entity
 * visibility, so invisible players stay invisible.
 */
public class PulseFxClient implements ClientModInitializer {
    private static KeyBinding menuKey;
    private final Random rnd = new Random();
    private Vec3d lastPos = Vec3d.ZERO;
    private boolean wasOnGround = true;
    private int tick;

    @Override
    public void onInitializeClient() {
        KeyBinding.Category cat = KeyBinding.Category.create(Identifier.of("pulsefx", "main"));
        menuKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.pulsefx.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, cat));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (world.isClient() && FxConfig.hitSparks && world instanceof ClientWorld cw && !entity.isInvisible()) {
                for (int i = 0; i < 14; i++) {
                    cw.addParticleClient(i % 2 == 0 ? ParticleTypes.CRIT : ParticleTypes.ENCHANTED_HIT,
                        entity.getX() + (rnd.nextDouble() - 0.5) * entity.getWidth(),
                        entity.getBodyY(0.3 + rnd.nextDouble() * 0.5),
                        entity.getZ() + (rnd.nextDouble() - 0.5) * entity.getWidth(),
                        (rnd.nextDouble() - 0.5) * 0.4, rnd.nextDouble() * 0.3, (rnd.nextDouble() - 0.5) * 0.4);
                }
            }
            return ActionResult.PASS;
        });
    }

    private void onTick(MinecraftClient mc) {
        while (menuKey.wasPressed()) {
            if (mc.currentScreen == null) mc.setScreen(new FxScreen());
        }
        ClientPlayerEntity p = mc.player;
        ClientWorld w = mc.world;
        if (p == null || w == null || mc.isPaused()) return;
        tick++;

        if (FxConfig.stars && tick % 2 == 0) {
            for (int i = 0; i < 3; i++) {
                double a = rnd.nextDouble() * Math.PI * 2, r = 3 + rnd.nextDouble() * 10;
                w.addParticleClient(ParticleTypes.END_ROD,
                    p.getX() + Math.cos(a) * r, p.getY() + 0.5 + rnd.nextDouble() * 6, p.getZ() + Math.sin(a) * r,
                    0, 0.01 + rnd.nextDouble() * 0.01, 0);
            }
        }

        if (FxConfig.petals && tick % 3 == 0) {
            for (int i = 0; i < 2; i++) {
                w.addParticleClient(ParticleTypes.CHERRY_LEAVES,
                    p.getX() + (rnd.nextDouble() - 0.5) * 16, p.getY() + 4 + rnd.nextDouble() * 3,
                    p.getZ() + (rnd.nextDouble() - 0.5) * 16, 0, -0.02, 0);
            }
        }

        Vec3d pos = p.getEntityPos();
        boolean moved = pos.squaredDistanceTo(lastPos) > 0.0025;
        if (FxConfig.trail && moved) {
            for (int i = 0; i < 4; i++) {
                float hue = ((tick * 4 + i * 20) % 360) / 360f;
                int rgb = MathHelper.hsvToRgb(hue, 0.8f, 1f);
                w.addParticleClient(new DustParticleEffect(rgb, 1.2f),
                    pos.x + (rnd.nextDouble() - 0.5) * 0.3, pos.y + 0.1 + rnd.nextDouble() * 0.9,
                    pos.z + (rnd.nextDouble() - 0.5) * 0.3, 0, 0, 0);
            }
        }
        lastPos = pos;

        if (FxConfig.jumpRings && wasOnGround && !p.isOnGround() && p.getVelocity().y > 0.1) {
            for (int i = 0; i < 24; i++) {
                double a = i / 24.0 * Math.PI * 2;
                w.addParticleClient(ParticleTypes.END_ROD, pos.x, pos.y + 0.1, pos.z,
                    Math.cos(a) * 0.15, 0.02, Math.sin(a) * 0.15);
            }
        }
        wasOnGround = p.isOnGround();
    }
}
