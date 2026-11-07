package pulsefx;

/** 7 sky colour presets: {sky (zenith), clouds}. 0xRRGGBB. */
public final class SkyPresets {
    public static final String[] NAMES = {
        "Ванильное", "Розовый закат", "Неоновая ночь", "Аврора",
        "Лавандовый рассвет", "Океан", "Кровавая луна", "Золотой час"
    };
    private static final int[][] COLORS = {
        {0, 0},
        {0xFF6FA8, 0xFFD1E3}, // розовый закат
        {0x1B0A3C, 0xB400FF}, // неоновая ночь
        {0x0B3D3A, 0x7CFFCB}, // аврора
        {0xB79CFF, 0xFFE6F7}, // лавандовый рассвет
        {0x0E5C8C, 0xBFEFFF}, // океан
        {0x4A0000, 0xFF3B3B}, // кровавая луна
        {0xFFB347, 0xFFF1C9}  // золотой час
    };

    public static int sky() { return COLORS[FxConfig.sky][0]; }
    public static int clouds() { return COLORS[FxConfig.sky][1]; }
    public static String name() { return NAMES[FxConfig.sky]; }

    private SkyPresets() {}
}
