package lab1;

import java.util.Arrays;

public class Tasks {
    public static void main(String[] args) {

        int[] test = getsWarmerIn(new int[]{3, 4, 5, 1, -1, 2, 6, 3});
        for(int i : test){
            System.out.println(i);
        }

        System.out.println(getCanonicalPath("/home/"));
        System.out.println(getCanonicalPath("/../"));
        System.out.println(getCanonicalPath("/home//foo/"));
        System.out.println(getCanonicalPath("/a/./b/../../c/"));
    }

    public static int[] getsWarmerIn(int[] temperatures) {
        if (temperatures == null) return null;

        int[] timeArr = new int[temperatures.length];
        Arrays.fill(timeArr, 0);

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

    //solve in linear time, unfinished, doesn't work
    /*
    public static int[] getsWarmerInLinear(int[] temperatures) {
        if (temperatures == null) return null;

        int[] timeArr = new int[temperatures.length];
        //boolean[] zeroes = new boolean[temperatures.length];
        Arrays.fill(timeArr, 0);
        //Arrays.fill(zeroes, true);

        int peakIndex = 0;
        boolean ascending = false;
        int descendingCount = 0;

        for (int i = 0; i < temperatures.length; i++) {
            if (i + 1 < temperatures.length) {
                if (temperatures[i] < temperatures[i + 1]) {
                    timeArr[i] = 1;
                    if (ascending == false) {
                        for (int j = 0; j < i; j++) {
                            if (temperatures[j] < temperatures[i + 1]) {
                                timeArr[j] = i + 1 - j;
                            }
                        }
                        ascending = true;
                    }
                    descendingCount = 0;
                    continue;
                } else {
                    ascending = false;
                    descendingCount++;
                }
            } else {
                if (temperatures[i - 1] < temperatures[i]) {
                    timeArr[i - 1] = 1;
                }
            }
        }
        return timeArr;
    }
*/
    public static String getCanonicalPath(String path) {

        if (path == null) return null;

        StringBuilder canonicalPathBuffer = new StringBuilder();

        for (int i = 0; i < path.length() - 1; i++) {
            if (path.charAt(i) == '/' && path.charAt(i + 1) == '/') continue;
            canonicalPathBuffer.append(path.charAt(i));
        }

        String canonicalPathString = new String(canonicalPathBuffer);
        String[] words = canonicalPathString.split("/");
        canonicalPathBuffer.delete(1, canonicalPathBuffer.length());

        for (String s : words) {
            if (s.equals("")) continue;

            if (s.equals("..")) {
                canonicalPathBuffer.deleteCharAt(canonicalPathBuffer.length() - 1);

                if (!canonicalPathBuffer.isEmpty()) {
                    canonicalPathBuffer.delete(canonicalPathBuffer.lastIndexOf("/"), canonicalPathBuffer.length());
                }
                continue;
            }

            if (s.equals(".")) {
                continue;
            }

            if (canonicalPathBuffer.isEmpty()) {
                canonicalPathBuffer.append('/');
            }

            canonicalPathBuffer.append(s);
            canonicalPathBuffer.append('/');
        }
        if (canonicalPathBuffer.isEmpty()) {
            canonicalPathBuffer.append('/');
        } else if (canonicalPathBuffer.length() > 1) {
            canonicalPathBuffer.deleteCharAt(canonicalPathBuffer.length() - 1);
        }

        return new String(canonicalPathBuffer);
    }
}
