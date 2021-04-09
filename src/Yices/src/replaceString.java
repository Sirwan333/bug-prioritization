public class replaceString {
    public replaceString() {
    }

    public String replaceAllString(String src, String search, String replace) {
        int i;
        do {
            i = src.indexOf(search);
            if (i != -1) {
                String result = src.substring(0, i);
                result = result + replace;
                result = result + src.substring(i + search.length());
                src = result;
            }
        } while(i != -1);

        return src;
    }
}
