package net.monofraps.gradlecurse;

/**
 * The markup types available for change log and known caveats.
 *
 * @author monofraps
 */
public enum MarkupType
{
    CREOLE("creole"), PLAIN("plain");

    private String stringRepresentation;

    MarkupType(final String stringRepresentation)
    {
        this.stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString()
    {
        return stringRepresentation;
    }
}
