package com.worldpay.utils;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

	/**
	 * 发送get请求
	 *
	 * @throws Exception
	 */
	public static String get(String url) {

		String result = "";
		InputStream in = null;
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("GET");
			// 建立实际的连接
			conn.connect();
			// 定义输入流来读取URL的响应
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 发送post请求
	 *
	 * @throws Exception
	 */
	public static String post(String url, String paramStr) {
		InputStream in = null;
		OutputStream os = null;
		String result = "";
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			// 发送POST请求须设置
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			os = conn.getOutputStream();
			// 注意编码格式，防止中文乱码
			if (StringUtils.hasText(paramStr)) {
				os.write(paramStr.getBytes("utf-8"));
				os.close();
			}
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	
	/**
	 * 发送ems post请求
	 *
	 * @throws Exception
	 */
	public static String emsPost(String url, String paramStr,String apiKey) {
		InputStream in = null;
		OutputStream os = null;
		String result = "";
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("17token", apiKey);
			// 发送POST请求须设置
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			os = conn.getOutputStream();
			// 注意编码格式，防止中文乱码
			if (StringUtils.hasText(paramStr)) {
				os.write(paramStr.getBytes("utf-8"));
				os.close();
			}
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param out
	 */
	public static void getImage(String url, OutputStream out) {
		InputStream in = null;
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "image/jpeg");
			conn.setRequestProperty("Accept", "aimage/jpeg");
			conn.setRequestMethod("GET");
			// 建立实际的连接
			conn.connect();
			// 定义输入流来读取URL的响应
			in = conn.getInputStream();
			StreamUtils.copy(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 发送post请求
	 *
	 * @throws Exception
	 */
	public static String sendPost(String url, String paramStr) {
		InputStream in = null;
		OutputStream os = null;
		String result = "";
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 compatible; MSIE 5.0;Windows NT; DigExt)");// （主要是这一句）
//			conn.setRequestProperty("Accept", "application/json");
			// 发送POST请求须设置
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			os = conn.getOutputStream();
			// 注意编码格式，防止中文乱码
			if (StringUtils.hasText(paramStr)) {
				os.write(paramStr.getBytes("utf-8"));
				os.close();
			}
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// post请求
	public static String sendPostRequest(String url, String jsonParam) {
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;

		try {
			// 打开连接
			conn = (HttpURLConnection) new URL(url).openConnection();

			// 设置请求属性
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			// 将 JSON 数据写入请求体
			try (OutputStream os = conn.getOutputStream()) {
				os.write(jsonParam.getBytes(StandardCharsets.UTF_8));
			}

			// 读取响应
			try (var in = conn.getInputStream();
					var reader = new java.io.BufferedReader(
							new java.io.InputStreamReader(in, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return result.toString();
	}
	
	
	/**
	 * 发送post请求
	 *
	 * @throws Exception
	 */
	public static String postRobot(String url, String paramStr) {
		InputStream in = null;
		OutputStream os = null;
		String result = "";
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			// 发送POST请求须设置
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			os = conn.getOutputStream();
			// 注意编码格式，防止中文乱码
			if (StringUtils.hasText(paramStr)) {
				os.write(paramStr.getBytes("utf-8"));
				os.close();
			}
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 使用原生 HttpURLConnection 发送 POST 请求
	 *
	 * @param urlStr  请求地址
	 * @param headers 请求头
	 * @param body    请求体
	 * @return 响应结果
	 */
	public static String doPostNative(String urlStr, Map<String, String> headers, String body) {
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		DataOutputStream outputStream = null;
		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();

			// 1. 基础配置
			connection.setRequestMethod("POST");
			// 允许写入
			connection.setDoOutput(true);
			// 允许读取
			connection.setDoInput(true);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setUseCaches(false);

			// 2. 设置请求头
			if (headers != null && !headers.isEmpty()) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			// 默认 Content-Type
			if (!connection.getRequestProperties().containsKey("Content-Type")) {
				connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			}

			// 3. 发送请求体
			if (body != null && !body.isEmpty()) {
				byte[] data = body.getBytes(StandardCharsets.UTF_8);
				connection.setRequestProperty("Content-Length", String.valueOf(data.length));
				outputStream = new DataOutputStream(connection.getOutputStream());
				outputStream.write(data);
				outputStream.flush();
			}

			// 4. 获取响应
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
				String line;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
			} else {
				// 错误流处理
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
				String line;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				System.err.println("POST request failed with status: " + responseCode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5. 关闭资源
			try {
				if (outputStream != null) outputStream.close();
				if (reader != null) reader.close();
				if (connection != null) connection.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result.toString();
	}


	
	public static void main(String[] args) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("apikey","27ed9cb2f4b36e5dd1a311d691c81f9c");
		JSONObject params = new JSONObject();
		params.put("userid", "rz40012");
		String response = HttpUtil.doPostNative("http://154.82.73.134:8687/merchant/makesign", headers, params.toString());
		System.out.println(response);
		JSONObject jsonObject = JSONObject.parseObject(response);
		String code = jsonObject.getString("code");
		if (code.equals("1")) {
			System.out.println(jsonObject.getJSONObject("data").get("sign"));
		}
		System.out.println(response);
	}

}