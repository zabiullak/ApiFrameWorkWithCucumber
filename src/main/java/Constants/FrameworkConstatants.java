package Constants;

public final class FrameworkConstatants {

    private FrameworkConstatants(){

    }
    private static final String RESOURCEPATH = System.getProperty("user.dir")+"/src/main/resources/";

    public static String getRESOURCEPATH() {
        return RESOURCEPATH;
    }



}
