<?php 
    require_once("../db.php");

    $conn=db_connect();
    mysqli_query("set names utf8");
   
    $u_id=$_POST['id'];
    $u_pw=$_POST['pw'];
	
    $sql = "SELECT IF(strcmp(users_pwd, '$u_pw'),0,1) pw_chk FROM users WHERE users_id='$u_id'";

    $result=mysqli_query($conn, $sql);
    
    if($result){
  		$row = mysqli_fetch_array($result);
		if(is_null($row['pw_chk'])){
			echo 'ID를 다시 입력해 주세요.';
		}
		else if($row['pw_chk'] == 0){
			echo $row['pw_chk'];
		}
		else if($row['pw_chk'] == 1){
			echo $row['pw_chk'];
			$_SESSION['valid_user'] = $id;
		}
	}else{
		echo mysql_errno($conn);
	}
	mysqli_close($conn);
?>