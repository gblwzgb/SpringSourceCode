/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.core.env;

/**
 * Interface indicating a component that contains and exposes an {@link Environment} reference.
 *
 * <p>All Spring application contexts are EnvironmentCapable, and the interface is used primarily
 * for performing {@code instanceof} checks in framework methods that accept BeanFactory
 * instances that may or may not actually be ApplicationContext instances in order to interact
 * with the environment if indeed it is available.
 *
 * <p>As mentioned, {@link org.springframework.context.ApplicationContext ApplicationContext}
 * extends EnvironmentCapable, and thus exposes a {@link #getEnvironment()} method; however,
 * {@link org.springframework.context.ConfigurableApplicationContext ConfigurableApplicationContext}
 * redefines {@link org.springframework.context.ConfigurableApplicationContext#getEnvironment
 * getEnvironment()} and narrows the signature to return a {@link ConfigurableEnvironment}.
 * The effect is that an Environment object is 'read-only' until it is being accessed from
 * a ConfigurableApplicationContext, at which point it too may be configured.
 *
 * @author Chris Beams
 * @since 3.1
 * @see Environment
 * @see ConfigurableEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment()
 */

/**
 * 表示包含并暴露Environment引用的组件的接口。
 *
 * 所有Spring应用程序上下文都具有EnvironmentCapable功能，
 * 并且该接口主要用于在接受BeanFactory实例的框架方法中执行instanceof检查，
 * 以便可以与environment进行交互（如果实际上是ApplicationContext实例）。
 *
 * 如前所述，ApplicationContext扩展了EnvironmentCapable，从而公开了getEnvironment()方法；
 * 但是，ConfigurableApplicationContext重新定义了getEnvironment()并缩小了签名范围，以返回ConfigurableEnvironment。
 * 结果是环境对象是“只读的”，直到从ConfigurableApplicationContext访问它为止，此时也可以对其进行配置。
 */
// 就是说实现这个接口的组件，有包含并暴露Environment引用的能力。通过getEnvironment()方法来暴露引用。
public interface EnvironmentCapable {

	/**
	 * Return the {@link Environment} associated with this component.
	 */
	Environment getEnvironment();

}
