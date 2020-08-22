<?php
    require_once("../db.php");

    $conn=db_connect();
    mysqli_query("set names utf8");

    $oldPW = $_POST['oldPw'];
    $newPW = $_POST['newPw']; 
    $ID = $_POST['id'];
    
    $trigg = 0;

    $login_check = $conn->query("SELECT * FROM users WHERE users_id='$ID' and users_pwd='$oldPW'");

    $result=$conn->query("UPDATE users SET users_pwd = '$newPW' WHERE users_id = '$ID'");


    if($login_check->num_rows > 0){
        if($result){
            $trigg = 1;
            echo $trigg;
        }else{
        	$trigg = 0;
            echo $trigg;
        }
    }else{
	echo mysql_errno($conn);
    }
   
    mysqli_close($conn);
?>