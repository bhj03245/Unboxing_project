<?php

    require_once('../db.php');
    
    function getNormalList(){
        $conn=db_connect(); 
        $result_norm=$conn->query("SELECT * FROM normal");
        
        if(!$result){
            return false; 
        }
        $num_norm=@$result->num_rows;
        if($num_norm==0){
            return false;
        }
        $result_norm=db_result_to_array($result_norm)
        mysqli_close($conn);
        
        return $result_norm;
    }

    function getManualList(){
        $conn=db_connect(); 
        $result_manl=$conn->query("SELECT * FROM manual");
        
        if(!$result){
            return false; 
        }
        $num_manl=@$result->num_rows;
        if($num_manl==0){
            return false;
        }
        $result_manl=db_result_to_array($result_manl)
        mysqli_close($conn);

        return $result_manl;
    }

    function getParkingList(){
        $conn=db_connect(); 
        $result_park=$conn->query("SELECT * FROM parking");
        
        if(!$result){
            return false; 
        }
        $num_park=@$result->num_rows;
        if($num_park==0){
            return false;
        }
        $result_park=db_result_to_array($result_park)
        mysqli_close($conn);

        return $result_park;
    }

    function getImpactList(){
        $conn=db_connect(); 
        $result_impt=$conn->query("SELECT * FROM impact");
        
        if(!$result){
            return false; 
        }
        $num_impt=@$result->num_rows;
        if($num_impt==0){
            return false;
        }
        $result_impt=db_result_to_array($result_impt)
        mysqli_close($conn);

        return $result_impt;
    }
?>