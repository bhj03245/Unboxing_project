<?php

    require_once('../lib.php');
    session_start();

    if(!isset($_POST['id']) || !isset($_POST['pw'])) echo "shit";


    $id = $_POST['id'];
    $pw = $_POST['pw'];

    if ( ($id=='') || ($pw=='') ) {
	  echo "<script>window.alert('아이디 또는 비밀번호를 입력하여 주세요.');history.back();</script>";
	  exit;
	}
    if($id && $pw){
        try{
            login($id, $pw);
            $_SESSION['valid_user'] = $id;
            echo "<script>location.href='http://192.168.1.123/webServ/web_index.php'</script>";
        }catch(Exception $e){
            echo "<script>window.alert('아이디 또는 비밀번호가 틀렸습니다.');history.back();</script>";
            exit;
        }
    }

?>  

