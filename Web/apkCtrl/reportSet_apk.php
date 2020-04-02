<?php 

    require_once("../db.php");

    $conn = db_connect();

    mysqli_query("set names utf8");
    
    $u_phone=$_POST['phone'];
    $u_content=$_POST['content'];

    $result = $conn->query("SELECT * FROM msg");
    //echo "gg";
    
    if(mysqli_num_rows($result) > 0){
        $update = $conn->query("UPDATE msg SET msg_phone='$u_phone', msg_content='$u_content' WHERE msg_num=1)");
	if($update==TRUE){
	    echo "Update";
	}else{
	    echo mysql_errno($conn);
        }
    }else{
	$add = $conn -> query("INSERT INTO msg VALUES(1, '$u_phone', '$u_content', 'trigger')");
	if($add==TRUE){
	    echo "Insert";
	}else{
            echo mysql_errno($conn);
	}
    }


    mysqli_close($conn);

?>