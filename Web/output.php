<?php 
    require_once('db.php');
?>
<!DOCTYPE>
<html>
<head>
	<meta charset="utf-8">
</head>

<?php
    function display_header(){
?>  
    <body>
        <h1>UB_BlackBox</h1>    
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
    </html>
<?php
    }
?>

<?php
    function display_info(){
?> 
  <frameset cols="30%, *" border="1">
		<frame src="menu.php" scrolling=no noresize>
		<frame src="info.php">
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
	<div align='center'>
		<form method="post" action="\webCtrl\login.php">
		
			<p>ID: <input name="id" type="text"></p>
			<p>PW: <input name="pw" type="password"></p>
			<input type="submit" value="login">
		</form>
		<br />
	</div>
<?php
    }
?>

<?php
    function display_main(){
?> 
    <body>
    <h1> 웹 페이지 사용방법 </h1>
    1. 사용자 설정<br>
	2. 영상 목록<br>
	3. 블랙박스 정보<br>
<?php
    }
?>

<?php
    function display_menu(){
?> 
<body> 
    <h1>UB_BlackBox</h1>
	<a href="http://localhost/webServ/users.php" target="_top">사용자 설정</a><br>
	영상목록<br>
	&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://localhost/webServ/normPage.php" target="_top">상시 녹화</a><br>
	&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://localhost/webServ/manlPage.php" target="_top">수동 녹화</a><br>
	&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://localhost/webServ/parkPage.php" target="_top">주차 녹화</a><br>
	&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://localhost/webServ/imptPage.php" target="_top">이벤트 녹화</a><br>
	<a href="http://localhost/webServ/info.php" target="_top">블랙박스 정보</a><br>

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
    <div class="paging">
    	<?php echo $paging ?>
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