/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.threadlocal;

import java.util.Map.Entry;

import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommon.util.cache.ConcurrentMapCache;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommon.util.threadlocal.starter.ThreadLocalStarter;
import it.csi.siac.siaccommon.util.threadlocal.starter.ThreadLocalStarterReflectiveImpl;

/**
 * Utilities for thread-local handling
 * @author Marchino Alessandro
 *
 */
public class ThreadLocalUtil {

	/** The underlying cached application-wide registry */
	private static final Cache<String, ThreadLocal<?>> REGISTRY = new ConcurrentMapCache<String, ThreadLocal<?>>();
	/** Logger, only used at TRACE level */
	private static final LogUtil LOG = new LogUtil(ThreadLocalUtil.class);
	
	/** Private constructor to prevent accidental instantiation */
	private ThreadLocalUtil() {
		// Prevent instantiation
	}
	
	/**
	 * Register a thread local with the current registry
	 * @param key the key with which to register the thread local
	 * @param threadLocalStarter the thread local starter to use to register
	 * @param <W> the parameterization of the wrapped type
	 * @return the registered thread local
	 */
	@SuppressWarnings("unchecked")
	public static <W> ThreadLocal<W> registerThreadLocal(String key, ThreadLocalStarter<W> threadLocalStarter) {
		return (ThreadLocal<W>) ThreadLocalUtil.REGISTRY.get(key, new ThreadLocalStarterCacheElementInitializer(threadLocalStarter));
	}
	
	/**
	 * Register a thread local with the current registry
	 * @param registeringClass the registering class
	 * @param threadLocalStarter the thread local starter to use to register
	 * @param <W> the parameterization of the wrapped type
	 * @return the registered thread local
	 */
	public static <W> ThreadLocal<W> registerThreadLocal(Class<?> registeringClass, ThreadLocalStarter<W> threadLocalStarter) {
		final String key = (registeringClass != null ? registeringClass.getName() + "|" : "") + threadLocalStarter.getName();
		return registerThreadLocal(key, threadLocalStarter);
	}
	
	/**
	 * Register a thread local with the current registry
	 * @param threadLocalStarter the thread local starter to use to register
	 * @param <W> the parameterization of the wrapped type
	 * @return the registered thread local
	 */
	public static <W> ThreadLocal<W> registerThreadLocal(ThreadLocalStarter<W> threadLocalStarter) {
		return registerThreadLocal(threadLocalStarter.getName(), threadLocalStarter);
	}
	
	/**
	 * Register a thread local with the current registry
	 * @param threadLocalClass the thread class to be registered
	 * @param <W> the parameterization of the wrapped type
	 * @return the registered thread local
	 */
	public static <W, T extends ThreadLocal<W>> ThreadLocal<W> registerThreadLocal(Class<T> threadLocalClass) {
		// Register via reflection
		final ThreadLocalStarter<W> threadLocalStarter = new ThreadLocalStarterReflectiveImpl<W, T>(threadLocalClass);
		return registerThreadLocal(threadLocalStarter.getName(), threadLocalStarter);
	}
	
	/**
	 * Register a thread local with the current registry
	 * @param key the key with which to register the thread local
	 * @param threadLocalClass the thread class to be registered
	 * @param <W> the parameterization of the wrapped type
	 * @return the registered thread local
	 */
	public static <W, T extends ThreadLocal<W>> ThreadLocal<W> registerThreadLocal(String key, Class<T> threadLocalClass) {
		// Register via reflection
		final ThreadLocalStarter<W> threadLocalStarter = new ThreadLocalStarterReflectiveImpl<W, T>(threadLocalClass);
		return registerThreadLocal(key + "|" + threadLocalStarter.getName(), threadLocalStarter);
	}
	
	/**
	 * Cleans the registered Thread locals for the current thread
	 */
	public static void cleanThreadLocals() {
		final String methodName = "cleanThreadLocals";
		final String threadName = Thread.currentThread().getName();
		final boolean isTraceEnabled = LOG.isTraceEnabled();
		long initTime = 0L;
		
		if(isTraceEnabled) {
			// Tracing of the operation: keep track of the time elapsed
			initTime = System.currentTimeMillis();
		}
		
		for(Entry<String, ThreadLocal<?>> entry : ThreadLocalUtil.REGISTRY.entrySet()) {
			entry.getValue().remove();
			if(isTraceEnabled) {
				// Tracing of the operation: logs which thread local was cleared for the current thread
				LOG.trace(methodName, "Cleared thread local for key " + entry.getKey() + " for thread " + threadName);
			}
		}
		if(isTraceEnabled) {
			// Tracing of the operation: logs of the time elapsed
			long endTime = System.currentTimeMillis();
			LOG.trace(methodName, "Cleared threadlocals for thread " + threadName + " in " + (endTime - initTime) + "ms");
		}
	}
	
	/**
	 * Retrieves the thread local for the given key
	 * @param key the thread-local key
	 * @return the given thread local
	 */
	@SuppressWarnings("unchecked")
	public static <W extends ThreadLocal<?>> W getThreadLocal(String key) {
		return (W) ThreadLocalUtil.REGISTRY.get(key);
	}
	
	/**
	 * The cache initializer for the thread local
	 * @author Marchino Alessandro
	 *
	 * @param <W> the underlying thread-localized class
	 */
	private static class ThreadLocalStarterCacheElementInitializer implements CacheElementInitializer<String, ThreadLocal<?>> {

		/** Wrapper starter */
		private final ThreadLocalStarter<?> threadLocalStarter;
		
		/**
		 * Wrap constructor
		 * @param threadLocalStarter the starter
		 */
		public ThreadLocalStarterCacheElementInitializer(ThreadLocalStarter<?> threadLocalStarter) {
			this.threadLocalStarter = threadLocalStarter;
		}

		@Override
		public ThreadLocal<?> initialize(String key) {
			return threadLocalStarter.initialize();
		}
	}
}
