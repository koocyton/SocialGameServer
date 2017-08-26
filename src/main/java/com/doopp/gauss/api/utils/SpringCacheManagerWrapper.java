package com.doopp.gauss.api.utils;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
@Component
public class SpringCacheManagerWrapper implements CacheManager {

    public static final Logger log = LoggerFactory.getLogger(SpringCacheManagerWrapper.class);

    @Resource
    private org.springframework.cache.CacheManager springCacheManger;

    @Resource
    private net.sf.ehcache.CacheManager cacheManager;

    /**
     * Set the backing EhCache {@link net.sf.ehcache.CacheManager}.
     */
    public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Return the backing EhCache {@link net.sf.ehcache.CacheManager}.
     */
    public net.sf.ehcache.CacheManager getCacheManager() {
        return this.cacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        log.debug("getCache: springCacheManger <== " + springCacheManger);
        org.springframework.cache.Cache springCache = springCacheManger.getCache(name);
        return new SpringCacheWrapper<K, V>(springCache);
    }

    private class SpringCacheWrapper<K, V> implements Cache<K, V> {
        private org.springframework.cache.Cache springCache;
        public SpringCacheWrapper(org.springframework.cache.Cache springCache) {
            this.springCache = springCache;
        }

        @Override
        public V get(K key) throws CacheException {
            Object value = springCache.get(key);
            if (value instanceof SimpleValueWrapper) {
                return (V) ((SimpleValueWrapper)value).get();
            }
            return (V) value;
        }

        @Override
        public V put(K key, V value) throws CacheException {
            springCache.put(key, value);
            return value;
        }

        @Override
        public V remove(K key) throws CacheException {
            Object value = get(key);
            springCache.evict(key);
            return (V) value;
        }

        @Override
        public void clear() throws CacheException {
            springCache.clear();
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException("invoke spring cache abstract size method not supported");

        }

        @Override
        public Set<K> keys() {
            throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");

        }

        @Override
        public Collection<V> values() {
            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
        }
    }
}