package com.blankcat.teva.models

import java.time.Duration

enum class Repeat {
    DAILY,
    WEEKLY,
    BI_WEEKLY,
    MONTHLY,
    // TODO(jan): CUSTOM
}

fun Repeat.toDuration(): Duration {
    return when (this) {
        Repeat.DAILY -> Duration.ofDays(1)
        Repeat.WEEKLY -> Duration.ofDays(7)
        Repeat.BI_WEEKLY -> Duration.ofDays(14)
        Repeat.MONTHLY -> Duration.ofDays(30)
    }
}