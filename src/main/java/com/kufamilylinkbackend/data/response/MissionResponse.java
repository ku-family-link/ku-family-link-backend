package com.kufamilylinkbackend.data.response;

public record MissionResponse(
        String description,
        int goal,
        int progress,
        boolean completed
) {}
