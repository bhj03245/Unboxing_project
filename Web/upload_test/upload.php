<!DOCTYPE html>
<html>
<head>
  <title>업로드 처리</title>
</head>
<body>
   <h1>파일 업로드</h1>

<?php

  if ($_FILES['videos']['error'] > 0)
  {
    echo 'Problem: ';
    switch ($_FILES['videos']['error'])
    {
      case 1:  
         echo '업로드 최대 파일 크기 초과';
         break;
      case 2:  
         echo '최대 파일 크기 초과';
         break;
      case 3:  
         echo '미완전한 업로드';
         break;
      case 4:  
         echo '업로드된 파일이 없음';
         break;
      case 6:  
         echo 'temp폴더에 파일이 없음';
         break;
      case 7:  
         echo '디스크 읽기 실패';
         break;
    }
    exit;
  }
    /*if ($_FILES['videos']['type'] != 'video/x-msvideoFile')
  {
    echo 'Problem: file is not a AVI file.';
    exit;
  }*/

  //echo "<p>mime type : ".$_FILES['videos']['type']".</p>";
 

  $uploaded_file = './uploads/'.$_FILES['videos']['name'];

  if (is_uploaded_file($_FILES['videos']['tmp_name']))
  {
    
     if (!move_uploaded_file($_FILES['videos']['tmp_name'], $uploaded_file))
     {
        echo '지정 폴더에 파일 이동 불가';
        exit;
     }
  }
  else
  {
    echo '파일 업로드 실패, 파일 이름: ';
    echo $_FILES['videos']['name'];
    exit;
  }

  echo '파일 업로드 성공';
	

?>
</body>
</html>
