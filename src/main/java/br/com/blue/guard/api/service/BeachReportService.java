package br.com.blue.guard.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.blue.guard.api.controller.BeachReportController;
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

    public EntityModel<BeachReport> createReport(BeachReport beachReport, JwtAuthenticationToken jwtToken) {
        var userOptional = userRepository.findById(Long.parseLong(jwtToken.getName()));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            beachReport.setUser(user);
            BeachReport savedReport = beachReportRepository.save(beachReport);

            return EntityModel.of(savedReport,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeachReportController.class).getReportById(savedReport.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeachReportController.class).getAllReports(null)).withRel("allReports"));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or unauthorized!");
        }
    }

    public EntityModel<BeachReport> updateReport(Long id, BeachReport updatedBeachReport, JwtAuthenticationToken jwtToken) {
        var report = beachReportRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found, id: " + id));

        var userOptional = userRepository.findById(Long.parseLong(jwtToken.getName()));
        if (userOptional.isPresent() && report.getUser().getId().equals(userOptional.get().getId())) {
            report.setDescription(updatedBeachReport.getDescription());
            report.setCondition(updatedBeachReport.getCondition());
            report.setLocation(updatedBeachReport.getLocation());
            report.setImageUrl(updatedBeachReport.getImageUrl());

            BeachReport savedReport = beachReportRepository.save(report);

            return EntityModel.of(savedReport,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeachReportController.class).getReportById(savedReport.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeachReportController.class).getAllReports(null)).withRel("allReports"));
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

    public EntityModel<BeachReport> getReportById(Long id) {
        BeachReport report = beachReportRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beach report not found: ID " + id));

        return EntityModel.of(report,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeachReportController.class).getReportById(id)).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeachReportController.class).getAllReports(null)).withRel("allReports"));
    }

    public PagedModel<EntityModel<BeachReport>> getAllReports(Pageable pageable) {
        Page<BeachReport> reports = beachReportRepository.findAll(pageable);
        List<EntityModel<BeachReport>> reportModels = reports.getContent().stream()
            .map(report -> EntityModel.of(report,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeachReportController.class).getReportById(report.getId())).withSelfRel()))
            .collect(Collectors.toList());

        return PagedModel.of(reportModels, new PagedModel.PageMetadata(reports.getSize(), reports.getNumber(), reports.getTotalElements()));
    }
}
