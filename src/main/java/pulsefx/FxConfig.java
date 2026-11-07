package pulsefx;

/** Runtime settings (in-memory; resets on restart). */
public final class FxConfig {
    public static boolean stars = true;
    public static boolean trail = true;
    public static boolean jumpRings = true;
    public static boolean hitSparks = true;
    public static boolean petals = false;
    /** 0 = vanilla sky, 1..7 = custom presets */
    public static int sky = 0;

    private FxConfig() {}
}
