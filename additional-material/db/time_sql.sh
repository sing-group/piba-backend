#!/bin/bash

create_updates() {
	file_type=$1;
	table_name=$2;
	create_column_name=$3;
	update_column_name=$4;

	for i in *.$file_type;
	do
		uuid=${i%.$file_type};
		creation=$(date +"%Y-%m-%d %H:%M:%S.%N" -d @$(stat -c %Y $i));
		creation=${creation:0:23};
		modification=$(date +"%Y-%m-%d %H:%M:%S.%N" -d @$(stat -c %Z $i));
		modification=${modification:0:23};

		echo "UPDATE ${table_name} SET ${create_column_name}='$creation', ${update_column_name}='$modification' WHERE id='$uuid';";
	done;
}

update_dates_cross_table() {
    update_table=$1;
    update_id=$2;
    reference_table=$3;
    reference_id=$4;

    echo "UPDATE $update_table u
    SET
        creation_date = (
            SELECT creation_date FROM $reference_table WHERE u.$update_id = $reference_id ORDER BY creation_date ASC LIMIT 1
        ),
        update_date = (
            SELECT update_date FROM $reference_table WHERE u.$update_id = $reference_id ORDER BY update_date DESC LIMIT 1
        );
    ";
}

create_updates png image created update_date;
create_updates mp4 video creation_date update_date;

echo "UPDATE polyplocation pl SET creation_date = (SELECT i.update_date FROM image i WHERE i.id = pl.image_id);";

update_dates_cross_table "polyprecording" "video_id" "video" "id";

echo "UPDATE exploration e
    SET
        creation_date = (
            SELECT creation_date FROM video WHERE exploration_id = e.id ORDER BY creation_date ASC LIMIT 1
        ),
        update_date = (
            SELECT update_date FROM 
                (SELECT exploration_id, update_date FROM video
                UNION ALL 
                SELECT exploration_id, update_date FROM polyp) AS cd
            WHERE cd.exploration_id = e.id
            ORDER BY cd.update_date DESC
            LIMIT 1
        );
";

update_dates_cross_table "polyp" "id" "polyprecording" "polyp_id";
update_dates_cross_table "polyphistology" "id" "polyp" "histology_id";
update_dates_cross_table "patient" "id" "exploration" "patient_id";
update_dates_cross_table "idspace" "id" "patient" "idspace_id";
update_dates_cross_table "videomodification" "video_id" "video" "id";
update_dates_cross_table "modifier" "id" "videomodification" "modifier_id";


echo "UPDATE gallery g
    SET
        creation_date = (
            SELECT created FROM image WHERE gallery_id = g.id ORDER BY created ASC LIMIT 1
        ),
        update_date = (
            SELECT update_date FROM 
                (SELECT gallery_id, update_date FROM image
                 UNION ALL
                 SELECT i.gallery_id, pl.creation_date FROM polyplocation pl JOIN image i ON pl.image_id = i.id) AS cd
            WHERE cd.gallery_id = g.id
            ORDER BY cd.update_date DESC
            LIMIT 1
        );
";
