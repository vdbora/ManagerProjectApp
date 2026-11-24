package com.example.managerproject.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

/*
 * Material 3 type scale. The default sizes are kept (they scale with the user's font size
 * setting because they use sp); only the weights of titles are made a bit stronger so
 * board and card titles stand out from body text.
 *
 * Usage: titleLarge = app bar, titleMedium = card / board titles,
 *        bodyMedium = descriptions, labelMedium = chips and badges.
 */
private val Default = Typography()

val Typography = Typography(
    titleLarge = Default.titleLarge.copy(fontWeight = FontWeight.SemiBold),
    titleMedium = Default.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    titleSmall = Default.titleSmall.copy(fontWeight = FontWeight.SemiBold),
    labelLarge = Default.labelLarge.copy(fontWeight = FontWeight.SemiBold),
)
