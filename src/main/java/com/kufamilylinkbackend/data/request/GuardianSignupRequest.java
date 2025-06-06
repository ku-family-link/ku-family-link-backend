package com.kufamilylinkbackend.data.request;

public record GuardianSignupRequest(
    String email,
    String password,
    String phone,
    String name,
    String relationship,
    String clientageId
) {

}
