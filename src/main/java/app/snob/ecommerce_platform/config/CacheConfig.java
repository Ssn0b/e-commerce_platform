package app.snob.ecommerce_platform.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(this.createCaffeine());
        return manager;
    }
    private Caffeine<Object, Object> createCaffeine() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES);
    }
}