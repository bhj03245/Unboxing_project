<?php

    require_once("../lib.php");
    session_start();

	$old_pw=$_POST['old_pw'];
	$new_pw=$_POST['new_pw'];
	$re_pw=$_POST['re_pw'];
    
    try{
        check_valid_user();
        if(!filled_out($_POST)){
            throw new Exception('You have not filled out the form completely. Please try again.');
        }
        
        if($new_pw != $re_pw){
            throw new Exception('Passwords entered were not the same.  Not changed.');
        }

        if((strlen($new_pw) > 16) || (strlen($new_pw) < 8)){
            throw new Exception('New password must be between 8 and 16 characters. Try again.');
        }
        
        $ch_pw=change_password($_SESSION['valid_user'], $old_pw, $new_pw);
        echo 'Password changed.';
        

    }catch(Exception $e){
        echo $e->getMessage();
    }
   
?>  