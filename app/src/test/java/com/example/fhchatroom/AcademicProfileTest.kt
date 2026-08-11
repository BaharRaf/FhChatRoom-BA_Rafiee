package com.example.fhchatroom

import com.example.fhchatroom.data.academicRoomTemplatesFor
import com.example.fhchatroom.data.normalizeStudyPath
import com.example.fhchatroom.data.repairAcademicProfile
import com.example.fhchatroom.data.semesterBucketFor
import com.example.fhchatroom.data.semesterOptions
import com.example.fhchatroom.data.semesterOptionsForStudyPath
import com.example.fhchatroom.data.studyPathOptions
import com.example.fhchatroom.data.withRepairedAcademicProfile
import com.example.fhchatroom.data.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AcademicProfileTest {

    @Test
    fun `normalizes study path whitespace`() {
        assertEquals(
            "Computer Science and Digital Communications",
            normalizeStudyPath("  Computer   Science   and   Digital Communications  ")
        )
    }

    @Test
    fun `maps semester to privacy bucket`() {
        assertEquals("Unknown", semesterBucketFor(0))
        assertEquals("1-2", semesterBucketFor(1))
        assertEquals("1-2", semesterBucketFor(2))
        assertEquals("3-4", semesterBucketFor(4))
        assertEquals("5-6", semesterBucketFor(5))
        assertEquals("7+", semesterBucketFor(7))
    }

    @Test
    fun `creates exactly two academic rooms (study-path and semester) for csdc`() {
        val templates = academicRoomTemplatesFor("CSDC", 3)

        // The model is two predefined rooms per student: a study-path room and
        // a study-path + semester room (lecture-specific rooms were removed).
        assertEquals(2, templates.size)
        assertTrue(templates.any { it.kind == "STUDY_PATH" })
        assertTrue(templates.any { it.kind == "SEMESTER" })
        assertTrue(templates.any { it.name == "Computer Science and Digital Communications - All Students" })
        assertTrue(templates.any { it.name == "Computer Science and Digital Communications - Semester 3" })
        assertTrue(templates.all { it.studyPath == "Computer Science and Digital Communications" })
        assertEquals(0L, templates.single { it.kind == "STUDY_PATH" }.semester)
        assertEquals(3L, templates.single { it.kind == "SEMESTER" }.semester)
        assertEquals(templates.map { it.id }.distinct(), templates.map { it.id })
    }

    @Test
    fun `creates two academic rooms for official non csdc study paths`() {
        val templates = academicRoomTemplatesFor("Bioengineering", 3)

        assertEquals(2, templates.size)
        assertTrue(templates.any { it.kind == "STUDY_PATH" })
        assertTrue(templates.any { it.kind == "SEMESTER" })
        assertTrue(templates.all { it.studyPath == "Bioengineering" })
        assertEquals(3L, templates.single { it.kind == "SEMESTER" }.semester)
    }

    @Test
    fun `every official study path produces a study-path and a semester room`() {
        studyPathOptions.forEach { studyPath ->
            val templates = academicRoomTemplatesFor(studyPath, 1)
            assertEquals("$studyPath should create exactly two academic rooms", 2, templates.size)
            assertTrue("$studyPath should create a study-path room", templates.any { it.kind == "STUDY_PATH" })
            assertTrue("$studyPath should create a semester room", templates.any { it.kind == "SEMESTER" })
        }
    }

    @Test
    fun `unknown study paths still create the two general academic rooms`() {
        val templates = academicRoomTemplatesFor("Unofficial Test Path", 4)

        assertEquals(2, templates.size)
        assertTrue(templates.all { it.category == "Academic" })
        assertTrue(templates.all { it.lectureName == "" })
        assertTrue(templates.any { it.kind == "STUDY_PATH" })
        assertTrue(templates.any { it.kind == "SEMESTER" })
    }

    @Test
    fun `academic dropdown options include official study path and semesters`() {
        assertTrue(studyPathOptions.contains("Computer Science and Digital Communications"))
        assertEquals((1L..12L).toList(), semesterOptions)
    }

    @Test
    fun `semester options are the six bachelor semesters for any valid study path`() {
        // The app offers 1..6 for every valid path rather than maintaining a
        // per-programme catalogue; a blank path yields no options.
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 6L), semesterOptionsForStudyPath("Bioengineering"))
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 6L), semesterOptionsForStudyPath("Green Mobility"))
        assertEquals(emptyList<Long>(), semesterOptionsForStudyPath(""))
    }

    @Test
    fun `repair academic profile clamps invalid semester to valid range`() {
        val repaired = repairAcademicProfile("Green Mobility", 8L)

        assertEquals("Green Mobility", repaired.studyPath)
        assertEquals(6L, repaired.semester)
        assertEquals("5-6", repaired.semesterBucket)
        assertTrue(repaired.wasAdjusted)
    }

    @Test
    fun `repair academic profile canonicalizes known aliases`() {
        val repaired = repairAcademicProfile("CSDC", 3L)

        assertEquals("Computer Science and Digital Communications", repaired.studyPath)
        assertEquals(3L, repaired.semester)
        assertTrue(repaired.wasAdjusted)
    }

    @Test
    fun `user repair keeps unset semester unchanged`() {
        val repairedUser = User(
            email = "test@stud.hcw.ac.at",
            studyPath = "Bioengineering",
            semester = 0L
        ).withRepairedAcademicProfile()

        assertEquals("Bioengineering", repairedUser.studyPath)
        assertEquals(0L, repairedUser.semester)
        assertEquals("Unknown", repairedUser.semesterBucket)
    }
}
