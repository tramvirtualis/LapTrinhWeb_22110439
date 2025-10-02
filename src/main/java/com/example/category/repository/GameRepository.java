package com.example.category.repository;

import com.example.category.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(g.genre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(g.platform) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Game> search(@Param("keyword") String keyword, Pageable pageable);
}


