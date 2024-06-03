package br.com.blue.guard.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.blue.guard.api.model.BeachReport;

public interface BeachReportRepository extends JpaRepository<BeachReport, Long>{
    
}
