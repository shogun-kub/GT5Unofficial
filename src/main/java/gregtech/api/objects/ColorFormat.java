package gregtech.api.objects;

import net.minecraft.util.EnumChatFormatting;

public enum ColorFormat {
    BAD(EnumChatFormatting.RED),
    BETTER(EnumChatFormatting.YELLOW),
    GOOD(EnumChatFormatting.GREEN),

    NEUTRAL(EnumChatFormatting.AQUA),
    SPECIAL(EnumChatFormatting.LIGHT_PURPLE);

    private final EnumChatFormatting prefix;

    public final static DoubleFun DEFAULT = a -> a, MULTIBLOCK_STATUS = d -> d-1999d;

    ColorFormat(EnumChatFormatting prefix) {
        this.prefix = prefix;
    }

    public String format(String input) {
        return prefix + input + EnumChatFormatting.RESET;
    }

    public static String format(int value, int minValue, int maxValue, DoubleFun decider) {
        maxValue -= minValue;
        int tempValue = value - minValue;

        tempValue = (int) (decider.decide(((double) tempValue) / (((double) maxValue) / 10000d)));
        if (tempValue <= 2000)
            return BAD.format(Integer.toString(value));
        if (tempValue >= 8000)
            return GOOD.format(Integer.toString(value));
        return BETTER.format(Integer.toString(value));
    }

    public static String format(int value, int minValue, int maxValue) {
        return format(value, minValue, maxValue, DEFAULT);
    }

    interface DoubleFun {
        /**
         * accepts value between 0 and 10000, returns modified value
         * used to control good and bad events
         * @param regularValue val
         * @return modified val
         *
         * eg.
         *
         * you want to modify func that operates between 0 and 6, for 6 and less be Better or Bad
         * and only 6 be GOOD
         * than Multiblock_Status is your func
         *
         */
        double decide (double regularValue);
    }



}
