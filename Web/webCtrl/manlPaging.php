<?php
    require_once('../db.php');
    
    if(isset($_GET['page'])){
        $page=$_GET['page'];
    }else{
        $page=1;
    }

    $db=db_connect();
    $sql='SELECT count(*) as cnt from manl ORDER BY manl_num desc';
    $result=$db->query($sql);
    $row=$result->fetch_assoc();

    $allCnt=$row['cnt']; //전체 개수
    $onePage=15; //한 페이지에 보여줄 개수
    $allPage=ceil($allCnt/$onePage); 
    
    if($page<1 || ($allPage && $page > $allPage)){
?>
        <script>
            alert("존재하지 않는 페이지입니다.");
            history.back();
        </script>
<?php
        exit;
    }

    $oneBlock=10; //한번에 보여줄 총 페이지 개수
    $currentBlock=ceil($page/$oneBlock);
    $allBlock=ceil($allPage/$oneBlock);
    
    $firstPage=($currentBlock*$oneBlock) - ($oneBlock - 1); //현재 블럭의 첫 페이지
    if($currentBlock == $allBlock){
        $lastPage=$allPage;
    }else {
        $lastPage=$currentBlock*$oneBlock; //현재 블럭의 마지막 페이지 
    }

    $prevPage=(($currentBlock-1) * $oneBlock); //이전 페이지
    $nextPage=(($currentBlock+1) * $oneBlock) - ($oneBlock-1); //다음 페이지

    $paging.='<ul>'; //페이징을 저장할 변수

    if($page!=1){ //첫 페이지가 아니면 처음 버튼 생성
        $paging .= '<li class="page page_start"><a href="./normList.php?page=1">처음</a></li>';
    }

    if($currentBlock!=1){ //첫 블럭이 아니면 이전 버튼 생성
        $paging .= '<li class="page page_prev"><a href="./normList.php?page=' . $prevPage . '">이전</a></li>';
    }

    for($i=$firstPage; $i<=$lastPage; $i++){
        if($i==$page){
            $paging .= '<li class="page current">' .$i. '</li>';
        }else{
            $paging .= '<li class="page"><a href="./normList.php?page='.$i.'">'.$i.'</a></li>';
        }
    }

    if($currentBlock!=$allBlock){ //마지막 블럭이 아니면 다음 버튼 생성
        $paging .= '<li class="page page_next"><a href="./normList.php?page='.$nextPage.'">다음</a></li>';
    }

    if($page!=$allPage){
        $paging .= '<li class="page page end"><a href="./normList.php?page='.$allPage.'">마지막</a></li>';
    }
    $paging .= '</ul>';

    $currentLimit=($onePage*$page)-$onePage; //몇 번째의 글을 가져오는지
    $sqlLimit = ' limit ' . $currentLimit . ', ' . $onePage; //limit sql 구문

    $sql='SELECT * FROM manl ORDER BY manl_num ASC'.$sqlLimit; //원하는 개수만큼 가져옴
    $result=$db->query($sql);
    

?>