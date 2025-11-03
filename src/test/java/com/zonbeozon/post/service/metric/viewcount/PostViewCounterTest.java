package com.zonbeozon.post.service.metric.viewcount;

import com.zonbeozon.post.repository.PostMetricRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PostViewCounterTest {
    @Mock
    private PostMetricRepositoryImpl postMetricRepositoryImpl;
    @InjectMocks
    private LazyPostViewCounter counter;

    @Test
    @DisplayName("각 ID에 대해 조회수를 1씩 증가시킨다")
    void increaseViewCountByOneForEachId() {
        counter.increase(List.of(1L , 2L));
        counter.flushViewCountsToDatabase();

        ArgumentCaptor<Map<Long, Long>> captor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(postMetricRepositoryImpl, Mockito.times(1)).updateViewCounts(captor.capture());


        Map<Long, Long> capturedMap = captor.getValue();

        assertThat(capturedMap).hasSize(2);
        assertThat(capturedMap).containsEntry(1L, 1L);
        assertThat(capturedMap).containsEntry(2L, 1L);
    }

    @Test
    @DisplayName("중복된 ID에 대해 조회수가 올바르게 누적된다.")
    void AccumulateViewCountsForDuplicateIds() {
        counter.increase(List.of(1L , 2L));
        counter.increase(List.of(1L , 2L));
        counter.increase(List.of(1L , 2L));
        counter.flushViewCountsToDatabase();

        ArgumentCaptor<Map<Long, Long>> captor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(postMetricRepositoryImpl, Mockito.times(1)).updateViewCounts(captor.capture());


        Map<Long, Long> capturedMap = captor.getValue();

        assertThat(capturedMap).hasSize(2);
        assertThat(capturedMap).containsEntry(1L, 3L);
        assertThat(capturedMap).containsEntry(2L, 3L);
    }
}
