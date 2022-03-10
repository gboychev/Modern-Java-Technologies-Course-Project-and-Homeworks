package lab1;

public class PathUtils {
    public static String getCanonicalPath(String path) {

        if (path == null) return null;

        StringBuilder canonicalPathBuffer = new StringBuilder();

        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/' && i < path.length() - 1 && path.charAt(i + 1) == '/') continue;
            canonicalPathBuffer.append(path.charAt(i));
        }

        String canonicalPathString = new String(canonicalPathBuffer);
        String[] words = canonicalPathString.split("/");
        canonicalPathBuffer.delete(0, canonicalPathBuffer.length());

        for (String s : words) {
            if (s.equals("")) continue;

            if (s.equals("..")) {
                if (!canonicalPathBuffer.isEmpty()) {
                    canonicalPathBuffer.deleteCharAt(canonicalPathBuffer.length() - 1);
                }

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
            if (canonicalPathBuffer.charAt(canonicalPathBuffer.length() - 1) != '/') {
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
//
//    public static void main(String[] args) {
//        System.out.println(getCanonicalPath("/cat/some/../ok"));
//        System.out.println(getCanonicalPath("log/./ok"));
//        System.out.println(getCanonicalPath("/home/"));
//        System.out.println(getCanonicalPath("/../"));
//        System.out.println(getCanonicalPath("/home//foo/"));
//        System.out.println(getCanonicalPath("/a/./b/../../c/"));
//    }
}
