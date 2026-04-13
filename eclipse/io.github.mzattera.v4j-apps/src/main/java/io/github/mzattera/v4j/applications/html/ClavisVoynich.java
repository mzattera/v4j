/**
 * */
package io.github.mzattera.v4j.applications.html;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.PageSplitter;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;

/**
 * Generates the Clavis Voynich (https://mzattera.github.io/v4j/clavis/), an
 * HTML version of the Voynich highlighting some of the structures identified
 * over time.
 * 
 * * @author Massimiliano "Maxi" Zattera
 */
public class ClavisVoynich {

	// Template which is filled with actual content
	private final static String TEMPLATE = "<!DOCTYPE html>\n" //
			+ "<html lang=\"en\">\n" //
			+ "<head>\n" //
			+ "    <meta charset=\"UTF-8\">\n" //
			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" //
			+ "    <title>Clavis Voynich</title>\n" //
			+ "    <style>\n" //
			+ "        /* =========================================\n" //
			+ "           Variables & General Look and Feel\n" //
			+ "           ========================================= */\n" //
			+ "        :root {\n" //
			+ "            --bg-color: #121212;\n" //
			+ "            --sidebar-bg: #1e1e1e;\n" //
			+ "            --text-main: #e0e0e0;\n" //
			+ "            --text-muted: #a0a0a0;\n" //
			+ "            --border-color: #333;\n" //
			+ "            --accent-red: #ff6b6b;\n" //
			+ "            --accent-blue: #5c9ebf;\n" //
			+ "            --font-modern: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;\n" //
			+ "            --top-bar-height: 60px;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        @font-face {\n" //
			+ "            font-family: 'VoynichEva1';\n" //
			+ "            src: url('./eva1.ttf') format('truetype');\n" //
			+ "            font-weight: normal;\n" //
			+ "            font-style: normal;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        body {\n" //
			+ "            margin: 0;\n" //
			+ "            padding: 0;\n" //
			+ "            background-color: var(--bg-color);\n" //
			+ "            color: var(--text-main);\n" //
			+ "            font-family: var(--font-modern);\n" //
			+ "            display: flex;\n" //
			+ "            height: 100vh;\n" //
			+ "            overflow: hidden;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        /* =========================================\n" //
			+ "           Layout: Sidebar\n" //
			+ "           ========================================= */\n" //
			+ "        .sidebar {\n" //
			+ "            width: 150px;\n" //
			+ "            background-color: var(--sidebar-bg);\n" //
			+ "            border-right: 1px solid var(--border-color);\n" //
			+ "            display: flex;\n" //
			+ "            flex-direction: column;\n" //
			+ "            transition: width 0.3s ease;\n" //
			+ "            overflow-x: hidden;\n" //
			+ "            flex-shrink: 0;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .sidebar.collapsed {\n" //
			+ "            width: 60px;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .sidebar-header {\n" //
			+ "            display: flex;\n" //
			+ "            align-items: center;\n" //
			+ "            justify-content: space-between;\n" //
			+ "            padding: 0 20px;\n" //
			+ "            height: var(--top-bar-height);\n" //
			+ "            border-bottom: 1px solid var(--border-color);\n" //
			+ "            box-sizing: border-box;\n" //
			+ "            flex-shrink: 0;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .toggle-btn {\n" //
			+ "            background: none;\n" //
			+ "            border: none;\n" //
			+ "            color: var(--text-main);\n" //
			+ "            font-size: 1.5rem;\n" //
			+ "            cursor: pointer;\n" //
			+ "            padding: 0;\n" //
			+ "            display: flex;\n" //
			+ "            align-items: center;\n" //
			+ "            justify-content: center;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .nav-links {\n" //
			+ "            padding: 10px 0;\n" //
			+ "            display: flex;\n" //
			+ "            flex-direction: column;\n" //
			+ "            overflow-y: auto;" //
			+ "            flex-grow: 1;" //
			+ "        }\n" //
			+ "\n" //
			+ "        .nav-links a {\n" //
			+ "            color: var(--text-main);\n" //
			+ "            text-decoration: none;\n" //
			+ "            display: block;\n" //
			+ "            white-space: nowrap;\n" //
			+ "            overflow: hidden;\n" //
			+ "            text-overflow: ellipsis;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .nav-links a:hover {\n" //
			+ "            background-color: #2a2a2a;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        /* CSS for collapsible Sidebar Navigation groups */\n" //
			+ "        .nav-group summary {\n" //
			+ "            font-weight: bold;\n" //
			+ "            font-size: 1.1rem;\n" //
			+ "            cursor: pointer;\n" //
			+ "            list-style: none;\n" //
			+ "            display: flex;\n" //
			+ "            align-items: center;\n" //
			+ "            padding-left: 20px;\n" //
			+ "        }\n" //
			+ "        .nav-group summary::-webkit-details-marker {\n" //
			+ "            display: none;\n" //
			+ "        }\n" //
			+ "        .nav-group summary::before {\n" //
			+ "            content: '▶';\n" //
			+ "            font-size: 0.8rem;\n" //
			+ "            margin-right: 8px;\n" //
			+ "            transition: transform 0.2s ease;\n" //
			+ "            color: var(--text-muted);\n" //
			+ "        }\n" //
			+ "        .nav-group[open] summary::before {\n" //
			+ "            transform: rotate(90deg);\n" //
			+ "        }\n" //
			+ "        .nav-group summary a {\n" //
			+ "            padding: 10px 20px 10px 0;\n" //
			+ "            color: var(--text-main);\n" //
			+ "            text-decoration: none;\n" //
			+ "            flex-grow: 1;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .nav-h2 {\n" //
			+ "            font-size: 0.85rem !important;\n" // /* Font più compatto */
			+ "            padding: 4px 20px 4px 45px !important;\n" // /* Padding verticale ridotto */
			+ "            color: var(--text-muted) !important;\n" //
			+ "            line-height: 1.2;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .sidebar.collapsed .nav-links span,\n" //
			+ "        .sidebar.collapsed #sidebar-title {\n" //
			+ "            display: none;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        /* =========================================\n" //
			+ "           Layout: Main Content & Scrollable Area\n" //
			+ "           ========================================= */\n" //
			+ "        .main-content {\n" //
			+ "            flex-grow: 1;\n" //
			+ "            display: flex;\n" //
			+ "            flex-direction: column;\n" //
			+ "            overflow: hidden;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .scrollable-area {\n" //
			+ "            flex-grow: 1;\n" //
			+ "            overflow-y: auto;\n" //
			+ "            overflow-x: hidden;\n" //
			+ "            padding: 0 40px 20px 40px;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        /* =========================================\n" //
			+ "           Info Section (Collapsible & Fixed Top)\n" //
			+ "           ========================================= */\n" //
			+ "        .info-section {\n" //
			+ "            background-color: var(--sidebar-bg);\n" //
			+ "            flex-shrink: 0; \n" //
			+ "            z-index: 10;\n" //
			+ "            box-shadow: 0 4px 6px rgba(0,0,0,0.2);\n" //
			+ "            margin: 0;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .info-section summary {\n" //
			+ "            font-weight: bold;\n" //
			+ "            font-size: 1.1rem;\n" //
			+ "            cursor: pointer;\n" //
			+ "            list-style: none;\n" //
			+ "            display: flex;\n" //
			+ "            align-items: center;\n" //
			+ "            color: var(--text-main);\n" //
			+ "            height: var(--top-bar-height);\n" //
			+ "            padding: 0 40px;\n" //
			+ "            border-bottom: 1px solid var(--border-color);\n" //
			+ "            box-sizing: border-box;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .info-section summary::-webkit-details-marker {\n" //
			+ "            display: none;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .info-section summary::before {\n" //
			+ "            content: '▶';\n" //
			+ "            font-size: 0.8rem;\n" //
			+ "            margin-right: 10px;\n" //
			+ "            transition: transform 0.2s ease;\n" //
			+ "            color: var(--text-muted);\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .info-section[open] summary::before {\n" //
			+ "            transform: rotate(90deg);\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .info-content {\n" //
			+ "            color: var(--text-muted);\n" //
			+ "            line-height: 1.6;\n" //
			+ "            padding: 20px 40px;\n" //
			+ "            border-bottom: 1px solid var(--border-color);\n" //
			+ "        }\n" //
			+ "        .info-content a {\n" //
			+ "            color: var(--accent-blue);\n" //
			+ "            text-decoration: none;\n" //
			+ "            border-bottom: 1px dashed var(--accent-blue);\n" //
			+ "            transition: all 0.2s ease;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .info-content a:hover {\n" //
			+ "            color: #7ab8d9; /* Un blu leggermente più luminoso */\n" //
			+ "            border-bottom-style: solid;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .info-content .highlight {\n" //
			+ "            background-color: rgba(255, 212, 59, 0.15); /* Giallo scuro trasparente */\n" //
			+ "            color: #ffd43b; /* Testo giallo/oro */\n" //
			+ "            padding: 2px 6px;\n" //
			+ "            border-radius: 4px;\n" //
			+ "            font-weight: 500;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        /* =========================================\n" //
			+ "           Typography & Content Styles\n" //
			+ "           ========================================= */\n" //
			+ "        h1 {\n" //
			+ "            border-bottom: 2px solid var(--border-color);\n" //
			+ "            padding-bottom: 10px;\n" //
			+ "            margin-top: 40px; \n" //
			+ "        }\n" //
			+ "\n" //
			+ "        h2 {\n" //
			+ "            color: var(--text-muted);\n" //
			+ "            font-size: 1.2rem;\n" //
			+ "            margin-top: 40px;\n" //
			+ "            word-wrap: break-word;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .voynich {\n" //
			+ "            font-family: 'VoynichEva1', sans-serif;\n" //
			+ "            font-size: 0.77rem; \n" //
			+ "            line-height: 1.6;\n" //
			+ "            letter-spacing: 1px;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        p {\n" //
			+ "            line-height: 1.5;\n" //
			+ "            margin-bottom: 15px;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        hr {\n" //
			+ "            border: 0;\n" //
			+ "            height: 1px;\n" //
			+ "            background: var(--border-color);\n" //
			+ "            margin: 20px 0;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .hl-red { color: var(--accent-red); font-weight: bold; }\n" //
			+ "        .hl-blue { color: var(--accent-blue); font-weight: bold; }\n" //
			+ "\n" //
			+ "        /* =========================================\n" //
			+ "           Responsive Entry Layout (Flexbox)\n" //
			+ "           ========================================= */\n" //
			+ "        .entry-section {\n" //
			+ "            margin-bottom: 60px;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .entry-layout {\n" //
			+ "            display: flex;\n" //
			+ "            flex-wrap: wrap;\n" //
			+ "            gap: 30px;\n" //
			+ "            background-color: var(--sidebar-bg);\n" //
			+ "            padding: 20px;\n" //
			+ "            border-radius: 8px;\n" //
			+ "            border: 1px solid var(--border-color);\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .image-container {\n" //
			+ "            flex: 1 1 300px;\n" //
			+ "            max-width: 40%;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .text-container {\n" //
			+ "            flex: 1 1 400px;\n" //
			+ "            min-width: 300px;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        .image-container img {\n" //
			+ "            width: 100%;\n" //
			+ "            height: auto;\n" //
			+ "            border-radius: 4px;\n" //
			+ "            display: block;\n" //
			+ "        }\n" //
			+ "\n" //
			+ "        @media (max-width: 900px) {\n" //
			+ "            .image-container { max-width: 100%; }\n" //
			+ "        }\n" //
			+ "        /* =========================================\n" //
			+ "           Custom Scrollbar\n" //
			+ "           ========================================= */\n" //
			+ "        ::-webkit-scrollbar {\n" //
			+ "            width: 8px;\n" //
			+ "        }\n" //
			+ "        ::-webkit-scrollbar-track {\n" //
			+ "            background: var(--bg-color);\n" //
			+ "        }\n" //
			+ "        ::-webkit-scrollbar-thumb {\n" //
			+ "            background: #444;\n" //
			+ "            border-radius: 4px;\n" //
			+ "        }\n" //
			+ "        ::-webkit-scrollbar-thumb:hover {\n" //
			+ "            background: #666;\n" //
			+ "        }\n" //
			+ " </style>\n" //
			+ "</head>\n" //
			+ "<body>\n" //
			+ "\n" //
			+ "    <nav class=\"sidebar\" id=\"sidebar\">\n" //
			+ "        <div class=\"sidebar-header\">\n" //
			+ "            <span id=\"sidebar-title\">Index</span>\n" //
			+ "            <button class=\"toggle-btn\" id=\"toggleBtn\" title=\"Toggle Menu\">☰</button>\n" //
			+ "        </div>\n" //
			+ "        <div class=\"nav-links\">\n" //
			+ "			{{navbar}}\n" //
			+ "        </div>\n" //
			+ "    </nav>\n" //
			+ "\n" //
			+ "    <main class=\"main-content\">\n" //
			+ "        \n" //
			+ "        <details class=\"info-section\">\n" //
			+ "            <summary>About the Clavis Voynich</summary>\n" //
			+ "            <div class=\"info-content\">\n" //
			+ "                {{about}}\n" //
			+ "            </div>\n" //
			+ "        </details>\n" //
			+ "\n" //
			+ "        <div class=\"scrollable-area\">\n" //
			+ "			{{content}}\n" //
			+ "        </div> \n" //
			+ "	</main>\n" //
			+ "\n" //
			+ "    <script>\n" //
			+ "        const sidebar = document.getElementById('sidebar');\n" //
			+ "        const toggleBtn = document.getElementById('toggleBtn');\n" //
			+ "        const sidebarTitle = document.getElementById('sidebar-title');\n" //
			+ "\n" //
			+ "        toggleBtn.addEventListener('click', () => {\n" //
			+ "            sidebar.classList.toggle('collapsed');\n" //
			+ "            if (sidebar.classList.contains('collapsed')) {\n" //
			+ "                sidebarTitle.style.display = 'none';\n" //
			+ "            } else {\n" //
			+ "                setTimeout(() => { sidebarTitle.style.display = 'inline'; }, 150);\n" //
			+ "            }\n" //
			+ "        });\n" //
			+ "    </script>\n" //
			+ "</body>\n" //
			+ "</html>";

	// "About" text
	private static final String ABOUT = "<p>Welcome to the <span class=\"highlight\">Clavis Voynich</span> (last updated Apr. 13, 2026).</p>\n" //
			+ "\n" //
			+ "<p>This is part of my <a href=\"../index.html\" target=\"_blank\">Repeatable Voynich</a> site, please have a look at its main page to understand some of the conventions and the lexicon I normally use.</p>\n" //
			+ "\n" //
			+ "<p>This page provides a side-by-side comparison of the original manuscript images alongside their transcriptions. The text is presented in two formats: first using Voynich Eva font to mimic the original script, and second using a custom transliterated format. This transliteration is meant to highlight patterns in the Voynich text, as resulting from my <a href=\"../index.html#notes\" target=\"_blank\">working notes</a>.</p>\n" //
			+ "\n" //
			+ "<ul>\n" //
			+ "\n" //
			+ "<li>The transliteration uses the <a href=\"../005/index.html#slot\" target=\"_blank\">Slot alphabet</a>.</li>\n" //
			+ "\n" //
			+ "<li>Pages are grouped in <a href=\"../003/index.html\" target=\"_blank\">clusters</a>. Pages not falling in any cluster are not shown.\n" //
			+ "Notice that, as explained in <a href=\"../009/index.html\" target=\"_blank\">Note 009</a>, these clusters seems to reflect structural differences in Voynich words.\n" //
			+ "</li>\n" //
			+ "\n" //
			+ "<li>Only running text in paragraphs is shown (e.g. \"labels\" are omitted).</li>\n" //
			+ "\n" //
			+ "<li>Words that contain unreadable characters are <span style=\"color:CadetBlue\">grayed out</span>.</li>\n" //
			+ "\n" //
			+ "<li><a href=\"../005/index.html#unstructured\" target=\"_blank\">Unstructured</a> words are shown in <span style=\"color:red\">red</span>.</li>\n" //
			+ "\n" //
			+ "<li><a href=\"../005/index.html#separable\" target=\"_blank\">Separable</a> words are split into their two components, separated by an equal ('=') sign.\n" //
			+ "For example, 'chockhy' can be seen as the combination of two <a href=\"../005/index.html#regular\" target=\"_blank\">regular</a> words, 'cho' and 'ckhy'.\n" //
			+ "Therefore, 'chockhy' shows as 'cho=ckhy' in the transliteration.\n" //
			+ "</li>\n" //
			+ "\n" //
			+ "</ul>\n" //
			+ "\n" //
			+ "<p>This page has been generated automatically using <a href=\"https://github.com/mzattera/v4j/blob/master/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mzattera/v4j/applications/html/ClavisVoynich.java\" target=\"_blank\">ClavisVoynic.java</a>.</p>\n" //
			+ "\n" //
			+ "<hr/>\n" //
			+ "\n" //
			+ "<p>Copyright Massimiliano Zattera.</p>\n" //
			+ "\n" //
			+ "<p><a rel=\"license\" href=\"http://creativecommons.org/licenses/by-nc-sa/4.0/\"><img alt=\"Creative Commons License\" style=\"border-width:0\" src=\"https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png\" /></a><br />This work is licensed under a <a rel=\"license\" href=\"http://creativecommons.org/licenses/by-nc-sa/4.0/\">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.</p>\n" //
			+ "\n" //
			+ "<p>Images of the Cipher manuscript (Voynich manuscript). General Collection, Beinecke Rare Book and Manuscript Library, Yale University, contained in these pages\n" //
			+ " have been downloaded from <a href=\"https://archive.org/details/voynich/\" target=\"_blank\">Internet Archive</a> and are assumed to be available for public use.\n" //
			+ "\n" //
			+ "Massimiliano Zattera, the publisher of this site, holds no rights on these images.</p>";

	/**
	 * @return HTML version of the entire Voynich.
	 * 
	 * @throws URISyntaxException
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String getHtml() throws IOException, ParseException {
		return getHtml(VoynichFactory.getDocument(Transcription.AUGMENTED, TranscriptionType.MAJORITY, Alphabet.SLOT));

	}

	/**
	 * * @return HTML version of given cluster of the Voynich.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String getHtml(String cluster) throws IOException, ParseException {
		return getHtml(VoynichFactory.getDocument(Transcription.AUGMENTED, TranscriptionType.MAJORITY, Alphabet.SLOT)
				.filterPages(new PageFilter.Builder().cluster(cluster).build()));
	}

	// Style for unreadable words
	private static final String UNREADABLE_STYLE = "color:CadetBlue";

	// Style for unstructured words
	private static final String UNSTRUCTURED_STYLE = "color:red";

	private final static List<String> CLUSTERS = List.of("HA", "HB", "BB", "PA", "SB");
	private final static Map<String, String> CLUSTER_DESCRIPTION = new HashMap<>();
	static {
		CLUSTER_DESCRIPTION.put("HA", "(Herbal A)");
		CLUSTER_DESCRIPTION.put("HB", "(Herbal B)");
		CLUSTER_DESCRIPTION.put("BB", "(Biological B)");
		CLUSTER_DESCRIPTION.put("PA", "(Pharmaceutical A)");
		CLUSTER_DESCRIPTION.put("SB", "(Stars B)");
	}

	/**
	 * * @return HTML version for given document.
	 */
	public static String getHtml(IvtffText document) {

		// Consider only paragraph text
		document = document.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

		// Group pages by cluster when printing
		Map<String, List<IvtffPage>> clusters = document.splitElements(new PageSplitter.Builder().byCluster().build());

		StringBuilder html = new StringBuilder();
		StringBuilder navHtml = new StringBuilder();
		for (String cluster : CLUSTERS) {

			List<IvtffPage> pages = clusters.get("Cluster=" + cluster);

			// Cluster ID / title
			html.append("<h1 id=\"section-").append(cluster).append("\">Cluster ").append(cluster).append(" ")
					.append(CLUSTER_DESCRIPTION.get(cluster)).append("</h1>\n\n");

			// Sidebar Nav Group
			navHtml.append("<details class=\"nav-group\" open>\n");
			navHtml.append("    <summary><a href=\"#section-").append(cluster).append("\"><span>").append(cluster)
					.append("</span></a></summary>\n");

			for (IvtffPage page : pages) {

				// Page ID / title
				html.append("<div class=\"entry-section\">\n\n");
				html.append("<h2 id=\"").append(page.getDescriptor().getId()).append("\">Folio ")
						.append(page.getDescriptor().getId()).append("</h2>\n\n");

				// Sidebar Sublink
				navHtml.append("    <a href=\"#").append(page.getDescriptor().getId())
						.append("\" class=\"nav-h2\"><span>").append(page.getDescriptor().getId())
						.append("</span></a>\n");

				html.append("<div class=\"entry-layout\">\n");

				// Image
				html.append("<div class=\"image-container\">\n");
				html.append("<img src=\"./images/").append(page.getId()).append(".jpg\" alt=\"Folio ")
						.append(page.getId()).append("\" />\n");
				html.append("</div>\n\n");

				// EVA text
				html.append("<div class=\"text-container\">\n\n");
				html.append(getHtml(page, true));
				html.append("\n<hr/>\n");

				// Slot text
				html.append(getHtml(page, false));

				html.append("</div>\n");
				html.append("</div>\n");
				html.append("</div>\n");
			}
			navHtml.append("</details>\n");
		}

		return TEMPLATE.replace("{{about}}", ABOUT) //
				.replace("{{content}}", html.toString()) //
				.replace("{{navbar}}", navHtml.toString());
	}

	/**
	 * @param page
	 * @param toEva If true, means we want to print the result using EVA font.
	 *              * @return HTML for a single page.
	 */
	private static String getHtml(IvtffPage page, boolean toEva) {
		StringBuilder html = new StringBuilder();

		// Split words in regular & separable
		Map<String, TermDecomposition> decomposition = SlotAlphabet.decompose(page.getWords(true).itemSet());

		boolean isFirstLine = true; // First line in paragraph?
		for (IvtffLine line : page.getElements()) {

			// Beginning of paragraph
			if (isFirstLine) {
				if (toEva)
					html.append("<p class=\"voynich\">\n");
				else
					html.append("<p>\n");
			}

			// Line ID
			if (!toEva)
				html.append("&lt;").append(line.getDescriptor().getId().toString()).append("&gt;&nbsp;&nbsp;");

			String[] words = line.splitWords();
			for (int idx = 0; idx < words.length; ++idx) {

				String w = words[idx];

				// We use styles to indicate word properties
				List<String> styles = new ArrayList<>();

				if (Alphabet.SLOT.isUnreadable(w)) {

					// Unreadable word
					styles.add(UNREADABLE_STYLE);
				} else {

					// Word can be read
					TermDecomposition d = decomposition.get(w);
					switch (d.classification) {
					case UNSTRUCTURED:
						styles.add(UNSTRUCTURED_STYLE);
						break;
					case SEPARABLE:
						// Separate the word
						w = d.part1 + "=" + d.part2;
						break;
					default:
						break;
					}
				}

				w = toEva ? toEva(w) : w;
				if (styles.size() > 0) { // This word was marked with some special styles
					StringBuilder s = new StringBuilder();
					s.append("<span style=\"");
					for (int i = 0; i < styles.size(); ++i) {
						s.append(styles.get(i));
						if (i < styles.size() - 1)
							s.append(';');
					}
					s.append("\">").append(w).append("</span>");
					w = s.toString();
				}
				html.append(w).append("&nbsp;");

			} // for each word

			html.append("</br>\n");
			if (line.isLast()) // end of paragraph
				html.append("</p>\n");

			// Keeps track whether next line is first in paragraph
			isFirstLine = line.isLast();

		} // for each line

		return html.toString();
	}

	/**
	 * * @param txt
	 * 
	 * @return Given word that uses Slot alphabet and special chars as a sequence of
	 *         chars to best be printed using EVA font.
	 */
	private static String toEva(String txt) {
		txt = txt.replace("K", "cKh");
		txt = txt.replace("F", "cFh");
		txt = txt.replace("T", "cTh");
		txt = txt.replace("P", "cPh");

		txt = txt.replace("C", "Ch");
		txt = txt.replace("S", "Sh");

		txt = txt.replace("B", "eee");
		txt = txt.replace("E", "ee");

		txt = txt.replace("U", "iii");
		txt = txt.replace("J", "ii");

		txt = txt.replace("=", "");

		return txt;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String html = getHtml();

			System.out.print(html);
			Files.writeString(Path.of("D:\\Users\\mzatt\\Projects\\Git - v4j\\docs\\clavis\\index.html"), html);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			System.out.print("\n\nFinished!\n");
		}

	}
}