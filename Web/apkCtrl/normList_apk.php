<?php
    require_once("../db.php");

    $conn=db_connect();
    mysqli_query("set names utf8");

    $result = $conn ->query("SELECT norm_num, norm_name, norm_size, norm_length, norm_url FROM norm ORDER BY norm_num ASC");

    $data = array();

    if($result){
        while($row=mysqli_fetch_array($result)){
            array_push($data,
                array('num'=>$row[0],
                'title'=>$row[1],
                'size'=>$row[2],
                'length'=>$row[3],
                'url'=>$row[4]));
        }

        header('Content-Type:application/json; charset=utf-8');
	    $json=json_encode(array("fromWeb"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	    echo $json;

    }else{
        echo "SQL Error : ";
        echo mysqli_error($conn);
    }
    mysqli_close($conn)

?>
