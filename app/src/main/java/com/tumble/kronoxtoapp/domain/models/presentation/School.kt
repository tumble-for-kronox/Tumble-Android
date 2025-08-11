package com.tumble.kronoxtoapp.domain.models.presentation

import com.tumble.kronoxtoapp.R

val logoNameToLogoMap: Map<String, Int> = mapOf(
    "hkr_logo" to R.drawable.hkr_logo,
    "mau_logo" to R.drawable.mau_logo,
    "oru_logo" to R.drawable.oru_logo,
    "ltu_logo" to R.drawable.ltu_logo,
    "hig_logo" to R.drawable.hig_logo,
    "sh_logo" to R.drawable.sh_logo,
    "hv_logo" to R.drawable.hv_logo,
    "hb_logo" to R.drawable.hb_logo,
    "mdu_logo" to R.drawable.mdu_logo
)

data class School(
    val id: Int,
    val kronoxUrl: String,
    val name: String,
    val logoName: String,
    val loginRq: Boolean,
    val color: String,
    val schoolUrl: String,
    val domain: String,
) {
    val logo: Int
        get() = logoNameToLogoMap[logoName]!!
}