<?php
	$host = "127.0.0.1";
	$user = "root";
	$pass="";
	$dbname="event_org";
	$con = mysqli_connect($host, $user, $pass, $dbname);
	$sql = "SELECT org_name, nfc_id, user_type FROM users";
	$result = mysqli_query($con, $sql);
	$array['items'] = array();
	while($row = mysqli_fetch_array($result)){
		array_push($array['items'], array('org_name'=>$row[0], 'nfc_id'=>$row[1], 'user_type'=>$row[2]));
	}
	printf( json_encode($array));
?>