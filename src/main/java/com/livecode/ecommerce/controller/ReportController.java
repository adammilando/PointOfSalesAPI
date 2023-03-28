package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Request.DailySalesReportRequest;
import com.livecode.ecommerce.model.Request.MontlySalesRepostRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/daily")
    public ResponseEntity getDailySaleReport(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date
            ){
        List<DailySalesReportRequest>dailySalesReportRequests = reportService.getDailySaleReport(date);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get Daily Report", dailySalesReportRequests));
    }

    @GetMapping("/monthly")
    public ResponseEntity getMonthlySaleReport(
            @RequestParam int month,
            @RequestParam int year
    ){
        List<MontlySalesRepostRequest> montlySalesRepostRequests= reportService.getMonthlySaleReport(month, year);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get Monthly Report", montlySalesRepostRequests));
    }
}
