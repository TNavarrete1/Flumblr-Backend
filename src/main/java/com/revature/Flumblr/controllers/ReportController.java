package com.revature.Flumblr.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.Flumblr.services.TokenService;
import com.revature.Flumblr.services.ReportService;
import com.revature.Flumblr.dtos.requests.ReportPostRequest;
import com.revature.Flumblr.dtos.responses.ReportResponse;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
import com.revature.Flumblr.utils.custom_exceptions.UnauthorizedException;
import com.revature.Flumblr.entities.Report;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/reports")
public class ReportController {
    // dependency injection ie. services
    private final TokenService tokenService;
    private final ReportService reportService;
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping()
    public ResponseEntity<?> reportPost(@RequestBody ReportPostRequest postReport,
            @RequestHeader("Authorization") String token) {
        String requesterId = tokenService.extractUserId(token);
        logger.trace("reporting post " + postReport.getPostId() + " requested by " + requesterId);
        reportService.create(postReport.getPostId(), requesterId, postReport.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all/{page}")
    public ResponseEntity<List<ReportResponse>> getReports(@PathVariable int page,
            @RequestHeader("Authorization") String token) {
        if (page <= 0)
            throw new BadRequestException("page must be > 0");
        String roleName = tokenService.extractRole(token);
        if (!roleName.equals("ADMIN")) {
            throw new UnauthorizedException("Must be Admin to see reports");
        }
        List<Report> reports = reportService.findAll(page - 1);
        List<ReportResponse> responses = new ArrayList<ReportResponse>();
        for (Report report : reports) {
            responses.add(new ReportResponse(report));
        }
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportResponse>> getUserReports(@PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        Map<String, Object> claimMap = tokenService.extractAllClaims(token);
        String roleName = (String) claimMap.get("role");
        String tokenId = (String) claimMap.get("id");
        if (!roleName.equals("ADMIN") || !tokenId.equals(userId)) {
            logger.debug("unauthorized report query by user " + tokenId);
            throw new UnauthorizedException("Must be Admin to see reports");
        }
        List<Report> reports = reportService.findAllByUserId(userId);
        List<ReportResponse> responses = new ArrayList<ReportResponse>();
        for (Report report : reports) {
            responses.add(new ReportResponse(report));
        }
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable String reportId,
            @RequestHeader("Authorization") String token) {
        Map<String, Object> claimMap = tokenService.extractAllClaims(token);
        String roleName = (String) claimMap.get("role");
        String tokenId = (String) claimMap.get("id");
        Report report = reportService.findById(reportId);
        if (!roleName.equals("ADMIN") || !tokenId.equals(report.getUser().getId())) {
            logger.debug("unauthorized delete report by user " + tokenId);
            throw new UnauthorizedException("Must be Admin to delete reports");
        }
        logger.trace("deleting report " + reportId);
        reportService.delete(report);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
