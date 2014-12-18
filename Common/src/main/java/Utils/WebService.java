package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.lorecraft.phparser.SerializedPhpParser;

import Utils.DataBase.DatabaseType;

/**
 * 
 * @author vidya.priyadarshini
 *
 */
public class WebService {

	public static JSONObject Execute(Config testConfig, TestDataReader data,
			int webServiceRow) {
		JSONObject jObject = null;
		testConfig.endExecutionOnfailure = true;
		URL url;
		HttpURLConnection connection;
		String type = data.GetData(webServiceRow, "Type").trim().toLowerCase();
		String comments = data.GetData(webServiceRow, "Comments").trim();
		String method = data.GetData(webServiceRow, "Method").trim();
		String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "BaseUrl").trim());
		String command = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Command").trim());
		String parameters = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Parameters").trim());
		String Url = null;

		Url = baseUrl + command + parameters;

		try {
			url = new URL(Url);
			testConfig.logComment("Executing '" + comments + "' on '" + Url
					+ "'");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			// connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod(method);
			// connection.setUseCaches (false);
			int httpResponseCode = connection.getResponseCode();
			String line;
			String response = "";
			if ((httpResponseCode == 200 || httpResponseCode == 302)
					&& !method.contentEquals("DELETE")) {
				InputStream responseStream = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						responseStream));
				while ((line = rd.readLine()) != null) {
					response += line;
				}
				try {
					jObject = new JSONObject(response);
					if (null != jObject) {
						return jObject;
					}
				} catch (JSONException e) {
					testConfig.logException(e);
				}
			} else if (!method.contentEquals("DELETE")) {
				try {
					// get error stream in case of error
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(
									(InputStream) connection.getErrorStream()));
					while ((line = rd.readLine()) != null) {
						response += line;
					}
					String message = StringUtils.substringBetween(response
							.toString().trim(), "<h1>", "</h1>");
					String description = StringUtils.substringBetween(response
							.toString().trim(), "description</b> <u>", "</u>");
					String exception = message + "\n\n" + description;
					testConfig.logFail(exception);
				} catch (Exception e) {
					testConfig.logException(e);
				}
			}
			connection.disconnect();
		} catch (MalformedURLException e) {
			testConfig.logException(e);
		} catch (IOException e) {
			testConfig.logException(e);
		}
		testConfig.endExecutionOnfailure = false;
		return jObject;

	}

	public static JSONObject ExecutePaisaAuthLoginReleasePayment(
			Config testConfig, TestDataReader data, int webServiceRow,
			String paymentStatus) {
		JSONObject jsonobject = null;
		testConfig.endExecutionOnfailure = true;
		String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "BaseUrl").trim());
		String command = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Command").trim());
		String Url = null;

		Url = baseUrl + command;
		final DefaultHttpClient client = new DefaultHttpClient();
		client.setRedirectStrategy(new DefaultRedirectStrategy() {
			public boolean isRedirected(HttpRequest request,
					HttpResponse response, HttpContext context) {
				boolean isRedirect = false;
				try {
					isRedirect = super.isRedirected(request, response, context);
				} catch (ProtocolException e) {
					e.printStackTrace();
				}
				if (!isRedirect) {
					int responseCode = response.getStatusLine().getStatusCode();
					if (responseCode == 301 || responseCode == 302) {
						return true;
					}
				}
				return false;
			}
		});
		try {
			HttpPost httpPost = new HttpPost(Url);
			httpPost.addHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			System.out.println("executing request " + httpPost.getURI());
			// add parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("username", testConfig
					.getRunTimeProperty("Username")));
			postParameters.add(new BasicNameValuePair("password", testConfig
					.getRunTimeProperty("Password")));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			httpPost.setEntity(formEntity);
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(httpPost, responseHandler);
			List<Cookie> cookies = client.getCookieStore().getCookies();
			// maintain cookie to get access token
			client.setCookieStore(client.getCookieStore());
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
					testConfig.putRunTimeProperty("cookie", cookies.get(i)
							.toString());
				}
			}
			System.out.println("post response" + responseBody);
			String url = testConfig.getRunTimeProperty("authredirectURL");
			HttpGet httpget = new HttpGet(url);
			HttpContext context = new BasicHttpContext();
			HttpResponse response = client.execute(httpget, context);
			System.out.println("redirect url is"
					+ response.getFirstHeader("Location").getValue());
			String redirectUrl = response.getFirstHeader("Location").getValue();
			String accessToken = redirectUrl.split("=")[1].split("&")[0];
			testConfig.putRunTimeProperty("accessToken", accessToken);
			DefaultHttpClient client1 = new DefaultHttpClient();
			// maintain cookie to initiate payment
			client1.setCookieStore(client.getCookieStore());
			JSONObject TransObj = null;
			JSONObject resultObj = null;
			String sourceAmountMap = "{PAYU:45.0}";
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("sourceAmountMap",
					sourceAmountMap));

			HashMap<String, String> postHeaders = new HashMap<String, String>();
			postHeaders.put("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			postHeaders.put("Authorization",
					"Bearer " + testConfig.getRunTimeProperty("accessToken"));
			webServiceRow = 33;
			baseUrl = Helper.replaceArgumentsWithRunTimeProperties(testConfig,
					data.GetData(webServiceRow, "BaseUrl").trim());
			command = Helper.replaceArgumentsWithRunTimeProperties(testConfig,
					data.GetData(webServiceRow, "Command").trim());

			if (postParameters != null)
				Url = baseUrl + command;
			try {
				httpPost = new HttpPost(Url);
				// set request header
				for (Entry<String, String> entry : postHeaders.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					httpPost.addHeader(key, value);
				}
				System.out.println("executing request " + httpPost.getURI());
				if (postParameters != null) {
					formEntity = new UrlEncodedFormEntity(postParameters);
					httpPost.setEntity(formEntity);
				}
				// Create a response handler
				responseHandler = new BasicResponseHandler();
				responseBody = client1.execute(httpPost, responseHandler);
				System.out.println("Initiate payment response is ---> "
						+ responseBody);

				TransObj = new JSONObject(responseBody);
				// verify initiated message
				Helper.compareExcelEquals(testConfig, "initiated message",
						"Payment Initiated", TransObj.getString("message"));
				// verify status is initiated
				Helper.compareEquals(testConfig, "transaction status",
						"Initiated", TransObj.getJSONObject("result")
								.getJSONObject("payment").getString("status"));
				String sourceReferenceId = TransObj.getJSONObject("result")
						.getJSONObject("sourceMap").getJSONObject("PAYU")
						.getJSONObject("paymentTransaction")
						.getString("sourceReferenceId");
				testConfig.putRunTimeProperty("sourceReferenceId",
						sourceReferenceId);
				String paymentTransactionId = TransObj.getJSONObject("result")
						.getJSONObject("sourceMap").getJSONObject("PAYU")
						.getJSONObject("paymentTransaction")
						.getString("paymentTransactionId");
				testConfig.putRunTimeProperty("paymentTransactionId",
						paymentTransactionId);
				String Txnid = TransObj.getJSONObject("result")
						.getJSONObject("payment")
						.getString("paymentTransactionIds");
				testConfig.putRunTimeProperty("Txnid", Txnid);
				// update transaction status to success
				webServiceRow = 35;
				DefaultHttpClient client3 = new DefaultHttpClient();
				// maintain cookie for update transaction
				client3.setCookieStore(client1.getCookieStore());
				baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
						testConfig, data.GetData(webServiceRow, "BaseUrl")
								.trim());
				command = Helper.replaceArgumentsWithRunTimeProperties(
						testConfig, data.GetData(webServiceRow, "Command")
								.trim());

				Url = baseUrl + command;
				httpPost = new HttpPost(Url);
				// set request header
				for (Entry<String, String> entry : postHeaders.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					httpPost.addHeader(key, value);
				}
				// add parameters for update transaction
				// query database to get id of initiated transaction
				testConfig.putRunTimeProperty("merchantId", "5840");
				Map<String, String> map = DataBase.executeSelectQuery(
						testConfig, 1, 1, DatabaseType.Online);
				if (map != null) {
					String payUId = map.get("id");
					testConfig.putRunTimeProperty("payUId", payUId);
				}

				postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("status",
						paymentStatus));
				postParameters.add(new BasicNameValuePair("mode", "CC"));
				postParameters.add(new BasicNameValuePair("amount", "45.0"));
				postParameters.add(new BasicNameValuePair("txnid", testConfig
						.getRunTimeProperty("paymentTransactionId")));
				postParameters.add(new BasicNameValuePair("bank_ref_no",
						"refno"));
				postParameters.add(new BasicNameValuePair("mihpayid",
						testConfig.getRunTimeProperty("payUId")));

				formEntity = new UrlEncodedFormEntity(postParameters);
				httpPost.setEntity(formEntity);
				System.out.println("executing request " + httpPost.getURI());
				// Create a response handler
				responseHandler = new BasicResponseHandler();
				responseBody = client3.execute(httpPost, responseHandler);
				System.out.println("update transaction response is ---> "
						+ responseBody);
				TransObj = new JSONObject(responseBody);
				// verify update transaction message
				Helper.compareExcelEquals(testConfig,
						"payment updated message", "Payment Status Updated",
						TransObj.getString("message"));
				// verify status is set to paymentStatus
				Helper.compareEquals(testConfig, "transaction status",
						paymentStatus, TransObj.getJSONObject("result")
								.getString("status"));

				// release payment
				webServiceRow = 34;
				DefaultHttpClient client2 = new DefaultHttpClient();
				// maintain cookie to release payment
				client2.setCookieStore(client1.getCookieStore());
				baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
						testConfig, data.GetData(webServiceRow, "BaseUrl")
								.trim());
				command = Helper.replaceArgumentsWithRunTimeProperties(
						testConfig, data.GetData(webServiceRow, "Command")
								.trim());

				Url = baseUrl + command;
				httpPost = new HttpPost(Url);
				// set request header
				for (Entry<String, String> entry : postHeaders.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					httpPost.addHeader(key, value);
				}
				System.out.println("executing request " + httpPost.getURI());
				// Create a response handler
				responseHandler = new BasicResponseHandler();
				responseBody = client2.execute(httpPost, responseHandler);
				System.out.println("Release Payment response is ---> "
						+ responseBody);
				resultObj = new JSONObject(responseBody);
				// verify release transaction message
				Helper.compareExcelEquals(testConfig,
						"payment release message", "Result Is",
						resultObj.getString("message"));
				// verify status is release payment
				Helper.compareEquals(testConfig, "transaction status",
						"Release Payment", resultObj.getJSONObject("result")
								.getString("status"));

				if (null != resultObj) {
					return resultObj;
				}

			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			testConfig.endExecutionOnfailure = false;
			client.getConnectionManager().shutdown();
		}
		return jsonobject;
	}

	public static JSONObject ExecutePaisaAuthPOSTWebServices(Config testConfig,
			TestDataReader data, int webServiceRow,
			ArrayList<NameValuePair> postParameters,
			HashMap<String, String> postHeaders) {
		JSONObject jObject = null;
		testConfig.endExecutionOnfailure = true;
		String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "BaseUrl").trim());
		String command = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Command").trim());
		String parameters = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Parameters").trim());
		String Url = null;
		if (postParameters != null)
			Url = baseUrl + command;
		else
			Url = baseUrl + command + parameters;
		final DefaultHttpClient client = new DefaultHttpClient();

		try {
			HttpPost httpPost = new HttpPost(Url);
			// set request header
			for (Entry<String, String> entry : postHeaders.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				httpPost.addHeader(key, value);
			}
			System.out.println("executing request " + httpPost.getURI());
			if (postParameters != null) {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				httpPost.setEntity(formEntity);
			}
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(httpPost, responseHandler);
			System.out.println(responseBody);
			jObject = new JSONObject(responseBody);
			client.getConnectionManager().shutdown();
			if (null != jObject) {
				return jObject;
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			testConfig.logException(e);
		}

		testConfig.endExecutionOnfailure = false;
		return jObject;

	}

	public static PaisaPOSTAPIReturnValues<JSONObject, DefaultHttpClient> ExecutePaisaAuthPOSTWebServicesRet2Values(
			Config testConfig, TestDataReader data, int webServiceRow,
			ArrayList<NameValuePair> postParameters,
			HashMap<String, String> postHeaders) {
		JSONObject jObject = null;
		testConfig.endExecutionOnfailure = true;
		String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "BaseUrl").trim());
		String command = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Command").trim());
		String parameters = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Parameters").trim());

		String Url = null;
		if (postParameters != null)
			Url = baseUrl + command;
		else
			Url = baseUrl + command + parameters;
		final DefaultHttpClient client = new DefaultHttpClient();

		try {
			HttpPost httpPost = new HttpPost(Url);
			// set request header
			for (Entry<String, String> entry : postHeaders.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				httpPost.addHeader(key, value);
			}
			// httpPost.addHeader("Content-Type",
			// "application/x-www-form-urlencoded;charset=UTF-8");
			System.out.println("executing request " + httpPost.getURI());
			if (postParameters != null) {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				httpPost.setEntity(formEntity);
			}
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(httpPost, responseHandler);
			System.out.println(responseBody);
			jObject = new JSONObject(responseBody);
			client.getConnectionManager().shutdown();
			if (null != jObject) {
				return new PaisaPOSTAPIReturnValues<JSONObject, DefaultHttpClient>(
						jObject, client);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			testConfig.logException(e);
		}

		testConfig.endExecutionOnfailure = false;
		return new PaisaPOSTAPIReturnValues<JSONObject, DefaultHttpClient>(
				jObject, client);

	}

	public static JSONObject ExecutePaisaAuthPOSTWebServices(Config testConfig,
			TestDataReader data, int webServiceRow,
			ArrayList<NameValuePair> postParameters,
			HashMap<String, String> postHeaders, DefaultHttpClient loginClient) {
		JSONObject jObject = null;
		testConfig.endExecutionOnfailure = true;
		String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "BaseUrl").trim());
		String command = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Command").trim());
		String parameters = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Parameters").trim());

		String Url = null;
		if (postParameters != null)
			Url = baseUrl + command;
		else
			Url = baseUrl + command + parameters;
		final DefaultHttpClient client = new DefaultHttpClient();

		try {
			HttpPost httpPost = new HttpPost(Url);
			// set request header
			for (Entry<String, String> entry : postHeaders.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				httpPost.addHeader(key, value);
			}
			// maintain cookie from loginclient
			client.setCookieStore(loginClient.getCookieStore());
			// httpPost.addHeader("Content-Type",
			// "application/x-www-form-urlencoded;charset=UTF-8");
			System.out.println("executing request " + httpPost.getURI());
			if (postParameters != null) {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				httpPost.setEntity(formEntity);
			}
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(httpPost, responseHandler);
			System.out.println(responseBody);
			jObject = new JSONObject(responseBody);
			client.getConnectionManager().shutdown();
			if (null != jObject) {
				return jObject;
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			testConfig.logException(e);
		}

		testConfig.endExecutionOnfailure = false;
		return jObject;

	}

	public static JSONObject ExecutePaisaAuthGet(Config testConfig,TestDataReader data, int webServiceRow, String referrer,
			String authorization, ArrayList<NameValuePair> parameters) {
		
		DefaultHttpClient client = new DefaultHttpClient();
		JSONObject result = null;
		String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "BaseUrl").trim());
		String command = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Command").trim());
		URIBuilder urlBuilder = new URIBuilder().setScheme("http")
			    .setHost(baseUrl.split("//")[1])
			    .setPath(command);
		for(NameValuePair i : parameters)
		{
			String name = i.getName();
			String value = i.getValue();
			urlBuilder.setParameter(name, value);
		}
		
		
		URI url = null;
		try {
			url = urlBuilder.build();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		HttpGet httpget = new HttpGet(url);
		httpget.addHeader("Accept", "application/json");
		httpget.addHeader("Accept-Encoding", "gzip, deflate");
		httpget.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; rv:24.0) Gecko/20100101 Firefox/24.0");
		//httpget.addHeader("X-Requested-With", "XMLHttpRequest");
		httpget.addHeader("Referer", referrer);
		httpget.addHeader("Host", testConfig.getRunTimeProperty("Host"));
		httpget.addHeader("authorization", authorization);
		/*GetMethod getM = new GetMethod();
		getM.setURI(url);
		getM.setRequestHeader("Authorization", authorization);
		*/
		//HttpContext context = new BasicHttpContext();
		try {
			HttpResponse response = client.execute(httpget);
			StatusLine httpResponseCode = response.getStatusLine();
			int responseCode = httpResponseCode.getStatusCode();
			String line;
			String responseText = "";
			if (responseCode == 200) {
				InputStream responseStream = response.getEntity().getContent();
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						responseStream));
				while ((line = rd.readLine()) != null) {
					responseText += line;
				}
			}
			result = new JSONObject(responseText);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static DefaultHttpClient ExecutePaisaAuthLogin(Config testConfig,
			TestDataReader data, int webServiceRow, boolean redirectURL) {

		JSONObject jsonobject = null;
		testConfig.endExecutionOnfailure = true;
		String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "BaseUrl").trim());
		String command = Helper.replaceArgumentsWithRunTimeProperties(
				testConfig, data.GetData(webServiceRow, "Command").trim());
		String Url = null;

		Url = baseUrl + command;
		final DefaultHttpClient client = new DefaultHttpClient();
		client.setRedirectStrategy(new DefaultRedirectStrategy() {
			public boolean isRedirected(HttpRequest request,
					HttpResponse response, HttpContext context) {
				boolean isRedirect = false;
				try {
					isRedirect = super.isRedirected(request, response, context);
				} catch (ProtocolException e) {
					e.printStackTrace();
				}
				if (!isRedirect) {
					int responseCode = response.getStatusLine().getStatusCode();
					if (responseCode == 301 || responseCode == 302) {
						return true;
					}
				}
				return false;
			}
		});
		try {
			HttpPost httpPost = new HttpPost(Url);
			httpPost.addHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			System.out.println("executing request " + httpPost.getURI());
			// add parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("username", testConfig
					.getRunTimeProperty("Username")));
			postParameters.add(new BasicNameValuePair("password", testConfig
					.getRunTimeProperty("Password")));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			httpPost.setEntity(formEntity);
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(httpPost, responseHandler);
			List<Cookie> cookies = client.getCookieStore().getCookies();
			// maintain cookie to get access token
			client.setCookieStore(client.getCookieStore());
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
					testConfig.putRunTimeProperty("cookie", cookies.get(i)
							.toString());
				}
			}
			System.out.println("post response" + responseBody);
			String url = testConfig.getRunTimeProperty("authredirectURL");
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Accept", "application/json");
			httpget.addHeader("Accept-Encoding", "gzip, deflate");
			httpget.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; rv:24.0) Gecko/20100101 Firefox/24.0");
			httpget.addHeader("X-Requested-With", "XMLHttpRequest");
			httpget.addHeader("Referer", "https://www.payumoney.com/");
			httpget.addHeader("Host", "www.payumoney.com");
			HttpContext context = new BasicHttpContext();
			// HttpResponse response = client.execute(httpget, context);
			HttpResponse response = client.execute(httpget);

			/*
			 * System.out.println("redirect url is" +
			 * response.getFirstHeader("Location").getValue()); String
			 * redirectUrl = response.getFirstHeader("Location").getValue();
			 */
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String result = rd.readLine();
			System.out.println("GET call response is " + result.toString());

			String accessToken = result.split(",")[0].split(":")[1];
			testConfig.putRunTimeProperty("accessToken",
					accessToken.split("\"")[1].split("\"")[0]);
			System.out.println("GET call response is " + result.toString());

			// String accessToken = redirectUrl.split("=")[1].split("&")[0];

			// testConfig.putRunTimeProperty("accessToken", accessToken);

			jsonobject = new JSONObject(responseBody);
			if (null != jsonobject) {
				try {
					if ((Integer) jsonobject.get("status") == 0) {
						testConfig.putRunTimeProperty("userId", jsonobject
								.getJSONObject("result").get("adminMerchantId")
								.toString().trim());

					} else {
						testConfig.logFail("Error : "
								+ jsonobject.get("message").toString());
					}
				} catch (JSONException e) {
					testConfig.logException(e);
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			testConfig.logException(e);
		}

		testConfig.endExecutionOnfailure = false;
		client.getConnectionManager().shutdown();
		return client;

	}

	/*
	 * public static JSONObject ExecutePaisaWebService(Config testConfig,
	 * TestDataReader data, int webServiceRow) { String comments =
	 * data.GetData(webServiceRow, "Comments").trim().toLowerCase(); String
	 * method = data.GetData(webServiceRow, "Method").trim().toLowerCase();
	 * String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(testConfig,
	 * data.GetData(webServiceRow, "BaseUrl").trim()); String command =
	 * Helper.replaceArgumentsWithRunTimeProperties(testConfig,
	 * data.GetData(webServiceRow, "Command").trim()); String parameters =
	 * Helper.replaceArgumentsWithRunTimeProperties(testConfig,
	 * data.GetData(webServiceRow, "Parameters").trim());
	 * 
	 * String url = null;
	 * 
	 * url = baseUrl + command + parameters;
	 * 
	 * ClientResource resource = new ClientResource(url); Representation entity
	 * = null;
	 * 
	 * testConfig.logComment("Executing '" + comments + "' on '" + url + "'");
	 * 
	 * switch(method) { case "get": entity =
	 * resource.get(MediaType.APPLICATION_JSON); break; case "post": entity =
	 * resource.post(null,MediaType.APPLICATION_JSON); break; }
	 * 
	 * JsonRepresentation represent = null; try { represent = new
	 * JsonRepresentation(entity); } catch (IOException e) {
	 * testConfig.logFail(e.toString()); }
	 * 
	 * JSONObject jObject = null; try { jObject = represent.getJsonObject(); }
	 * catch (JSONException e1) { testConfig.logFail(e1.toString()); }
	 * 
	 * return jObject; }
	 */
	public static Object ExecuteProductWebService(Config testConfig,
			TestDataReader transactionData, int transactionRow,
			TestDataReader webServiceData, int webServiceRow) {
		Object returnOb = null;
		try {
			System.setProperty("https.protocols", "SSLv3");
			String comments = webServiceData.GetData(webServiceRow, "Comments");
			String method = webServiceData.GetData(webServiceRow, "Method")
					.toLowerCase();
			String baseUrl = Helper.replaceArgumentsWithRunTimeProperties(
					testConfig, webServiceData
							.GetData(webServiceRow, "BaseUrl").trim());
			String command = webServiceData.GetData(webServiceRow, "Command");
			String parameters = Helper.replaceArgumentsWithRunTimeProperties(
					testConfig,
					webServiceData.GetData(webServiceRow, "Parameters").trim());

			// Set the base URL of request
			URL url = new URL(baseUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			switch (method) {
			case "get":
				con.setRequestMethod("GET");
				break;
			case "post":
				con.setRequestMethod("POST");
				break;
			}
			con.setDoOutput(true);
			PrintStream ps = new PrintStream(con.getOutputStream());

			String key = transactionData.GetCurrentEnvironmentData(
					transactionRow, "key");
			String salt = transactionData.GetCurrentEnvironmentData(
					transactionRow, "salt");

			// Compute Hash
			String hashInput = key + "|" + command;
			String[] params = parameters.split("&");
			String varValue = "";
			for (String param : params) {
				varValue = param.split("=")[1];
				hashInput = hashInput + "|" + varValue;
			}
			hashInput = hashInput + "|" + salt;
			String hash = GetHash(testConfig, hashInput);

			String request = "hash=" + hash + "&key=" + key + "&command="
					+ command + "&" + parameters;
			ps.print(request);
			ps.close();

			// Execute the WebService
			testConfig.logComment("Executing '" + comments
					+ "' web service - '" + baseUrl + "?" + request + "'");
			con.connect();
			if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String output = "";
				String line;
				while ((line = br.readLine()) != null) {
					output = output + line;
				}
				br.close();

				// convert PHP response to Java Object
				SerializedPhpParser parser = new SerializedPhpParser(output);
				returnOb = parser.parse();
			}
			con.disconnect();
		} catch (Exception e) {
			testConfig.logException(e);
		}

		return returnOb;
	}

	public static String GetHash(Config testConfig, String inputString) {
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] mdbytes = md.digest(inputString.getBytes());
			// convert the byte to hex format method
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			hash = sb.toString();
			testConfig.logComment("'" + inputString + "' hash is-" + hash);
		} catch (Exception e) {
			testConfig.logException(e);
		}
		return hash;
	}
}
