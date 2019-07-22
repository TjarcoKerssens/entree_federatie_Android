package com.kennisnet.entreefederatie.models

import com.beust.klaxon.Json

/**
 * A class to be used to hold the data returned in JSON by executing JavaScript on the WebPage.
 */
class ReferentieProperties(
    var uid: String,
    var givenName: String,
    @Json(name = "sn")
    var surname: String
)