package br.com.blue.guard.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.blue.guard.api.model.BeachReport;
import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.repository.BeachReportRepository;
import br.com.blue.guard.api.repository.UserRepository;

@Service
public class BeachReportService {

    @Autowired
    private BeachReportRepository beachReportRepository;

    @Autowired
    private UserRepository userRepository;

    public BeachReport createReport(BeachReport beachReport, JwtAuthenticationToken jwtToken) {
        var userOptional = userRepository.findById(Long.parseLong(jwtToken.getName()));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            beachReport.setUser(user);
            return beachReportRepository.save(beachReport);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or unauthorized!");
        }
    }

    public BeachReport updateReport(Long id, BeachReport updatedBeachReport, JwtAuthenticationToken jwtToken) {
        var report = beachReportRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found, id: " + id));

        var userOptional = userRepository.findById(Long.parseLong(jwtToken.getName()));
        if (userOptional.isPresent() && report.getUser().getId().equals(userOptional.get().getId())) {
            report.setDescription(updatedBeachReport.getDescription());
            report.setCondition(updatedBeachReport.getCondition());
            report.setLocation(updatedBeachReport.getLocation());
            report.setImageUrl(updatedBeachReport.getImageUrl());

            return beachReportRepository.save(report);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or unauthorized!");
        }
    }

    public void deleteReport(Long id, JwtAuthenticationToken jwtToken) {
        var report = beachReportRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found, id: " + id));
        
        var userOptional = userRepository.findById(Long.parseLong(jwtToken.getName()));
        if (userOptional.isPresent() && report.getUser().getId().equals(userOptional.get().getId())) {
            beachReportRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or unauthorized!");
        }
    }

    public BeachReport getReportById(Long id) {
        return beachReportRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beach report not found: ID " + id));
    }

    public Page<BeachReport> getAllReports(Pageable pageable) {
        return beachReportRepository.findAll(pageable);
    }
}
