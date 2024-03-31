package opekope2.recipemanager;

public final class Util {
    private Util() {
    }

    public static final String RECIPE_EXTRA_KEY = "Recipe";

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
