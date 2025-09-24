package com.zonbeozon.global.s3.outbox;

import com.zonbeozon.image.ImageS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageOutboxProcessor {
    private final OutboxProcessor outboxProcessor;
    private final ImageS3Properties imageS3Properties;

    @Scheduled(fixedRateString = "${outbox.schedule.fixed-rate}", initialDelayString = "${outbox.schedule.initial-delay}")
    public void deleteImageOutbox() {
        outboxProcessor.processDeletionByBucket(imageS3Properties.getBucket());
    }
}
