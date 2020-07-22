/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springframework.aop.aspectj;

import org.springframework.aop.PointcutAdvisor;

/**
 * 由Spring AOP Advisors实现的接口，其中包装了可能具有惰性初始化策略的AspectJ切面。
 * 例如，perThis实例化模型将意味着advice的延迟初始化。
 */

/**
 * Interface to be implemented by Spring AOP Advisors wrapping AspectJ
 * aspects that may have a lazy initialization strategy. For example,
 * a perThis instantiation model would mean lazy initialization of the advice.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 */
public interface InstantiationModelAwarePointcutAdvisor extends PointcutAdvisor {

	/**
	 * 返回此advisor是否正在延迟初始化其底层advice。
	 */
	/**
	 * Return whether this advisor is lazily initializing its underlying advice.
	 */
	boolean isLazy();

	/**
	 * 返回此advisor是否已实例化其advice。
	 */
	/**
	 * Return whether this advisor has already instantiated its advice.
	 */
	boolean isAdviceInstantiated();

}
