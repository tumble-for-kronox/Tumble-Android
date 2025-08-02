package tumble.app.tumble.domain.models.util

import java.time.Instant
import java.time.format.DateTimeFormatter

fun parseIsoToInstant(iso: String): Instant = Instant.parse(iso)

fun formatInstantToIso(instant: Instant): String =
    DateTimeFormatter
        .ISO_INSTANT.format(instant)
