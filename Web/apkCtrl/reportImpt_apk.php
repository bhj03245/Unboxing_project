<?php
	$trig = $_POST['request_impt'];
	$fp = fopen("impt_data.txt", "r");
	$fr = fread($fp, 10);
	
	if($trig != null){
		echo $fr;	
	}
	fclose($fp);
?>