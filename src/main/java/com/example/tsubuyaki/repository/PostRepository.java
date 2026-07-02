package com.example.tsubuyaki.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findTop50ByDeletedAtIsNullOrderByCreatedAtDescIdDesc();

    @Query("""
            SELECT p
            FROM PostEntity p
            WHERE p.deletedAt IS NULL
              AND (
                  LOCATE(LOWER(:keyword), LOWER(p.body)) > 0
                  OR LOCATE(LOWER(:keyword), LOWER(p.author)) > 0
              )
            ORDER BY p.createdAt DESC, p.id DESC
            """)
    List<PostEntity> searchActiveByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<PostEntity> findTop50ByDeletedAtIsNullAndTagsNameOrderByCreatedAtDescIdDesc(String tagName);

    Optional<PostEntity> findByIdAndDeletedAtIsNull(Long id);
}
