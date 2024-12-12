package com.hdbsthor012airflow.repository;

import com.hdbsthor012airflow.entity.SystemVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemVersionRepository extends JpaRepository<SystemVersion,String> {

    Page<SystemVersion> findAll(Pageable pageable);
}
