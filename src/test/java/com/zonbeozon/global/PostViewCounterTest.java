package com.zonbeozon.global;

import com.zonbeozon.post.service.viewcount.LazyPostViewCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class PostViewCounterTest {
    @Mock
    private ContentEntityFinder finder;
    @InjectMocks
    private LazyPostViewCounter counter;

    private ContentEntity contentEntity_1, contentEntity_2;

    @BeforeEach
    void setup() {
        contentEntity_1 = Mockito.mock(ContentEntity.class);
        Mockito.when(contentEntity_1.getId()).thenReturn(1L);
        contentEntity_2 = Mockito.mock(ContentEntity.class);
        Mockito.when(contentEntity_2.getId()).thenReturn(2L);
        Mockito.doReturn(List.of(contentEntity_1, contentEntity_2)).when(finder).findByIdIn(Mockito.eq(Set.of(1L, 2L)));
    }

    @Test
    @DisplayName("각 ID에 대해 조회수를 1씩 증가시킨다")
    void increaseViewCountByOneForEachId() {
        counter.increase(List.of(1L , 2L));
        counter.flushViewCountsToDatabase();

        Mockito.verify(contentEntity_1, Mockito.times(1)).increaseViewCount(1L);
        Mockito.verify(contentEntity_2, Mockito.times(1)).increaseViewCount(1L);
    }

    @Test
    @DisplayName("중복된 ID에 대해 조회수가 올바르게 누적된다.")
    void AccumulateViewCountsForDuplicateIds() {
        counter.increase(List.of(1L , 2L));
        counter.increase(List.of(1L , 2L));
        counter.increase(List.of(1L , 2L));
        counter.flushViewCountsToDatabase();

        Mockito.verify(contentEntity_1, Mockito.times(1)).increaseViewCount(3L);
        Mockito.verify(contentEntity_2, Mockito.times(1)).increaseViewCount(3L);
    }
}
