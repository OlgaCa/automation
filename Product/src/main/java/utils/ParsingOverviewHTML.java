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

public class ParsingOverviewHTML {
	
	public static void main(String[] args) throws Exception {
		String path = null;
		try {
			path = "http://10.100.80.56/"
					+ LastModifiedFolder.getLastModifiedFolderName()+"/html";
		} catch (Exception e) {

			e.printStackTrace();
		}
		File htmlFile = new File(
				"C:\\PayuAutomation\\automation\\Product\\test-output\\archive\\20141115-1906\\html\\overview.html");
		Document doc = Jsoup.parse(htmlFile, "UTF-8");
		Elements content = doc.select(".test>a");
		System.out.println(content);
		for (Element ele : content) {
			String attrValue=ele.attr("href");
			String finalPath=path+"/"+attrValue;
			ele.attr("href",path+"/"+attrValue);
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


