/*
 * Copyright 2010 Vladimir Ritz Bossicard.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Author      : Vladimir Ritz Bossicard
 * Version     : $Revision: 370 $
 * Last edit   : $Date: 2010-03-09 19:14:13 +0100 (Tue, 09 Mar 2010) $
 * Last editor : $Author: vbossica $
 */
package org.workingonit.gwtbridge;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Specific {@link BeanPostProcessor} that registers the beans implementing the
 * {@link RemoteService} interface. The mapping between the GWT url and the
 * Spring bean is defined by the {@link GwtRemoteService} annotation that must
 * be defined by the implementing class.
 * 
 * @author Vladimir Ritz Bossicard
 */
public class GwtServicesRegistrar implements BeanPostProcessor, BeanFactoryAware {

    private final static Log logger = LogFactory.getLog(GwtServicesRegistrar.class);
    
    /**
     * Contains the mapping between the {@link GwtRemoteService}
     * annotation and the name of the beans. Note that the beans are not cached.
     */
    private Map<String, String> serviceNames = new HashMap<String, String>();

    private ListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    /**
     * Retrieves the {@link RemoteService} that matches the given path.
     */
    public RemoteService retrieveGwtService(String path) {
        if (!serviceNames.containsKey(path)) {
            logger.error("GWT service named '" + path + "' is not registered");
            throw new IllegalArgumentException("GWT service named '" + path + "' is not registered");
        }
        return this.beanFactory.getBean(this.serviceNames.get(path), RemoteService.class);
    }

    /**
     * Registers all beans implementing the {@link RemoteService} interface and
     * looks for value of their respective {@link GwtRemoteService}
     * annotation.
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof RemoteService) {
            if (logger.isInfoEnabled()) {
                logger.info("found bean '" + beanName + "'");
            }
            GwtRemoteService annotation = this.beanFactory.findAnnotationOnBean(beanName, GwtRemoteService.class);
            if (annotation != null) {
	            String path = annotation.value();
	            if (serviceNames.containsKey(path)) {
	            	logger.warn("A GWT service with path '" + path + "' was already registered. The bean '" + beanName + "' is ignored!");
	            } else {
	            	if (logger.isInfoEnabled()) {
	            		logger.info("Registering bean '" + beanName + "' for the path '" + path + "'");
	            	}
	            	serviceNames.put(path, beanName);
	            }
            }
        }
        return bean;
    }

    /**
     * Does nothing.
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

}