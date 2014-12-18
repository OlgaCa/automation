package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class will parse the HTML text and map the src link with server ip
 * address so that file can be accessed from remote machine as well.
 * Written using JSoup API. To execute this file we need to add dependency of JSoup.
 * 
 * @author shishir.dwivedi
 * 
 */
public class ParsingHTML {

	public static void main(String[] args) throws Exception {
		String path = null;
		try {
			path = "ftp://10.100.62.15/"
					+ LastModifiedFolder.getLastModifiedFolderName()+"/html";
		} catch (Exception e) {

			e.printStackTrace();
		}
		File htmlFile = new File(
				LastModifiedFolder.getCompletePathOfFile()+"\\html\\index.html");
		Document doc = Jsoup.parse(htmlFile, "UTF-8");
		Elements content = doc.select("html>frameset>frame");
		for (Element ele : content) {
			String attrValue=ele.attr("name");
			/*if (ele.attr("name").equalsIgnoreCase("suites"))
				ele.attr("src", path + "/suites.html");
			else if (ele.attr("name").equalsIgnoreCase("main"))
				ele.attr("src", path + "/overview.html");*/
		}

		
		// saving same file
		BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(htmlFile), "UTF-8"));
		htmlWriter.write(doc.toString());
		htmlWriter.flush();
		htmlWriter.close();

	}
}
