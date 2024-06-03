package br.com.blue.guard.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.blue.guard.api.model.BeachReport;
import br.com.blue.guard.api.service.BeachReportService;

@RestController
@RequestMapping("/beachReport")
public class BeachReportController {

    @Autowired
    private BeachReportService beachReportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeachReport createReport(@RequestBody BeachReport beachReport, JwtAuthenticationToken jwtToken) {
        return beachReportService.createReport(beachReport, jwtToken);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeachReport> updateReport(@PathVariable Long id, @RequestBody BeachReport updatedBeachReport,
            JwtAuthenticationToken jwtToken) {
        BeachReport updatedReport = beachReportService.updateReport(id, updatedBeachReport, jwtToken);
        return ResponseEntity.ok(updatedReport);
    }

    @GetMapping
    public ResponseEntity<Page<BeachReport>> getAllReports(@PageableDefault(sort = "timestamp", direction = Direction.DESC) Pageable pageable) {
        Page<BeachReport> page = beachReportService.getAllReports(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeachReport> getReportById(@PathVariable Long id) {
        BeachReport report = beachReportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id, JwtAuthenticationToken jwtToken) {
        beachReportService.deleteReport(id, jwtToken);
        return ResponseEntity.noContent().build();
    }
}
