package com.example.fhchatroom

/**
 * Compile-time feature switches.
 *
 * MEDIA_UPLOADS_ENABLED gates all Firebase Storage uploads (profile photos,
 * chat images, voice notes). It is OFF because the project runs on the free
 * Spark plan, and Firebase Storage now requires the paid Blaze plan to set up
 * a bucket. Media is outside the BA2 evaluation scope (the contribution is the
 * recommendation pipeline and text chat), so disabling it keeps the app
 * zero-cost and avoids runtime upload errors.
 *
 * To re-enable later: set up a Storage bucket in the Firebase console, deploy
 * `storage.rules` (already versioned in the repo), and flip this to `true`.
 */
object AppFeatures {
    const val MEDIA_UPLOADS_ENABLED = false
}
