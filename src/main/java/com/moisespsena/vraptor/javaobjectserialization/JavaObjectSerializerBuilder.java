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
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.commons.lang.SerializationException;

import br.com.caelum.vraptor.serialization.SerializerBuilder;

/**
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 1.0 25/08/2011
 * 
 */
public class JavaObjectSerializerBuilder implements SerializerBuilder {
	private final OutputStream outputStream;
	private Object source;

	public JavaObjectSerializerBuilder(final OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.caelum.vraptor.serialization.Serializer#exclude(java.lang.String
	 * [])
	 */
	@Override
	public JavaObjectSerializerBuilder exclude(final String... names) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.caelum.vraptor.serialization.SerializerBuilder#from(java.lang.
	 * Object)
	 */
	@Override
	public <T> JavaObjectSerializerBuilder from(final T object) {
		source = object;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.caelum.vraptor.serialization.SerializerBuilder#from(java.lang.
	 * Object, java.lang.String)
	 */
	@Override
	public <T> JavaObjectSerializerBuilder from(final T object,
			final String alias) {
		return from(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.caelum.vraptor.serialization.Serializer#include(java.lang.String
	 * [])
	 */
	@Override
	public JavaObjectSerializerBuilder include(final String... names) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.vraptor.serialization.Serializer#recursive()
	 */
	@Override
	public JavaObjectSerializerBuilder recursive() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.vraptor.serialization.Serializer#serialize()
	 */
	@Override
	public void serialize() {
		ObjectOutputStream out = null;

		try {
			if (outputStream instanceof ObjectOutputStream) {
				out = (ObjectOutputStream) outputStream;
			} else {
				out = new ObjectOutputStream(outputStream);
			}

			out.writeObject(source);
			out.flush();
		} catch (final IOException e) {
			throw new SerializationException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					throw new SerializationException(e);
				}
			}
		}
	}

}
