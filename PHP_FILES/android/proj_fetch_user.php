<?php
	$host = "127.0.0.1";
	$user = "root";
	$pass="";
	$dbname="event_org";
	$con = mysqli_connect($host, $user, $pass, $dbname);
	$sql = "SELECT id, org_name FROM users";
	$result = mysqli_query($con, $sql);
	$array['items'] = array();
	while($row = mysqli_fetch_array($result)){
		array_push($array['items'], array('id'=>$row[0]), 'org_name'=>$row[1]));
	}
	printf( json_encode($array));
?>