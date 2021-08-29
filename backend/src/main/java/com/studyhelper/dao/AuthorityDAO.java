package com.studyhelper.dao;

import com.studyhelper.entity.Authorities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/*@RepositoryRestResource(collectionResourceRel = "authorities", path = "authorities")
public interface AuthorityDAO extends PagingAndSortingRepository<Authorities, Integer> {
    Page<Authorities> findAll(Pageable pageable);
    Iterable<Authorities> findAll();
    Optional<Authorities> findById(Integer id);
    boolean existsById(Integer id);
}*/
