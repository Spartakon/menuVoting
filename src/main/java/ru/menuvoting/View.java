package ru.menuvoting;

import javax.validation.groups.Default;

public class View {
    // Validate only REST
    public interface Rest extends Default {}

    // Validate only when DB save/update
    public interface Persist extends Default {}
}
