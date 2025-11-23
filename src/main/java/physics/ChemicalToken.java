package physics;

/**
 * Represents a possible token resulting from parsing a chemical formula
 * @param string String representation of the token
 * @param type Logical type of the token
 */
public record ChemicalToken(String string, TokenType type) { }