/*
 * PIBAdb Cohort Statistics Queries
 * ================================
 * 
 * This document contains utility queries for the PIBAdb Cohort, a comprehensive database
 * of colorectal polyp videos and images with histological information. The queries are
 * designed to extract statistical information, analyze video segments, and summarize
 * polyp characteristics across different modalities and classifications.
 * 
 * Variable Definitions:
 * - @not_polyp_id: Identifier for segments without polyps
 * - @normal_mucosa_id: Identifier for normal mucosa segments
 * - @clean_mucosa_id: Identifier for clean mucosa segments
 * - @nbi_id: Identifier for NBI (Narrow Band Imaging) segments
 */

-- Variable definition
SET @not_polyp_id = (SELECT id FROM modifier WHERE name = "Not polyp");
SET @normal_mucosa_id = (SELECT id FROM modifier WHERE name = "Normal mucosa");
SET @clean_mucosa_id = (SELECT id FROM modifier WHERE name = "Clean mucosa");
SET @nbi_id = (SELECT id FROM modifier WHERE name = "NBI");

-- =============================================================================
-- POLYP STATISTICS
-- =============================================================================
-- Queries for analyzing polyp distribution by histology, classification systems
-- (WASP, NICE, Paris), and other characteristics.
-- =============================================================================

-- Polyps by histology
-- Groups polyps by histological type (DTYPE), adenoma type, and dysplasia grade.
-- Returns polyp count and associated image count for each combination.
SELECT "Polyps by histology" AS title,
	ph.DTYPE,
    IFNULL(ph.adenomatype, "") AS adenoma,
    IFNULL(ph.adenomadysplasing, IFNULL(ph.ssadysplasia, IFNULL(ph.tsadysplasia, ""))) AS dysplasia,
    COUNT(DISTINCT ph.id) AS polyp_count,
    COUNT(DISTINCT i.id) AS image_count
FROM polyphistology ph
	JOIN polyp p ON p.histology_id = ph.id
    LEFT JOIN image i ON i.polyp_id = p.id
GROUP BY ph.DTYPE, adenoma, dysplasia
ORDER BY ph.DTYPE ASC, adenoma ASC, dysplasia ASC;

-- Polyps by WASP classification
-- Analyzes polyp distribution according to the WASP (Workgroup serrAted polypS and Polyposis)
-- classification system.
SELECT "Polyps by WASP" AS title,
	wasp,
    COUNT(*) AS polyp_count
FROM polyp
GROUP BY wasp
ORDER BY wasp ASC;

-- Polyps by NICE classification
-- Groups polyps according to the NICE (NBI International Colorectal Endoscopic)
-- classification system.
SELECT "Polyps by NICE" AS title,
	nice,
    COUNT(*) AS polyp_count
FROM polyp
GROUP BY nice
ORDER BY nice ASC;

-- Polyps by Paris classification
-- Analyzes polyp distribution using the Paris morphological classification,
-- including both primary and secondary descriptors.
SELECT "Polyps by Paris" AS title,
	paris_primary,
	paris_secondary,
    COUNT(*) AS polyp_count
FROM polyp
GROUP BY paris_primary, paris_secondary
ORDER BY paris_primary ASC, paris_secondary ASC;

-- =============================================================================
-- PATIENT DEMOGRAPHICS
-- =============================================================================
-- Queries for analyzing patient characteristics including age distribution,
-- sex distribution, and associated video and polyp counts.
-- =============================================================================

-- Total patient count
-- Returns the total number of patients in the study.
SELECT COUNT(*) FROM patient p;

-- Patient age statistics by sex
-- Calculates minimum, maximum, average, and standard deviation of patient ages,
-- grouped by sex and overall total.
SELECT "Patients" AS title,
	sex,
    MIN(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS min_age,
    MAX(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS max_age,
    AVG(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS avg_age,
    STDDEV(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS stddev_age
FROM patient p
	JOIN exploration e ON e.patient_id = p.id
    JOIN video v ON v.exploration_id = e.id
    LEFT JOIN polyprecording pr ON pr.video_id = v.id
GROUP BY sex
UNION ALL
SELECT "Patients" AS title,
	"Total" AS total,
    MIN(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS min_age,
    MAX(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS max_age,
    AVG(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS avg_age,
    STDDEV(YEAR(CURRENT_TIMESTAMP) - YEAR(birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(birthdate, 5))) AS stddev_age
FROM patient p
	JOIN exploration e ON e.patient_id = p.id
    JOIN video v ON v.exploration_id = e.id
    LEFT JOIN polyprecording pr ON pr.video_id = v.id;

-- Patient, video, and polyp counts by sex
-- Provides comprehensive counts of patients, videos, and polyps,
-- grouped by sex and overall total.
SELECT "Patients" AS title,
	sex,
    COUNT(DISTINCT p.id) AS patients,
    COUNT(DISTINCT v.id) AS videos,
    COUNT(DISTINCT pr.polyp_id) AS polyps
FROM patient p
	JOIN exploration e ON e.patient_id = p.id
    JOIN video v ON v.exploration_id = e.id
    LEFT JOIN polyprecording pr ON pr.video_id = v.id
GROUP BY sex
UNION ALL
SELECT "Patients" AS title,
	"Total" AS total,
    COUNT(DISTINCT p.id) AS patients,
    COUNT(DISTINCT v.id) AS videos,
    COUNT(DISTINCT pr.polyp_id) AS polyps
FROM patient p
	JOIN exploration e ON e.patient_id = p.id
    JOIN video v ON v.exploration_id = e.id
    LEFT JOIN polyprecording pr ON pr.video_id = v.id;

-- =============================================================================
-- VIDEO SEGMENT ANALYSIS
-- =============================================================================
-- Queries for analyzing video segment durations, comparing polyp vs non-polyp
-- segments, and analyzing different video modifications.
-- =============================================================================

-- Polyp vs Not Polyp segment comparison
-- Compares statistical measures (count, duration) between segments containing
-- polyps and segments without polyps.
SELECT "Polyp vs Not Polyp" AS title,
	"Polyp" AS name,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM polyprecording
UNION ALL
SELECT "Polyp vs Not Polyp" AS title,
	"Not Polyp" AS name,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM videomodification
WHERE modifier_id = @not_polyp_id;

-- Summary statistics for all segments (Polyp and Not Polyp combined)
-- Aggregates statistics across both polyp-containing and non-polyp segments,
-- including standard deviation for variability analysis.
SELECT "Summary Polyp vs Not Polyp" AS title,
	"Polyp" AS name,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(STDDEV(end - start + 1)) AS stddev_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    STDDEV(end - start + 1) AS `stddev`,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM (
    SELECT start, end FROM polyprecording
    UNION ALL
    SELECT start, end
    FROM videomodification
    WHERE modifier_id = @not_polyp_id
) AS recordings;

-- Video modifications by type (excluding not polyp modifiers)
-- Analyzes video modification segments grouped by modifier type,
-- excluding not polyp, normal mucosa, and clean mucosa segments.
SELECT "Video modifications by type (without all not polyp)" AS title,
    m.name,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum  
FROM videomodification vm JOIN modifier m
	ON modifier_id NOT IN (@not_polyp_id, @normal_mucosa_id, @clean_mucosa_id)
        AND vm.modifier_id = m.id
GROUP BY m.id ORDER BY m.name ASC;

-- Video modifications by type (excluding not polyp and NBI)
-- Similar to previous query but also excludes NBI segments.
-- Includes standard deviation for variability analysis.
SELECT "Video modifications by type (without all not polyp and NBI)" AS title,
    m.name,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(STDDEV(end - start + 1)) AS sd_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    STDDEV(end - start + 1) AS sd,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM videomodification vm JOIN modifier m
	ON modifier_id NOT IN (@not_polyp_id, @normal_mucosa_id, @clean_mucosa_id, @nbi_id)
        AND vm.modifier_id = m.id
GROUP BY m.id ORDER BY m.name ASC;

-- Aggregate video modification statistics (excluding not polyp)
-- Provides overall statistics for video modifications,
-- excluding not polyp, normal mucosa, and clean mucosa segments.
SELECT "Video modification (without all not polyp)" AS title,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM videomodification
WHERE modifier_id NOT IN (@not_polyp_id, @normal_mucosa_id, @clean_mucosa_id);

-- Aggregate video modification statistics (excluding not polyp and NBI)
-- Similar to previous query but also excludes NBI segments.
-- Includes standard deviation for variability analysis.
SELECT "Video modification (without all not polyp and NBI)" AS title,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(STDDEV(end - start + 1)) AS sd_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    STDDEV(end - start + 1) AS sd,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM videomodification
WHERE modifier_id NOT IN (@not_polyp_id, @normal_mucosa_id, @clean_mucosa_id, @nbi_id);

-- Polyp recording statistics
-- Provides overall statistics for all polyp recording segments.
SELECT "Polyp recording" AS title,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM polyprecording;

-- Video modification statistics (all types)
-- Provides overall statistics for all video modification segments.
SELECT "Video modification" AS title,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM videomodification;

-- Combined video segment statistics
-- Aggregates statistics across all video segments (polyp recordings and modifications).
SELECT "Video segment" AS title,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM (
	SELECT start, end FROM polyprecording
	union all
	SELECT start, end FROM videomodification
) AS videosegments;

-- Total video modifications with standard deviation
-- Comprehensive statistics for all video modifications,
-- including standard deviation for variability analysis.
SELECT "Total video modifications" AS title,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(STDDEV(end - start + 1)) AS sd_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    STDDEV(end - start + 1) AS sd,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum 
FROM videomodification vm JOIN modifier m
ON vm.modifier_id = m.id;

-- Video modifications by type (all modifiers)
-- Complete breakdown of video modifications by modifier type,
-- including all modifiers with standard deviation.
SELECT "Video modifications by type" AS title,
    m.name,
	COUNT(*) AS total,
	SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(STDDEV(end - start + 1)) AS sd_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
	SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    STDDEV(end - start + 1) AS sd,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum  
FROM videomodification vm JOIN modifier m
	ON vm.modifier_id = m.id
GROUP BY m.id ORDER BY m.name ASC;

-- =============================================================================
-- POLYP RECORDING ANALYSIS
-- =============================================================================
-- Queries for analyzing polyp recordings by histological type and subtype,
-- including both white light (WL) and NBI modalities.
-- =============================================================================

-- Total polyp recording statistics
-- Overall statistics for all polyp recordings across all histological types.
SELECT "Total polyp recording" AS title,
    COUNT(*) AS count,
    SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
    SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM polyp p
	JOIN polyprecording pr ON p.id = pr.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id;

-- Polyp recording statistics by histological type
-- Groups polyp recording statistics by DTYPE (histological type).
SELECT "Polyp recording by type" AS title,
    ph.DTYPE,
    COUNT(*) AS count,
    SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
    SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum
FROM polyp p
	JOIN polyprecording pr ON p.id = pr.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
GROUP BY ph.DTYPE
ORDER BY ph.DTYPE ASC;

-- Polyp recording statistics by subtype
-- Detailed breakdown by histological type and subtype
-- (adenoma type, dysplasia grade, etc.).
SELECT "Polyp recording by subtype" AS title,
    ph.DTYPE,
    IFNULL(ph.adenomatype, IFNULL(ph.adenomadysplasing, IFNULL(ph.ssadysplasia, IFNULL(ph.tsadysplasia, "")))) AS subtype, COUNT(*) AS count,
    SEC_TO_TIME(SUM(end - start + 1)) AS total_time,
    SEC_TO_TIME(AVG(end - start + 1)) AS average_time,
    SEC_TO_TIME(MIN(end - start + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(end - start + 1)) AS maximum_time,
    SUM(end - start + 1) AS total,
    AVG(end - start + 1) AS average,
    MIN(end - start + 1) AS minimum,
    MAX(end - start + 1) AS maximum 
FROM polyp p
	JOIN polyprecording pr ON p.id = pr.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
GROUP BY ph.DTYPE, subtype
ORDER BY ph.DTYPE ASC, subtype ASC;

-- Total polyp recording statistics (NBI only)
-- Analyzes polyp recordings that overlap with NBI video segments.
-- Uses interval intersection to calculate NBI-specific durations.
SELECT "Total polyp recording (NBI)" AS title,
    COUNT(DISTINCT pr.id) AS count,
    SEC_TO_TIME(SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS total_time,
    SEC_TO_TIME(SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) / COUNT(DISTINCT pr.id)) AS average_time,
    SEC_TO_TIME(MIN(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS maximum_time,
    SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS total,
    SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) / COUNT(DISTINCT pr.id) AS average,
    MIN(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS minimum,
    MAX(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS maximum 
FROM polyp p
	JOIN polyprecording pr ON p.id = pr.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
    JOIN videomodification vm ON vm.modifier_id = @nbi_id
		AND pr.video_id = vm.video_id
        AND (pr.start BETWEEN vm.start AND vm.end OR pr.end BETWEEN vm.start AND vm.end OR vm.start BETWEEN pr.start AND pr.end);

-- Polyp recording by histological type (NBI only)
-- Groups NBI polyp recordings by histological type (DTYPE).
SELECT "Polyp recording by type (NBI)" AS title,
	ph.DTYPE,
    COUNT(DISTINCT pr.id) AS count,
    SEC_TO_TIME(SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS total_time,
    SEC_TO_TIME(SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) / COUNT(DISTINCT pr.id)) AS average_time,
    SEC_TO_TIME(MIN(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS maximum_time,
    SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS total,
    SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) / COUNT(DISTINCT pr.id) AS average,
    MIN(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS minimum,
    MAX(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS maximum 
FROM polyp p
	JOIN polyprecording pr ON p.id = pr.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
    JOIN videomodification vm ON vm.modifier_id = @nbi_id
		AND pr.video_id = vm.video_id
        AND (pr.start BETWEEN vm.start AND vm.end OR pr.end BETWEEN vm.start AND vm.end OR vm.start BETWEEN pr.start AND pr.end)
GROUP BY ph.DTYPE
ORDER BY ph.DTYPE ASC;

-- Polyp recording by subtype (NBI only)
-- Detailed breakdown of NBI polyp recordings by histological type and subtype.
SELECT "Polyp recording by subtype (NBI)" AS title,
	ph.DTYPE,
    IFNULL(ph.adenomatype, IFNULL(ph.adenomadysplasing, IFNULL(ph.ssadysplasia, IFNULL(ph.tsadysplasia, "")))) AS subtype,
    COUNT(DISTINCT pr.id) AS count,
    SEC_TO_TIME(SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS total_time,
    SEC_TO_TIME(SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) / COUNT(DISTINCT pr.id)) AS average_time,
    SEC_TO_TIME(MIN(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS minimum_time,
    SEC_TO_TIME(MAX(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1)) AS maximum_time,
    SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS total,
    SUM(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) / COUNT(DISTINCT pr.id) AS average,
    MIN(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS minimum,
    MAX(LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1) AS maximum 
FROM polyp p
	JOIN polyprecording pr ON p.id = pr.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
    JOIN videomodification vm ON vm.modifier_id = @nbi_id
		AND pr.video_id = vm.video_id
        AND (pr.start BETWEEN vm.start AND vm.end OR pr.end BETWEEN vm.start AND vm.end OR vm.start BETWEEN pr.start AND pr.end)
GROUP BY ph.DTYPE, subtype
ORDER BY ph.DTYPE ASC, subtype ASC;

-- =============================================================================
-- IMAGE ANALYSIS
-- =============================================================================
-- Queries for analyzing polyp images by histological characteristics,
-- modality (NBI vs White Light), and manual selection status.
-- =============================================================================

-- Images by gallery
-- Groups images by gallery classification.
SELECT "Images by gallery" AS title, g.title, count(*)
	FROM gallery g JOIN image i ON g.id = i.gallery_id
    GROUP BY g.title;

-- Total image count by gallery
-- Returns the number of images for each gallery.
SELECT "Image count by gallery" AS title,
    g.title AS gallery_name,
    COUNT(i.id) AS image_count
FROM gallery g
    LEFT JOIN image i ON g.id = i.gallery_id
GROUP BY g.id, g.title
ORDER BY g.title ASC;

-- Polyp images by subtype (all modalities)
-- Analyzes image distribution by histological type, subtype,
-- and manual selection status.
SELECT "Polyp images by subtype" AS title, ph.DTYPE, IFNULL(ph.adenomatype, IFNULL(ph.adenomadysplasing, IFNULL(ph.ssadysplasia, IFNULL(ph.tsadysplasia, "")))) AS subtype, COUNT(*) AS count, i.manually_selected
FROM polyp p
	JOIN image i ON p.id = i.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
GROUP BY ph.DTYPE, subtype, i.manually_selected
ORDER BY ph.DTYPE ASC, subtype ASC, i.manually_selected ASC;

-- Polyp images by subtype (NBI only)
-- Analyzes NBI images only, grouped by histological type, subtype,
-- and manual selection status.
SELECT "Polyp images by subtype (NBI)" AS title,
	ph.DTYPE,
    IFNULL(ph.adenomatype, IFNULL(ph.adenomadysplasing, IFNULL(ph.ssadysplasia, IFNULL(ph.tsadysplasia, "")))) AS subtype,
    COUNT(DISTINCT i.id) AS count,
    i.manually_selected
FROM polyp p
	JOIN image i ON p.id = i.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
    JOIN video v ON v.id = i.video_id
    JOIN videomodification vm ON vm.video_id = i.video_id
							  AND vm.modifier_id = @nbi_id
                              AND i.num_frame BETWEEN (vm.start * v.fps) AND ((vm.end + 1) * v.fps - 1)
GROUP BY ph.DTYPE, subtype, i.manually_selected
ORDER BY i.manually_selected ASC, ph.DTYPE ASC, subtype ASC;

-- Polyp images by subtype (White Light only)
-- Analyzes white light images only (excludes NBI), grouped by histological type,
-- subtype, and manual selection status.
SELECT "Polyp image by subtype (WL)" AS title,
	ph.DTYPE,
    IFNULL(ph.adenomatype, IFNULL(ph.adenomadysplasing, IFNULL(ph.ssadysplasia, IFNULL(ph.tsadysplasia, "")))) AS subtype,
    COUNT(DISTINCT i.id) AS count,
    i.manually_selected
FROM polyp p
	JOIN image i ON p.id = i.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
	WHERE i.id NOT IN (
		SELECT i2.id FROM image i2
		JOIN video v2 ON v2.id = i2.video_id
		JOIN videomodification vm2 ON vm2.video_id = i2.video_id
									AND vm2.modifier_id = @nbi_id
									AND i2.num_frame BETWEEN (vm2.start * v2.fps) AND ((vm2.end + 1) * v2.fps - 1)
	)
GROUP BY ph.DTYPE, subtype, i.manually_selected
ORDER BY i.manually_selected ASC, ph.DTYPE ASC, subtype ASC;

-- Polyp images by histological type (all modalities)
-- Groups images by histological type and manual selection status.
SELECT "Polyp images by type" AS title, ph.DTYPE, COUNT(*) AS count, i.manually_selected
FROM polyp p
	JOIN image i ON p.id = i.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
GROUP BY ph.DTYPE, i.manually_selected
ORDER BY ph.DTYPE ASC, i.manually_selected ASC;

-- Polyp images by histological type (NBI only)
-- Groups NBI images by histological type and manual selection status.
SELECT "Polyp images by type (NBI)" AS title,
	ph.DTYPE,
    COUNT(DISTINCT i.id) AS count,
    i.manually_selected
FROM polyp p
	JOIN image i ON p.id = i.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
    JOIN video v ON v.id = i.video_id
    JOIN videomodification vm ON vm.video_id = i.video_id
							  AND vm.modifier_id = @nbi_id
                              AND i.num_frame BETWEEN (vm.start * v.fps) AND ((vm.end + 1) * v.fps - 1)
GROUP BY ph.DTYPE, i.manually_selected
ORDER BY i.manually_selected ASC, ph.DTYPE ASC;

-- Polyp images by histological type (White Light only)
-- Groups white light images (excludes NBI) by histological type
-- and manual selection status.
SELECT "Polyp images by type (WL)" AS title,
	ph.DTYPE,
    COUNT(DISTINCT i.id) AS count,
    i.manually_selected
FROM polyp p
	JOIN image i ON p.id = i.polyp_id
	JOIN polyphistology ph ON p.histology_id = ph.id
	WHERE i.id NOT IN (
		SELECT i2.id FROM image i2
		JOIN video v2 ON v2.id = i2.video_id
		JOIN videomodification vm2 ON vm2.video_id = i2.video_id
									AND vm2.modifier_id = @nbi_id
									AND i2.num_frame BETWEEN (vm2.start * v2.fps) AND ((vm2.end + 1) * v2.fps - 1)
	)
GROUP BY ph.DTYPE, i.manually_selected
ORDER BY i.manually_selected ASC, ph.DTYPE ASC;

-- Detailed image information
-- Comprehensive query returning all relevant information for each image,
-- including polyp characteristics, location, size, and histology.
-- Excludes removed images.
SELECT "Image information" AS title,
		i.video_id, i.id as image_id, p.id as polyp_id, p.name,
		i.num_frame, i.manually_selected, pl.x, pl.y, pl.width, pl.height, ABS(pl.width * pl.height) as area,
		p.location, p.location, p.lst, p.nice, p.paris_primary, p.paris_secondary, p.size,
        ph.*
	FROM image i JOIN polyp p ON i.polyp_id = p.id
		JOIN polyphistology ph ON p.histology_id = ph.id
        JOIN polyplocation pl ON pl.image_id = i.id
    WHERE NOT i.is_removed;
