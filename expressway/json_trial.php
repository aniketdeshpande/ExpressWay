<?php
$json='{ "destination_addresses" : [ "SH 88, Gujarat, India" ], "origin_addresses" : [ "Saikheda Rd, Maharashtra, India" ], "rows" : [ { "elements" : [ { "distance" : { "text" : "217 km", "value" : 217164 }, "duration" : { "text" : "3 hours 47 mins", "value" : 13617 }, "status" : "OK" } ] } ], "status" : "OK" }';
var_dump(json_decode($json,true));
?>