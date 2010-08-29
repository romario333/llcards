package templates.extensions;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import play.templates.JavaExtensions;

public class WikiExtensions extends JavaExtensions {
	
	public static String textile(String markup) {
		MarkupParser parser = new MarkupParser(new TextileLanguage());
		return parser.parseToHtml(markup);
	}

}
