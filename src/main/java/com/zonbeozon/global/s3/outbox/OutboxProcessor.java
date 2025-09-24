package com.zonbeozon.global.s3.outbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OutboxProcessor {
    private final OutboxRepository outboxRepository;
    private final S3Client s3Client;
    private final TransactionTemplate transactionTemplate;

    public OutboxProcessor(
            OutboxRepository outboxRepository,
            S3Client s3Client,
            PlatformTransactionManager transactionManager
    ) {
        this.outboxRepository = outboxRepository;
        this.s3Client = s3Client;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void processDeletionByBucket(String bucket) {
        List<Outbox> outboxes = outboxRepository.findByBucket(bucket);

        Set<ObjectIdentifier> keysToDelete = outboxes.stream()
                .map(outbox -> ObjectIdentifier.builder().key(outbox.getObjectKey()).build())
                .collect(Collectors.toSet());
        log.debug("삭제할 키 개수: {}", keysToDelete.size());
        if(keysToDelete.isEmpty()) return;
        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucket)
                .delete(Delete.builder().objects(keysToDelete).build())
                .build();

        final List<Outbox> s3DeletedOutboxes;
        DeleteObjectsResponse res = s3Client.deleteObjects(deleteObjectsRequest);
        if(res.hasErrors()) {
            log.warn("s3 응답 에러 발생 - 삭제된 일부 객체만 db 삭제 작업을 진행");
            Set<String> deletedKeys = res.deleted().stream().map(DeletedObject::key).collect(Collectors.toSet());
            s3DeletedOutboxes = outboxes.stream().filter(outbox -> deletedKeys.contains(outbox.getObjectKey())).toList();
        } else {
            s3DeletedOutboxes = outboxes;
        }
        log.debug("s3로 부터 삭제된 key 개수: {}", s3DeletedOutboxes.size());

        if (s3DeletedOutboxes.isEmpty()) return;
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                outboxRepository.deleteAllInBatch(s3DeletedOutboxes);
            }
        });
        log.debug("{} 개의 키가 성공적으로 삭제되었습니다.", keysToDelete.size());
    }
}
