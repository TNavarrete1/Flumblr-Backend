package com.revature.Flumblr.services;

import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.repositories.*;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    private ReportService reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    private User user;

    private Post post;

    private Report report;

    private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

    @BeforeEach
    public void setup() {
        reportService = new ReportService(reportRepository, userService, postService);
        user = new User();
        user.setId(userId);
        post = new Post("testPost", null, null, user, null);
        report = new Report(post, user, "testReason");
    }

    @Test
    public void createTest() {
        when(postService.findById(post.getId())).thenReturn(post);
        when(userService.findById(userId)).thenReturn(user);
        reportService.create(post.getId(), userId, "testReason");
        ArgumentCaptor<Report> reportArg = ArgumentCaptor.forClass(Report.class);
        verify(reportRepository, times(1)).save(reportArg.capture());
        assertEquals("testReason", reportArg.getValue().getReason());
    }

    @Test
    public void deleteTest() {
        reportService.delete(report);
        verify(reportRepository, times(1)).delete(isA(Report.class));
    }

    @Test
    public void findAllTest() {
        List<Report> reports = new ArrayList<Report>();
        reports.add(report);
        when(reportRepository.findAllBy(isA(Pageable.class))).thenReturn(reports);
        List<Report> resReports = reportService.findAll(1);
        verify(reportRepository, times(1)).findAllBy(isA(Pageable.class));
        assertEquals(resReports.size(), 1);
    }

    @Test
    public void findByIdTest() {
        String reportId = report.getId();
        String noReportId = "ac997ca0-852e-4b7b-b9c7-94f47cf38969";
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(reportRepository.findById(noReportId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            reportService.findById(noReportId);
        });
        assertEquals(report, reportService.findById(reportId));
        verify(reportRepository, times(2)).findById(anyString());
    }

    @Test
    public void findByUserIdTest() {
        List<Report> reports = new ArrayList<Report>();
        reports.add(report);
        when(reportRepository.findAllByUserId(userId)).thenReturn(reports);
        List<Report> resReports = reportService.findAllByUserId(userId);
        verify(reportRepository, times(1)).findAllByUserId(userId);
        assertEquals(resReports.size(), 1);
    }
}
