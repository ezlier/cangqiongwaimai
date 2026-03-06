package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportServices {
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
