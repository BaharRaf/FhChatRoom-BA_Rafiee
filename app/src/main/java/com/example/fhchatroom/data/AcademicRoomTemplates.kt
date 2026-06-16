package com.example.fhchatroom.data

data class AcademicRoomTemplate(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val studyPath: String,
    val semester: Long,
    val kind: String,
    val lectureName: String = ""
)

private val studyPathAliases = mapOf(
    studyPathCatalogKey("csdc") to "Computer Science and Digital Communications",
    studyPathCatalogKey("computer science") to "Computer Science and Digital Communications",
    studyPathCatalogKey("computer science and digital communications") to "Computer Science and Digital Communications"
)

// Each student gets exactly two predefined academic rooms:
//   1. a study-path room shared by ALL students of that study path
//      (every semester), and
//   2. a study-path + semester room shared by students of that path in the
//      same semester.
// Lecture-specific rooms were removed: they added clutter and depended on a
// large, partly-inferred lecture catalogue.
fun academicRoomTemplatesFor(studyPath: String, semester: Long): List<AcademicRoomTemplate> {
    if (semester !in 1L..12L) {
        return emptyList()
    }

    val normalizedStudyPath = normalizeStudyPath(studyPath)
    if (normalizedStudyPath.isBlank()) {
        return emptyList()
    }

    val canonicalStudyPath = canonicalStudyPathFor(normalizedStudyPath)
    val studyPathRoom = AcademicRoomTemplate(
        // semester-independent id so every semester's students share one room
        id = "academic-${slugify(canonicalStudyPath)}",
        name = "$canonicalStudyPath - All Students",
        description = "Study-path group for all $canonicalStudyPath students across every semester.",
        category = "Academic",
        studyPath = canonicalStudyPath,
        semester = 0L,
        kind = "STUDY_PATH"
    )
    val semesterRoom = AcademicRoomTemplate(
        id = "academic-${slugify(canonicalStudyPath)}-s$semester",
        name = "$canonicalStudyPath - Semester $semester",
        description = "Semester group for $canonicalStudyPath students in semester $semester.",
        category = "Academic",
        studyPath = canonicalStudyPath,
        semester = semester,
        kind = "SEMESTER"
    )

    return listOf(studyPathRoom, semesterRoom)
}

private fun canonicalStudyPathFor(studyPath: String): String {
    val catalogKey = studyPathCatalogKey(studyPath)
    return studyPathAliases[catalogKey]
        ?: studyPathOptions.firstOrNull { studyPathCatalogKey(it) == catalogKey }
        ?: studyPath
}

internal fun studyPathCatalogKey(studyPath: String): String {
    return normalizeStudyPath(studyPath)
        .lowercase()
        .replace("[–—−]".toRegex(), "-")
        .replace("\\s*-\\s*".toRegex(), " - ")
}

private fun slugify(value: String): String {
    val slug = value
        .lowercase()
        .replace("[^a-z0-9]+".toRegex(), "-")
        .trim('-')

    return slug.ifBlank { "room" }
}
