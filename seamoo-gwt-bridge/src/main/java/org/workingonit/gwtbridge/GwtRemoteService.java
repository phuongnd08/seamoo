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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that must be used alongside the official one because of the
 * problem described in the GWT issue #3803.<p>
 * 
 * This annotation will be deleted as soon as a new GWT version fixing the above
 * mentioned issue has been released.
 * 
 * @author Vladimir Ritz Bossicard
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GwtRemoteService {

	String value();

}
