package net.monofraps.gradlecurse;

/**
 * This file type refers to the type of file we are uploading to curse.
 *
 * @author monofraps
 */
public enum FileType
{
    RELEASE("r"), BETA("b"), ALPHA("a");

    private String stringRepresentation;

    FileType(final String stringRepresentation)
    {
        this.stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString()
    {
        return stringRepresentation;
    }
}
