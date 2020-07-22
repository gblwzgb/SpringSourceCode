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

package org.springframework.transaction;

import org.springframework.lang.Nullable;

/**
 * 这是Spring事务基础架构中的核心接口。
 * 应用程序可以直接使用它，但是它并不是主要用于API：通常，应用程序可以通过AOP与TransactionTemplate或声明性事务划分一起使用。
 *
 * 对于实现者，建议从提供的org.springframework.transaction.support.AbstractPlatformTransactionManager类派生，
 * 该类预实现定义的传播行为并负责事务同步处理。
 * 子类必须为基础事务的特定状态实现模板方法，例如：begin，suspend，resume，commit。
 *
 * 该策略接口的默认实现是JtaTransactionManager和DataSourceTransactionManager，它们可以用作其他事务策略的实现指南。
 */

/**
 * This is the central interface in Spring's transaction infrastructure.
 * Applications can use this directly, but it is not primarily meant as API:
 * Typically, applications will work with either TransactionTemplate or
 * declarative transaction demarcation through AOP.
 *
 * <p>For implementors, it is recommended to derive from the provided
 * {@link org.springframework.transaction.support.AbstractPlatformTransactionManager}
 * class, which pre-implements the defined propagation behavior and takes care
 * of transaction synchronization handling. Subclasses have to implement
 * template methods for specific states of the underlying transaction,
 * for example: begin, suspend, resume, commit.
 *
 * <p>The default implementations of this strategy interface are
 * {@link org.springframework.transaction.jta.JtaTransactionManager} and
 * {@link org.springframework.jdbc.datasource.DataSourceTransactionManager},
 * which can serve as an implementation guide for other transaction strategies.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 16.05.2003
 * @see org.springframework.transaction.support.TransactionTemplate
 * @see org.springframework.transaction.interceptor.TransactionInterceptor
 */
public interface PlatformTransactionManager extends TransactionManager {

	/**
	 * 根据指定的传播行为，返回当前活动的事务或创建一个新的事务。
	 *
	 * 请注意，诸如隔离级别或超时之类的参数将仅应用于新事务，因此在参与活动事务时将被忽略。
	 *
	 * 此外，并非每个事务管理器都支持所有事务定义设置：当遇到不受支持的设置时，正确的事务管理器实现应引发异常。
	 *
	 * 上述规则的一个例外是只读标志，如果不支持显式只读模式，则应忽略该标志。本质上，只读标志只是潜在优化的提示。
	 *
	 * @param definition TransactionDefinition实例（默认情况下可以为null），描述传播行为，隔离级别，超时等。
	 * @return 代表新事务或当前事务的，事务状态对象
	 */
	/**
	 * Return a currently active transaction or create a new one, according to
	 * the specified propagation behavior.
	 * <p>Note that parameters like isolation level or timeout will only be applied
	 * to new transactions, and thus be ignored when participating in active ones.
	 * <p>Furthermore, not all transaction definition settings will be supported
	 * by every transaction manager: A proper transaction manager implementation
	 * should throw an exception when unsupported settings are encountered.
	 * <p>An exception to the above rule is the read-only flag, which should be
	 * ignored if no explicit read-only mode is supported. Essentially, the
	 * read-only flag is just a hint for potential optimization.
	 * @param definition the TransactionDefinition instance (can be {@code null} for defaults),
	 * describing propagation behavior, isolation level, timeout etc.
	 * @return transaction status object representing the new or current transaction
	 * @throws TransactionException in case of lookup, creation, or system errors
	 * @throws IllegalTransactionStateException if the given transaction definition
	 * cannot be executed (for example, if a currently active transaction is in
	 * conflict with the specified propagation behavior)
	 * @see TransactionDefinition#getPropagationBehavior
	 * @see TransactionDefinition#getIsolationLevel
	 * @see TransactionDefinition#getTimeout
	 * @see TransactionDefinition#isReadOnly
	 */
	TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
			throws TransactionException;

	/**
	 * 提交给定事务的状态。如果已通过编程将事务标记为仅回滚（rollback-only），请执行回滚。
	 *
	 * 如果该事务不是新事务，则忽略commit以正确参与surrounding的事务。
	 * 如果先前的事务已被暂停以能够创建新事务，则在commit新事务后恢复上一个事务。
	 *
	 * 请注意，当commit调用完成时，无论是正常还是引发异常，事务都必须完全完成并清理。
	 * 在这种情况下，不应进行回滚调用。
	 *
	 * 如果此方法引发了TransactionException以外的其他异常，则某些commit前错误将导致commit尝试失败。
	 * 例如，一个O/R映射工具可能已尝试在提交之前立即刷新对数据库的更改，结果DataAccessException导致事务失败。
	 * 在这种情况下，原始异常将传播到此commit方法的调用方。
	 */
	/**
	 * Commit the given transaction, with regard to its status. If the transaction
	 * has been marked rollback-only programmatically, perform a rollback.
	 * <p>If the transaction wasn't a new one, omit the commit for proper
	 * participation in the surrounding transaction. If a previous transaction
	 * has been suspended to be able to create a new one, resume the previous
	 * transaction after committing the new one.
	 * <p>Note that when the commit call completes, no matter if normally or
	 * throwing an exception, the transaction must be fully completed and
	 * cleaned up. No rollback call should be expected in such a case.
	 * <p>If this method throws an exception other than a TransactionException,
	 * then some before-commit error caused the commit attempt to fail. For
	 * example, an O/R Mapping tool might have tried to flush changes to the
	 * database right before commit, with the resulting DataAccessException
	 * causing the transaction to fail. The original exception will be
	 * propagated to the caller of this commit method in such a case.
	 * @param status object returned by the {@code getTransaction} method
	 * @throws UnexpectedRollbackException in case of an unexpected rollback
	 * that the transaction coordinator initiated
	 * @throws HeuristicCompletionException in case of a transaction failure
	 * caused by a heuristic decision on the side of the transaction coordinator
	 * @throws TransactionSystemException in case of commit or system errors
	 * (typically caused by fundamental resource failures)
	 * @throws IllegalTransactionStateException if the given transaction
	 * is already completed (that is, committed or rolled back)
	 * @see TransactionStatus#setRollbackOnly
	 */
	void commit(TransactionStatus status) throws TransactionException;

	/**
	 * 执行给定事务的回滚。如果该事务不是新事务，则只需将其设置为“仅回滚”(rollback-only)即可正确参与surrounding的事务。
	 *
	 * 如果先前的事务已被挂起以便能够创建新的事务，则在回滚新事务之后恢复上一个事务。
	 *
	 * 如果提交引发异常，请不要在事务上调用回滚。即使commit异常，该事务也将在提交返回时已经完成并清理。
	 * 因此，提交失败后的回滚调用将导致IllegalTransactionStateException。
	 */
	/**
	 * Perform a rollback of the given transaction.
	 * <p>If the transaction wasn't a new one, just set it rollback-only for proper
	 * participation in the surrounding transaction. If a previous transaction
	 * has been suspended to be able to create a new one, resume the previous
	 * transaction after rolling back the new one.
	 * <p><b>Do not call rollback on a transaction if commit threw an exception.</b>
	 * The transaction will already have been completed and cleaned up when commit
	 * returns, even in case of a commit exception. Consequently, a rollback call
	 * after commit failure will lead to an IllegalTransactionStateException.
	 * @param status object returned by the {@code getTransaction} method
	 * @throws TransactionSystemException in case of rollback or system errors
	 * (typically caused by fundamental resource failures)
	 * @throws IllegalTransactionStateException if the given transaction
	 * is already completed (that is, committed or rolled back)
	 */
	void rollback(TransactionStatus status) throws TransactionException;

}
