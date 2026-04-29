package com.example.fhchatroom.data

import kotlin.math.abs

val studyPathOptions = listOf(
    "Advanced Manufacturing Technologies and Management",
    "Advanced Nursing Counseling",
    "Advanced Nursing Education",
    "Advanced Nursing Practice – Schwerpunkt Pflegemanagement",
    "Angewandte Elektronik und Technische Informatik",
    "Architektur – Green Building",
    "Bauingenieurwesen – Baumanagement",
    "Bioengineering",
    "Bioinformatik",
    "Biomedizinische Analytik",
    "Bioprocess Engineering",
    "Biotechnologisches Qualitätsmanagement",
    "Clinical Engineering",
    "Computer Science and Digital Communications",
    "Diätologie",
    "Elementarpädagogik",
    "Ergotherapie",
    "Funktionsdiagnostik",
    "Gerontologische Gesundheits- und Krankenpflege",
    "Gesundheits- und Krankenpflege",
    "Green Mobility",
    "Health Assisting Engineering",
    "Health Studies",
    "Health Tech and Clinical Engineering",
    "Hebammen",
    "High Tech Manufacturing",
    "Histopathologie",
    "Integriertes Risikomanagement",
    "Integriertes Sicherheitsmanagement",
    "IT-Security",
    "Kinder- und Familienzentrierte Soziale Arbeit",
    "Kinder- und Jugendlichenpflege",
    "Logopädie",
    "Molecular Biotechnology",
    "Molekulare Biotechnologie",
    "Multilingual Technologies",
    "Nachhaltige Verpackungstechnologie",
    "Nachhaltiges Ressourcenmanagement",
    "Orthoptik",
    "Pflegepädagogik",
    "Physiotherapie",
    "Primary Health Care Nursing",
    "Psychiatrische Gesundheits- und Krankenpflege",
    "Public Management",
    "Radiologietechnologie",
    "Simulation in Health Care",
    "Software Design and Engineering",
    "Sonography",
    "Soziale Arbeit",
    "Sozialmanagement in der Elementarpädagogik",
    "Sozialraumorientierte und Klinische Soziale Arbeit",
    "Sozialwirtschaft",
    "Sustainability Assessment and Resource Management",
    "Sustainable Packaging Design and Technology",
    "Tax Consulting",
    "Tax Management",
    "Technical Management",
    "Technische Gebäudeausstattung",
    "Technische Informatik",
    "Zytologie"
)

val semesterOptions = (1L..12L).toList()

fun semesterOptionsForStudyPath(studyPath: String): List<Long> {
    val normalizedStudyPath = normalizeStudyPath(studyPath)
    if (normalizedStudyPath.isBlank()) {
        return emptyList()
    }

    return lectureCatalogByStudyPath[studyPathCatalogKey(normalizedStudyPath)]
        ?.keys
        ?.sorted()
        .orEmpty()
}

data class AcademicProfileRepair(
    val studyPath: String,
    val semester: Long,
    val semesterBucket: String,
    val wasAdjusted: Boolean
)

fun repairAcademicProfile(studyPath: String, semester: Long): AcademicProfileRepair {
    val normalizedStudyPath = normalizeStudyPath(studyPath)
    val canonicalStudyPath = canonicalStudyPathOption(normalizedStudyPath)
    val availableSemesters = semesterOptionsForStudyPath(canonicalStudyPath)
    val normalizedSemester = semester.coerceAtLeast(0L)

    val repairedSemester = when {
        canonicalStudyPath.isBlank() || availableSemesters.isEmpty() -> normalizedSemester
        normalizedSemester == 0L -> 0L
        normalizedSemester in availableSemesters -> normalizedSemester
        normalizedSemester < availableSemesters.first() -> availableSemesters.first()
        normalizedSemester > availableSemesters.last() -> availableSemesters.last()
        else -> availableSemesters.minByOrNull { abs(it - normalizedSemester) } ?: normalizedSemester
    }

    return AcademicProfileRepair(
        studyPath = canonicalStudyPath,
        semester = repairedSemester,
        semesterBucket = semesterBucketFor(repairedSemester),
        wasAdjusted = canonicalStudyPath != studyPath || repairedSemester != semester
    )
}

fun User.withRepairedAcademicProfile(): User {
    val repaired = repairAcademicProfile(studyPath, semester)
    if (!repaired.wasAdjusted) {
        return this
    }

    return copy(
        studyPath = repaired.studyPath,
        semester = repaired.semester,
        semesterBucket = repaired.semesterBucket
    )
}

private fun canonicalStudyPathOption(studyPath: String): String {
    if (studyPath.isBlank()) {
        return ""
    }

    val studyPathKey = studyPathCatalogKey(studyPath)

    val aliasMatch = when (studyPathKey) {
        studyPathCatalogKey("csdc"),
        studyPathCatalogKey("computer science"),
        studyPathCatalogKey("computer science and digital communications") ->
            "Computer Science and Digital Communications"

        else -> null
    }
    if (aliasMatch != null) {
        return aliasMatch
    }

    return studyPathOptions.firstOrNull { option ->
        studyPathCatalogKey(option) == studyPathCatalogKey(studyPath)
    } ?: studyPath
}
