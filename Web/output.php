<?php
    require_once('db.php');
?>
<!DOCTYPE>
<html>
<head>
   <meta charset="utf-8">
   <link href="//netdna.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
   <script src="//netdna.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
   <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
   <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
   <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
   
   <style>
   
    body {
        font-family: "Lato", sans-serif;
        height:100%;
    }



        .main-head{
            height: 150px;
            background: #FFF;
        
        }

        .sidenav {
            height: 100%;
            background-color: #0CF;
            overflow-x: hidden;
            padding-top: 20px;
        }
        .sidemenu{
           height: 100%;
           background-color: #0CF;
           padding-left:80px;
           }


        .main {
            padding: 0px 10px;
        }

        @media screen and (max-height: 450px) {
            .sidenav {padding-top: 15px;}
        }

        @media screen and (max-width: 450px) {
            .login-form{
                margin-top: 10%;
            }

            .register-form{
                margin-top: 10%;
            }
        }

        @media screen and (min-width: 768px){
            .main{
                margin-left: 40%;
            }

            .sidenav{
                width: 40%;
                position: fixed;
                z-index: 1;
                top: 0;
                left: 0;
            }

            .login-form{
                margin-top: 70%;
            }

            .register-form{
                margin-top: 20%;
            }
        }


        .login-main-text{
            margin-top: 20%;
            padding: 60px;
            color: #fff;
        }

        .login-main-text h2{
            font-weight: 300;
        }

        .btn-skyblue{
            background-color: #0CF !important;
            color: #fff;
        }
        .btn-white{
           color: #fff;
        }
        h2{
           margin-left:30px;
        }
        .manual{
           margin-left:300px;
        }
        .pagination{
           margin-left:350px;
        }
        
   </style>
   
</head>

<?php
    function display_header(){
?>
    <body>
          <div align="center">
      <img src="http://localhost/images/Unboxing_logo.png" width="200" height="100"></div>
<?php
    }
?>

<?php
    function display_index(){
?>
    <frameset cols="30%, *" border="1">
      <frame src="menu.php" scrolling=no noresize>
      <frame src="main.php">
    </frameset>
    </html>
                                                                                               
<?php
    }
?>

<?php
    function display_users(){
?>
    <frameset cols="30%, *" border="1">
      <frame src="menu.php" scrolling=no noresize>
      <frame src="userSetting.php">
    </frameset>
    </html>
<?php
    }
?>

<?php
    function display_normal(){
?>
   <frameset cols="30%, *" border="1">
      <frame src="menu.php" scrolling=no noresize>
      <frame src="normList.php">
    </frameset>
    </html>
<?php
    }
?>

<?php
    function display_manual(){
?>
    <frameset cols="30%, *" border="1">
      <frame src="menu.php" scrolling=no noresize>
      <frame src="manlList.php">
    </frameset>
    </html>
<?php
    }
?>

<?php
    function display_parking(){
?>
    <frameset cols="30%, *" border="1">
      <frame src="menu.php" scrolling=no noresize>
      <frame src="parkList.php">
    </frameset>
    </html>
<?php
    }
?>

<?php
    function display_impact(){
?>
    <frameset cols="30%, *" border="1">
      <frame src="menu.php" scrolling=no noresize>
      <frame src="imptList.php">
    </frameset>
    </html>
<?php
    }
?>

<?php
    function display_info(){
?>
  <frameset cols="30%, *" border="1">
      <frame src="menu.php" scrolling=no noresize>
      <frame src="infoTable.php">
    </frameset>
    </html>
<?php
    }
?>

<?php
    function display_footer(){
?>
    </body>
    </html>
<?php
    }
?>


<?php
    function display_login(){
?>
   <br>
   <div class="sidenav">
        <div class="login-main-text">
           <h2>Unboxing<br> Login Page</h2>
           <p>Login from here to access BlackBox.</p>
        </div>
    </div>
    <div class="main">
        <div class="col-md-6 col-sm-12">
           <div class="login-form">
                <form method="post" action="\webCtrl\login.php">
                    <div class="form-group">
                        <label>ID</label>
                        <input name="id" type="text" class="form-control" placeholder="ID">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input name="pw" type="password" class="form-control" placeholder="Password">
                    </div>
                    <button type="submit" class="btn btn-skyblue">Login</button>
                </form>
            </div>
         </div>
    </div>
<?php
    }
?>

<?php
    function display_main(){
?>
    <body><br><br>
   <div align="center">
   <h1> Unboxing BlackBox <br>홈페이지에 오신 것을 환영합니다!</h1></div>
   <br><br>
    <div class="manual">
    <h3> 웹 페이지 사용방법 </h3>
    1. 사용자 설정<br>
    &nbsp;&nbsp;- 비밀번호를 변경할 수 있는 페이지입니다.<br>
   2. 영상 목록<br>
   &nbsp;&nbsp;- 각 영상 목록의 확인과 다운로드를 할 수 있는 페이지입니다.<br>
   3. 블랙박스 정보<br>
   &nbsp;&nbsp;- 사용자의 블랙박스 정보를 확인할 수 있는 페이지입니다.
   </div>
   
<?php
    }
?>

<?php
    function display_menu(){
?>
<body>
   <div class="sidemenu">
   <div style="color: #FFF;" >
   <h2>UnBoxing</h2><hr style="border: solid 1px #ffffff">
   
   <button type="button" class="btn btn-skyblue" onclick="parent.location.href='http://localhost/webServ/users.php'" >사용자 설정</button><br>
   &nbsp;&nbsp;영상목록<br>
   &nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-skyblue" onclick="parent.location.href='http://localhost/webServ/normPage.php'">상시 녹화</button><br>
   &nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-skyblue" onclick="parent.location.href='http://localhost/webServ/manlPage.php'">수동 녹화</button><br>
   &nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-skyblue" onclick="parent.location.href='http://localhost/webServ/parkPage.php'">주차 녹화</button><br>
   &nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-skyblue" onclick="parent.location.href='http://localhost/webServ/imptPage.php'">충격 녹화</button><br>
   <button type="button" class="btn btn-skyblue" onclick="parent.location.href='http://localhost/webServ/info.php'">블랙박스 정보</button><br>
   </div></div>
<?php
    }
?>

<?php
    function display_userSetting(){
?>
<body>
   
   <h2>User Setting</h2>
   <div align='center'>
      <form method="post" action="\webCtrl\userCtrl.php">
         <p>Old Password: <input name="old_pw" type="text"></p>
         <p>New Password: <input name="new_pw" type="text"></p>
         <p>Retype Password: <input name="re_pw" type="text"></p>
         <input type="submit" value="edit" >
      </form>
   </div>
<?php
    }
?>

<?php
    function display_normalList($paging, $result){
?>

    <body>
    <div class="container">
    <table class="table table-list-search">
        <thead>
            <tr align='center'>
                <th scope="col" class="No">No</th>
                <th scope="col" class="Name">Name</th>
                <th scope="col" class="Size">Size</th>
                <th scope="col" class="Length">Length</th>
                <th scope="col" class="Mktime">MakeTime</th>
                <th scope="col" class="Resolution">Resolution</th>
      <th scope="col" class="Url">Download</th>
            </tr>
        </thead>
<?php
    while($row=mysqli_fetch_array($result)){
?>
        <tbody>
            <tr align='center'>
                <td class="No"><?php echo $row['norm_num']?></td>
                <td class="Name"><?php echo $row['norm_name']?></td>
                <td class="Size"><?php echo $row['norm_size']?></td>
                <td class="Length"><?php echo $row['norm_length']?></td>
                <td class="Mktime"><?php echo $row['norm_mktime']?></td>
                <td class="Resolution"><?php echo $row['norm_resolution']?></td>
      <td class="Url"><a href="<?php echo $row['norm_url']?>"><img src="http://localhost/images/download_btn.png"></a></td>
            </tr>
<?php
    }
?>
        </tbody>
    </table>
    <div class="pagination">
       <?php echo $paging ?>
    </div>
    </div>
    </body>
    </html>

<?php
    }
?>


<?php
    function display_manualList($paging, $result){
?>

    <body>
    <table>
        <thead>
            <tr align='center'>
                <th scope="col" class="No">No</th>
                <th scope="col" class="Name">Name</th>
                <th scope="col" class="Size">Size</th>
                <th scope="col" class="Length">Length</th>
                <th scope="col" class="Mktime">MakeTime</th>
                <th scope="col" class="Resolution">Resolution</th>
      <th scope="col" class="Url">Download</th>
            </tr>
        </thead>
<?php
    while($row=mysqli_fetch_array($result)){
?>
        <tbody>
            <tr align='center'>
                <td class="No"><?php echo $row['manl_num']?></td>
                <td class="Name"><?php echo $row['manl_name']?></td>
                <td class="Size"><?php echo $row['manl_size']?></td>
                <td class="Length"><?php echo $row['manl_length']?></td>
                <td class="Mktime"><?php echo $row['manl_mktime']?></td>
                <td class="Resolution"><?php echo $row['manl_resolution']?></td>
      <td class="Url"><a href="<?php echo $row['manl_url']?>"><img src="http://localhost/images/download_btn.png"></a></td>
            </tr>
<?php
    }
?>
        </tbody>
    </table>
    <div class="paging">
       <?php echo $paging ?>
    </div>
    </body>
    </html>

<?php
    }
?>

<?php
    function display_parkingList($paging, $result){
?>

    <body>
    <table>
        <thead>
            <tr align='center'>
                <th scope="col" class="No">No</th>
                <th scope="col" class="Name">Name</th>
                <th scope="col" class="Size">Size</th>
                <th scope="col" class="Length">Length</th>
                <th scope="col" class="Mktime">MakeTime</th>
                <th scope="col" class="Resolution">Resolution</th>
      <th scope="col" class="Url">Download</th>
            </tr>
        </thead>
<?php
    while($row=mysqli_fetch_array($result)){
?>
        <tbody>
            <tr align='center'>
                <td class="No"><?php echo $row['park_num']?></td>
                <td class="Name"><?php echo $row['park_name']?></td>
                <td class="Size"><?php echo $row['park_size']?></td>
                <td class="Length"><?php echo $row['park_length']?></td>
                <td class="Mktime"><?php echo $row['park_mktime']?></td>
                <td class="Resolution"><?php echo $row['park_resolution']?></td>
      <td class="Url"><a href="<?php echo $row['park_url']?>"><img src="http://localhost/images/download_btn.png"></a></td>
            </tr>
<?php
    }
?>
        </tbody>
    </table>
    <div class="paging">
       <?php echo $paging ?>
    </div>
    </body>
    </html>

<?php
    }
?>

<?php
    function display_impactList($paging, $result){
?>
    <body>
    <table>
        <thead>
            <tr align='center'>
                <th scope="col" class="No">No</th>
                <th scope="col" class="Name">Name</th>
                <th scope="col" class="Size">Size</th>
                <th scope="col" class="Length">Length</th>
                <th scope="col" class="Mktime">MakeTime</th>
                <th scope="col" class="Resolution">Resolution</th>
      <th scope="col" class="Url">Download</th>
            </tr>
        </thead>
<?php
    while($row=mysqli_fetch_array($result)){
?>
        <tbody>
            <tr align='center'>
                <td class="No"><?php echo $row['impt_num']?></td>
                <td class="Name"><?php echo $row['impt_name']?></td>
                <td class="Size"><?php echo $row['impt_size']?></td>
                <td class="Length"><?php echo $row['impt_length']?></td>
                <td class="Mktime"><?php echo $row['impt_mktime']?></td>
                <td class="Resolution"><?php echo $row['impt_resolution']?></td>
      <td class="Url"><a href="<?php echo $row['impt_url']?>"><img src="http://localhost/images/download_btn.png"></a></td>
            </tr>
<?php
    }
?>
        </tbody>
    </table>
    <div class="paging">
       <?php echo $paging ?>
    </div>
    </body>
    </html>

<?php
    }
?>

<?php
    function display_infoTable(){
?>
    <body>
    <table class='table table-list-search'>
        <thead>
            <th>상세 정보</th>
        </thead>
        <tbody>
        <tr>
            <td>카메라</td>
            <td>V2 (RPI 8MP CAMERA BOARD)</td>
        </tr>
        <tr>
            <td>디스플레이</td>
            <td>3.5 터치 LCD (320x480 Pixel)</td>
        </tr>
        <tr>
            <td>해상도</td>
            <td>8MP (640x480)</td>
        </tr>
        <tr>
            <td>이미지센서</td>
            <td>Sony IMX 219 PQ CMOS</td>
        </tr>
        <tr>
            <td>녹화방식</td>
            <td>상시녹화, 수동녹화, 주차녹화, 이벤트녹화</td>
        </tr>
        <tr>
            <td>녹화 프레임 수</td>
            <td>최대 30프레임</td>
        </tr>
        <tr>
            <td>화각</td>
            <td>62.2</td>
        </tr>
        <tr>
            <td>메모리</td>
            <td>MicroSD 메모리카드 (128GB)</td>
        </tr>
        <tr>
            <td>가속도 센서</td>
            <td>3축 가속도 센서</td>
        </tr>
        <tr>
            <td>GPS</td>
            <td>외장 GPS 모듈</td>
        </tr>
        <tr>
            <td>추가 기능</td>
            <td>졸음 방지, 차선이탈 방지 기능</td>
        </tr>
        </tbody>
    </table>
<?php
    }
?>
