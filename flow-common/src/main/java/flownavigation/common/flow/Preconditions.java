package flownavigation.common.flow;

/**
 * Copy paste from flow-path
 *
 * @author lukasz piliszczuk - lukasz.pili@gmail.com
 */
final class Preconditions {
    private Preconditions() {
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> T checkNotNull(T reference, String errorMessage, Object... args) {
        if (reference == null) {
            throw new NullPointerException(String.format(errorMessage, args));
        }

        return reference;
    }

    /**
     * @throws IllegalArgumentException if condition is false.
     */
    static void checkArgument(boolean condition, String errorMessage, Object... args) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(errorMessage, args));
        }
    }
}