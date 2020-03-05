<?php

function db_connect(){
    $result = new mysqli('localhost', 'pi', 'myub', 'ub_project');
    if (!$result) {
      throw new Exception('Could not connect to database server');
    } else {
      return $result;
    }
}

?>