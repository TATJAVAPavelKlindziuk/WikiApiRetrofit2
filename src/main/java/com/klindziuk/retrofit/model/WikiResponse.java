package com.klindziuk.retrofit.model;

import java.util.List;
import java.util.Map;

public class WikiResponse {
	private static final String SEPARATOR = System.getProperty("line.separator");
	private int code;
	private String body;
	private String message;
	private Map<String, List<String>> headers;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + code;
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WikiResponse other = (WikiResponse) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (code != other.code)
			return false;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RESPONSE " + this.hashCode() + SEPARATOR + "[ code=" + code + " ]" + SEPARATOR + "[ body=" + body
				+ " ]" + SEPARATOR + "[ message=" + message + " ]" + SEPARATOR + "[ headers=" + headers + "]"
				+ SEPARATOR + "END OF RESPONSE "+ this.hashCode() + SEPARATOR;
	}
}
