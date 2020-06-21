<?php
	require_once("../db.php");
	$conn = db_connect();
	mysqli_query("set names utf8");	
	
	$mode = $_POST['request_impt'];

	$fp = fopen("impt_data.txt", "r");
	$fr = fread($fp, 10);
	
	if($mode == 'true'){
		echo $fr;
		$result_norm = $conn->query("UPDATE users SET users_mode = 'NORM' WHERE users_num = 1");
	}
	else if($mode == 'false'){
		$result_park = $conn->query("UPDATE users SET users_mode = 'PARK' WHERE users_num = 1");
	}

	fclose($fp);
?>