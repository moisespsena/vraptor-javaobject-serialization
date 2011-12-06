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

import br.com.caelum.vraptor.Lazy;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

import com.moisespsena.vraptor.advancedrequest.ResourceNotFoundException;
import com.moisespsena.vraptor.listenerexecution.ExecutionStack;
import com.moisespsena.vraptor.listenerexecution.ExecutionStackException;
import com.moisespsena.vraptor.listenerexecution.topological.ListenerOrder;
import com.moisespsena.vraptor.resourcetranslator.listener.ResourceTranslate;
import com.moisespsena.vraptor.resourcetranslator.listener.ResourceTranslateListener;
import com.moisespsena.vraptor.resourcetranslator.listener.ResourceTranslateListenerExecutor;

/**
 * @author Moises P. Sena (http://moisespsena.com)
 * @since 1.0 29/08/2011
 */
@Lazy
@Component
@ListenerOrder
@ApplicationScoped
@ResourceTranslate
public class JavaObjectResourceTranslatorListener implements
		ResourceTranslateListener {
	@Override
	public boolean accepts(final ResourceTranslateListenerExecutor executor) {
		if (JavaObjectRequestStatic.isValid(executor.getRequestInfo()
				.getRequest())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ResourceMethod translate(
			final ExecutionStack<ResourceTranslateListener> stack,
			final ResourceTranslateListenerExecutor executor)
			throws ExecutionStackException {
		final JavaObjectRequestDeserializer deserializer = new JavaObjectRequestDeserializer(
				executor.getRequestInfo().getRequest(),
				executor.getResourcesResolver());
		ResourceMethod resourceMethod;
		try {
			resourceMethod = deserializer.deserializeResourceMethod();
		} catch (final ResourceNotFoundException e) {
			throw new ExecutionStackException(e);
		}
		return resourceMethod;
	}
}