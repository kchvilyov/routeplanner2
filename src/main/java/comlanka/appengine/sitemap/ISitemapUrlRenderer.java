package comlanka.appengine.sitemap;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

interface ISitemapUrlRenderer<T extends ISitemapUrl> {
	
	public Class<T> getUrlClass();
	public String getXmlNamespaces();
	public void render(T url, OutputStreamWriter out, W3CDateFormat dateFormat) throws IOException;
	public void render(T url, PrintWriter out, W3CDateFormat dateFormat) throws IOException;
}
