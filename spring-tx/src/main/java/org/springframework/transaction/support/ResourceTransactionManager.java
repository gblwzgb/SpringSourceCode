/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.transaction.support;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * Extension of the {@link org.springframework.transaction.PlatformTransactionManager}
 * interface, indicating a native resource transaction manager, operating on a single
 * target resource. Such transaction managers differ from JTA transaction managers in
 * that they do not use XA transaction enlistment for an open number of resources but
 * rather focus on leveraging the native power and simplicity of a single target resource.
 *
 * <p>This interface is mainly used for abstract introspection of a transaction manager,
 * giving clients a hint on what kind of transaction manager they have been given
 * and on what concrete resource the transaction manager is operating on.
 *
 * @author Juergen Hoeller
 * @since 2.0.4
 * @see TransactionSynchronizationManager
 */
public interface ResourceTransactionManager extends PlatformTransactionManager {

	/*
	 * 返回此事务管理器操作的资源工厂，例如 JDBC DataSource 或 JMS ConnectionFactory。
	 *
	 * 这个返回值 Object，一般来说会绑定在 TransactionSynchronizationManager 的 ThreadLocal 里，作为key。
	 */
	/**
	 * Return the resource factory that this transaction manager operates on,
	 * e.g. a JDBC DataSource or a JMS ConnectionFactory.
	 * <p>This target resource factory is usually used as resource key for
	 * {@link TransactionSynchronizationManager}'s resource bindings per thread.
	 * @return the target resource factory (never {@code null})
	 * @see TransactionSynchronizationManager#bindResource
	 * @see TransactionSynchronizationManager#getResource
	 */
	Object getResourceFactory();

}
