package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyAppGCPMessage {

    private String id;
    private String name;

    @JsonAlias("email_id")
    private String emailId;

}
