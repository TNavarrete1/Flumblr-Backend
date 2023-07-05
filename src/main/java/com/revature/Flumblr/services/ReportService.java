package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.ReportRepository;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.User;

import lombok.AllArgsConstructor;

import com.revature.Flumblr.dtos.responses.ReportResponse;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.Report;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserService userService;
    private final PostService postService;

    public void create(String postId, String userId, String reason) {
        Post reported = postService.findById(postId);
        User reporter = userService.findById(userId);
        Report report = new Report(reported, reporter, reason);
        reportRepository.save(report);
    }

    public void delete(Report report) {
        this.reportRepository.delete(report);
    }

    public List<ReportResponse> findAll(int page) {
        List<Report> reports = reportRepository.findAllBy(
            PageRequest.of(page, 20, Sort.by("createTime").ascending()));
        List<ReportResponse> responses = new ArrayList<ReportResponse>();
        for(Report report : reports) {
            responses.add(new ReportResponse(report));
        }
        return responses;
    }

    public Report findById(String postId) {
        Optional<Report> report = this.reportRepository.findById(postId);
        if (report.isEmpty())
            throw new ResourceNotFoundException("Report(" + postId + ") Not Found");
        return report.get();
    }

    public List<ReportResponse> findAllByUserId(String userId) {
        List<Report> reports = reportRepository.findAllByUserId(userId);
        List<ReportResponse> responses = new ArrayList<ReportResponse>();
        for(Report report : reports) {
            responses.add(new ReportResponse(report));
        }
        return responses;
    }
}
