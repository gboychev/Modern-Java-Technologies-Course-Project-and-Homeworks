package lab1;

public class ArrayAnalyzer {
    public static boolean isMountainArray(int[] array) {
        if (array == null) return false;

        if (array.length < 3) return false;

        int index = 1;
        for (int i = 1; i < array.length - 1; i++) {
            if (array[i - 1] > array[i]) {
                index = i - 1;
                break;
            }
        }
        for (int i = array.length - 1; i > index; i--) {
            if (array[i] >= array[i - 1]) {
                return false;
            }
        }
        return true;
    }
}

