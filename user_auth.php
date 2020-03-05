<?php

    require_once('../db.php');

    function login($id, $pw){
        $conn=db_connect();

        $result=$conn->query("SELECT * FROM users WHERE users_id='$id' and users_pwd='$pw'");

        if(!$result){
            throw new Exception('could not log you in');
        }

        if($result->num_rows > 0){
            return true;
        }else{
            throw new Exception('could not log you in');
        }
        mysqli_close($conn);
    }
    
    function check_valid_user(){
        if(isset($_SESSION['valid_user'])){
            echo "Logged in as ".$_SESSION['valid_user'].".<br>";
        }else{
            echo 'You are not logged in.<br>';
            exit;
        }
    }

    function change_password($id, $old_pw, $new_pw){
        login($id, $old_pw);
        $conn=db_connect();
        $result=$conn->query("UPDATE users SET users_pwd = '$new_pw' WHERE users_id = '$id'");

        if(!$result){
            throw new Exception('Password could not be changed');
        }else{
            mysqli_close($conn);
            return true;
        }
        
    }
?>