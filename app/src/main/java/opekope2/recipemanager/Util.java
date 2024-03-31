package opekope2.recipemanager;

public final class Util {
    private Util() {
    }

    public static final String USERS_COLLECTION_NAME = "users";
    public static final String RECIPES_COLLECTION_NAME = "recipes";
    public static final String RECIPE_EXTRA_KEY = "Recipe";

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
