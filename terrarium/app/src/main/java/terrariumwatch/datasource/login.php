<?php
	 include 'config.php';

	 // Check whether username or password is set from android
     if(isset($_POST['username']) && isset($_POST['password']))
     {
		  // Innitialize Variable
		  $result = '';
	   	  $username = $_POST['username'];
          $password = $_POST['password'];

		  // Query database for row exist or not
          $sql = 'SELECT time FROM seven_day ORDER BY time DESC LIMIT 1';
          $stmt = $conn->prepare($sql);
          $stmt->execute();
          if($stmt->rowCount())
          {
			 $result = "true";
          }
          elseif(!$stmt->rowCount())
          {
			 $result = "false";
          }

		  // send result back to android
   		  echo $result;
  	}

?>