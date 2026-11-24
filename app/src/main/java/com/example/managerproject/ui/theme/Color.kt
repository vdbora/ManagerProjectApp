package com.example.managerproject.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Colour palette for ManagerProject.
 * Base: white surfaces. Accent: soft orange.
 * Text on the orange accent is dark brown (not white) so the contrast stays readable.
 */

// ---------- Light theme ----------
val OrangeLight = Color(0xFFF0904A)            // soft orange accent (buttons, FAB, selection)
val OnOrangeLight = Color(0xFF2E1500)          // dark text/icons on the orange
val OrangeContainerLight = Color(0xFFFFE0CC)   // pale orange for chips and selected items
val OnOrangeContainerLight = Color(0xFF3A1D00)

val SecondaryLight = Color(0xFF77574A)         // warm brown-grey for secondary actions
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFFFE6DC)
val OnSecondaryContainerLight = Color(0xFF2C160D)

val BackgroundLight = Color(0xFFFFFFFF)        // white base
val OnBackgroundLight = Color(0xFF201A17)
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF201A17)
val SurfaceVariantLight = Color(0xFFF4E9E3)
val OnSurfaceVariantLight = Color(0xFF52443C)
val SurfaceContainerLowLight = Color(0xFFFCF8F6)
val SurfaceContainerLight = Color(0xFFF7F2EF)  // columns on the board
val SurfaceContainerHighLight = Color(0xFFF1EBE8)
val OutlineLight = Color(0xFF85746B)
val OutlineVariantLight = Color(0xFFD7C3B8)
val ErrorLight = Color(0xFFBA1A1A)

// ---------- Dark theme ----------
val OrangeDark = Color(0xFFFFB68A)             // lighter orange so it stands out on dark grey
val OnOrangeDark = Color(0xFF4A2300)
val OrangeContainerDark = Color(0xFF6B3A12)
val OnOrangeContainerDark = Color(0xFFFFDCC6)

val SecondaryDark = Color(0xFFE7BEAE)
val OnSecondaryDark = Color(0xFF442A1F)
val SecondaryContainerDark = Color(0xFF5D4034)
val OnSecondaryContainerDark = Color(0xFFFFDBCE)

val BackgroundDark = Color(0xFF161210)
val OnBackgroundDark = Color(0xFFEDE0DA)
val SurfaceDark = Color(0xFF161210)
val OnSurfaceDark = Color(0xFFEDE0DA)
val SurfaceVariantDark = Color(0xFF52443C)
val OnSurfaceVariantDark = Color(0xFFD7C3B8)
val SurfaceContainerLowDark = Color(0xFF1E1A18)
val SurfaceContainerDark = Color(0xFF231E1B)
val SurfaceContainerHighDark = Color(0xFF2E2825)
val OutlineDark = Color(0xFFA08D84)
val OutlineVariantDark = Color(0xFF52443C)
val ErrorDark = Color(0xFFFFB4AB)

// ---------- Task status colours ----------
// To Do = red, In Progress = purple, Done = green.
// "Container" is the soft background, "on container" is the text drawn on it.
val TodoLight = Color(0xFFD32F2F)
val TodoContainerLight = Color(0xFFFDE3E1)
val OnTodoContainerLight = Color(0xFF5F1412)
val InProgressLight = Color(0xFF7E57C2)
val InProgressContainerLight = Color(0xFFEEE6FB)
val OnInProgressContainerLight = Color(0xFF2E1A5C)
val DoneLight = Color(0xFF2E7D32)
val DoneContainerLight = Color(0xFFDDF2DE)
val OnDoneContainerLight = Color(0xFF0F3D13)

val TodoDark = Color(0xFFFF8A80)
val TodoContainerDark = Color(0xFF5C1A17)
val OnTodoContainerDark = Color(0xFFFFDAD6)
val InProgressDark = Color(0xFFCBB4FF)
val InProgressContainerDark = Color(0xFF3F2C70)
val OnInProgressContainerDark = Color(0xFFEADDFF)
val DoneDark = Color(0xFF81C784)
val DoneContainerDark = Color(0xFF1B4D20)
val OnDoneContainerDark = Color(0xFFC8F0CA)
