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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.SerializationException;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.serialization.Serialization;

/**
 * Query String Serialization
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 1.0 23/08/2011
 * 
 */
@Component
@RequestScoped
public class JavaObjectSerialization implements Serialization {
	private final HttpServletResponse response;

	public JavaObjectSerialization(final HttpServletResponse response) {
		this.response = response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.caelum.vraptor.serialization.Serialization#accepts(java.lang.String
	 * )
	 */
	@Override
	public boolean accepts(final String format) {
		if ((format != null)
				&& format.contains("application/x-java-serialized-object")) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.caelum.vraptor.serialization.NoRootSerialization#from(java.lang
	 * .Object)
	 */
	@Override
	public <T> JavaObjectSerializerBuilder from(final T object) {
		try {
			response.setContentType("application/x-java-serialized-object");
			return new JavaObjectSerializerBuilder(response.getOutputStream())
					.from(object);
		} catch (final IOException e) {
			throw new SerializationException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.caelum.vraptor.serialization.Serialization#from(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public <T> JavaObjectSerializerBuilder from(final T object,
			final String alias) {
		try {
			return new JavaObjectSerializerBuilder(response.getOutputStream())
					.from(object, alias);
		} catch (final IOException e) {
			throw new SerializationException(e);
		}
	}
}
