package lab1;

public class WeatherForecaster {
    public static int[] getsWarmerIn(int[] temperatures) {
        if (temperatures == null) return null;

        int[] timeArr = new int[temperatures.length];

        for (int i = 0; i < temperatures.length; i++) {
            for (int j = i + 1; j < temperatures.length; j++) {
                if (temperatures[i] < temperatures[j]) {
                    timeArr[i] = j - i;
                    break;
                }
            }
        }
        return timeArr;
    }
}
