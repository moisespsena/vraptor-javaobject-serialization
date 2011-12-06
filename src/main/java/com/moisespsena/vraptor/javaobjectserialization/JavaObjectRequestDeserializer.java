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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.ResourceClass;
import br.com.caelum.vraptor.resource.ResourceMethod;

import com.moisespsena.vraptor.advancedrequest.RequestMethodInfo;
import com.moisespsena.vraptor.advancedrequest.RequestMethodInfoStatic;
import com.moisespsena.vraptor.advancedrequest.ResourceMethodRequest;
import com.moisespsena.vraptor.advancedrequest.ResourceNotFoundException;
import com.moisespsena.vraptor.advancedrequest.ResourcesResolver;

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 29/08/2011
 */
public class JavaObjectRequestDeserializer {
	private static final String DESERIALIZED_OBJECT = JavaObjectRequestDeserializer.class
			.getName() + "_DESERIALIZED_OBJECT";
	private final ResourcesResolver resourcesResolver;
	private final HttpServletRequest servletRequest;

	public JavaObjectRequestDeserializer(
			final HttpServletRequest servletRequest,
			final ResourcesResolver resourcesResolver) {
		this.servletRequest = servletRequest;
		this.resourcesResolver = resourcesResolver;
	}

	public Object deserialize() {
		final Object attr = servletRequest.getAttribute(DESERIALIZED_OBJECT);
		Object object = null;
		if (attr == null) {
			InputStream in;
			try {
				in = servletRequest.getInputStream();
				final ObjectInputStream oin = new ObjectInputStream(in);
				object = oin.readObject();
				servletRequest.setAttribute(DESERIALIZED_OBJECT, object);

				return object;
			} catch (final IOException e) {
				throw new JavaObjectRequestDeserializerException(e);
			} catch (final ClassNotFoundException e) {
				throw new JavaObjectRequestDeserializerException(e);
			}
		} else {
			return attr;
		}
	}

	public ResourceMethod deserializeResourceMethod()
			throws ResourceNotFoundException {
		final Object requestObject = deserialize();

		if ((requestObject == null)
				|| !(requestObject instanceof ResourceMethodRequest)) {
			return null;
		}

		final ResourceMethodRequest resourceMethodRequest = (ResourceMethodRequest) requestObject;

		final Class<?> type = resourcesResolver
				.resourceClassFromName(resourceMethodRequest.getResourceName());
		final Class<?>[] paramsTypes = resourcesResolver.typesFromMethodName(
				type, resourceMethodRequest.getMethodName());
		final String methodName = resourceMethodRequest.getMethodName();

		final Method method = new Mirror().on(type).reflect()
				.method(methodName).withArgs(paramsTypes);
		final ResourceClass resourceClass = new DefaultResourceClass(type);
		final ResourceMethod resourceMethod = new DefaultResourceMethod(
				resourceClass, method);

		final RequestMethodInfo requestMethodInfo = new RequestMethodInfo(type,
				method, resourceMethodRequest.getParameters(), false);
		RequestMethodInfoStatic.hydatesRequest(servletRequest,
				requestMethodInfo);

		return resourceMethod;
	}

	public Object[] getResourceMethodParameters() {
		final Object object = deserialize();

		if ((object == null) || !(object instanceof ResourceMethodRequest)) {
			return null;
		}

		final ResourceMethodRequest resourceMethodRequest = (ResourceMethodRequest) object;

		final Object[] parameters = resourceMethodRequest.getParameters();
		return parameters;
	}
}
