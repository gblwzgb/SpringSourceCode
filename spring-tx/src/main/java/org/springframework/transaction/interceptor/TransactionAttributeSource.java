/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;

import org.springframework.lang.Nullable;

/**
 * TransactionInterceptor用于元数据检索的策略接口。
 *
 * 实现知道如何从配置，源级别的元数据属性（例如Java 5批注）或其他任何位置获取事务属性。
 */

/**
 * Strategy interface used by {@link TransactionInterceptor} for metadata retrieval.
 *
 * <p>Implementations know how to source transaction attributes, whether from configuration,
 * metadata attributes at source level (such as Java 5 annotations), or anywhere else.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 15.04.2003
 * @see TransactionInterceptor#setTransactionAttributeSource
 * @see TransactionProxyFactoryBean#setTransactionAttributeSource
 * @see org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
 */
public interface TransactionAttributeSource {

	/**
	 * 确定给定的类是否是此TransactionAttributeSource的元数据格式的事务属性的候选项。
	 * 如果此方法返回false，则不会遍历给定类上的方法进行getTransactionAttribute自省。
	 * 因此，返回false是对不受影响的类的优化，而返回true只是意味着该类需要对给定类上的每个方法分别进行全面自省。
	 */
	/**
	 * Determine whether the given class is a candidate for transaction attributes
	 * in the metadata format of this {@code TransactionAttributeSource}.
	 * <p>If this method returns {@code false}, the methods on the given class
	 * will not get traversed for {@link #getTransactionAttribute} introspection.
	 * Returning {@code false} is therefore an optimization for non-affected
	 * classes, whereas {@code true} simply means that the class needs to get
	 * fully introspected for each method on the given class individually.
	 * @param targetClass the class to introspect
	 * @return {@code false} if the class is known to have no transaction
	 * attributes at class or method level; {@code true} otherwise. The default
	 * implementation returns {@code true}, leading to regular introspection.
	 * @since 5.2
	 */
	default boolean isCandidateClass(Class<?> targetClass) {
		return true;
	}

	/**
	 * 返回给定方法的事务属性，如果该方法是非事务性的，则返回null。
	 *
	 * @param method 要内省的方法
	 * @param targetClass 目标类（可以为null，在这种情况下，必须使用方法的声明类）
	 * @return 匹配的事务属性；如果找不到，则为null
	 */
	/**
	 * Return the transaction attribute for the given method,
	 * or {@code null} if the method is non-transactional.
	 * @param method the method to introspect
	 * @param targetClass the target class (may be {@code null},
	 * in which case the declaring class of the method must be used)
	 * @return the matching transaction attribute, or {@code null} if none found
	 */
	@Nullable
	TransactionAttribute getTransactionAttribute(Method method, @Nullable Class<?> targetClass);

}
