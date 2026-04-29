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

fun academicRoomTemplatesFor(studyPath: String, semester: Long): List<AcademicRoomTemplate> {
    if (semester !in 1L..12L) {
        return emptyList()
    }

    val normalizedStudyPath = normalizeStudyPath(studyPath)
    if (normalizedStudyPath.isBlank()) {
        return emptyList()
    }

    val canonicalStudyPath = canonicalStudyPathFor(normalizedStudyPath)
    val catalogKey = studyPathCatalogKey(canonicalStudyPath)
    val lectureNames = lectureCatalogByStudyPath[catalogKey]?.get(semester).orEmpty()
    val mainRoom = AcademicRoomTemplate(
        id = academicRoomId(canonicalStudyPath, semester, "main"),
        name = "$canonicalStudyPath - Semester $semester",
        description = "General semester group for $canonicalStudyPath students in semester $semester.",
        category = "Academic",
        studyPath = canonicalStudyPath,
        semester = semester,
        kind = "MAIN"
    )
    val lectureRooms = lectureNames.map { lectureName ->
        AcademicRoomTemplate(
            id = academicRoomId(canonicalStudyPath, semester, lectureName),
            name = "$canonicalStudyPath S$semester - $lectureName",
            description = "Lecture group for $lectureName in semester $semester of $canonicalStudyPath.",
            category = "Lecture",
            studyPath = canonicalStudyPath,
            semester = semester,
            kind = "LECTURE",
            lectureName = lectureName
        )
    }

    return listOf(mainRoom) + lectureRooms
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

private fun academicRoomId(studyPath: String, semester: Long, roomName: String): String {
    return "academic-${slugify(studyPath)}-s$semester-${slugify(roomName)}"
}

private fun slugify(value: String): String {
    val slug = value
        .lowercase()
        .replace("[^a-z0-9]+".toRegex(), "-")
        .trim('-')

    return slug.ifBlank { "room" }
}
