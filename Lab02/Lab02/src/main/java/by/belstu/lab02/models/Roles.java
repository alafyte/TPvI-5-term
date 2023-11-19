package by.belstu.lab02.models;

public enum Roles
{
    GUEST("GUEST"), WORKER("WORKER"), ADMIN("ADMIN");

    private final String type;

    Roles(String string) {
        type = string;
    }

    @Override
    public String toString() {
        return type;
    }
}
