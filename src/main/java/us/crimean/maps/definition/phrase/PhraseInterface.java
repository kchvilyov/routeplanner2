package us.crimean.maps.definition.phrase;

public interface PhraseInterface {

	public abstract String getFullVersion(int versionIndex);

	/**
	 * @param versionIndexByPartIndex
	 *
	 * @return Phrase string addition for all parts
	 * 			 where versionIndexByPartIndex[partIndes] >= 0<br>
	 * so count of parts is equal to not negative version indexes count in versionIndexByPartIndex<br> 
	 * versionIndexByPartIndex.length can be bigger partsWithVersions.length if you want add partVariants more than once
	 */
	public abstract String getFullVersion(int[] versionIndexByPartIndex);

	public abstract String getFullRandom();

}