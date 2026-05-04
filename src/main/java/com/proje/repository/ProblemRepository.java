package com.proje.repository;

import com.proje.entity.Problem;
import com.proje.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByOwnerOrderByCreatedAtDesc(User owner);

    List<Problem> findAllByOrderByCreatedAtDesc();

    @Query("select p from Problem p where " +
            "lower(p.title) like lower(concat('%', :keyword, '%')) or " +
            "lower(p.category) like lower(concat('%', :keyword, '%')) " +
            "order by p.createdAt desc")
    List<Problem> searchAll(@Param("keyword") String keyword);

    @Query("select p from Problem p where p.owner = :owner and (" +
            "lower(p.title) like lower(concat('%', :keyword, '%')) or " +
            "lower(p.category) like lower(concat('%', :keyword, '%'))) " +
            "order by p.createdAt desc")
    List<Problem> searchByOwner(@Param("owner") User owner, @Param("keyword") String keyword);
}
