/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.beans.factory;

/**
 * A marker superinterface indicating that a bean is eligible to be notified by the
 * Spring container of a particular framework object through a callback-style method.
 * The actual method signature is determined by individual subinterfaces but should
 * typically consist of just one void-returning method that accepts a single argument.
 *
 * <p>Note that merely implementing {@link Aware} provides no default functionality.
 * Rather, processing must be done explicitly, for example in a
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}.
 * Refer to {@link org.springframework.context.support.ApplicationContextAwareProcessor}
 * for an example of processing specific {@code *Aware} interface callbacks.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 */

/**
 * 标记超级接口，用于指示bean有资格通过回调机制让Spring容器通知特定框架对象。
 * 实际的方法签名由各个子接口确定，但通常应仅由"一个！"接受单个参数的返回void的方法组成。
 *
 * 请注意，仅实现Aware不会提供默认功能。
 * 相反，必须显式完成处理，例如在org.springframework.beans.factory.config.BeanPostProcessor中。
 * 有关处理特定*Aware接口回调的示例，请参考org.springframework.context.support.ApplicationContextAwareProcessor。
 */
// 提供了一种能力，告诉Spring，我需要XXX回调，比如我需要感知到我的beanName，
// 我就实现一个BeanNameAware，那么Spring就会来调用你实现setBeanName()方法，来把beanName传递给你。
// 再比如我们经常实现一个ApplicationContextAware接口，就可以拿到Spring的上下文了。
// 不过这样的话，项目代码就和Spring强耦合起来了，问题不大。
public interface Aware {

}
