<?php
$host = "127.0.0.1";
$user = "root";
$pass="";
$dbname = "dborder";
$con = mysqli_connect($host,$user,$pass,$dbname);
$sql = "SELECT name, price FROM tblitem";
$result= mysqli_query($con,$sql);
$array = array();


$counts = array_count_values($array);
$arrSize = count($array);
/*echo $arrSize."<br>";
echo $counts[$array[0]]."<br>"; 
echo $array[1]."<br>"; 
print_r($array);*/
/*
echo "<br><br><br>";*/
$arrUnique = array_unique ($array);
$arrayKey = array();
$arrayKey = array_keys($arrUnique);
/*foreach ($arrayKey as &$value) {
 echo $arrUnique[$value]."<br>";

}*/
?>


<html>
<head>
  <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  <script type="text/javascript">
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {

      var data = google.visualization.arrayToDataTable([
        ['Task', 'Hours per Day'],
        <?php
        foreach ($arrayKey as &$value) {
          echo "['".$arrUnique[$value]."',".$counts[$array[$value]]."],";

       }
	   while ($row = mysqli_fetch_array($result)) {
		   echo "['".$row['name']."',".$row['price']."],";
		}
       ?>
       ]);

      var options = {
        title: 'Item\'s price Percentage'
      };

      var chart = new google.visualization.PieChart(document.getElementById('piechart'));

      chart.draw(data, options);
    }
  </script>
</head>
<body>
  <div id="piechart" style="width: 98vw; height: 100vh;"></div>
</body>
</html>