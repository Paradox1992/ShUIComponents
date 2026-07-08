package shui.repositories.text;

import shui.contracts.text.ShInputValidator;

/**
 * Factory for ShInput validation repositories.
 */
public final class ShInputValidationRepositories {

    private static final ShInputValidator DEFAULT_REPOSITORY = new ShInputValidationRepository();

    private ShInputValidationRepositories() {
    }

    public static ShInputValidator defaultRepository() {
        return DEFAULT_REPOSITORY;
    }
}
