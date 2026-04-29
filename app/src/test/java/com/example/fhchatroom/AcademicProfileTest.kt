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
    fun `creates deterministic academic room templates for semester`() {
        val templates = academicRoomTemplatesFor("CSDC", 3)

        assertEquals(7, templates.size)
        assertTrue(templates.any { it.kind == "MAIN" })
        assertTrue(templates.any { it.name == "Computer Science and Digital Communications - Semester 3" })
        assertTrue(templates.any { it.lectureName == "DevOps | ILV" })
        assertTrue(templates.any { it.lectureName == "Introduction to AI and Data Science | ILV" })
        assertTrue(templates.all { it.studyPath == "Computer Science and Digital Communications" })
        assertTrue(templates.all { it.semester == 3L })
        assertEquals(templates.map { it.id }.distinct(), templates.map { it.id })
    }

    @Test
    fun `creates lecture rooms for official non csdc study paths`() {
        val templates = academicRoomTemplatesFor("Bioengineering", 3)

        assertEquals(10, templates.size)
        assertTrue(templates.any { it.kind == "MAIN" })
        assertTrue(templates.any { it.lectureName == "Biochemie | VO" })
        assertTrue(templates.any { it.lectureName == "Zellbiologie | VO" })
        assertTrue(templates.all { it.studyPath == "Bioengineering" })
        assertTrue(templates.all { it.semester == 3L })
    }

    @Test
    fun `creates lecture rooms for each official study path`() {
        studyPathOptions.forEach { studyPath ->
            val hasLectureRooms = semesterOptions.any { semester ->
                academicRoomTemplatesFor(studyPath, semester).any { it.kind == "LECTURE" }
            }

            assertTrue("$studyPath should create at least one lecture-specific room", hasLectureRooms)
        }
    }

    @Test
    fun `unknown study paths only create one general semester room`() {
        val templates = academicRoomTemplatesFor("Unofficial Test Path", 4)

        assertEquals(1, templates.size)
        assertEquals("MAIN", templates.single().kind)
        assertEquals("Academic", templates.single().category)
        assertEquals("", templates.single().lectureName)
    }

    @Test
    fun `academic dropdown options include official study path and semesters`() {
        assertTrue(studyPathOptions.contains("Computer Science and Digital Communications"))
        assertEquals((1L..12L).toList(), semesterOptions)
    }

    @Test
    fun `semester options are limited by selected study path`() {
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 6L), semesterOptionsForStudyPath("Bioengineering"))
        assertEquals(listOf(1L, 2L, 3L, 4L), semesterOptionsForStudyPath("Green Mobility"))
        assertEquals(emptyList<Long>(), semesterOptionsForStudyPath(""))
    }

    @Test
    fun `repair academic profile clamps invalid semester to valid range`() {
        val repaired = repairAcademicProfile("Green Mobility", 8L)

        assertEquals("Green Mobility", repaired.studyPath)
        assertEquals(4L, repaired.semester)
        assertEquals("3-4", repaired.semesterBucket)
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
