<?php
// Get values from database
ini_set("mysql.default_socket","/Applications/XAMPP/xamppfiles/var/mysql/mysql.sock");
mysql_connect('localhost','akshay','password') or die("aaaaa");
mysql_select_db("expressway");

$result = mysql_query("select * from coords order by time desc limit 2");
$origin="pune";
$destination="mumbai";

$row1 = mysql_fetch_assoc($result);
$row2 = mysql_fetch_assoc($result);

echo "Row 1 : data";
echo $row1['latitude'];
echo $row1['longitude'];
echo " time : ".$row1['time'];
echo "<br>";

echo "<br><br>Row 2 : data";
echo $row2['latitude'];
echo $row2['longitude'];
echo " time : ".$row2['time'];
echo "<br><br>";


$origin = $row1["latitude"].",".$row1["longitude"];
$destination = $row2["latitude"].",".$row2["longitude"];

$startTime = $row2["time"];
$endTime = $row1["time"];

mysql_free_result($result);


$url="http://maps.googleapis.com/maps/api/distancematrix/json?origins=".$origin."&destinations=".$destination."&sensor=false";
//$url="http://maps.googleapis.com/maps/api/distancematrix/json?origins=20.0,74.0&destinations=21.0,73.0&sensor=false";
//echo "Querying:".$url;

// create a new cURL resource
$ch = curl_init();

// set URL and other appropriate options
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_HEADER, 0);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
// grab URL and pass it to the browser
$response = curl_exec($ch);
curl_close($ch);
//$json_response=substr($response,0,strrpos($response, '}'));

//echo $json_response."<br><br>";
// close cURL resource, and free up system resources


$responseData = json_decode($response,true);
//echo "<br><br> JSON decoded : ";
//print_r($responseData['rows'][0]['elements'][0]['distance']['value']);

$distance = (float) $responseData['rows'][0]['elements'][0]['distance']['value'];
$distance = $distance/1000;
echo "<br>Distance : ".$distance."<br>";

$duration = $endTime - $startTime;
echo $duration."<br>";
$seconds  = $duration/1000;
$hours = $seconds/3600;
echo $hours."<br>";

echo "<br>Speed : ".$distance/$hours;

//echo "<br><br>Distance : ".$responseData['rows'][0][0][0];

?>