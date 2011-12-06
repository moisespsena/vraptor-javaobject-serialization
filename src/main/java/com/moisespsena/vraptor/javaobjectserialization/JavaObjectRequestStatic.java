/***
 * Copyright (c) 2011 Moises P. Sena - www.moisespsena.com
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package com.moisespsena.vraptor.javaobjectserialization;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;

import com.moisespsena.vraptor.advancedrequest.AdvancedRequestInfo;

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 29/08/2011
 */
public class JavaObjectRequestStatic {
	public static String controllerFromRequest(final HttpServletRequest request) {
		final String param = request.getParameter("controller");

		return param;
	}

	public static void hydateUrlConnection(
			final HttpURLConnection httpURLConnection) {
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setDefaultUseCaches(false);

		httpURLConnection.setRequestProperty("Content-Type",
				AdvancedRequestInfo.CT_JAVA_OBJECT);
		httpURLConnection.setRequestProperty("Accept",
				AdvancedRequestInfo.CT_JAVA_OBJECT);
	}

	public static String hydrateContextPathURL(String contextPathURL) {
		final char lastChar = contextPathURL
				.charAt(contextPathURL.length() - 1);
		if (lastChar != '/') {
			contextPathURL += '/';
		}
		contextPathURL += "_JO_" + System.currentTimeMillis();
		return contextPathURL;
	}

	public static boolean isValid(final HttpServletRequest request) {
		final String contentType = request.getContentType();
		return isValid(contentType);
	}

	public static boolean isValid(final String contentType) {
		if ((contentType != null)
				&& contentType.contains(AdvancedRequestInfo.CT_JAVA_OBJECT)) {
			return true;
		}
		return false;
	}

	public static String methodNameFromRequest(final HttpServletRequest request) {
		final String param = request.getParameter("method");

		return param;
	}
}
