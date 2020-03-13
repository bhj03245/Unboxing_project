<?php

    function filled_out($form_vars){
        foreach($form_vars as $key => $value){
            if((!isset($key)) || ($value == ""))
                return false;
        }
        return true;
    }

?>