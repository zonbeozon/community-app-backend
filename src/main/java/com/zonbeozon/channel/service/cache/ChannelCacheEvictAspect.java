package com.zonbeozon.channel.service.cache;

import com.zonbeozon.global.cache.AbstractCacheEvictAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Profile("cache")
public class ChannelCacheEvictAspect extends AbstractCacheEvictAspect<Long> {
    private final List<ChannelCacheInvalidator> invalidators;

    public ChannelCacheEvictAspect(List<ChannelCacheInvalidator> invalidators) {
        super(ChannelCacheEvict.class, Long.class);
        this.invalidators = invalidators;
    }

    @Pointcut("@within(com.zonbeozon.channel.service.cache.ChannelCacheEvict) || @annotation(com.zonbeozon.channel.service.cache.ChannelCacheEvict)")
    @Override
    protected void callAt() {}

    @Override
    protected void evict(Long key) {
        invalidators.forEach(invalidator -> invalidator.invalidate(key));
    }
}
