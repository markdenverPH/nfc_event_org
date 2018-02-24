<?php
$host = "127.0.0.1";
	$user = "root";
	$pass="";
	$dbname="event_org";
	$con = mysqli_connect($host, $user, $pass, $dbname);
	$sql = "SELECT * FROM events";
	$result = mysqli_query($con, $sql);
	$array['items'] = array();
	while($row = mysqli_fetch_array($result)){
		array_push($array['items'], array('id'=>$row[0],'event_name'=>$row[1]
		,'org_name'=>$row[2],'start_date'=>$row[3],'end_date'=>$row[4]
		,'time'=>$row[5],'event_desc'=>$row[6],'stud_attend'=>$row[7]));
	}
	echo json_encode($array);
?>