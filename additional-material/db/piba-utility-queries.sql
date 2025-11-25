/*
 * PIBAdb Cohort Utility Queries
 * ==============================
 * 
 * This document contains utility queries for the PIBAdb Cohort, a comprehensive database
 * of colorectal polyp videos and images with histological information. These queries are
 * designed for practical data extraction and filtering tasks, such as retrieving images
 * from specific galleries, videos with certain modifications, polyps by classification,
 * and other common data retrieval operations.
 *  * 
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
-- GALLERY AND IMAGE QUERIES
-- =============================================================================

-- Get all images from a specific gallery
-- Replace 'Gallery Name' with the desired gallery title
SELECT i.id, i.num_frame, i.manually_selected, i.creation_date,
       v.id AS video_id, v.title AS video_title,
       p.id AS polyp_id, p.name AS polyp_name
FROM image i
JOIN gallery g ON i.gallery_id = g.id
LEFT JOIN video v ON i.video_id = v.id
LEFT JOIN polyp p ON i.polyp_id = p.id
WHERE g.title = 'Gallery Name'
  AND NOT i.is_removed
ORDER BY i.creation_date DESC;

-- Get all available galleries with image counts
SELECT g.id, g.title, g.description, 
       COUNT(i.id) AS total_images,
       SUM(CASE WHEN NOT i.is_removed THEN 1 ELSE 0 END) AS active_images,
       g.creation_date
FROM gallery g
LEFT JOIN image i ON g.id = i.gallery_id
GROUP BY g.id, g.title, g.description, g.creation_date
ORDER BY g.title;

-- Get all manually selected images
SELECT i.id, i.num_frame, i.creation_date,
       v.id AS video_id, v.title AS video_title,
       p.id AS polyp_id, p.name AS polyp_name,
       g.title AS gallery_name
FROM image i
LEFT JOIN video v ON i.video_id = v.id
LEFT JOIN polyp p ON i.polyp_id = p.id
LEFT JOIN gallery g ON i.gallery_id = g.id
WHERE i.manually_selected = 1
  AND NOT i.is_removed
ORDER BY i.creation_date DESC;

-- Get images with their polyp location coordinates
SELECT i.id AS image_id, i.num_frame,
       v.title AS video_title,
       p.name AS polyp_name,
       pl.x, pl.y, pl.width, pl.height,
       ABS(pl.width * pl.height) AS area
FROM image i
JOIN polyp p ON i.polyp_id = p.id
JOIN video v ON i.video_id = v.id
JOIN polyplocation pl ON pl.image_id = i.id
WHERE NOT i.is_removed
ORDER BY v.title, i.num_frame;

-- =============================================================================
-- VIDEO QUERIES
-- =============================================================================

-- Get all videos with NBI segments
SELECT DISTINCT v.id, v.title, v.fps, v.creation_date,
       e.title AS exploration_title, e.date AS exploration_date,
       COUNT(vm.id) AS nbi_segment_count,
       SUM(vm.end - vm.start + 1) AS total_nbi_seconds
FROM video v
JOIN videomodification vm ON v.id = vm.video_id
JOIN exploration e ON v.exploration_id = e.id
WHERE vm.modifier_id = @nbi_id
GROUP BY v.id, v.title, v.fps, v.creation_date, e.title, e.date
ORDER BY v.creation_date DESC;

-- Get all videos with a specific modification type
-- Replace 'Modifier Name' with the desired modifier (e.g., 'NBI', 'Instrumental', etc.)
SELECT DISTINCT v.id, v.title, v.fps, v.creation_date,
       e.title AS exploration_title,
       m.name AS modifier_name,
       COUNT(vm.id) AS video_segment_count,
       SUM(vm.end - vm.start + 1) AS total_seconds
FROM video v
JOIN videomodification vm ON v.id = vm.video_id
JOIN modifier m ON vm.modifier_id = m.id
JOIN exploration e ON v.exploration_id = e.id
WHERE m.name = 'Modifier Name'
GROUP BY v.id, v.title, v.fps, v.creation_date, e.title, m.name
ORDER BY v.creation_date DESC;

-- Get videos with polyp recordings
SELECT DISTINCT v.id, v.title, v.fps,
       e.title AS exploration_title,
       COUNT(DISTINCT pr.polyp_id) AS polyp_count,
       COUNT(pr.id) AS polyp_recording_count,
       SUM(pr.end - pr.start + 1) AS total_polyp_seconds
FROM video v
JOIN exploration e ON v.exploration_id = e.id
JOIN polyprecording pr ON v.id = pr.video_id
GROUP BY v.id, v.title, v.fps, e.title
ORDER BY polyp_count DESC;

-- Get all video modifications for a specific video
-- Replace 'VIDEO_ID' with the actual video ID
SELECT vm.id, vm.start, vm.end, 
       (vm.end - vm.start + 1) AS duration_seconds,
       m.name AS modifier_type,
       vm.confirmed, vm.creation_date
FROM videomodification vm
JOIN modifier m ON vm.modifier_id = m.id
WHERE vm.video_id = 'VIDEO_ID'
ORDER BY vm.start;

-- =============================================================================
-- POLYP QUERIES
-- =============================================================================

-- Get all polyps by NICE classification
-- Replace 'TYPE_1' with desired value (i.e., 'TYPE_1', 'TYPE_2', 'TYPE_3')
SELECT p.id, p.name, p.location, p.size, p.nice,
       p.paris_primary, p.paris_secondary, p.wasp,
       e.title AS exploration_title,
       ph.DTYPE AS histology_type
FROM polyp p
JOIN exploration e ON p.exploration_id = e.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
WHERE p.nice = 'TYPE_1'
ORDER BY p.creation_date DESC;

-- Get all polyps by Paris classification
-- Replace 'SESSILE' with desired value (i.e., 'SESSILE', 'SLIGHTLY_ELEVATED', 
-- 'PEDUNCULATED', 'FLAT', 'DEPRESSED', 'ULCERATED')
SELECT p.id, p.name, p.location, p.size,
       p.paris_primary, p.paris_secondary,
       p.nice, p.wasp,
       e.title AS exploration_title,
       ph.DTYPE AS histology_type
FROM polyp p
JOIN exploration e ON p.exploration_id = e.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
WHERE p.paris_primary = 'SESSILE'
ORDER BY p.size DESC;

-- Get all polyps by histological type
-- Replace 'Adenoma' with desired value (i.e. 'Adenoma', 'SessileSerratedAdenoma',
-- 'TraditionalSerratedAdenoma', 'Hyperplastic', 'Invasive', 'NonEpithelialNeoplastic')
SELECT p.id, p.name, p.location, p.size,
       p.nice, p.paris_primary, p.paris_secondary,
       ph.DTYPE AS histology_type,
       ph.adenomatype, ph.adenomadysplasing,
       ph.ssadysplasia, ph.tsadysplasia,
       e.title AS exploration_title
FROM polyp p
JOIN polyphistology ph ON p.histology_id = ph.id
JOIN exploration e ON p.exploration_id = e.id
WHERE ph.DTYPE = 'Adenoma'
ORDER BY p.creation_date DESC;

-- Get polyps with high-grade dysplasia (adenomas)
-- Replace 'HIGH' with desired dysplasia value (i.e. 'LOW', 'HIGH', 'UNDEFINED', 'INTRAMUCOSAL_CARCINOMA')
SELECT p.id, p.name, p.location, p.size,
       p.nice, p.paris_primary,
       ph.DTYPE AS histology_type,
       ph.adenomatype, ph.adenomadysplasing,
       e.title AS exploration_title
FROM polyp p
JOIN polyphistology ph ON p.histology_id = ph.id
JOIN exploration e ON p.exploration_id = e.id
WHERE ph.DTYPE = 'Adenoma'
  AND ph.adenomadysplasing = 'HIGH'
ORDER BY p.size DESC;

-- Get all polyps from a specific exploration
-- Replace 'EXPLORATION_TITLE' with the actual exploration title
SELECT p.id, p.name, p.location, p.size,
       p.nice, p.paris_primary, p.paris_secondary, p.wasp,
       ph.DTYPE AS histology_type,
       ph.adenomatype, ph.adenomadysplasing
FROM polyp p
JOIN exploration e ON p.exploration_id = e.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
WHERE e.title = 'EXPLORATION_TITLE'
ORDER BY p.name;

-- Get polyps by size range (in mm)
-- Replace MIN_SIZE and MAX_SIZE with desired values
SELECT p.id, p.name, p.location, p.size,
       p.nice, p.paris_primary, p.wasp,
       ph.DTYPE AS histology_type,
       e.title AS exploration_title
FROM polyp p
JOIN exploration e ON p.exploration_id = e.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
WHERE p.size BETWEEN 5 AND 10  -- Replace with MIN_SIZE and MAX_SIZE
ORDER BY p.size DESC;

-- =============================================================================
-- PATIENT AND EXPLORATION QUERIES
-- =============================================================================

-- Get all patients with their exploration counts
SELECT p.id, p.patientID, p.sex, p.birthdate,
       ids.name AS id_space,
       YEAR(CURRENT_TIMESTAMP) - YEAR(p.birthdate) - (RIGHT(CURRENT_TIMESTAMP, 5) < RIGHT(p.birthdate, 5)) AS age,
       COUNT(DISTINCT e.id) AS exploration_count,
       COUNT(DISTINCT v.id) AS video_count
FROM patient p
JOIN idspace ids ON p.idSpace_id = ids.id
LEFT JOIN exploration e ON p.id = e.patient_id
LEFT JOIN video v ON e.id = v.exploration_id
GROUP BY p.id, p.patientID, p.sex, p.birthdate, ids.name
ORDER BY p.creation_date DESC;

-- Get all explorations for a specific patient
-- Replace 'PATIENT_ID' and 'ID_SPACE_NAME' with actual values
SELECT e.id, e.title, e.date, e.location, e.confirmed,
       COUNT(DISTINCT v.id) AS video_count,
       COUNT(DISTINCT p.id) AS polyp_count
FROM exploration e
JOIN patient pat ON e.patient_id = pat.id
JOIN idspace ids ON pat.idSpace_id = ids.id
LEFT JOIN video v ON e.id = v.exploration_id
LEFT JOIN polyp p ON e.id = p.exploration_id
WHERE pat.patientID = 'PATIENT_ID'
  AND ids.name = 'ID_SPACE_NAME'
GROUP BY e.id, e.title, e.date, e.location, e.confirmed
ORDER BY e.date DESC;

-- Get explorations by date range
-- Replace the dates with desired range
SELECT e.id, e.title, e.date, e.location,
       pat.patientID, pat.sex,
       COUNT(DISTINCT v.id) AS video_count,
       COUNT(DISTINCT p.id) AS polyp_count
FROM exploration e
JOIN patient pat ON e.patient_id = pat.id
LEFT JOIN video v ON e.id = v.exploration_id
LEFT JOIN polyp p ON e.id = p.exploration_id
WHERE e.date BETWEEN '2018-01-01' AND '2025-12-31'  -- Replace with desired date range
GROUP BY e.id, e.title, e.date, e.location, pat.patientID, pat.sex
ORDER BY e.date DESC;

-- =============================================================================
-- POLYP RECORDING QUERIES
-- =============================================================================

-- Get all polyp recordings for a specific video
-- Replace 'VIDEO_ID' with the actual video ID
SELECT pr.id, pr.start, pr.end,
       (pr.end - pr.start + 1) AS duration_seconds,
       p.name AS polyp_name, p.location, p.size,
       ph.DTYPE AS histology_type,
       pr.confirmed, pr.creation_date
FROM polyprecording pr
JOIN polyp p ON pr.polyp_id = p.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
WHERE pr.video_id = 'VIDEO_ID'
ORDER BY pr.start;

-- Get polyp recordings that overlap with NBI segments
SELECT DISTINCT pr.id, pr.start, pr.end,
       v.title AS video_title,
       p.name AS polyp_name,
       ph.DTYPE AS histology_type,
       vm.start AS nbi_start, vm.end AS nbi_end,
       LEAST(pr.end, vm.end) - GREATEST(pr.start, vm.start) + 1 AS overlap_seconds
FROM polyprecording pr
JOIN polyp p ON pr.polyp_id = p.id
JOIN video v ON pr.video_id = v.id
JOIN videomodification vm ON vm.video_id = pr.video_id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
WHERE vm.modifier_id = @nbi_id
  AND (pr.start BETWEEN vm.start AND vm.end 
       OR pr.end BETWEEN vm.start AND vm.end 
       OR vm.start BETWEEN pr.start AND pr.end)
ORDER BY v.title, pr.start;

-- Get all recordings for a specific polyp
-- Replace 'POLYP_ID' with the actual polyp ID
SELECT pr.id, pr.start, pr.end,
       (pr.end - pr.start + 1) AS duration_seconds,
       v.title AS video_title, v.fps,
       pr.confirmed, pr.creation_date
FROM polyprecording pr
JOIN video v ON pr.video_id = v.id
WHERE pr.polyp_id = 'POLYP_ID'
ORDER BY v.title, pr.start;

-- =============================================================================
-- DATASET QUERIES
-- =============================================================================

-- Get all available datasets with polyp counts
SELECT pd.id, pd.title, pd.description,
       g.title AS default_gallery,
       COUNT(DISTINCT pid.polyp_id) AS polyp_count,
       pd.creation_date
FROM polypdataset pd
LEFT JOIN gallery g ON pd.defaultGallery_id = g.id
LEFT JOIN polypsindataset pid ON pd.id = pid.polypdataset_id
GROUP BY pd.id, pd.title, pd.description, g.title, pd.creation_date
ORDER BY pd.title;

-- Get all polyps in a specific dataset
-- Replace 'DATASET_TITLE' with the actual dataset title
SELECT p.id, p.name, p.location, p.size,
       p.nice, p.paris_primary, p.wasp,
       ph.DTYPE AS histology_type,
       e.title AS exploration_title
FROM polyp p
JOIN polypsindataset pid ON p.id = pid.polyp_id
JOIN polypdataset pd ON pid.polypdataset_id = pd.id
JOIN exploration e ON p.exploration_id = e.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
WHERE pd.title = 'DATASET_TITLE'
ORDER BY p.name;

-- =============================================================================
-- MODIFIER QUERIES
-- =============================================================================

-- Get all available modifiers
SELECT id, name, creation_date, update_date
FROM modifier
ORDER BY name;

-- Get video segments by modifier type with video details
SELECT m.name AS modifier_type,
       v.id AS video_id, v.title AS video_title,
       vm.start, vm.end,
       (vm.end - vm.start + 1) AS duration_seconds,
       vm.confirmed
FROM videomodification vm
JOIN modifier m ON vm.modifier_id = m.id
JOIN video v ON vm.video_id = v.id
WHERE m.name = 'Modifier Name'  -- Replace with desired modifier name
ORDER BY v.title, vm.start;

-- =============================================================================
-- COMBINED QUERIES
-- =============================================================================

-- Get complete polyp information with all related data
-- Replace 'POLYP_ID' with the actual polyp ID
SELECT p.id, p.name, p.location, p.size,
       p.nice, p.paris_primary, p.paris_secondary, p.wasp, p.lst,
       ph.DTYPE AS histology_type,
       ph.adenomatype, ph.adenomadysplasing,
       ph.ssadysplasia, ph.tsadysplasia,
       e.title AS exploration_title, e.date AS exploration_date,
       pat.patientID, pat.sex, pat.birthdate,
       COUNT(DISTINCT pr.id) AS recording_count,
       COUNT(DISTINCT i.id) AS image_count
FROM polyp p
JOIN exploration e ON p.exploration_id = e.id
JOIN patient pat ON e.patient_id = pat.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
LEFT JOIN polyprecording pr ON p.id = pr.polyp_id
LEFT JOIN image i ON p.id = i.polyp_id AND NOT i.is_removed
WHERE p.id = 'POLYP_ID'  -- Replace with actual polyp ID
GROUP BY p.id, p.name, p.location, p.size, p.nice, p.paris_primary, 
         p.paris_secondary, p.wasp, p.lst, ph.DTYPE, ph.adenomatype,
         ph.adenomadysplasing, ph.ssadysplasia, ph.tsadysplasia,
         e.title, e.date, pat.patientID, pat.sex, pat.birthdate;

-- Get all images from NBI video segments with polyp information
SELECT i.id AS image_id, i.num_frame, i.manually_selected,
       v.id AS video_id, v.title AS video_title, v.fps,
       p.id AS polyp_id, p.name AS polyp_name,
       ph.DTYPE AS histology_type,
       vm.start AS nbi_start, vm.end AS nbi_end,
       g.title AS gallery_name
FROM image i
JOIN video v ON i.video_id = v.id
JOIN videomodification vm ON vm.video_id = v.id
LEFT JOIN polyp p ON i.polyp_id = p.id
LEFT JOIN polyphistology ph ON p.histology_id = ph.id
LEFT JOIN gallery g ON i.gallery_id = g.id
WHERE vm.modifier_id = @nbi_id
  AND i.num_frame BETWEEN (vm.start * v.fps) AND ((vm.end + 1) * v.fps - 1)
  AND NOT i.is_removed
ORDER BY v.title, i.num_frame;
