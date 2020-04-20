<?php
    require_once("../db.php");

    $conn = db_connect();
    $trigger = $_POST['request_gps'];

    //if($trigger == 'trigger'){
    $result = $conn->query("SELECT location_lat, location_lng FROM location");
    system("python gps.py");
    
    //}
    while($row = mysqli_fetch_array($result)){
        echo "<p>$row[0]/$row[1]</p>";  
    }

?>