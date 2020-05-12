<?php
    require_once("../db.php");

    $conn = db_connect();
   
    $trigger = $_POST['request_gps'];
    $strCom = strcmp($trigger, 'trigger');
	
    if(!$strCom){
    $result = $conn->query("SELECT location_lat, location_lng FROM location");
    system("sudo python park_img.py");
    system("sudo python gps.py");

    }
    while($row = mysqli_fetch_array($result)){
        echo "$row[0]&$row[1]";  
    }
?>