package com.zonbeozon.image;

import com.zonbeozon.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Image i WHERE i.id IN :imageIds")
    void deleteImagesByIdsInBatch(List<Long> imageIds);

    Optional<Image> findByObjectKey(String objectKey);

}
