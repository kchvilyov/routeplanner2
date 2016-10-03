/**
 * 
 */
package us.crimean.maps.definition.phrase;

/**
 * Simplest phrase based on single string without variants
 * @author Konstantin Chvilyov
 */
public class StringPhrase implements PhraseInterface {
	private String string = null;

	public StringPhrase(String string) {
		this.string = string;
	}

	/* (non-Javadoc)
	 * @see us.crimean.maps.definition.phrase.PhraseInterface#getFullVersion(int)
	 */
	@Override
	public String getFullVersion(int versionIndex) {
		return string;
	}

	/* (non-Javadoc)
	 * @see us.crimean.maps.definition.phrase.PhraseInterface#getFullVersion(int[])
	 */
	@Override
	public String getFullVersion(int[] versionIndexByPartIndex) {
		return string;
	}

	/* (non-Javadoc)
	 * @see us.crimean.maps.definition.phrase.PhraseInterface#getFullRandom()
	 */
	@Override
	public String getFullRandom() {
		return string;
	}

}
