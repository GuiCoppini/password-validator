package iti.passwordvalidator.domain.config

import iti.passwordvalidator.common.logging.LogUtils.getLogger
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CacheCleaner {

    val log = getLogger(this::class.java)

    // a cada 15 minutos
    @Scheduled(cron = "0 0/15 * * * *")
    @CacheEvict(value = ["password"], allEntries = true)
    fun clearCache() {
        log.info("[JOB] Clearing Password cache")
    }
}