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

package org.springframework.transaction.annotation;

import org.springframework.transaction.TransactionManager;

/**
 * 与@EnableTransactionManagement一起注解的@Configuration类要实现的接口，
 * 该类希望（或需要）显式指定用于注释驱动的事务管理的默认PlatformTransactionManager bean（或ReactiveTransactionManager bean），
 * 而不是by的默认方法。类型的查询。可能有必要的一个原因是，如果容器中存在两个PlatformTransactionManager bean。
 *
 * 有关一般示例和上下文，请参见@EnableTransactionManagement。有关详细说明，请参见注解DrivenTransactionManager()。
 *
 * 请注意，在按类型查找消除歧义的情况下，
 * 实现此接口的另一种方法是简单地将有问题的PlatformTransactionManager @Bean方法之一标记为@Primary。
 * 这甚至通常是首选，因为它不会导致PlatformTransactionManager bean的早期初始化。
 */

/**
 * Interface to be implemented by @{@link org.springframework.context.annotation.Configuration
 * Configuration} classes annotated with @{@link EnableTransactionManagement} that wish to
 * (or need to) explicitly specify the default {@code PlatformTransactionManager} bean
 * (or {@code ReactiveTransactionManager} bean) to be used for annotation-driven
 * transaction management, as opposed to the default approach of a by-type lookup.
 * One reason this might be necessary is if there are two {@code PlatformTransactionManager}
 * beans present in the container.
 *
 * <p>See @{@link EnableTransactionManagement} for general examples and context;
 * see {@link #annotationDrivenTransactionManager()} for detailed instructions.
 *
 * <p>Note that in by-type lookup disambiguation cases, an alternative approach to
 * implementing this interface is to simply mark one of the offending
 * {@code PlatformTransactionManager} {@code @Bean} methods as
 * {@link org.springframework.context.annotation.Primary @Primary}.
 * This is even generally preferred since it doesn't lead to early initialization
 * of the {@code PlatformTransactionManager} bean.
 *
 * @author Chris Beams
 * @since 3.1
 * @see EnableTransactionManagement
 * @see org.springframework.context.annotation.Primary
 * @see org.springframework.transaction.PlatformTransactionManager
 * @see org.springframework.transaction.ReactiveTransactionManager
 */
public interface TransactionManagementConfigurer {

	/**
	 * 返回默认的事务管理器bean，用于注解驱动的数据库事务管理，即在处理@Transactional方法时。有两种基本方法可以实现此方法：
	 *
	 * 1. 实现该方法并使用@Bean对其进行注解
	 * 在这种情况下，实现@Configuration类将实现此方法，并使用@Bean对其进行标记，并直接在方法体内配置并返回事务管理器：
	 *       @Bean
	 *       @Override
	 *       public PlatformTransactionManager annotationDrivenTransactionManager() {
	 * 	       return new DataSourceTransactionManager(dataSource());
	 *       }
	 * 2. 在没有@Bean的情况下实现该方法并委托给另一个现有的@Bean方法
	 *       @Bean
	 *       public PlatformTransactionManager txManager() {
	 * 	       return new DataSourceTransactionManager(dataSource());
	 *       }
	 *
	 *       @Override
	 *       public PlatformTransactionManager annotationDrivenTransactionManager() {
	 * 	       return txManager(); // 引用上面的现有@Bean方法
	 *       }
	 *
	 * 如果采用方法2，请确保只有一种方法标有@Bean！
	 * 在＃1或＃2场景中，将PlatformTransactionManager实例作为容器中的Spring bean管理非常重要，
	 * 因为所有PlatformTransactionManager实现都利用Spring生命周期回调（例如InitializingBean和BeanFactoryAware）。
	 */
	// 由用户实现
	/**
	 * Return the default transaction manager bean to use for annotation-driven database
	 * transaction management, i.e. when processing {@code @Transactional} methods.
	 * <p>There are two basic approaches to implementing this method:
	 * <h3>1. Implement the method and annotate it with {@code @Bean}</h3>
	 * In this case, the implementing {@code @Configuration} class implements this method,
	 * marks it with {@code @Bean} and configures and returns the transaction manager
	 * directly within the method body:
	 * <pre class="code">
	 * &#064;Bean
	 * &#064;Override
	 * public PlatformTransactionManager annotationDrivenTransactionManager() {
	 *     return new DataSourceTransactionManager(dataSource());
	 * }</pre>
	 * <h3>2. Implement the method without {@code @Bean} and delegate to another existing
	 * {@code @Bean} method</h3>
	 * <pre class="code">
	 * &#064;Bean
	 * public PlatformTransactionManager txManager() {
	 *     return new DataSourceTransactionManager(dataSource());
	 * }
	 *
	 * &#064;Override
	 * public PlatformTransactionManager annotationDrivenTransactionManager() {
	 *     return txManager(); // reference the existing {@code @Bean} method above
	 * }</pre>
	 * If taking approach #2, be sure that <em>only one</em> of the methods is marked
	 * with {@code @Bean}!
	 * <p>In either scenario #1 or #2, it is important that the
	 * {@code PlatformTransactionManager} instance is managed as a Spring bean within the
	 * container as all {@code PlatformTransactionManager} implementations take advantage
	 * of Spring lifecycle callbacks such as {@code InitializingBean} and
	 * {@code BeanFactoryAware}.
	 * @return a {@link org.springframework.transaction.PlatformTransactionManager} or
	 * {@link org.springframework.transaction.ReactiveTransactionManager} implementation
	 */
	TransactionManager annotationDrivenTransactionManager();

}
