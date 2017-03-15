package us.crimean.maps.definition;

public class DecodeParam {

	/**
	 * @return true for remote OpenShift server
	 * 		false for local Windows server
	 */
	public static boolean isDecodeParam() {
		return true;
		/* UTF-8 for local and remote server
		if (decodeParam == null) {
			//decodeParam = !Charset.forName("windows-1251").equals(Charset.defaultCharset());
			decodeParam = !Charset.forName("UTF-8").equals(Charset.defaultCharset());
		}
		return decodeParam;
		*/
	}

}
