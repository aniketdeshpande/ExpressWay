<?php
set_include_path('../amazon_sqs_lib/src');
include_once ('.config.inc.php');
$service = new Amazon_SQS_Client(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
invokeReceiveMessage($service, array('QueueUrl' => 'https://ap-southeast-1.queue.amazonaws.com/471848172502/INPUTQUEUE'));

function invokeReceiveMessage(Amazon_SQS_Interface $service, $request){
  try{
    $response = $service->receiveMessage($request);

    //    echo "Response : ".$response.length;
    $receiveMessageResult = $response->getReceiveMessageResult();
    $messageList = $receiveMessageResult->getMessage();

    foreach ($messageList as $message){
      $attributeList = $message->getAttribute();
      $body = $message->getBody();
      $receiptHandle = $message->getReceiptHandle();
      echo $body;

      $latlong=explode("---",$body);
      $latitude=$latlong[0];
      echo "\n".$latitude;
      $longitude=$latlong[1];
      echo "\n".$longitude;
      $time=$latlong[2];
      echo "\n".$time;

      mysql_select_db('expressway');
      $query = "insert into coords values(0,'".$latitude."','".$longitude."','".$time."')";

      echo "\n".$query;
      mysql_query($query);

      invokeDeleteMessage($service, array('QueueUrl'=> 'https://ap-southeast-1.queue.amazonaws.com/471848172502/INPUTQUEUE', 'ReceiptHandle'=>$receiptHandle));
    }   
  } catch (Amazon_SQS_Exception $ex) {
    echo "Shifu";
  }
}
function invokeDeleteMessage(Amazon_SQS_Interface $service, $request) 
{
  try {
    $response = $service->deleteMessage($request);
              
    echo ("Service Response\n");
    echo ("=============================================================================\n");

    echo("        DeleteMessageResponse\n");
    if ($response->isSetResponseMetadata()) { 
      echo("            ResponseMetadata\n");
      $responseMetadata = $response->getResponseMetadata();
      if ($responseMetadata->isSetRequestId()) 
        {
          echo("                RequestId\n");
          echo("                    " . $responseMetadata->getRequestId() . "\n");
        }
    } 
  } catch (Amazon_SQS_Exception $ex) {
    echo("Caught Exception: " . $ex->getMessage() . "\n");
    echo("Response Status Code: " . $ex->getStatusCode() . "\n");
    echo("Error Code: " . $ex->getErrorCode() . "\n");
    echo("Error Type: " . $ex->getErrorType() . "\n");
    echo("Request ID: " . $ex->getRequestId() . "\n");
    echo("XML: " . $ex->getXML() . "\n");
  }
}

//echo get_include_path();

?>

