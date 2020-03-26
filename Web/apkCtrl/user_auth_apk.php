<?php 
    require_once("../db.php");

    $conn=db_connect();
    mysqli_query("set names utf8");
   
    $u_id=$_POST['id'];
    $u_pw=$_POST['pw'];
    

    $result=$conn->query("SELECT * FROM users WHERE users_id='$u_id' and users_pwd='$u_pw'");
    
   
    if($result){
        echo "1";
    }else{
        echo "0";
	    echo mysql_errno($conn);
    }

?>