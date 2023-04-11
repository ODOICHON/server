package com.example.jhouse_server.global.jwt

import com.fasterxml.jackson.annotation.JsonProperty

class TokenDto (
        @JsonProperty("access_token") val accessToken: String
) {
}