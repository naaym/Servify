package com.servify.paymentsconnect.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectAccountLinkResponse {
    private String accountId;
    private String url;
}
