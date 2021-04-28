package com.lnlr.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PatchcaService {

    void doGet(String action, String appid, String key, HttpServletRequest request, HttpServletResponse response) throws IOException;

    boolean validate(String queryParam, String code);

    boolean validateExpire(String queryParam);
}
