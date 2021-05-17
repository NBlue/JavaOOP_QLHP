package utilities.helper;

public class UtilityHelper {
    public static boolean approximatelyEqual(float desiredValue, float actualValue) {
        return approximatelyEqual(desiredValue, actualValue, 2); // default 2% value of higher
    }

    public static boolean approximatelyEqual(float desiredValue, float actualValue, float tolerancePercentage) {
        return approximatelyEqual(desiredValue, actualValue, 0, tolerancePercentage);
    }

    public static boolean approximatelyEqual(float desiredValue, float actualValue, float minPercent, float maxPercent) {
        float diff = Math.abs(desiredValue - actualValue);

        float higher = Math.max(desiredValue, actualValue);

        float toleranceMax = maxPercent/100 * higher;
        float toleranceMin = minPercent/100 * higher;

        return diff >= toleranceMin && diff < toleranceMax;
    }
}
