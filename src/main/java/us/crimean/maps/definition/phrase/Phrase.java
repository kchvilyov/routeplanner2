package us.crimean.maps.definition.phrase;

import us.crimean.maps.definition.Definition;

public class Phrase implements PhraseInterface {
	protected PhraseInterface[][] partsWithVersions;
	protected int maxVersionsNumber = -1;
	private String delimiter = Definition.SPACE;

	public Phrase(PhraseInterface[][] partsWithVersions) {
		super();
		this.partsWithVersions = partsWithVersions;
	}

	public Phrase(String[][] stringsWithVersions) {
		if (stringsWithVersions == null || stringsWithVersions.length < 1) {
			partsWithVersions = null;
			return;
		}
		partsWithVersions = new PhraseInterface[stringsWithVersions.length][];
		for (int i = 0; i < stringsWithVersions.length; i++) {
			String[] stringVersions = stringsWithVersions[i];
			PhraseInterface[] phraseVersions = null;
			if (stringVersions != null && stringVersions.length > 0) {
				phraseVersions = new PhraseInterface[stringVersions.length];
				for (int v = 0; v < stringVersions.length; v++) {
					phraseVersions[v] = new StringPhrase(stringVersions[v]);
				}
			}
			partsWithVersions[i] = phraseVersions; 
		}
	}

	protected String getVersion(String[] versions, int versionIndex) {
		if (null == versions || versions.length < 1 || versionIndex < 0) {
			return null;
		}
		return versions[versionIndex % versions.length];
	}

	protected PhraseInterface[] getVersionsOfPart(int partIndex) {
		if (null == partsWithVersions || partsWithVersions.length < 1 || partIndex < 0) {
			return null;
		}
		return partsWithVersions[partIndex % partsWithVersions.length];
	}

	public String addition(String firstPart, String secondPart) {
		if (firstPart == null || firstPart.length() == 0) {
			return secondPart; 
		} else if (secondPart == null || secondPart.length() == 0) {
			return firstPart;
		}
//		if (firstPart.matches(".*[\\s,.;:]$") 
//			|| secondPart.matches("^[\\s,.;:].*")) {
		if (firstPart.matches(".*\\W$") 
				|| secondPart.matches("^\\W.*")) {
			return firstPart + secondPart;
		}
		return firstPart + delimiter + secondPart;
	}
	
	/* (non-Javadoc)
	 * @see us.crimean.maps.definition.PhraseInterface#getFullVersion(int)
	 */
	@Override
	public String getFullVersion(int versionIndex) {
		String res = null;
		for (PhraseInterface[] versions : partsWithVersions) {
			res = addition(res, getVersion(versions, versionIndex));
		}
		return res;
	}
	
	private String getVersion(PhraseInterface[] versions, int versionIndex) {
		if (null == versions || versions.length < 1 || versionIndex < 0) {
			return null;
		}
		PhraseInterface version = versions[versionIndex % versions.length];
		if (null == version) {
			return null;
		}
		return version.getFullVersion(versionIndex);
	}

	/* (non-Javadoc)
	 * @see us.crimean.maps.definition.PhraseInterface#getFullVersion(int[])
	 */
	@Override
	public String getFullVersion(int[] versionIndexByPartIndex) {
		if (null == versionIndexByPartIndex) {
			return null;
		}
		String res = null;
		for (int partIndex = 0; partIndex < versionIndexByPartIndex.length; partIndex++) {
			int versionIndex = versionIndexByPartIndex[partIndex]; 
			res = addition(res, getVersion(getVersionsOfPart(partIndex), versionIndex));
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see us.crimean.maps.definition.PhraseInterface#getFullRandom()
	 */
	@Override
	public String getFullRandom() {
		String res = null;
		for (PhraseInterface[] versions : partsWithVersions) {
			if (null != versions) {
				res = addition(res, getVersion(versions, getRandom()));
			}
		}
		return res;
	}
	
	/**
	 * @return random value >= 0 and < maxValue
	 */
	public int getRandom() {
		if (maxVersionsNumber < 0) {
			generateMaxVersionsNumber();
		}
		return (int) ((Math.random() * maxVersionsNumber) % maxVersionsNumber);
	}

	private void generateMaxVersionsNumber() {
		maxVersionsNumber = 0;
		for (PhraseInterface[] part : partsWithVersions) {
			if (null != part) {
				maxVersionsNumber = Math.max(part.length, maxVersionsNumber);
			}
		}
	}
}
