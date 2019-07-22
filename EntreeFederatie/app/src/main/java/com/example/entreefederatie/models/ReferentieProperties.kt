package com.example.entreefederatie.models

import com.beust.klaxon.Json

class ReferentieProperties(
    var uid: String,
    var givenName: String,
    @Json(name = "sn")
    var surname: String
)