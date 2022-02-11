package com.kodigo.alltodo_api.model.httpResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {

    private String status;
    private String msg;
}
