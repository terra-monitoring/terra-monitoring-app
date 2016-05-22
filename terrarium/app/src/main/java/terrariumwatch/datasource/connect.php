<?php
    $reportingLevel = -1; //0 für alle PHP Fehler und Warnungen ausblenden, -1 für alle anzeigen
    error_reporting($reportingLevel);

    //Sicherheitsabfrage ob der Authentifizierungscode mit übergeben wurde.
    //Wenn der Code nicht übergeben wurde wird die gesamte Prozedur abgebrochen.
    checkAuthCode();

    // Check Authentication Code
    function checkAuthCode(){
        $fehler0 = "Fehler 0: Keine erfolgreiche Authentifizierung!";
        if (isset($_POST['authkey']) AND isset($_POST['method'])){
            $authkey = $_POST['authkey'];
            if ($authkey != 'terra5#'){
                die($authkey);
            }
        } else {
            die(var_dump($_POST));
        }
    }

    //Datenbankverbindung aufbauen
    //$connection = getDBConnection();

    function getDBConnection(){
        //Einstellungen der Datenbank
        $dbusername='pi'; //Benutzer
        $dbpassword='terra5#'; //Passwort
        $dburl='http://pi-terra.ddns.de'; //URL
        $dbname='terra.sqlite'; //Datenbankname

        $fehler1 = "Fehler 1: Fehler beim Aufbauen der Datenbankverbindung!";
    	$link = mysqli_connect($dburl, $dbusername, $dbpassword, $dbname);
    	if (!$link) {
    		die('Verbindung schlug fehl: ' . mysqli_error());
    	}

        /* check connection */
        if (mysqli_connect_errno()) {
            die($fehler1);
        }
        return $link;
    }


    // Close Connection to Database
    function closeConnection($connection){
        mysqli_close($connection);
    }

    $method =  $_POST['method'];

    switch ($method) {
        case 'connect':
            $connection = getDBConnection();
            break;
        case 'allEntrys':
            getAllEntrys($connection);
            break;
        default:
            break;
    }

    // Get all entrys
    function getAllEntrys($connection){
        $data = array();

        $sqlStmt = "SELECT time, s1, s2, s3, s4 FROM seven_day ORDER BY time DESC LIMIT 1";
        $result =  mysqli_query($connection,$sqlStmt);
        if ($result = $connection->query($sqlStmt)) {
            while ($row = $result->fetch_assoc()) {
                $time = $row["time"];
                $s1 = $row["s1"];
                $s2 = $row["s2"];
                $s3 = $row["s3"];
                $s4 = $row["s4"];
                array_push($data,array("Time"=>$time,"S1"=>$s1,"S2"=> $s2,"S3"=>$s3,"S4"=>$s4));
            }
        }

        $sqlStmt = "SELECT auto_mod, max, min, status FROM luefter";
        $result =  mysqli_query($connection,$sqlStmt);
        if ($result = $connection->query($sqlStmt)) {
            while ($row = $result->fetch_assoc()) {
                $auto_mod = $row["auto_mod"];
                $max = $row["max"];
                $min = $row["min"];
                $status = $row["status"];
                array_push($data,array("Auto"=>$auto_mod,"Max"=>$max,"Min"=>$min,"Status"=>$status));
            }
        }

        $sqlStmt = "SELECT sunrise, sunset FROM sun";
        $result =  mysqli_query($connection,$sqlStmt);
        if ($result = $connection->query($sqlStmt)) {
            while ($row = $result->fetch_assoc()) {
                $sunrise = $row["sunrise"];
                $sunset = $row["sunset"];
                array_push($data,array("Sunrise"=>$sunrise,"Sunset"=>$sunset));
            }
        }
        $result->free();
        closeConnection($connection);
    }
?>