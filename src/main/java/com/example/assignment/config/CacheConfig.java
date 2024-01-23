package com.example.assignment.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CacheConfig {
//    @Bean
//    CacheManagerCustomizer<ConcurrentMapCacheManager> customizer() {
//        return new ConcurrentCustomizer();
//    }
//
//    class ConcurrentCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {
//
//        @Override
//        public void customize(ConcurrentMapCacheManager cacheManager) {
//            cacheManager.setAllowNullValues(false);
//            cacheManager.setStoreByValue(true);
//            System.out.println("cache obj is created");
//        }
//    }

    @Bean
    CacheManager cacheManager(){
        return new EhCacheCacheManager(ehCacheManager());
    }

    private net.sf.ehcache.CacheManager ehCacheManager() {
        EhCacheManagerFactoryBean ehCache = new EhCacheManagerFactoryBean();
        ehCache.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehCache.setShared(true);
        return ehCache.getObject();
    }
}
