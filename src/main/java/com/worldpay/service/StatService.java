package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.query.PublicQueryEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;

public interface StatService {

ResponseBase adminStat(HttpServletRequest req, PublicQueryEntity entity);

ResponseBase appStat(HttpServletRequest req, @RequestBody PublicQueryEntity entity);

ResponseBase appTotal(HttpServletRequest request);

ResponseBase total(HttpServletRequest request);

ResponseBase channelTotal(PublicQueryEntity entity);

}
