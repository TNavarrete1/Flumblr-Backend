package com.revature.Flumblr.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.Flumblr.services.*;
import com.revature.Flumblr.entities.*;
import com.revature.Flumblr.dtos.requests.ReportPostRequest;
import com.revature.Flumblr.dtos.responses.ReportResponse;
import com.revature.Flumblr.utils.custom_exceptions.BadRequestException;
import com.revature.Flumblr.utils.custom_exceptions.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {
    private ReportController reportController;

    @Mock
    private TokenService tokenService;

    @Mock
    private ReportService reportService;

    private static final String userId = "51194080-3452-4503-b271-6df469cb7983";

    private User user;

    private Post post;

    private ReportPostRequest reportRequest;

    private Report report;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(userId);
        User otherUser = new User();
        otherUser.setId("3eefc30f-c7f9-49da-ae78-38d6f64a954d");
        post = new Post("testPost", null, null, otherUser);
        reportRequest = new ReportPostRequest(post.getId(), "DISLIKE");
        reportController = new ReportController(tokenService, reportService);
        report = new Report(post, user, "DISLIKE");
    }

    @Test
    public void reportPostTest() {
        when(tokenService.extractUserId(eq("nonAdminDummyToken"))).thenReturn(userId);
        ResponseEntity<?> response = reportController.reportPost(reportRequest, "nonAdminDummyToken");
        verify(reportService, times(1)).create(reportRequest.getPostId(), userId, "DISLIKE");
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void getReportsTest() {
        when(tokenService.extractRole(anyString())).thenAnswer(invocationMock -> {
            switch((String) invocationMock.getArguments()[0]) {
                case "nonAdminDummyToken": return "USER";
                case "adminToken": return "ADMIN";
                default: return null;
            }
        });
        assertThrows(BadRequestException.class, ()-> {
            reportController.getReports(0, "adminToken");
        });
        assertThrows(UnauthorizedException.class, ()-> {
            reportController.getReports(1, "nonAdminDummyToken");
        });
        List<Report> reports = new ArrayList<Report>();
        reports.add(report);
        when(reportService.findAll(0)).thenReturn(reports);
        ResponseEntity<List<ReportResponse>> resReports = reportController.getReports(1, "adminToken");
        verify(reportService, times(1)).findAll(0);
        assertEquals(resReports.getStatusCode(), HttpStatus.OK);
        assertEquals(resReports.getBody().get(0).getPostId(), post.getId());
        assertEquals(resReports.getBody().size(), 1);
    }

    @Test
    public void getUserReportsTest() {
        when(tokenService.extractAllClaims(anyString())).thenAnswer(invocationMock -> {
            Map<String, Object> claims = new HashMap<String, Object>();
            switch((String) invocationMock.getArguments()[0]) {
                case "neitherToken":
                    claims.put("id", "3eefc30f-c7f9-49da-ae78-38d6f64a954d");
                    claims.put("role", "USER");
                    break;
                case "adminToken":
                    claims.put("id", userId);
                    claims.put("role", "ADMIN");
                    break;
                default:;
            }
            return Jwts.claims(claims);
        });
        assertThrows(UnauthorizedException.class, ()-> {
            reportController.getUserReports(userId, "neitherToken");
        });
        List<Report> reports = new ArrayList<Report>();
        reports.add(report);
        when(reportService.findAllByUserId(userId)).thenReturn(reports);
        ResponseEntity<List<ReportResponse>> resReports = reportController.getUserReports(userId, "adminToken");
        verify(reportService, times(1)).findAllByUserId(userId);
        assertEquals(resReports.getStatusCode(), HttpStatus.OK);
        assertEquals(resReports.getBody().get(0).getPostId(), post.getId());
        assertEquals(resReports.getBody().size(), 1);
    }

    @Test
    public void deleteReportTest() {
        when(tokenService.extractAllClaims(anyString())).thenAnswer(invocationMock -> {
            Map<String, Object> claims = new HashMap<String, Object>();
            switch((String) invocationMock.getArguments()[0]) {
                case "neitherToken":
                    claims.put("id", "3eefc30f-c7f9-49da-ae78-38d6f64a954d");
                    claims.put("role", "USER");
                    break;
                case "adminToken":
                    claims.put("id", userId);
                    claims.put("role", "ADMIN");
                    break;
                default:;
            }
            return Jwts.claims(claims);
        });
        when(reportService.findById(report.getId())).thenReturn(report);
        assertThrows(UnauthorizedException.class, ()-> {
            reportController.deleteReport(report.getId(), "neitherToken");
        });
        ResponseEntity<?> result = reportController.deleteReport(report.getId(), "adminToken");
        verify(reportService, times(1)).delete(report);
        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);
    }
}
